package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Class testing the functions changing player or enemy stats.
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
public class AdjustingStatsTest {
    private Player warrior;
    private Equip helmet;
    private Enemy enemy;

    /**
     * Creating Player object, Enemy object and Equip object, which are necessary for testing.
     */
    @Before
    public void setUp() {
        warrior = new Warrior("TestName");
        helmet = new Equip("Helmet5", "helmet_05.jpg", ItemTags.HEAD, 30, 8, 10);
        enemy = new Enemy("Bear", "Just a common bear", "bear.png", 50, 5, 20);
    }

    /**
     * Testing the functions that increase player gold balance, HP, XP, Energy and Damage.
     * Also testing the function for increasing enemy HP and Damage.
     */
    @Test
    public void increaseStats() {
        // adding gold
        warrior.addGold(20);
        assertEquals(warrior.getGold(), 20);
        warrior.addGold(-30);
        assertEquals(warrior.getGold(), -10);

        // adding xp
        warrior.addXp(30);
        assertEquals(warrior.getXp(), 30);
        warrior.addXp(-20);
        assertEquals(warrior.getXp(), 10);
        warrior.addXp(-20);
        assertEquals(warrior.getXp(), 0);

        // adding hp
        warrior.addHp(-30);
        assertEquals(warrior.getHp(), warrior.getMaxHp() - 30);
        warrior.addHp(-1000000);
        assertEquals(warrior.getHp(), 0);
        warrior.setHp(warrior.getMaxHp());

        // adding energy
        warrior.addEnergy(-50);
        assertEquals(warrior.getEnergy(), warrior.getMaxEnergy() - 50);
        warrior.addEnergy(-10000);
        assertEquals(warrior.getEnergy(), 0);

        // adding equip
        warrior.getEquip()[0] = helmet;
        warrior.updateStatsFromEquip();
        assertEquals(warrior.getDmg(), 8);
        assertEquals(warrior.getMaxHp(), warrior.getHp() + 10);

        // increasing player level
        int previousBaseMaxHp = warrior.getBaseMaxHp();
        int previousBaseDmg = warrior.getBaseDmg();
        warrior.increaseLevel();
        assertEquals((int)(previousBaseDmg * 1.5), warrior.getBaseDmg());
        assertEquals((int)(previousBaseMaxHp * 1.5), warrior.getBaseMaxHp());

        // increasing enemy level
        enemy.setLvl(10);
        assertEquals(10 * enemy.getBaseHp(), enemy.getMaxHp());
        assertEquals(10 * enemy.getBaseDmg(), enemy.getDmg());
        assertEquals(enemy.getMaxHp(), enemy.getHp());
    }
}
