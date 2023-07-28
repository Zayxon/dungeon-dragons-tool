package cz.cvut.fel.pjv.dd_simple_tool;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the functions of LoadGame class.
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
public class LoadGameTest {

    private LoadGame game;

    /**
     * Loading the game before each test.
     */
    @Before
    public void setUp() {
        try {
            game = new LoadGame();
            game.loadGame("save1.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * Testing if the players were loaded correctly
     */
    @Test
    public void testLoadPlayers() {
        assertEquals(game.getPlayerList().get(0).getName(), "Bivoj");
        assertEquals(game.getPlayerList().get(0).getEquip()[0].getName(), "Helmet1");
        assertEquals(game.getPlayerList().get(0).getEquip()[1].getName(), "Chest4");
        assertEquals(game.getPlayerList().get(0).getContainer()[0].getName(), "Eye");
        assertEquals(game.getPlayerList().get(0).getContainer()[1].getName(), "Bear hide");

        assertEquals(game.getPlayerList().get(1).getName(), "Lovec");
        assertEquals(game.getPlayerList().get(1).getEquip()[3].getName(), "Boots6");
        assertEquals(game.getPlayerList().get(1).getEquip()[5].getName(), "Crossbow2");
        assertEquals(game.getPlayerList().get(1).getContainer()[2].getName(), "Ham");
        assertEquals(game.getPlayerList().get(1).getContainer()[6].getName(), "Ham");

        assertEquals(game.getPlayerList().get(2).getName(), "Lecitel");
        assertEquals(game.getPlayerList().get(2).getEquip()[5].getName(), "Staff6");
        assertEquals(game.getPlayerList().get(2).getContainer()[1].getName(), "Energy Potion2");
        assertEquals(game.getPlayerList().get(2).getContainer()[3].getName(), "Health Potion1");
    }

}
