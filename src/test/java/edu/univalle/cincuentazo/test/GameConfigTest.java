package edu.univalle.cincuentazo.test;

import edu.univalle.cincuentazo.model.GameConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameConfigTest {

    @Test
    void testSingletonReturnsSameInstance() {
        GameConfig c1 = GameConfig.getInstance();
        GameConfig c2 = GameConfig.getInstance();
        assertSame(c1, c2, "Debe devolver siempre la misma instancia");
    }

    @Test
    void testValidMachinePlayerSetting() {
        GameConfig config = GameConfig.getInstance();
        config.setMachinePlayers(2);
        assertEquals(2, config.getMachinePlayers());
    }

    @Test
    void testInvalidMachinePlayerThrowsException() {
        GameConfig config = GameConfig.getInstance();
        assertThrows(IllegalArgumentException.class, () -> config.setMachinePlayers(5));
    }
}
