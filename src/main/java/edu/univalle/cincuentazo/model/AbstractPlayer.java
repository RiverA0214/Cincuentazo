package edu.univalle.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract base class for a player in the Cincuentazo game.
 * <p>
 * Implements the {@link IPlayer} interface and provides common functionality
 * for both human and machine players, including managing the player's hand,
 * elimination status, and basic playability checks.
 * </p>
 *
 * @see IPlayer
 * @since 1.0
 */
public abstract class AbstractPlayer implements IPlayer {

    /** The name of the player. */
    protected final String name;

    /** Indicates whether this player is a machine. */
    protected final boolean isMachine;

    /** The list of cards currently held by the player. */
    protected final List<Card> hand;

    /** Indicates whether the player has been eliminated from the game. */
    protected boolean eliminated = false;

    /**
     * Constructs a new player with a given name and type.
     *
     * @param name      the player's name
     * @param isMachine true if the player is controlled by the computer
     */
    public AbstractPlayer(String name, boolean isMachine) {
        this.name = name;
        this.isMachine = isMachine;
        this.hand = new ArrayList<>();
    }

    /**
     * Returns the player's name.
     *
     * @return the name of the player
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Checks if this player is a machine.
     *
     * @return true if the player is a machine, false otherwise
     */
    @Override
    public boolean isMachine() {
        return isMachine;
    }

    /**
     * Checks if this player has been eliminated.
     *
     * @return true if eliminated, false otherwise
     */
    @Override
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Sets the elimination status of the player.
     *
     * @param eliminated true to mark the player as eliminated, false otherwise
     */
    @Override
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    /**
     * Returns the current hand of the player.
     *
     * @return a list of cards held by the player
     */
    @Override
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param c the card to add
     */
    @Override
    public void addCard(Card c) {
        hand.add(c);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param c the card to remove
     */
    @Override
    public void removeCard(Card c) {
        hand.remove(c);
    }

    /**
     * Determines if the player can make a valid move in the current game state.
     * <p>
     * A player can play if any card in their hand does not cause the table sum
     * to exceed 50.
     * </p>
     *
     * @param game the current game instance
     * @return true if the player has at least one playable card, false otherwise
     */
    @Override
    public boolean canPlay(Game game) {
        for (Card c : hand) {
            if (game.getTableSum() + game.getCardValue(c) <= 50) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs the player's turn in the game.
     * <p>
     * This method must be implemented by subclasses to define the specific
     * behavior for human or machine players.
     * </p>
     *
     * @param game the current game instance
     */
    @Override
    public abstract void playTurn(Game game);
}
