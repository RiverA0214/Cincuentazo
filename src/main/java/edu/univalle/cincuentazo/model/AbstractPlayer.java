package edu.univalle.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer implements IPlayer {

    protected final String name;
    protected final boolean isMachine;
    protected final List<Card> hand;
    protected boolean eliminated = false;

    public AbstractPlayer(String name, boolean isMachine) {
        this.name = name;
        this.isMachine = isMachine;
        this.hand = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isMachine() {
        return isMachine;
    }

    @Override
    public boolean isEliminated() {
        return eliminated;
    }

    @Override
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    @Override
    public List<Card> getHand() {
        return hand;
    }

    @Override
    public void addCard(Card c) {
        hand.add(c);
    }

    @Override
    public void removeCard(Card c) {
        hand.remove(c);
    }

    @Override
    public boolean canPlay(Game game) {
        for (Card c : hand) {
            if (game.getTableSum() + game.getCardValue(c) <= 50) {
                return true;
            }
        }
        return false;
    }

    @Override
    public abstract void playTurn(Game game);
}
