package edu.univalle.cincuentazo.model;

import edu.univalle.cincuentazo.exceptions.DeckEmptyException;
import edu.univalle.cincuentazo.exceptions.InvalidCardPlayException;

import java.util.*;

public class Game {

    private final List<Card> deck = new ArrayList<>();
    private final List<IPlayer> players = new ArrayList<>();
    private final List<Card> table = new ArrayList<>(); // mesa de juego
    private Card currentTableCard;
    private int tableSum;
    private final Map<Card, Integer> values = new HashMap<>();
    private final EliminatedPlayers eliminatedPlayers = new EliminatedPlayers();

    public Game(int machinePlayers) {
        loadDeck();
        Collections.shuffle(deck);
        createPlayers(machinePlayers);
        dealInitialCards();
        initializeTable();
    }

    // ---------------------------
    // Carga y valores del mazo
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

    // ---------------------------
    // Crear jugadores
    private void createPlayers(int machinePlayers) {
        players.add(new HumanPlayer("Tú"));
        for (int i = 1; i <= machinePlayers; i++) {
            players.add(new MachinePlayer("Máquina " + i));
        }
    }

    // ---------------------------
    // Repartir cartas iniciales
    private void dealInitialCards() {
        for (int i = 0; i < 4; i++) {
            for (IPlayer p : players) {
                p.addCard(deck.remove(0));
            }
        }
    }

    // ---------------------------
    // Inicializar mesa
    private void initializeTable() {
        currentTableCard = deck.remove(0);
        table.add(currentTableCard);
        tableSum = getCardValue(currentTableCard);
    }

    // ---------------------------
    public int getCardValue(Card card) {
        return values.getOrDefault(card, 0);
    }

    public int getTableSum() {
        return tableSum;
    }

    public Card getCurrentTableCard() {
        return currentTableCard;
    }

    public List<IPlayer> getPlayers() {
        return players;
    }

    // ---------------------------
    // Jugar carta
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

    // ---------------------------
    // Robar carta del mazo
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

    // ---------------------------
    // Verificar si jugador debe ser eliminado
    public boolean mustBeEliminated(IPlayer player) {
        for (Card c : player.getHand()) {
            if (tableSum + getCardValue(c) <= 50) return false;
        }
        return true;
    }

    // ---------------------------
    // Eliminar jugador
    public void eliminatePlayer(IPlayer player) {
        // Mover cartas al mazo
        deck.addAll(player.getHand());
        player.getHand().clear();

        // Marcar como eliminado
        player.setEliminated(true);

        // Guardar en la estructura Set de eliminados
        eliminatedPlayers.add(player);
    }

    public Set<IPlayer> getEliminatedPlayers() {
        return eliminatedPlayers.getAll();
    }

    // ---------------------------
    // Fin de juego
    public boolean isGameOver() {
        long activeCount = players.stream()
                .filter(p -> !p.isEliminated())
                .count();
        return activeCount <= 1;
    }

    public IPlayer getWinner() {
        return players.stream()
                .filter(p -> !p.isEliminated())
                .findFirst()
                .orElse(null);
    }

    // ---------------------------
    // Para pruebas unitarias
    public int getDeckSize() {
        return deck.size();
    }

    public void setTableSum(int sum) {
        this.tableSum = sum;
    }
}
