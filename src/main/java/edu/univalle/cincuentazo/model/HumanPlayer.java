package edu.univalle.cincuentazo.model;

/**
 * Represents a human player in the Cincuentazo game.
 * <p>
 * Extends {@link AbstractPlayer} and implements the {@link IPlayer} interface.
 * Human players make their moves manually via the GUI, so the {@link #playTurn(Game)}
 * method does not perform automatic actions.
 * </p>
 *
 * @see AbstractPlayer
 * @see IPlayer
 * @since 1.0
 */
public class HumanPlayer extends AbstractPlayer {

    /**
     * Constructs a new human player with the given name.
     *
     * @param name the name of the player
     */
    public HumanPlayer(String name) {
        super(name, false);
    }

    /**
     * Performs the player's turn in the game.
     * <p>
     * For human players, the move is performed manually via the GUI.
     * This method only logs a message and does not perform any automatic actions.
     * </p>
     *
     * @param game the current game instance
     */
    @Override
    public void playTurn(Game game) {
        // El jugador humano juega manualmente desde la GUI,
        // así que aquí solo se deja la llamada vacía.
        System.out.println(name + " debe jugar su turno manualmente.");
    }
}
