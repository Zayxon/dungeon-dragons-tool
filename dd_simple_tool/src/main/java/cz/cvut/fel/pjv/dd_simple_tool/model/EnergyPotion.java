package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.BaseObject;

/**
 * Structure for creating EnergyPotion objects. Energy potions are defined by
 * unique name, image, tag, sell value and healAmount which defines how many
 * percent of players Energy the created object restores.
 *
 * Each Energy potion can be sold or used.
 *
 * @author Miroslav Falcmann
 */
public class EnergyPotion extends Item {

    private final int healAmount;

    public EnergyPotion(String name, String imageFilename, ItemTags tag, int sellValue, int healAmount) {
        super(name, imageFilename, tag, sellValue);
        this.healAmount = healAmount;
        adjustDescription("Restores " + healAmount + "% Energy.\n");
    }

    /**
     * Energy potion is used and restores given player "healAmount" percent of his/her HP.
     *
     * @param player Player whose Energy will be restored.
     */
    @Override
    public void use(Player player) {
        int newEnergy = player.getEnergy() + (int) (player.getMaxEnergy() * 0.01 * healAmount);
        if (newEnergy > player.getMaxEnergy()) {
            player.setEnergy(player.getMaxEnergy());
        } else {
            player.setEnergy(newEnergy);
        }
    }

    @Override
    public BaseObject createNew() {
        return new EnergyPotion(name, imageFilename, tag, sellValue, healAmount);
    }
    
    public int getHealAmount() {
        return healAmount;
    }
}
