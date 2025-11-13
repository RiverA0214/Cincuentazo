package edu.univalle.cincuentazo.model;

import edu.univalle.cincuentazo.exceptions.InvalidCardPlayException;

/**
 * Represents a machine-controlled player in the Cincuentazo game.
 * <p>
 * Extends {@link AbstractPlayer} and implements automatic behavior for
 * taking turns according to the game rules.
 * </p>
 *
 * <p>
 * On its turn, the machine player chooses the first card in its hand that
 * does not make the table sum exceed 50, plays it, and then draws a card
 * from the deck if possible. If no valid card is available, the machine
 * passes its turn.
 * </p>
 *
 * @see AbstractPlayer
 * @see IPlayer
 * @since 1.0
 */
public class MachinePlayer extends AbstractPlayer {

    /**
     * Constructs a new machine player with the given name.
     *
     * @param name the name of the machine player
     */
    public MachinePlayer(String name) {
        super(name, true);
    }

    /**
     * Performs the machine player's turn automatically.
     * <p>
     * Selects the first playable card that does not exceed the table limit of 50,
     * plays it, and draws a card from the deck. If no card can be played, the
     * machine passes its turn.
     * </p>
     *
     * @param game the current game instance
     * @throws RuntimeException if a card play fails due to an {@link InvalidCardPlayException}
     */
    @Override
    public void playTurn(Game game) {
        Card chosen = null;
        for (Card c : hand) {
            if (game.getTableSum() + game.getCardValue(c) <= 50) {
                chosen = c;
                break;
            }
        }

        if (chosen != null) {
            hand.remove(chosen);
            try {
                game.playCard(this, chosen);
            } catch (InvalidCardPlayException e) {
                throw new RuntimeException(e);
            }
            System.out.println(name + " jugó " + chosen + " (total: " + game.getTableSum() + ")");
            game.drawCard(this);
        } else {
            System.out.println(name + " pasa turno (no puede jugar sin exceder 50)");
        }
    }
}
