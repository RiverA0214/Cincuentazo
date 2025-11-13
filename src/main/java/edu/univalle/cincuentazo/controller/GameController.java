package edu.univalle.cincuentazo.controller;

import edu.univalle.cincuentazo.exceptions.DeckEmptyException;
import edu.univalle.cincuentazo.exceptions.InvalidCardPlayException;
import edu.univalle.cincuentazo.model.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

/**
 * Controller class for the main game GUI of Cincuentazo.
 * <p>
 * Manages the interactions between the game model ({@link Game}) and the
 * JavaFX view. Handles user input for the human player, updates the
 * game state, animates machine players' turns, and updates GUI elements
 * such as card displays, table sum, and turn messages.
 * </p>
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *     <li>Starting a new game and initializing the view</li>
 *     <li>Handling human player actions: playing cards and drawing from the deck</li>
 *     <li>Handling special rules, such as Ace value selection</li>
 *     <li>Updating the GUI to display hands, table, and machine players</li>
 *     <li>Executing machine player turns automatically with delays</li>
 *     <li>Checking for eliminated players and ending the game</li>
 *     <li>Displaying messages and alerts to the user</li>
 * </ul>
 *
 * @see Game
 * @see IPlayer
 * @see Card
 * @since 1.0
 */
public class GameController implements Initializable {

    @FXML private GridPane playerCardsGrid;
    @FXML private HBox machineTopArea;
    @FXML private VBox machineLeftArea;
    @FXML private VBox machineRightArea;
    @FXML private ImageView tableCardImage;
    @FXML private ImageView deckImage;
    @FXML private Label sumLabel;
    @FXML private HBox aceChoiceBox;
    @FXML private Button aceOneButton;
    @FXML private Button aceTenButton;
    @FXML private Label turnMessageLabel;


    /** The Ace card awaiting value selection. */
    private Card pendingAce;

    /** The game model instance. */
    private Game game;

    /** Reference to the human player. */
    private IPlayer humanPlayer;

    /** Tracks if the human has played a card this turn. */
    private boolean hasPlayedThisTurn = false;

    /** Indicates whether it is currently the human player's turn. */
    private boolean humanTurn = true;


    /**
     * Initializes the controller.
     * <p>
     * Sets the deck image to the back of a card.
     * </p>
     *
     * @param location  the location used to resolve relative paths
     * @param resources the resources used for localization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deckImage.setImage(new Image(getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png").toExternalForm()));
    }

    /**
     * Starts a new game with the specified number of machine players.
     *
     * @param numMachines the number of machine-controlled players
     */
    public void startGame(int numMachines) {
        game = new Game(numMachines);
        humanPlayer = game.getPlayers().get(0);
        updateView();
    }


    /**
     * Displays a temporary message on the screen for a given duration.
     *
     * @param message the message text
     * @param seconds duration in seconds to show the message
     */
    private void showTemporaryMessage(String message, int seconds) {
        turnMessageLabel.setText(message); // mostrar mensaje
        turnMessageLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> turnMessageLabel.setVisible(false)); // ocultar mensaje
        pause.play();
    }

    /**
     * Updates the table view and all hands in the GUI.
     */
    private void updateView() {
        tableCardImage.setImage(new Image(getClass().getResource(game.getCurrentTableCard().getResourcePath()).toExternalForm()));
        sumLabel.setText("Suma: " + game.getTableSum());
        displayHands();
        displayMachineHands();
    }

    /**
     * Displays the human player's hand with clickable cards.
     */
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

    /**
     * Displays machine players' hands as card backs.
     */
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

    /**
     * Updates the GUI controls depending on whose turn it is.
     */
    private void updateControls() {
        playerCardsGrid.setDisable(!humanTurn); // deshabilita click en cartas
        deckImage.setDisable(!humanTurn);       // deshabilita mazo
    }


    /**
     * Shows an error alert to the user.
     *
     * @param title the alert title
     * @param msg   the message content
     */
    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Handles the action when a human player clicks a card to play.
     *
     * @param card the card clicked by the player
     */
    private void playCard(Card card) {
        try {
            if (!humanTurn) return;

            if (hasPlayedThisTurn) {
                showError("Invalid action", "You already played a card this turn. Draw from the deck!");
                return;
            }

            if (card.isAce()) {
                // Guardamos el As pendiente y mostramos las opciones
                pendingAce = card;
                aceChoiceBox.setVisible(true);
                aceOneButton.setOnAction(e -> chooseAceValue(1));
                aceTenButton.setOnAction(e -> chooseAceValue(10));
                return;
            }

            // Jugada normal
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

    /**
     * Handles the human player drawing a card from the deck.
     */
    @FXML
    private void onDeckClicked() {
        try {
            if (!humanTurn) return;

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
    /**
     * Allows the human player to choose the value of an Ace.
     *
     * @param value the chosen value (1 or 10)
     */
    private void chooseAceValue(int value) {
        try {
            // Asignamos temporalmente el valor del As
            game.setCardValue(pendingAce, value);

            if (game.playCard(humanPlayer, pendingAce)) {
                System.out.println("Jugador jugó As como " + value + ". Suma = " + game.getTableSum());
                hasPlayedThisTurn = true;
                updateView();
            }

        } catch (InvalidCardPlayException e) {
            showError("Invalid play", e.getMessage());
        } finally {
            pendingAce = null;
            aceChoiceBox.setVisible(false);
        }
    }

    /**
     * Checks all players and eliminates any who cannot play a valid card.
     */
    private void checkEliminations() {
        for (IPlayer player : game.getPlayers()) {
            if (!player.isEliminated() && game.mustBeEliminated(player)) {
                game.eliminatePlayer(player);
                System.out.println(player.getName() + " ha sido eliminado.");
            }
        }
    }


    /**
     * Starts the human player's turn and updates controls.
     */
    private void startHumanTurn() {
        humanTurn = true;
        updateControls();
        hasPlayedThisTurn = false;
        showTemporaryMessage("¡Es tu turno!", 2); // mensaje de 2 segundos
    }

    /**
     * Ends the human player's turn and triggers machine turns.
     */
    private void endHumanTurn() {
        humanTurn = false;
        updateControls();
        playMachinesTurn();
    }

    /**
     * Executes the machine players' turns sequentially with delays.
     */
    private void playMachinesTurn() {
        List<IPlayer> machines = game.getPlayers().subList(1, game.getPlayers().size());
        playNextMachine(machines, 0);
    }

    /**
     * Plays the next machine player's turn recursively.
     *
     * @param machines the list of machine players
     * @param index    the current machine index
     */
    private void playNextMachine(List<IPlayer> machines, int index) {
        if (index >= machines.size()) {
            checkGameOver();
            // Activar turno humano
            Platform.runLater(this::startHumanTurn);

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


    /**
     * Checks if the game has ended and shows the winner message.
     */
    private void checkGameOver() {
        if (game.isGameOver()) {
            IPlayer winner = game.getWinner();
            System.out.println("🎉 ¡El juego ha terminado! Ganador: " + winner.getName());
            endGameMessage(winner);
        }
    }

    /**
     * Displays the end-of-game message with the winner's name.
     *
     * @param winner the winning player
     */
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
