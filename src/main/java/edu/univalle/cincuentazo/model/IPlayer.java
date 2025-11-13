package edu.univalle.cincuentazo.model;

import java.util.List;

public interface IPlayer {
    String getName();
    boolean isMachine();
    boolean isEliminated();
    void setEliminated(boolean eliminated);

    List<Card> getHand();
    void addCard(Card c);
    void removeCard(Card c);

    boolean canPlay(Game game);
    void playTurn(Game game);
}

