package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * AnchorPane showing the selected Tile within the MapCreatorWindow. Panel also
 * offers some functions, such as filling all tiles on the board with the
 * currently selected terrain.
 *
 * @author Miroslav Falcmann
 */
public class TilePanelMapCreator implements ShowSelected {

    private final AnchorPane anchorPane;
    private final FXMLLoader tilePanelLoader;
    private final HBox hBox;
    private final Circle icon;
    private final Label name;
    private final Label description;
    private final Button fillBoardBut;
    private ArrayList<Tile> tilesArr;
    private Terrain terrain;
    private MapCreatorWindow mcWindow;

    public TilePanelMapCreator(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        tilePanelLoader = new FXMLLoader(getClass().getResource("tile_panel_map_creator.fxml"));
        anchorPane = tilePanelLoader.load();

        icon = (Circle) anchorPane.getChildren().get(0);
        name = (Label) anchorPane.getChildren().get(1);
        description = (Label) anchorPane.getChildren().get(2);
        fillBoardBut = (Button) anchorPane.getChildren().get(3);
        addEventFilters();

    }

    /**
     * Add mouse click event to "fill the board" button. On click, all tiles on
     * board are filled with the selected terrain.
     */
    private void addEventFilters() {
        EventHandler<MouseEvent> fillBoard = (MouseEvent e) -> {
            for (Tile tile : tilesArr) {
                BaseObject baseObj = ((BaseObject) terrain).createNew();
                baseObj.setHexagon(tile.getHexagon());
                tile.getHexagon().setFill(terrain.getImgPat());
                tile.setTerrain((Terrain) baseObj);
            }
        };
        fillBoardBut.addEventFilter(MouseEvent.MOUSE_CLICKED, fillBoard);
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setTilesArr(ArrayList<Tile> tilesArr) {
        this.tilesArr = tilesArr;
    }

    public void setMcWindow(MapCreatorWindow mcWindow) {
        this.mcWindow = mcWindow;
    }

    @Override
    public void showObject(Enemy enemy) {
    }

    @Override
    public void showObject(Chest chest) {
    }

    @Override
    public void showObject(Item item) {
    }

    @Override
    public void showObject(Terrain terrain) {
        mcWindow.setShowedObject(terrain);
        this.terrain = terrain;
        icon.setFill(terrain.getImgPat());
        name.setText(terrain.getName());
        description.setText(terrain.getDescription());
        hBox.getChildren().set(0, anchorPane);
    }

    @Override
    public void showObject(Player player) {
    }

}
