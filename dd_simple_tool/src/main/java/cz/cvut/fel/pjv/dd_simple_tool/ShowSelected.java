package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;

/**
 * Interface for showing currently selected object in a panel within the
 * MapCreator window or Game window.
 *
 * @author Miroslav Falcmann
 */
public interface ShowSelected {

    /**
     * Shows the currently selected object in panel created for this purpose
     * (AnchorPane)
     *
     * @param enemy Selected enemy
     */
    void showObject(Enemy enemy);

    /**
     * Shows the currently selected object in panel created for this purpose
     * (AnchorPane)
     *
     * @param chest Selected chest
     */
    void showObject(Chest chest);

    /**
     * Shows the currently selected object in panel created for this purpose
     * (AnchorPane)
     *
     * @param item Selected item
     */
    void showObject(Item item);

    /**
     * Shows the currently selected object in panel created for this purpose
     * (AnchorPane)
     *
     * @param terrain Selected terrain
     */
    void showObject(Terrain terrain);

    /**
     * Shows the currently selected object in panel created for this purpose
     * (AnchorPane)
     *
     * @param player Selected player
     */
    void showObject(Player player);
}
