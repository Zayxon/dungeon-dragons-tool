package cz.cvut.fel.pjv.dd_simple_tool;


import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Warrior;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the in-Game combat functions.
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
public class CombatTest {

    private GameWindow game;
    private Player warrior;
    private Enemy enemy;
    private EnemyPanel enemyPanel;
    private PlayerPanel playerPanel;

    /**
     * Creating the game environment, the player and the enemy.
     */
    @Before
    public void setUp() {
        try {
            game = new GameWindow();
            warrior = new Warrior("TestName");
            enemy = new Enemy("Bear", "Just a common bear", "bear.png", 50, 5, 20);
            game.setSelectedPlayer(warrior);
            game.setShowedObject(enemy);
            game.setRolledNumber(10);
            enemyPanel = game.getBottomP().getEnemyPanel();
            enemyPanel.setEnemy(enemy);
            playerPanel = game.getBottomP().getPlayerPanel();
            playerPanel.setPlayer(warrior);
        } catch (IOException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * Testing if the enemy attack was done correctly
     */
    @Test
    public void enemyAttack() {
        // enemy is not dead, can attack
        double dmg = enemy.attack() * game.getRolledNumber() * 0.1;
        enemyPanel.attack();
        assertEquals(warrior.getHp(), warrior.getMaxHp() - dmg + (int) ((dmg * (warrior.getResist()/100))), 0.001);

        // enemy is dead, cant attack
        enemy.setHp(0);
        dmg = enemy.attack() * game.getRolledNumber() * 0.1;
        int playerCurrHp = warrior.getHp();
        enemyPanel.attack();
        assertEquals(warrior.getHp(), playerCurrHp);
    }

    /**
     * Testing if the player attack was done correctly
     */
    @Test
    public void playerAttack() {
        // player has energy and hp, can attack
        double dmg = warrior.attack(1.5, 30) * game.getRolledNumber() * 0.1;
        warrior.setEnergy(warrior.getMaxEnergy());
        playerPanel.attack(1.5, 30);
        assertEquals(enemy.getHp(), (int) (enemy.getMaxHp() - dmg + ((dmg * (enemy.getResist()/100)))));
        assertEquals(warrior.getEnergy(), warrior.getMaxEnergy() - 30);

        //player has no energy, cant attack
        warrior.setEnergy(0);
        int enemyCurrHp = enemy.getHp();
        playerPanel.attack(1.5, 30);
        assertEquals(enemy.getHp(), enemyCurrHp);

        //player is dead, cant attack
        warrior.setEnergy(warrior.getMaxEnergy());
        warrior.setHp(0);
        playerPanel.attack(1.5, 30);
        assertEquals(enemy.getHp(), enemyCurrHp);
    }
}
