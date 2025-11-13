package edu.univalle.cincuentazo.model;

/**
 * Singleton configuration class for the Cincuentazo game.
 * <p>
 * Stores global game settings, such as the number of machine players.
 * Ensures that only one instance exists throughout the application.
 * </p>
 *
 * @since 1.0
 */
public class GameConfig {

    /** The single instance of the GameConfig. */
    private static GameConfig instance;

    /** Number of machine players in the game (1 to 3). */
    private int machinePlayers;

    /**
     * Private constructor to prevent external instantiation.
     */
    private GameConfig() {}

    /**
     * Returns the singleton instance of GameConfig.
     * <p>
     * Creates the instance if it does not exist yet.
     * </p>
     *
     * @return the singleton GameConfig instance
     */
    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    /**
     * Returns the number of machine players.
     *
     * @return the number of machine players
     */
    public int getMachinePlayers() {
        return machinePlayers;
    }

    /**
     * Sets the number of machine players.
     * <p>
     * Valid values are 1 to 3 inclusive. An {@link IllegalArgumentException}
     * is thrown if an invalid value is provided.
     * </p>
     *
     * @param machinePlayers the number of machine players
     * @throws IllegalArgumentException if the number is not between 1 and 3
     */
    public void setMachinePlayers(int machinePlayers) {
        if (machinePlayers < 1 || machinePlayers > 3) {
            throw new IllegalArgumentException("El número de jugadores máquina debe estar entre 1 y 3.");
        }
        this.machinePlayers = machinePlayers;
    }
}
