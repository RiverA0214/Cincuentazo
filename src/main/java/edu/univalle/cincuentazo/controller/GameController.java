package edu.univalle.cincuentazo.controller;

import edu.univalle.cincuentazo.model.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML private GridPane playerCardsGrid;
    @FXML private HBox machineCardsArea;
    @FXML private ImageView tableCardImage;
    @FXML private ImageView deckImage;
    @FXML private Label sumLabel;

    private List<Card> deck = new ArrayList<>();
    private List<Card> playerHand = new ArrayList<>();
    private List<Card> machine1Hand = new ArrayList<>();

    private int tableSum = 0;
    private Card currentTableCard;
    private int numMachines;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Carga inicial (sin repartir todavía)
        loadDeck();
        deckImage.setImage(new Image(
                getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png").toExternalForm()
        ));
    }

    private void loadDeck() {
        deck.clear();
        deck.addAll(Arrays.asList(Card.values()));
        System.out.println("Cartas cargadas: " + deck.size());
    }

    private void dealInitialCards(int numMachines) {
        for (int i = 0; i < 4; i++) {
            playerHand.add(deck.remove(0));
            if (numMachines >= 1) machine1Hand.add(deck.remove(0));
        }
    }

    private void displayHands() {
        playerCardsGrid.getChildren().clear();

        // Mostrar mano del jugador humano (boca arriba)
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            URL url = getClass().getResource(card.getResourcePath());

            if (url != null) {
                ImageView img = new ImageView(new Image(url.toExternalForm()));
                img.setFitHeight(120);
                img.setFitWidth(80);
                playerCardsGrid.add(img, i, 0);
            } else {
                System.err.println("No se encontró imagen de: " + card.getResourcePath());
            }
        }

        // Mostrar cartas de la máquina boca abajo
        machineCardsArea.getChildren().clear();
        Image backImage = new Image(
                getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png").toExternalForm());
        for (int i = 0; i < machine1Hand.size(); i++) {
            ImageView back = new ImageView(backImage);
            back.setFitHeight(100);
            back.setFitWidth(70);
            machineCardsArea.getChildren().add(back);
        }
    }

    private void initializeTable() {
        currentTableCard = deck.remove(0);

        URL url = getClass().getResource(currentTableCard.getResourcePath());
        if (url != null) {
            tableCardImage.setImage(new Image(url.toExternalForm()));
        }

        tableSum = getCardValue(currentTableCard);
        sumLabel.setText("Suma: " + tableSum);
    }

    private int getCardValue(Card card) {
        String fileName = card.getFileName(); // ejemplo "s13.png"
        String number = fileName.substring(1, 3); // "13"
        return Integer.parseInt(number);
    }

    public void startGame(int numMachines) {
        this.numMachines = numMachines;

        loadDeck();
        Collections.shuffle(deck);
        dealInitialCards(numMachines);
        displayHands();
        initializeTable();
    }

    @FXML
    private void onHandleTakeCard() {
        System.out.println("El jugador tomó una carta.");
        // Más adelante: sacar carta del mazo y actualizar interfaz
    }

    public void initializeGame(edu.univalle.cincuentazo.model.GameConfig config) {
        System.out.println("Juego iniciado con " + config.getMachinePlayers() + " jugadores máquina.");
    }
}
