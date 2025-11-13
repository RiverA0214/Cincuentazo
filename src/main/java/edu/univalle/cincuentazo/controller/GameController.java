package edu.univalle.cincuentazo.controller;

import edu.univalle.cincuentazo.exceptions.DeckEmptyException;
import edu.univalle.cincuentazo.exceptions.InvalidCardPlayException;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deckImage.setImage(new Image(getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png").toExternalForm()));
    }

    public void startGame(int numMachines) {
        game = new Game(numMachines);
        humanPlayer = game.getPlayers().get(0);
        updateView();
    }

    // ------------------ VISTA ------------------

    private void updateView() {
        tableCardImage.setImage(new Image(getClass().getResource(game.getCurrentTableCard().getResourcePath()).toExternalForm()));
        sumLabel.setText("Suma: " + game.getTableSum());
        displayHands();
        displayMachineHands();
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

    private void displayMachineHands() {
        machineTopArea.getChildren().clear();
        machineLeftArea.getChildren().clear();
        machineRightArea.getChildren().clear();

        List<IPlayer> machines = game.getPlayers().subList(1, game.getPlayers().size());

        for (int i = 0; i < machines.size(); i++) {
            IPlayer machine = machines.get(i);
            if (machine.isEliminated()) continue;

            for (Card c : machine.getHand()) {
                ImageView img = new ImageView(new Image(getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png").toExternalForm()));
                img.setFitWidth(80);
                img.setFitHeight(120);

                switch (i) {
                    case 0 -> machineTopArea.getChildren().add(img);
                    case 1 -> machineLeftArea.getChildren().add(img);
                    case 2 -> machineRightArea.getChildren().add(img);
                }
            }
        }
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ------------------ JUEGO ------------------

    private void playCard(Card card) {
        try {
            if (hasPlayedThisTurn) {
                showError("Invalid action", "You already played a card this turn. Draw from the deck!");
                return;
            }

            if (game.playCard(humanPlayer, card)) {
                System.out.println("Jugador jugó " + card + ". Suma = " + game.getTableSum());
                hasPlayedThisTurn = true;
                updateView();
            }
        } catch (InvalidCardPlayException e) {
            System.out.println("❌ No puedes jugar esa carta: " + e.getMessage());
            showError("Invalid play", e.getMessage());
        }
    }

    @FXML
    private void onDeckClicked() {
        try {
            if (!hasPlayedThisTurn) {
                showError("Invalid action", "You must play a card before drawing from the deck!");
                return;
            }

            game.drawCard(humanPlayer);
            System.out.println("Robaste una carta del mazo.");

            checkEliminations(); // 🔹 Verificar si alguien queda eliminado

            hasPlayedThisTurn = false;
            updateView();
            endHumanTurn();

        } catch (DeckEmptyException e) {
            System.out.println("❌ No puedes robar carta: " + e.getMessage());
            showError("Deck empty", e.getMessage());
        }
    }

    // ------------------ ELIMINACIONES ------------------

    private void checkEliminations() {
        for (IPlayer player : game.getPlayers()) {
            if (!player.isEliminated() && game.mustBeEliminated(player)) {
                game.eliminatePlayer(player);
                System.out.println(player.getName() + " ha sido eliminado.");
            }
        }
    }

    // ------------------ TURNOS ------------------

    private void endHumanTurn() {
        playMachinesTurn();
    }

    private void playMachinesTurn() {
        List<IPlayer> machines = game.getPlayers().subList(1, game.getPlayers().size());
        playNextMachine(machines, 0);
    }

    private void playNextMachine(List<IPlayer> machines, int index) {
        if (index >= machines.size()) {
            checkGameOver();
            Platform.runLater(() -> hasPlayedThisTurn = false);
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
                try {
                    game.playCard(machine, chosen);
                    System.out.println(machine.getName() + " jugó " + chosen + " (total: " + game.getTableSum() + ")");
                    game.drawCard(machine);
                } catch (InvalidCardPlayException | DeckEmptyException ex) {
                    System.out.println("⚠️ Error en turno de máquina: " + ex.getMessage());
                }
            }

            updateView();
            checkEliminations(); // 🔹 Verificar eliminaciones después de cada turno de máquina
            playNextMachine(machines, index + 1);
        });
        pause.play();
    }

    // ------------------ FIN DEL JUEGO ------------------

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
