package edu.univalle.cincuentazo.controller;

import edu.univalle.cincuentazo.model.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML private GridPane playerCardsGrid;
    @FXML private HBox machineTopArea;
    @FXML private VBox machineLeftArea;
    @FXML private VBox machineRightArea;
    @FXML private ImageView tableCardImage;
    @FXML private ImageView deckImage;
    @FXML private Label sumLabel;

    private Game game;
    private IPlayer humanPlayer;
    private boolean hasPlayedThisTurn = false;
    private PauseTransition turnTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deckImage.setImage(new Image(getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png").toExternalForm()));
    }

    public void startGame(int numMachines) {
        game = new Game(numMachines);
        humanPlayer = game.getPlayers().get(0);
        updateView();
        startHumanTurnTimer();
    }

    // --------------------------------------------------

    private void updateView() {
        tableCardImage.setImage(new Image(getClass().getResource(game.getCurrentTableCard().getResourcePath()).toExternalForm()));
        sumLabel.setText("Suma: " + game.getTableSum());
        displayHands();
    }

    private void displayHands() {
        playerCardsGrid.getChildren().clear();
        for (int i = 0; i < humanPlayer.getHand().size(); i++) {
            Card card = humanPlayer.getHand().get(i);
            ImageView img = new ImageView(new Image(getClass().getResource(card.getResourcePath()).toExternalForm()));
            img.setFitHeight(120);
            img.setFitWidth(80);
            img.setOnMouseClicked(e -> playCard(card));
            playerCardsGrid.add(img, i, 0);
        }
    }

    // --------------------------------------------------

    private void playCard(Card card) {
        if (game.playCard(humanPlayer, card)) {
            System.out.println("Jugador jugó " + card + ". Suma = " + game.getTableSum());
            hasPlayedThisTurn = true;
            updateView();
        } else {
            System.out.println("❌ No puedes jugar esa carta (supera 50).");
        }
    }

    @FXML
    private void onDeckClicked() {
        // Permitir tomar carta en cualquier momento, no solo después de jugar
        if (game != null) {
            game.drawCard(humanPlayer);
            System.out.println("Robaste una carta del mazo.");
            updateView();
            endHumanTurn();
        }
    }


    // --------------------------------------------------

    private void startHumanTurnTimer() {
        if (turnTimer != null) turnTimer.stop();

        turnTimer = new PauseTransition(Duration.seconds(5));
        turnTimer.setOnFinished(e -> {
            System.out.println("⏰ Tiempo agotado, turno pasado automáticamente.");
            endHumanTurn();
        });
        turnTimer.play();
    }

    private void endHumanTurn() {
        if (turnTimer != null) turnTimer.stop();
        playMachinesTurn();
    }

    // --------------------------------------------------
    // --- Turno de las máquinas ---
    private void playMachinesTurn() {
        List<IPlayer> machines = game.getPlayers().subList(1, game.getPlayers().size());
        playNextMachine(machines, 0);
    }

    private void playNextMachine(List<IPlayer> machines, int index) {
        if (index >= machines.size()) {
            checkGameOver();
            // Cuando todas las máquinas terminen, vuelve el turno al humano
            Platform.runLater(this::startHumanTurnTimer);
            return;
        }

        IPlayer machine = machines.get(index);

        if (machine.isEliminated()) {
            playNextMachine(machines, index + 1);
            return;
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            Card chosen = null;
            for (Card c : new ArrayList<>(machine.getHand())) {
                if (game.getTableSum() + game.getCardValue(c) <= 50) {
                    chosen = c;
                    break;
                }
            }

            if (chosen != null) {
                game.playCard(machine, chosen);
                System.out.println(machine.getName() + " jugó " + chosen + " (total: " + game.getTableSum() + ")");
                game.drawCard(machine);
            } else if (game.mustBeEliminated(machine)) {
                System.out.println(machine.getName() + " eliminado (no puede jugar).");
                game.eliminatePlayer(machine);
            }

            updateView();
            playNextMachine(machines, index + 1);
        });
        pause.play();
    }

    private void checkGameOver() {
        if (game.isGameOver()) {
            IPlayer winner = game.getWinner();
            System.out.println("🎉 ¡El juego ha terminado! Ganador: " + winner.getName());
            endGameMessage(winner);
        }
    }

    private void endGameMessage(IPlayer winner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fin del juego");
            alert.setHeaderText("¡Tenemos un ganador!");
            alert.setContentText("El ganador es: " + winner.getName());
            alert.showAndWait();
        });
    }
}
