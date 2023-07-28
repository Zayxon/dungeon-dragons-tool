package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.*;
import javafx.scene.shape.Rectangle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the item functions (selling, using and discarding from the game).
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
public class SelectedItemPanelGameTest {


    private GameWindow game;
    private Player warrior;
    private PlayerPanel playerPanel;
    private SelectedItemPanelGame itemPanel;
    private HealthPotion hPotion;
    private EnergyPotion ePotion;
    private Food food;

    /**
     * Creating the game environment and the player.
     */
    @Before
    public void setUp() {
        try {
            game = new GameWindow();
            warrior = new Warrior("TestName");
            hPotion = new HealthPotion("Health Potion1", "health_potion1.jpg", ItemTags.USABLE, 5, 50);
            ePotion = new EnergyPotion("Energy Potion1", "energy_potion1.jpg", ItemTags.USABLE, 5, 50);
            food = new Food("Cheese", "food_cheese.jpg", ItemTags.USABLE, 5, 10);

            itemPanel = game.getRightP().getSelectedItemPanelGame();
            game.setSelectedPlayer(warrior);
            game.setSelectedItemIndex(0);
            playerPanel = game.getBottomP().getPlayerPanel();
            playerPanel.setPlayer(warrior);
        } catch (IOException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * Testing if the item is sold correctly
     */
    @Test
    public void sellingItem() {
        game.setSelectedObject(hPotion);
        game.getSelectedObject().setRectangle(new Rectangle());
        warrior.getContainer()[0] = hPotion;
        itemPanel.sellItem(warrior, game);
        assertEquals(game.getSelectedObject(), null);
        assertEquals(warrior.getGold(), 5);
        assertEquals(warrior.getContainer()[0], null);
    }

    /**
     * Testing if the Health Potion is used correctly
     */
    @Test
    public void usingHealthPotion() {
        warrior.setHp(10);
        int previousHp = warrior.getHp();
        game.setSelectedObject(hPotion);
        game.getSelectedObject().setRectangle(new Rectangle());
        warrior.getContainer()[0] = hPotion;
        itemPanel.useItem(warrior, game);
        assertEquals(game.getSelectedObject(), null);
        assertEquals(warrior.getHp(), previousHp + warrior.getMaxHp() / (100/50));
        assertEquals(warrior.getContainer()[0], null);
    }

    /**
     * Testing if the Energy Potion is used correctly
     */
    @Test
    public void usingEnergyPotion() {
        warrior.setEnergy(10);
        int previousEnergy = warrior.getEnergy();
        game.setSelectedObject(ePotion);
        game.getSelectedObject().setRectangle(new Rectangle());
        warrior.getContainer()[0] = ePotion;
        itemPanel.useItem(warrior, game);
        assertEquals(game.getSelectedObject(), null);
        assertEquals(warrior.getEnergy(), previousEnergy + warrior.getMaxEnergy() / (100/50));
        assertEquals(warrior.getContainer()[0], null);
    }

    /**
     * Testing if the Food item is used correctly
     */
    @Test
    public void usingFood() {
        warrior.setHp(10);
        warrior.setEnergy(10);
        int previousHp = warrior.getHp();
        int previousEnergy = warrior.getEnergy();
        game.setSelectedObject(food);
        game.getSelectedObject().setRectangle(new Rectangle());
        warrior.getContainer()[0] = food;
        itemPanel.useItem(warrior, game);
        assertEquals(game.getSelectedObject(), null);
        assertEquals(warrior.getHp(), previousHp + warrior.getMaxHp() / (100/10));
        assertEquals(warrior.getEnergy(), previousEnergy + warrior.getMaxEnergy() / (100/10));
        assertEquals(warrior.getContainer()[0], null);
    }

    /**
     * Testing if the item is discarded correctly
     */
    @Test
    public void discardingItem() {
        game.setSelectedObject(hPotion);
        game.getSelectedObject().setRectangle(new Rectangle());
        warrior.getContainer()[0] = hPotion;
        itemPanel.discardItem(warrior, game);
        assertEquals(game.getSelectedObject(), null);
        assertEquals(warrior.getContainer()[0], null);
    }

}
