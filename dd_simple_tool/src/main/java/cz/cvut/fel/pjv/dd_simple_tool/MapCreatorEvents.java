package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Class defining all the necessary events that need to be added to the map
 * within the MapCreator window.
 *
 * @author Miroslav Falcmann
 */
public class MapCreatorEvents {

    private final ArrayList<Tile> tilesArr;
    private final ArrayList<CircleObject> circleObjectsArr;
    private final ListView listView;
    private final ShowSelected enemyPanelListener;
    private final ShowSelected chestPanelListener;
    private final ShowSelected tilePanelListener;
    private final MapCreatorWindow mcWindow;
    private final AnchorPane ap;

    public MapCreatorEvents(ArrayList<Tile> tilesArr, ArrayList<CircleObject> circleObjectsArr, AnchorPane ap, MapCreatorWindow mcWindow) {
        this.tilesArr = tilesArr;
        this.circleObjectsArr = circleObjectsArr;
        this.ap = ap;
        this.mcWindow = mcWindow;
        listView = mcWindow.getRightP().getObjectsList();
        enemyPanelListener = mcWindow.getBottomP().getEnemyPanelMapCreator();
        chestPanelListener = mcWindow.getBottomP().getChestPanelMapCreator();
        tilePanelListener = mcWindow.getBottomP().getTilePanelMapCreator();
    }

    /**
     * Add mouse left and right click to the Tile (Polygon)
     *
     * @param tile Tile where event is need to be added
     */
    public void addHexagonEvent(Tile tile) {
        EventHandler<MouseEvent> addAndSelectObject = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.SECONDARY) {
                addObjectToMap(tile);
            } else if (button == MouseButton.PRIMARY) {
                selectHexagon(tile);
            }
        };
        tile.getHexagon().addEventFilter(MouseEvent.MOUSE_CLICKED, addAndSelectObject);
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
     * Set the circle showing object icon to certain tile in the map
     *
     * @param tile Tile where player needs to be placed
     */
    private void addObjectToMap(Tile tile) {
        Object obj = listView.getSelectionModel().getSelectedItem();
        if (obj != null && (obj instanceof Enemy || obj instanceof Chest)) {
            CircleObject circObj = new CircleObject(tile.getCenterX(), tile.getCenterY(), ((BaseObject) obj).createNew());
            circleObjectsArr.add(circObj);
            addCircleEvent(circObj.getBaseObj(), circObj);
            ap.getChildren().add(circObj.getCirc());
        } else if (obj != null && obj instanceof Terrain) {
            BaseObject baseObj = ((BaseObject) obj).createNew();
            tile.setTerrain((Terrain) baseObj);
            baseObj.setHexagon(tile.getHexagon());
            tile.getHexagon().setFill((tile.getTerrain()).getImgPat());
        }
    }

    /**
     * Select the Tile
     *
     * @param tile Tile that needs to be selected
     */
    private void selectHexagon(Tile tile) {
        if (tile.getTerrain() != null) {
            (tile.getTerrain()).registerToListener(tilePanelListener);
            if (mcWindow.getSelectedObject() == null) {
                tile.getTerrain().selectObject(mcWindow);
            } else {
                tile.getTerrain().reselectObject(mcWindow);
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
     * Add mouse left click event to the circle object in game Left click = show
     * the object in panel (AnchorPane)
     *
     * @param baseObj BaseObject object (Enemy or Chest) showed by the circle
     * object
     * @param circObj CircleObject object showing the BaseObject object (Enemy
     * or Chest)
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
                if (mcWindow.getSelectedObject() == null) {
                    baseObj.selectObject(mcWindow);
                } else {
                    baseObj.reselectObject(mcWindow);
                }
            }
        };
        circObj.getCirc().addEventFilter(MouseEvent.MOUSE_CLICKED, circleClick);
    }
}
