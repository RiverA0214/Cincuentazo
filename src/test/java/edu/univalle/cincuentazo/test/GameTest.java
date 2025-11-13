package edu.univalle.cincuentazo.test;

import edu.univalle.cincuentazo.exceptions.DeckEmptyException;
import edu.univalle.cincuentazo.exceptions.InvalidCardPlayException;
import edu.univalle.cincuentazo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private IPlayer player;

    @BeforeEach
    void setUp() {
        game = new Game(1); // humano + 1 máquina
        player = game.getPlayers().get(0);
    }

    @Test
    void testDrawCardReducesDeck() {
        // vaciamos la mano para permitir robar
        player.getHand().clear();

        int before = getDeckSize(game);
        game.drawCard(player);
        int after = getDeckSize(game);

        assertTrue(after < before, "El mazo debe reducirse después de robar una carta");
    }


    @Test
    void testPlayValidCardUpdatesSum() {
        Card card = player.getHand().get(0);
        int initialSum = game.getTableSum();
        try {
            boolean result = game.playCard(player, card);
            assertTrue(result);
            assertEquals(initialSum + game.getCardValue(card), game.getTableSum());
        } catch (InvalidCardPlayException e) {
            fail("No debería lanzar excepción con una jugada válida");
        }
    }

    @Test
    void testEliminatePlayerWhenCannotPlay() {
        player.getHand().clear();
        assertTrue(game.mustBeEliminated(player));
    }

    // helper para acceder al tamaño del mazo
    private int getDeckSize(Game g) {
        try {
            var field = Game.class.getDeclaredField("deck");
            field.setAccessible(true);
            return ((java.util.List<?>) field.get(g)).size();
        } catch (Exception e) {
            return -1;
        }
    }
}
