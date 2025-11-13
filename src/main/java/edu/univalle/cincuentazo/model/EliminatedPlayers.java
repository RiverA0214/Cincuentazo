package edu.univalle.cincuentazo.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Maintains a collection of players who have been eliminated from the game.
 * <p>
 * Provides methods to add eliminated players, check if a player is eliminated,
 * retrieve all eliminated players, and get the total count.
 * </p>
 *
 * @see IPlayer
 * @since 1.0
 */
public class EliminatedPlayers {

    /** Set of eliminated players. */
    private final Set<IPlayer> eliminated = new HashSet<>();

    /**
     * Adds a player to the eliminated set.
     *
     * @param player the player to mark as eliminated
     */
    public void add(IPlayer player) {
        eliminated.add(player);
    }

    /**
     * Checks if a player has been eliminated.
     *
     * @param player the player to check
     * @return true if the player is in the eliminated set, false otherwise
     */
    public boolean contains(IPlayer player) {
        return eliminated.contains(player);
    }

    /**
     * Returns a copy of all eliminated players.
     * <p>
     * The returned set is a new copy to avoid external modification
     * of the internal collection.
     * </p>
     *
     * @return a set containing all eliminated players
     */
    public Set<IPlayer> getAll() {
        return new HashSet<>(eliminated);
    }

    /**
     * Returns the number of eliminated players.
     *
     * @return the size of the eliminated set
     */
    public int count() {
        return eliminated.size();
    }
}
