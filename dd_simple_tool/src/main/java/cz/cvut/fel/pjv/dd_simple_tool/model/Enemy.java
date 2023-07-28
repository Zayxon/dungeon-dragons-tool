package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.ShowSelected;
import cz.cvut.fel.pjv.dd_simple_tool.BaseObject;
import cz.cvut.fel.pjv.dd_simple_tool.CircleObject;
import cz.cvut.fel.pjv.dd_simple_tool.GameWindow;
import cz.cvut.fel.pjv.dd_simple_tool.MapCreatorWindow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.StrokeType;

/**
 * Structure for enemy objects. Each created enemy is defined by unique name,
 * short description, image, health points, damage and resistance.
 *
 * Enemy is able to attack, be attacked or store up to 3 items which can be
 * later obtained by players. Enemy level can be later also adjusted to make
 * enemy stronger.
 *
 * Enemy is shown in map in JavaFX Circle object.
 *
 * @author Miroslav Falcmann
 */
public class Enemy extends BaseObject {

    protected int lvl;
    protected int hp;
    protected int maxHp;
    protected int baseHp;
    protected int dmg;
    protected int resist;
    private final int baseDmg;
    protected Item[] container;
    private CircleObject circObj;
    private MapCreatorWindow mcWindow;

    public Enemy(String name, String description, String imageFilename, int baseHp, int baseDmg, int baseResist) {
        lvl = 1;
        container = new Item[3];
        this.name = name;
        this.description = description;
        this.imageFilename = imageFilename;
        img = new Image(imageFilename);
        hp = baseHp;
        this.baseHp = baseHp;
        this.maxHp = baseHp;
        this.baseDmg = baseDmg;
        dmg = baseDmg;
        resist = baseResist;
        imgPat = new ImagePattern(img);
    }


    /**
     * If the enemy is alive, deals damage with its attack
     * @return Integer value representing the attack power.
     */
    public double attack() {
        if (getHp() != 0) {
            return dealDmg();
        }
        return 0;
    }

    /**
     * Function computing the damage which will be dealt by enemy in attack.
     *
     * @return double value of enemy attack damage.
     */
    private double dealDmg() {
        return baseDmg + dmg;
    }

    /**
     * Given damage is decreased according to enemy resistance and then dealt
     * to enemy. If the damage is greater than current enemy HP. HP is
     * set to 0.
     *
     * @param dmg Damage value, that will be decreased by certain amount and
     * then dealt to the enemy.
     */
    public void dealMeDmg(double dmg) {
        int currentHp = (int) (getHp() - dmg + (int) (dmg * (resist / 100)));
        if (currentHp < 0) {
            currentHp = 0;
        }
        setHp(currentHp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Item[] getContainer() {
        return container;
    }

    public MapCreatorWindow getMcWindow() {
        return mcWindow;
    }

    public void setMcWindow(MapCreatorWindow mcWindow) {
        this.mcWindow = mcWindow;
    }

    public void setCircObj(CircleObject circObj) {
        this.circObj = circObj;
    }

    public CircleObject getCircObj() {
        return circObj;
    }

    public int getDmg() {
        return dmg;
    }

    public int getResist() {
        return resist;
    }

    public int getLvl() {
        return lvl;
    }

    /**
     * Sets the level of the enemy and updates its stats.
     *
     * @param lvl Given int value describing the new level of the enemy. Sets
     * only positive number.
     */
    public void setLvl(int lvl) {
        if (lvl == 0) {
            return;
        }
        this.lvl = lvl;
        maxHp = baseHp * lvl;
        hp = maxHp;
        dmg = baseDmg * lvl;
    }

    @Override
    public void registerToListener(ShowSelected listener) {
        listener.showObject(this);
    }

    public int getHp() {
        return hp;
    }

    @Override
    public BaseObject createNew() {
        return new Enemy(name, description, imageFilename, maxHp, baseDmg, resist);
    }

    @Override
    public void selectObject(MapCreatorWindow mcWindow) {
        circle.setStroke(Color.YELLOW);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(2);
        mcWindow.setSelectedObject(this);
    }

    @Override
    public void selectObject(GameWindow gWindow) {
        circle.setStroke(Color.YELLOW);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(2);
        gWindow.setSelectedObject(this);
    }

    public void setContainer(Item[] container) {
        this.container = container;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public int getBaseDmg() {
        return baseDmg;
    }
}
