package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.BaseObject;

/**
 * Structure for creating HealthPotion objects. Health potions are defined by
 * unique name, image, tag, sell value and healAmount which defines how many
 * percent of players HP the created object restores.
 *
 * Each Health potion can be sold or used.
 *
 * @author Miroslav Falcmann
 */
public class HealthPotion extends Item{
    
    private final int healAmount;

    public HealthPotion(String name, String imageFilename, ItemTags tag, int sellValue, int healAmount) {
        super(name, imageFilename, tag, sellValue);
        this.healAmount = healAmount;
        adjustDescription("Restores " + healAmount + "% HP.\n");
    }

    /**
     * Health potion is used and restores given player "healAmount" percent of his/her HP.
     *
     * @param player Player whose HP will be restored.
     */
    @Override
    public void use(Player player) {
        int newHp = player.getHp() + (int) (player.getMaxHp() * 0.01 * healAmount);
        if (newHp > player.getMaxHp()) {
            player.setHp(player.getMaxHp());
        } else {
            player.setHp(newHp);
        }
    }

    public int getHealAmount() {
        return healAmount;
    }
    
    @Override
    public BaseObject createNew() {
        return new HealthPotion(name, imageFilename, tag, sellValue, healAmount);
    }
}
