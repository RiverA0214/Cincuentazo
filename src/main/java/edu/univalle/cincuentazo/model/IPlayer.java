package edu.univalle.cincuentazo.model;

import java.util.List;

/**
 * Represents a player in the Cincuentazo game.
 * <p>
 * Defines the basic operations that all players (human or machine) must implement,
 * including managing the player's hand, checking if a move is possible, and taking turns.
 * </p>
 *
 * @see AbstractPlayer
 * @see HumanPlayer
 * @see MachinePlayer
 * @since 1.0
 */
public interface IPlayer {

    /**
     * Returns the player's name.
     *
     * @return the name of the player
     */
    String getName();

    /**
     * Checks if this player is a machine.
     *
     * @return true if the player is controlled by the computer, false if human
     */
    boolean isMachine();

    /**
     * Checks if the player has been eliminated from the game.
     *
     * @return true if eliminated, false otherwise
     */
    boolean isEliminated();

    /**
     * Sets the elimination status of the player.
     *
     * @param eliminated true to mark the player as eliminated, false otherwise
     */
    void setEliminated(boolean eliminated);
    /**
     * Returns the current hand of the player.
     *
     * @return a list of cards held by the player
     */
    List<Card> getHand();

    /**
     * Adds a card to the player's hand.
     *
     * @param c the card to add
     */
    void addCard(Card c);

    /**
     * Removes a card from the player's hand.
     *
     * @param c the card to remove
     */
    void removeCard(Card c);

    /**
     * Determines whether the player can play any card without exceeding
     * the table limit.
     *
     * @param game the current game instance
     * @return true if the player has at least one playable card, false otherwise
     */
    boolean canPlay(Game game);

    /**
     * Performs the player's turn in the game.
     * <p>
     * The implementation may differ between human and machine players.
     * For humans, moves are handled manually via the GUI.
     * </p>
     *
     * @param game the current game instance
     */
    void playTurn(Game game);
}

