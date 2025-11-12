package edu.univalle.cincuentazo.controller;

import edu.univalle.cincuentazo.model.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    private List<Card> deck = new ArrayList<>();
    private List<Card> playerHand = new ArrayList<>();
    private List<Card> machine1Hand = new ArrayList<>();
    private List<Card> machine2Hand = new ArrayList<>();
    private List<Card> machine3Hand = new ArrayList<>();


    private int tableSum = 0;
    private Card currentTableCard;
    private int numMachines;

    private final Map<Card, Image> imageCache = new HashMap<>();

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
        deck.addAll(Arrays.asList(Card.values())); // deck es List<Card>

        // cargar imágenes en la caché
        imageCache.clear();
        for (Card c : Card.values()) {
            URL url = getClass().getResource(c.getResourcePath());
            if (url == null) {
                System.err.println("❌ No se encontró la carta: " + c.getResourcePath());
                continue;
            }
            imageCache.put(c, new Image(url.toExternalForm()));
        }

        // cargar imagen de reverso también
        URL backUrl = getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png");
        if (backUrl != null) {
            imageCache.put(null, new Image(backUrl.toExternalForm())); // opcional: usar null key para back
        }

        System.out.println("Cartas cargadas (enum): " + deck.size());
    }


    private void dealInitialCards(int numMachines) {
        for (int i = 0; i < 4; i++) {
            playerHand.add(deck.remove(0));
            if (numMachines >= 1) machine1Hand.add(deck.remove(0));
            if (numMachines >= 2) machine2Hand.add(deck.remove(0));
            if (numMachines >= 3) machine3Hand.add(deck.remove(0));
        }
    }

    private void displayHands() {
        // === Cartas del jugador humano (abajo) ===
        playerCardsGrid.getChildren().clear();
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            URL url = getClass().getResource(card.getResourcePath());
            if (url == null) {
                System.err.println("❌ No se encontró la imagen para " + card);
                continue;
            }
            Image image = new Image(url.toExternalForm());
            ImageView img = new ImageView(image);
            img.setFitHeight(120);
            img.setFitWidth(80);
            playerCardsGrid.add(img, i, 0);
        }

        // === Cartas de los jugadores máquina ===
        URL backUrl = getClass().getResource("/edu/univalle/cincuentazo/cards/card_back.png");
        if (backUrl == null) {
            System.err.println("No se encontró la carta reverso (card_back.png)");
            return;
        }
        Image backImage = new Image(backUrl.toExternalForm());

        // Máquina superior (machine1)
        machineTopArea.getChildren().clear();
        for (int i = 0; i < machine1Hand.size(); i++) {
            ImageView back = new ImageView(backImage);
            back.setFitHeight(80);
            back.setFitWidth(55);
            machineTopArea.getChildren().add(back);
        }

        // Máquina izquierda (machine2)
        machineLeftArea.getChildren().clear();
        for (int i = 0; i < machine2Hand.size(); i++) {
            ImageView back = new ImageView(backImage);
            back.setFitHeight(55);
            back.setFitWidth(80);
            machineLeftArea.getChildren().add(back);
        }

        // Máquina derecha (machine3)
        machineRightArea.getChildren().clear();
        for (int i = 0; i < machine3Hand.size(); i++) {
            ImageView back = new ImageView(backImage);
            back.setFitHeight(55);
            back.setFitWidth(80);
            machineRightArea.getChildren().add(back);
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
}
