package edu.univalle.cincuentazo.model;

import edu.univalle.cincuentazo.exceptions.DeckEmptyException;
import edu.univalle.cincuentazo.exceptions.InvalidCardPlayException;

import java.util.*;

/**
 * Represents a game of Cincuentazo.
 * <p>
 * This class manages the game state, including players, deck, table,
 * card values, eliminated players, and the game rules.
 * </p>
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *     <li>Initializing the deck and shuffling it</li>
 *     <li>Creating human and machine players</li>
 *     <li>Dealing initial cards</li>
 *     <li>Managing table state and card plays</li>
 *     <li>Handling card draws from the deck</li>
 *     <li>Checking and eliminating players</li>
 *     <li>Determining the game winner</li>
 * </ul>
 * </p>
 *
 * @see IPlayer
 * @see Card
 * @see InvalidCardPlayException
 * @see DeckEmptyException
 * @since 1.0
 */
public class Game {

    private final List<Card> deck = new ArrayList<>();
    private final List<IPlayer> players = new ArrayList<>();
    private final List<Card> table = new ArrayList<>(); // mesa de juego
    private Card currentTableCard;
    private int tableSum;
    private final Map<Card, Integer> values = new HashMap<>();
    private final EliminatedPlayers eliminatedPlayers = new EliminatedPlayers();

    /**
     * Constructs a new game with the specified number of machine players.
     * <p>
     * Initializes the deck, shuffles it, creates players, deals initial cards,
     * and sets up the table.
     * </p>
     *
     * @param machinePlayers the number of machine-controlled players
     */
    public Game(int machinePlayers) {
        loadDeck();
        Collections.shuffle(deck);
        createPlayers(machinePlayers);
        dealInitialCards();
        initializeTable();
    }


    private void loadDeck() {
        deck.clear();
        deck.addAll(Arrays.asList(Card.values()));

        for (Card c : deck) {
            String fileName = c.getFileName();
            int value = Integer.parseInt(fileName.substring(1, 3));
            if (value >= 11 && value <= 13) value = -10;
            values.put(c, value);
        }
    }


    private void createPlayers(int machinePlayers) {
        players.add(new HumanPlayer("Tú"));
        for (int i = 1; i <= machinePlayers; i++) {
            players.add(new MachinePlayer("Máquina " + i));
        }
    }


    private void dealInitialCards() {
        for (int i = 0; i < 4; i++) {
            for (IPlayer p : players) {
                p.addCard(deck.remove(0));
            }
        }
    }

    private void initializeTable() {
        currentTableCard = deck.remove(0);
        table.add(currentTableCard);
        tableSum = getCardValue(currentTableCard);
    }

    /**
     * Returns the value of a given card.
     *
     * @param card the card
     * @return the card's numeric value
     */
    public int getCardValue(Card card) {
        return values.getOrDefault(card, 0);
    }

    /**
     * Returns the current sum of card values on the table.
     *
     * @return the table sum
     */
    public int getTableSum() {
        return tableSum;
    }

    /**
     * Returns the current card on top of the table.
     *
     * @return the top table card
     */
    public Card getCurrentTableCard() {
        return currentTableCard;
    }

    /**
     * Returns the list of players in the game.
     *
     * @return the players list
     */
    public List<IPlayer> getPlayers() {
        return players;
    }

    /**
     * Plays a card for a player.
     *
     * @param player the player playing the card
     * @param card   the card to play
     * @return true if the play was successful
     * @throws InvalidCardPlayException if the play exceeds the table limit
     */
    public boolean playCard(IPlayer player, Card card) throws InvalidCardPlayException {
        int cardValue = getCardValue(card);
        if (tableSum + cardValue > 50) {
            throw new InvalidCardPlayException("La carta supera el límite de 50.");
        }
        tableSum += cardValue;
        currentTableCard = card;
        table.add(card);
        player.removeCard(card);
        return true;
    }

    /**
     * Draws a card from the deck for the specified player.
     * <p>
     * If the deck is empty, it refills from the table except the last card.
     * </p>
     *
     * @param player the player drawing a card
     * @throws DeckEmptyException if the deck cannot be replenished
     */
    public void drawCard(IPlayer player) {
        if (player.getHand().size() >= 4) return;

        if (deck.isEmpty()) {
            if (table.size() <= 1) {
                throw new DeckEmptyException("No hay cartas suficientes para recargar el mazo.");
            }
            // Tomar todas las cartas de la mesa excepto la última
            List<Card> newDeck = new ArrayList<>(table.subList(0, table.size() - 1));
            // Mantener solo la última carta en la mesa
            table.retainAll(Collections.singletonList(currentTableCard));
            Collections.shuffle(newDeck);
            deck.addAll(newDeck);
        }

        // Robar la carta superior
        Card drawn = deck.remove(0);
        player.addCard(drawn);
    }

    /**
     * Determines whether a player must be eliminated.
     *
     * @param player the player to check
     * @return true if the player cannot play any card without exceeding 50
     */
    public boolean mustBeEliminated(IPlayer player) {
        for (Card c : player.getHand()) {
            if (tableSum + getCardValue(c) <= 50) return false;
        }
        return true;
    }

    /**
     * Eliminates a player from the game.
     * <p>
     * Moves the player's cards back to the deck, clears their hand,
     * marks them eliminated, and stores them in the eliminated players set.
     * </p>
     *
     * @param player the player to eliminate
     */
    public void eliminatePlayer(IPlayer player) {
        // Mover cartas al mazo
        deck.addAll(player.getHand());
        player.getHand().clear();

        // Marcar como eliminado
        player.setEliminated(true);

        // Guardar en la estructura Set de eliminados
        eliminatedPlayers.add(player);
    }

    /**
     * Returns all eliminated players.
     *
     * @return a set of eliminated players
     */
    public Set<IPlayer> getEliminatedPlayers() {
        return eliminatedPlayers.getAll();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if only one or no players remain active
     */
    public boolean isGameOver() {
        long activeCount = players.stream()
                .filter(p -> !p.isEliminated())
                .count();
        return activeCount <= 1;
    }

    /**
     * Returns the winner of the game.
     *
     * @return the remaining active player, or null if none
     */
    public IPlayer getWinner() {
        return players.stream()
                .filter(p -> !p.isEliminated())
                .findFirst()
                .orElse(null);
    }

    // ---------------------------
    // Unit test helpers
    public int getDeckSize() {
        return deck.size();
    }

    public void setTableSum(int sum) {
        this.tableSum = sum;
    }

    public void setCardValue(Card card, int value) {
        values.put(card, value);
    }

}
