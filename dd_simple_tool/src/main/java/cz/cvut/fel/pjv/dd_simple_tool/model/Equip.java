package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.BaseObject;

/**
 * Structure for creating Equip objects. Equip objects are defined by unique
 * name, image, tag, sell value, attack damage bonus and health points bonus
 * defining how many bonus health points and damage points the wearer of this
 * equip item receives.
 *
 * Each Equip object can be sold or equipped.
 *
 * @author Miroslav Falcmann
 */
public class Equip extends Item {

    protected int attackDmg;
    protected int healthPoints;

    public Equip(String name, String imageFilename, ItemTags tag, int sellValue, int attackDmg, int hp) {
        super(name, imageFilename, tag, sellValue);
        this.attackDmg = attackDmg;
        this.healthPoints = hp;
        adjustDescription("HP: +" + healthPoints + "\nAttack damage: +" + attackDmg + "\n");
    }

    public int getAttackDmg() {
        return attackDmg;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    @Override
    public BaseObject createNew() {
        return new Equip(name, imageFilename, tag, sellValue, attackDmg, healthPoints);
    }
}
