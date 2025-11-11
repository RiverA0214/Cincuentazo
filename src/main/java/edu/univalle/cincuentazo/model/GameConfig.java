package edu.univalle.cincuentazo.model;

public class GameConfig {
    private int machinePlayers;

    public int getMachinePlayers() {
        return machinePlayers;
    }

    public void setMachinePlayers(int machinePlayers) {
        if (machinePlayers < 1 || machinePlayers > 3) {
            throw new IllegalArgumentException("Number of machine players must be between 1 and 3.");
        }
        this.machinePlayers = machinePlayers;
    }
}

