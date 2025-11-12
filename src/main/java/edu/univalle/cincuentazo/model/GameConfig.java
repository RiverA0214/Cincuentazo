package edu.univalle.cincuentazo.model;

public class GameConfig {

    private static GameConfig instance;

    private int machinePlayers;

    //  Constructor privado
    private GameConfig() {}

    //  Devuelve siempre la misma instancia
    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public int getMachinePlayers() {
        return machinePlayers;
    }

    public void setMachinePlayers(int machinePlayers) {
        if (machinePlayers < 1 || machinePlayers > 3) {
            throw new IllegalArgumentException("El número de jugadores máquina debe estar entre 1 y 3.");
        }
        this.machinePlayers = machinePlayers;
    }
}
