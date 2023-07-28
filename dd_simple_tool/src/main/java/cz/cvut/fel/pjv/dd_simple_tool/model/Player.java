package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.GameWindow;
import cz.cvut.fel.pjv.dd_simple_tool.ShowSelected;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.logging.Logger;

/**
 * Abstract structure for player objects. Player class defines all the necessary
 * functions and values which are used by non-abstract classes extending Player.
 * Each Player is defined by unique name.
 *
 * Player is able to attack, be attacked, store up to 12 items in inventory,
 * equip up to 6 Equip items to raise own stats, earn experience (XP), gold, use
 * items (if they can be used), sell items or reach new levels. The higher level
 * player has, the stronger player is.
 *
 * Player is shown in map in JavaFX Circle object.
 *
 * @author Miroslav Falcmann
 */
public abstract class Player {

    protected String className;
    protected String name;
    protected int maxHp;
    protected int maxEnergy;
    protected int baseMaxHp;
    protected int maxXp;
    protected int lvl;
    protected int xp;
    protected int hp;
    protected int energy;
    protected int dmg;
    protected int baseDmg;
    protected int resist;
    protected String classIconFilename;
    protected ImagePattern imgPat;
    protected Item[] container;
    protected Circle circle;
    protected boolean isAddedToMap;
    protected Equip[] equip;
    protected int gold;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Player(String name) {
        this.isAddedToMap = false;
        this.name = name;
        this.lvl = 1;
        this.gold = 0;
        this.dmg = 0;
        this.xp = 0;
        this.maxXp = 50;
        container = new Item[12];
        equip = new Equip[6];
    }

    /**
     * Updates player stats according to items the player is wearing (stored in
     * equip array).
     */
    public void updateStatsFromEquip() {
        dmg = 0;
        maxHp = baseMaxHp;
        for (Equip eq : equip) {
            if (eq != null) {
                maxHp += eq.getHealthPoints();
                dmg += eq.getAttackDmg();
            }
        }
        LOGGER.info("Player stats updated (bonus stats from equip included).");
    }

    /**
     * Increases player level by 1 and updates his stats.
     */
    public void increaseLevel() {
        lvl += 1;
        baseMaxHp += (int) (0.5 * baseMaxHp);
        baseDmg += (int) (0.5 * baseDmg);
    }

    /**
     * If the player is alive, deals damage with his/her attack and may lose some energy.
     * @return Integer value representing the attack power.
     */
    public double attack(double attackPower, int energyCost) {
        if ((getHp() != 0) && (getEnergy() >= energyCost)) {
            setEnergy(getEnergy() - energyCost);
            LOGGER.info("Player " + name + " attacked for " + (dealDmg() * attackPower) + " damage.");
            return dealDmg() * attackPower;
        }
        return 0;
    }

    /**
     * Function computing the damage which will be dealt by player in attack.
     *
     * @return double value of player attack damage.
     */
    private double dealDmg() {
        return baseDmg + dmg;
    }

    /**
     * Given damage is decreased according to player resistance and then dealt
     * to player. If the damage is greater than current player HP. HP is set to
     * 0.
     *
     * @param dmg Damage value, that will be decreased by certain amount and
     * then dealt to player.
     */
    public void dealMeDmg(double dmg) {
        int currentHp = (int) (getHp() - dmg + (int) (dmg * (resist / 100)));
        if (currentHp < 0) {
            currentHp = 0;
        }
        setHp(currentHp);
        LOGGER.info("Player " + name + " has been dealt" +
                " " + (dmg - (int) (dmg * (resist / 100))) + " damage.");
    }

    /**
     * To players current hp is added the given int amount.
     *
     * @param hp int value that will be added to players HP.
     */
    public void addHp(int hp) {
        int currentHp = getHp() + hp;
        if (currentHp >= getMaxHp()) {
            setHp(getMaxHp());
        } else if (currentHp < 0) {
            setHp(0);
        } else {
            setHp(currentHp);
        }
        LOGGER.info("Player " + name + " has been added " + hp + " hp.");
    }

    /**
     * To players current energy is added the given int amount.
     *
     * @param energy int value that will be added to players Energy.
     */
    public void addEnergy(int energy) {
        int currentEnergy = getEnergy() + energy;
        if (currentEnergy >= getMaxEnergy()) {
            setEnergy(getMaxEnergy());
        } else if (currentEnergy < 0) {
            setEnergy(0);
        } else {
            setEnergy(currentEnergy);
        }
        LOGGER.info("Player " + name + " has been added " + energy + " energy.");
    }

    /**
     * To players current gold balance is added the given int amount.
     *
     * @param gold int value that will be added to players gold balance.
     */
    public void addGold(int gold) {
        setGold(getGold() + gold);
        LOGGER.info("Player " + name + " has been added " + gold + " gold.");
    }

    /**
     * To players current experience (XP) is added the given int amount.
     *
     * @param xp int value that will be added to players XP.
     */
    public void addXp(int xp) {
        int currentXp = getXp() + xp;
        if (xp < 0) {
            if (currentXp < 0) {
                currentXp = 0;
            }
        } else {
            while (currentXp >= getMaxXp()) {
                increaseLevel();
                currentXp -= getMaxXp();
            }
            if (currentXp < 0) {
                currentXp += getMaxXp();
            }
        }
        setXp(currentXp);
        LOGGER.info("Player " + name + " has been added " + xp + " xp.");
    }

    /**
     * Initializes the circle showing players icon in map.
     */
    protected void circleInit() {
        circle = new Circle();
        circle.setCenterX(0);
        circle.setCenterY(0);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(0.75);
        circle.setRadius(25);
        circle.setFill(imgPat);

    }

    /**
     * Function invokes a listener function which is showing the player on
     * certain panel within the Game window.
     *
     * @param listener listener object that will show Player in his panel.
     */
    public abstract void registerToListener(ShowSelected listener);

    /**
     * Adds mouse left click event to a circle showing players icon on the map.
     *
     * @param gWindow pointer to GameWindow object
     * @param player Player which is connected to the certain circle.
     */
    public void addCircleEvent(GameWindow gWindow, Player player) {
        EventHandler<MouseEvent> circleClick = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.PRIMARY) {
                player.registerToListener(gWindow.getBottomP().getPlayerPanel());
                if (gWindow.getSelectedPlayer() == null) {
                    player.selectObject(gWindow);
                } else {
                    player.reselectObject(gWindow, player);
                }
                gWindow.setSelectedPlayer(player);
            }
        };
        circle.addEventFilter(MouseEvent.MOUSE_CLICKED, circleClick);
    }

    /**
     * Removes selection of previous object and selects a new one.
     *
     * @param gWindow pointer to GameWindow object
     * @param player currently selected player
     */
    public void reselectObject(GameWindow gWindow, Player player) {
        if (gWindow.getSelectedPlayer() instanceof Player) {
            gWindow.getSelectedPlayer().getCircle().setStroke(Color.BLACK);
            gWindow.getSelectedPlayer().getCircle().setStrokeWidth(0.75);
        }
        player.selectObject(gWindow);
    }

    /**
     * Selects new object.
     *
     * @param gWindow pointer to GameWindow object
     */
    public abstract void selectObject(GameWindow gWindow);

    /**
     * Set the new center of the circle showing the player icon in map.
     *
     * @param x X coordinate of circle center.
     * @param y Y coordinate of circle center.
     */
    public void setCircleCenter(double x, double y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public void setBaseMaxHp(int baseMaxHp) {
        this.baseMaxHp = baseMaxHp;
    }

    public void setMaxXp(int maxXp) {
        this.maxXp = maxXp;
    }

    public void setBaseDmg(int baseDmg) {
        this.baseDmg = baseDmg;
    }

    public void setContainer(Item[] container) {
        this.container = container;
    }

    public void setEquip(Equip[] equip) {
        this.equip = equip;
    }

    public int getBaseMaxHp() {
        return baseMaxHp;
    }

    public int getBaseDmg() {
        return baseDmg;
    }

    public void useItem(HealthPotion item) {
        item.use(this);
    }

    public void useItem(Food item) {
        item.use(this);
    }

    public void useItem(EnergyPotion item) {
        item.use(this);
    }

    public void sellItem(Item item) {
        gold += item.getSellValue();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getResist() {
        return resist;
    }

    public void setResist(int resist) {
        this.resist = resist;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getMaxXp() {
        return maxXp;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Equip[] getEquip() {
        return equip;
    }

    public boolean isAddedToMap() {
        return isAddedToMap;
    }

    public void setAddedToMap(boolean isAddedToMap) {
        this.isAddedToMap = isAddedToMap;
    }

    public Circle getCircle() {
        return circle;
    }

    public String getName() {
        return name;
    }

    public ImagePattern getImgPat() {
        return imgPat;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public Item[] getContainer() {
        return container;
    }

}
