package edu.univalle.cincuentazo.controller;

import edu.univalle.cincuentazo.model.Card;
import javafx.application.Platform;
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

import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class GameController implements Initializable {

    @FXML private GridPane playerCardsGrid;
    @FXML private HBox machineTopArea;
    @FXML private VBox machineLeftArea;
    @FXML private VBox machineRightArea;

    @FXML private ImageView tableCardImage; //imagen central de la mesa
    @FXML private ImageView deckImage;
    @FXML private Label sumLabel;

    private List<Card> deck = new ArrayList<>();
    private List<Card> playerHand = new ArrayList<>();
    private List<Card> machine1Hand = new ArrayList<>();
    private List<Card> machine2Hand = new ArrayList<>();
    private List<Card> machine3Hand = new ArrayList<>();


    private int tableSum = 0; //suma de la mesa
    private Card currentTableCard;
    private int numMachines;

    private final Map<Card, Image> imageCache = new HashMap<>();

    private boolean canDraw = false; // Controla si el jugador puede robar

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
                System.err.println("No se encontró la carta: " + c.getResourcePath());
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
                System.err.println("No se encontró la imagen para " + card);
                continue;
            }
            Image image = new Image(url.toExternalForm());
            ImageView img = new ImageView(image);
            img.setFitHeight(120);
            img.setFitWidth(80);

            //al hacer click
            img.setOnMouseClicked(e -> playCard(card));

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
        int value = Integer.parseInt(fileName.substring(1, 3)); // obtiene 13, 11, etc.

        // Ajustar valores de J, Q, K
        if (value >= 11 && value <= 13) {
            value = -10;
        }

        return value;
    }

    public void startGame(int numMachines) {
        this.numMachines = numMachines;

        loadDeck();
        Collections.shuffle(deck);
        dealInitialCards(numMachines);
        System.out.println("Esperando a que el jugador juegue una carta.");
        displayHands();
        initializeTable();
    }

    private void playCard(Card card) {
        int cardValue = getCardValue(card);

        if (tableSum + cardValue > 50) {
            System.out.println("No puedes jugar esta carta, sumaria mas de 50.");
            return;
        }

        //Juega la carta
        tableSum += cardValue;
        currentTableCard = card;
        playerHand.remove(card);

        // Actualiza la mesa
        updateTableCard(card);

        // Refresca la mano
        displayHands();

        System.out.println("Jugaste " + card + ". Suma actual: " + tableSum);

        canDraw = true; //ahora puede comer haciendo click en el maso
        System.out.println("Esperando a que el jugador tome una carta del mazo.");
    }

    private void updateTableCard(Card card) {
        try {
            Image cardImage = new Image(
                    getClass().getResource("/edu/univalle/cincuentazo/cards/" + card.getFileName()).toExternalForm()
            );
            tableCardImage.setImage(cardImage);
            sumLabel.setText("Suma: " + tableSum);
        } catch (Exception e) {
            System.err.println("No se pudo actualizar la carta en la mesa: " + card.getFileName());
        }
    }


    private void playMachinesTurnSequential() {
        List<List<Card>> machines = List.of(machine1Hand, machine2Hand, machine3Hand);
        playNextMachine(machines, 0); // Empieza con la primera
    }

    private void playNextMachine(List<List<Card>> machines, int index) {
        if (index >= machines.size()){
            PauseTransition pause = new PauseTransition(Duration.seconds(4));
            pause.setOnFinished(e -> System.out.println("Esperando a que el jugador juegue una carta."));
            pause.play();
            return;
        }

        List<Card> machine = machines.get(index);
        if (machine.isEmpty()) {
            playNextMachine(machines, index + 1);
            return;
        }

        int delay = (int) (2000 + Math.random() * 2000); // 2 a 4 segundos
        System.out.println("Máquina " + (index + 1) + " jugará en " + delay / 1000.0 + " segundos...");

        PauseTransition pause = new PauseTransition(Duration.millis(delay));
        pause.setOnFinished(e -> {
            Card chosen = null;
            for (Card c : machine) {
                if (tableSum + getCardValue(c) <= 50) {
                    chosen = c;
                    break;
                }
            }

            if (chosen != null) {
                Card finalChosen = chosen;
                tableSum += getCardValue(finalChosen);
                currentTableCard = finalChosen;
                machine.remove(finalChosen);
                updateTableCard(finalChosen);
                displayHands();
                System.out.println("Máquina " + (index + 1) + " jugó " + finalChosen + ". Total: " + tableSum);

                drawCard(machine, true);
            } else {
                System.out.println("Máquina " + (index + 1) + " pasa su turno (no puede jugar sin exceder 50).");
            }

            // Llamar recursivamente a la siguiente máquina
            playNextMachine(machines, index + 1);
        });

        pause.play();
    }

    private void drawCard(List<Card> hand, boolean isMachine) {
        if (!deck.isEmpty() && hand.size() < 4) {
            Card newCard = deck.remove(0); // Roba del mazo

            if (isMachine) {
                // Esperar entre 1 y 2 segundos antes de tomarla
                int delay = (int) (1000 + Math.random() * 1000);
                PauseTransition pause = new PauseTransition(Duration.millis(delay));
                pause.setOnFinished(e -> {
                    hand.add(newCard);
                    displayHands();
                    System.out.println("Máquina robó una carta después de " + delay / 1000.0 + " segundos.");
                });
                pause.play();

            } else {
                // Jugador humano roba inmediatamente (boca arriba)
                hand.add(newCard);
                displayHands();
                System.out.println("Jugador robó una carta: " + newCard);
            }
        }
    }

    @FXML
    private void onDeckClicked() {
        if (canDraw) {
            drawCard(playerHand, false);
            canDraw = false; // Evita que robe más de una vez
            System.out.println("Jugador tomó una carta del mazo.");

            // Inicia el turno de las máquinas
            playMachinesTurnSequential();
        } else {
            System.out.println("Aún no puedes tomar carta, primero juega una.");
        }
    }
}
