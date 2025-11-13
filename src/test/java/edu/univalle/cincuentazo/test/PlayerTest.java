package edu.univalle.cincuentazo.test;

import edu.univalle.cincuentazo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private IPlayer player;

    @BeforeEach
    void setUp() {
        player = new HumanPlayer("TestPlayer");
    }

    @Test
    void testPlayerStartsWithEmptyHand() {
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void testAddAndRemoveCard() {
        Card card = Card.H01;
        player.addCard(card);
        assertEquals(1, player.getHand().size());

        player.removeCard(card);
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void testSetAndCheckEliminatedStatus() {
        assertFalse(player.isEliminated());
        player.setEliminated(true);
        assertTrue(player.isEliminated());
    }
}
