package edu.univalle.cincuentazo.model;

public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(String name) {
        super(name, false);
    }

    @Override
    public void playTurn(Game game) {
        // El jugador humano juega manualmente desde la GUI,
        // así que aquí solo se deja la llamada vacía.
        System.out.println(name + " debe jugar su turno manualmente.");
    }
}
