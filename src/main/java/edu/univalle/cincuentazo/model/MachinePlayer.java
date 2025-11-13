package edu.univalle.cincuentazo.model;

public class MachinePlayer extends AbstractPlayer {

    public MachinePlayer(String name) {
        super(name, true);
    }

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
            game.playCard(this, chosen);
            System.out.println(name + " jugó " + chosen + " (total: " + game.getTableSum() + ")");
            game.drawCard(this);
        } else {
            System.out.println(name + " pasa turno (no puede jugar sin exceder 50)");
        }
    }
}
