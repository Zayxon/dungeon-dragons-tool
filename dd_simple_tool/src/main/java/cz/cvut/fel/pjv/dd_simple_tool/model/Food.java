package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.BaseObject;
import javafx.application.Platform;
import javafx.fxml.JavaFXBuilderFactory;

/**
 * Structure for creating Food objects. Food objects are defined by unique
 * name, image, tag, sell value and healAmount which defines how many percent of
 * players HP and Energy the created object restores.
 *
 * Each Food object can be sold or used.
 *
 * @author Miroslav Falcmann
 */
public class Food extends Item {

    private final int healAmount;

    public Food(String name, String imageFilename, ItemTags tag, int sellValue, int healAmount) {
        super(name, imageFilename, tag, sellValue);
        this.healAmount = healAmount;
        adjustDescription("Restores " + healAmount + "% HP and Energy.\n");
    }

    /**
     * Food object is used and restores given player "healAmount" percent of
     * his/her HP and Energy.
     *
     * @param player Player whose HP and Energy will be restored.
     */
    @Override
    public void use(Player player) {
        int newEnergy = player.getEnergy() + (int) (player.getMaxEnergy() * 0.01 * healAmount);
        if (newEnergy > player.getMaxEnergy()) {
            player.setEnergy(player.getMaxEnergy());
        } else {
            player.setEnergy(newEnergy);
        }
        int newHp = player.getHp() + (int) (player.getMaxHp() * 0.01 * healAmount);
        if (newHp > player.getMaxHp()) {
            player.setHp(player.getMaxHp());
        } else {
            player.setHp(newHp);
        }
    }

    @Override
    public BaseObject createNew() {
        return new Food(name, imageFilename, tag, sellValue, healAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }
}
