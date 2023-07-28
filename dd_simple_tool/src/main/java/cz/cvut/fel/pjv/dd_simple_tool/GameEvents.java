package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Class defining all the necessary events that need to be added to the map
 * within the Game window.
 *
 * @author Miroslav Falcmann
 */
public class GameEvents {

    private final ArrayList<Tile> tilesArr;
    private final ArrayList<CircleObject> circleObjectsArr;
    private final AnchorPane ap;
    private final GameWindow gWindow;

    private final ShowSelected enemyPanelListener;
    private final ShowSelected chestPanelListener;
    private final ShowSelected tilePanelListener;

    public GameEvents(ArrayList<Tile> tilesArr, ArrayList<CircleObject> circleObjectsArr, AnchorPane ap, GameWindow gWindow) {
        this.tilesArr = tilesArr;
        this.circleObjectsArr = circleObjectsArr;
        this.ap = ap;
        this.gWindow = gWindow;

        enemyPanelListener = gWindow.getBottomP().getEnemyPanel();
        chestPanelListener = gWindow.getBottomP().getChestPanel();
        tilePanelListener = gWindow.getBottomP().getSelectedTilePanel();
    }

    /**
     * Add mouse left and right click to the Tile (Polygon)
     * @param tile Tile where event is need to be added
     */
    public void addHexagonEvent(Tile tile) {
        EventHandler<MouseEvent> addAndSelectObject = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.PRIMARY) {
                selectHexagon(tile);
            } else if (button == MouseButton.SECONDARY) {
                setPlayerToTile(tile);
            }
        };
        tile.getHexagon().addEventFilter(MouseEvent.MOUSE_CLICKED, addAndSelectObject);
    }

    /**
     * Set the circle showing player icon to certain tile in the map
     * @param tile Tile where player needs to be placed
     */
    private void setPlayerToTile(Tile tile) {
        Player pl = gWindow.getSelectedPlayer();
        if (pl != null) {
            pl.setCircleCenter(tile.getCenterX(), tile.getCenterY());
            if (!pl.isAddedToMap()) {
                ap.getChildren().add(gWindow.getSelectedPlayer().getCircle());
                pl.setAddedToMap(true);
            }
        }
    }

    /**
     * Add events to all circles in the circle array.
     */
    public void addCircleEvents() {
        for (CircleObject circObj : circleObjectsArr) {
            addCircleEvent(circObj.getBaseObj(), circObj);
        }
    }

    /**
     * Add events to all tiles in the tile array.
     */
    public void addHexagonEvents() {
        for (Tile tile : tilesArr) {
            addHexagonEvent(tile);
        }
    }

    /**
     * Select the Tile
     * @param tile Tile that needs to be selected
     */
    private void selectHexagon(Tile tile) {
        if (tile.getTerrain() != null) {
            (tile.getTerrain()).registerToListener(tilePanelListener);
            if (gWindow.getSelectedObject() == null) {
                tile.getTerrain().selectObject(gWindow);
            } else {
                tile.getTerrain().reselectObject(gWindow);
            }
        }
    }

    /**
     * Add mouse left click event to the circle object in game
     * Left click = show the object in panel (AnchorPane)
     * @param baseObj BaseObject object (Enemy or Chest) showed by the circle object
     * @param circObj CircleObject object showing the BaseObject object (Enemy or Chest)
     */
    private void addCircleEvent(BaseObject baseObj, CircleObject circObj) {
        EventHandler<MouseEvent> circleClick = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.PRIMARY) {
                baseObj.setCircle(circObj.getCirc());
                if (baseObj instanceof Enemy) {
                    baseObj.registerToListener(enemyPanelListener);
                } else if (baseObj instanceof Chest) {
                    baseObj.registerToListener(chestPanelListener);
                }
                if (gWindow.getSelectedObject() == null) {
                    baseObj.selectObject(gWindow);
                } else {
                    baseObj.reselectObject(gWindow);
                }
            }
        };
        circObj.getCirc().addEventFilter(MouseEvent.MOUSE_CLICKED, circleClick);
    }
}
