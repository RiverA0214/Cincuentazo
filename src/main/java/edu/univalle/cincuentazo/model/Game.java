package edu.univalle.cincuentazo.model;

import java.util.*;

public class Game {

    private final List<Card> deck = new ArrayList<>();
    private final List<IPlayer> players = new ArrayList<>();
    private Card currentTableCard;
    private int tableSum;
    private final Map<Card, Integer> values = new HashMap<>();

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
        tableSum = getCardValue(currentTableCard);
    }

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

    public boolean playCard(IPlayer player, Card card) {
        int cardValue = getCardValue(card);
        if (tableSum + cardValue > 50) {
            return false;
        }
        tableSum += cardValue;
        currentTableCard = card;
        player.removeCard(card);
        return true;
    }

    public void drawCard(IPlayer player) {
        if (!deck.isEmpty() && player.getHand().size() < 4) {
            player.addCard(deck.remove(0));
        }
    }

    public boolean mustBeEliminated(IPlayer player) {
        for (Card c : player.getHand()) {
            if (tableSum + getCardValue(c) <= 50) return false;
        }
        return true;
    }

    public void eliminatePlayer(IPlayer player) {
        deck.addAll(player.getHand());
        player.getHand().clear();
        player.setEliminated(true);
    }

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

}

