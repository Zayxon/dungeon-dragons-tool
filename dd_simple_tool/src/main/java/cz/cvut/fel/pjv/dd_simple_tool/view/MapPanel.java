package cz.cvut.fel.pjv.dd_simple_tool.view;

import cz.cvut.fel.pjv.dd_simple_tool.BaseObject;
import cz.cvut.fel.pjv.dd_simple_tool.CircleObject;
import cz.cvut.fel.pjv.dd_simple_tool.MapCreatorWindow;
import cz.cvut.fel.pjv.dd_simple_tool.ShowSelected;
import cz.cvut.fel.pjv.dd_simple_tool.Tile;
import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

/**
 * Map Panel class creating a raw map out of hexagon tiles. Map is prepared for
 * changes within the MapCreator window. Each tile is connected by an event for
 * filling it with different terrain or setting an enemy or chest object on this
 * tile.
 *
 * @author Miroslav Falcmann
 */
public class MapPanel {

    private boolean objectSelected;
    private Circle selectedCirc;
    private Polygon selectedHexagon;
    private final AnchorPane ap;
    private final double hexSize;
    private final double hexW;
    private final double hexH;
    private final double hexLayoutXShift;
    private final double hexLayoutYShift;
    private ArrayList<Tile> tilesArr;
    private ArrayList<CircleObject> circleObjectsArr;
    private Tile tile;
    private ListView listView;

    private ShowSelected enemyPanelListener;
    private ShowSelected chestPanelListener;
    private ShowSelected tilePanelListener;
    private final MapCreatorWindow mcWindow;

    public MapPanel(MapCreatorWindow mcWindow) {
        this.objectSelected = false;
        this.circleObjectsArr = new ArrayList<>();
        this.tilesArr = new ArrayList<>();
        this.hexSize = 34;
        this.hexH = 2 * hexSize;
        this.hexW = Math.sqrt(3) * hexSize;
        this.hexLayoutYShift = 0; // to fit perfectly in the center
        this.hexLayoutXShift = 25; // to fit perfectly in the center
        this.ap = new AnchorPane();
        this.mcWindow = mcWindow;
        ap.setMaxSize(1025, 490);
        ap.setMinSize(1025, 490);
        createMap();
    }

    /**
     * Creates the map formed out of hexagon tiles. 16 tiles wide and 9 high.
     */
    private void createMap() {
        boolean shifted;
        for (int i = 0; i < 9; i++) {
            shifted = i % 2 == 1;
            for (int j = 0; j < 16; j++) {
                double layoutX;
                if (shifted) {
                    layoutX = j * hexW - 0.5 * hexW;
                } else {
                    layoutX = j * hexW;
                }
                double layoutY = i * (hexH - 0.25 * hexH);
                double centerX = layoutX + 1 * hexW + hexLayoutXShift;
                double centerY = layoutY + 0.5 * hexH + hexLayoutYShift;
                createTile(layoutX, layoutY, centerX, centerY);
            }
        }
    }

    /**
     * Creates a tile with unique hexagon (Polygon) and adds an mouse click
     * event to it.
     *
     * @param layoutX X coordinate of (hexagon) Polygon object (top left corner)
     * @param layoutY Y coordinate of (hexagon) Polygon object (top left corner)
     * @param centerX X coordinate of the hexagon center
     * @param centerY Y coordinate of the hexagon center
     */
    private void createTile(double layoutX, double layoutY, double centerX, double centerY) {
        tile = new Tile(createHexagon(hexSize, layoutX, layoutY), null);
        tile.setLayoutX(layoutX);
        tile.setLayoutY(layoutY);
        tile.setCenterX(centerX);
        tile.setCenterY(centerY);
        addHexagonEvents(tile);
        tilesArr.add(tile);
        ap.getChildren().add(tile.getHexagon());
    }

    /**
     * Creates hexagon (Polygon) with proper size and layout
     *
     * @param size Integer number defining the size of the hexagon
     * @param layoutX X coordinate of the hexagon layout
     * @param layoutY Y coordinate of the hexagon layout
     * @return Polygon - created hexagon
     */
    private Polygon createHexagon(double size, double layoutX, double layoutY) {
        Polygon hexagon = new Polygon();
        double w = Math.sqrt(3) * size;
        double h = 2 * size;
        hexagon.getPoints().addAll(new Double[]{
            w, 0.0,
            1.5 * w, 0.25 * h,
            1.5 * w, 0.75 * h,
            w, h,
            0.5 * w, 0.75 * h,
            0.5 * w, 0.25 * h,});
        hexagon.setFill(Color.TRANSPARENT);
        hexagon.setStroke(Color.BLACK);
        hexagon.setStrokeWidth(0.75);
        hexagon.setLayoutX(layoutX + hexLayoutXShift);
        hexagon.setLayoutY(layoutY + hexLayoutYShift);
        return hexagon;
    }

    /**
     * Adds mouse click events to the given Tile. On left click, tile is
     * selected. On right click, tile is filled with terrain selected in the
     * right panel.
     *
     * @param tile Tile which will be connected to this event.
     */
    private void addHexagonEvents(Tile tile) {
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
     * Adds selected object (Terrain, Enemy or Chest) from ListView in right
     * panel to the given tile.
     *
     * @param tile Tile which will be filled with terrain or will store selected
     * enemy/chest.
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
     * Adds mouse left click events to the circle on map displaying an enemy or
     * a chest.
     *
     * @param baseObj Selected object from the ListView in right panel.
     * @param circObj CircleObject already added to map displaying enemy/chest
     * position in map
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

    /**
     * Selects the tile and display it in "tile panel" within the MapCreator
     * window.
     *
     * @param tile Tile which will be selected.
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

    public void setTilesArr(ArrayList<Tile> tilesArr) {
        this.tilesArr = tilesArr;
    }

    public void setCircleObjectsArr(ArrayList<CircleObject> circleObjectsArr) {
        this.circleObjectsArr = circleObjectsArr;
    }

    public void setEnemyPanelListener(ShowSelected enemyPanelListener) {
        this.enemyPanelListener = enemyPanelListener;
    }

    public void setChestPanelListener(ShowSelected chestPanelListener) {
        this.chestPanelListener = chestPanelListener;
    }

    public void setTilePanelListener(ShowSelected tilePanelListener) {
        this.tilePanelListener = tilePanelListener;
    }

    public AnchorPane getAnchorPane() {
        return ap;
    }

    public ArrayList<Tile> getTilesArr() {
        return tilesArr;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void setSelectedCirc(Circle selectedCirc) {
        this.selectedCirc = selectedCirc;
    }

    public Circle getSelectedCirc() {
        return selectedCirc;
    }

    public void setObjectSelected(boolean objectSelected) {
        this.objectSelected = objectSelected;
    }

    public boolean isObjectSelected() {
        return objectSelected;
    }

    public Polygon getSelectedHexagon() {
        return selectedHexagon;
    }

    public void setSelectedHexagon(Polygon selectedHexagon) {
        this.selectedHexagon = selectedHexagon;
    }

    public ArrayList<CircleObject> getCircleObjectsArr() {
        return circleObjectsArr;
    }
}
