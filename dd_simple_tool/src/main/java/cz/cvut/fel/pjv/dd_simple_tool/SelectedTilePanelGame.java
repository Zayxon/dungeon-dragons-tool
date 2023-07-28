package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * AnchorPane showing the selected Tile within the Game window.
 *
 * @author Miroslav Falcmann
 */
public class SelectedTilePanelGame implements ShowSelected {

    private final FXMLLoader selectedFromListPanelLoader;
    private final AnchorPane anchorPane;
    private final Circle icon;
    private final Label name;
    private final Label description;
    private final HBox hBox;

    public SelectedTilePanelGame(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        selectedFromListPanelLoader = new FXMLLoader(getClass().getResource("selected_tile_panel_game.fxml"));
        anchorPane = selectedFromListPanelLoader.load();

        // selected object map creator
        icon = (Circle) anchorPane.getChildren().get(0);
        name = (Label) anchorPane.getChildren().get(1);
        description = (Label) anchorPane.getChildren().get(2);
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
        icon.setFill(terrain.getImgPat());
        name.setText(terrain.getName());
        description.setText(terrain.getDescription());
        hBox.getChildren().set(2, anchorPane);
    }

    @Override
    public void showObject(Player player) {
    }

    public AnchorPane getanchorPane() {
        return anchorPane;
    }
}
