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
 * AnchorPane showing the selected Object from ListView in right panel within
 * the MapCreator window.
 *
 * @author Miroslav Falcmann
 */
public class SelectedFromListPanelMapCreator implements ShowSelected {

    private final FXMLLoader selectedFromListPanelLoader;
    private final AnchorPane anchorPane;
    private final Circle icon;
    private final Label name;
    private final Label description;
    private final HBox hBox;

    public SelectedFromListPanelMapCreator(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        selectedFromListPanelLoader = new FXMLLoader(getClass().getResource("selected_from_list_panel.fxml"));
        anchorPane = selectedFromListPanelLoader.load();

        // selected object map creator
        icon = (Circle) anchorPane.getChildren().get(0);
        name = (Label) anchorPane.getChildren().get(1);
        description = (Label) anchorPane.getChildren().get(2);
    }

    @Override
    public void showObject(Enemy enemy) {
        icon.setFill(enemy.getImgPat());
        name.setText(enemy.getName());
        description.setText(enemy.getDescription());
        hBox.getChildren().set(1, anchorPane);
    }

    @Override
    public void showObject(Chest chest) {
        icon.setFill(chest.getImgPat());
        name.setText(chest.getName());
        description.setText(chest.getDescription());
        hBox.getChildren().set(1, anchorPane);
    }

    @Override
    public void showObject(Item item) {
        icon.setFill(item.getImgPat());
        name.setText(item.getName());
        description.setText(item.getDescription());
        hBox.getChildren().set(1, anchorPane);
    }

    @Override
    public void showObject(Terrain terrain) {
        icon.setFill(terrain.getImgPat());
        name.setText(terrain.getName());
        description.setText(terrain.getDescription());
        hBox.getChildren().set(1, anchorPane);
    }

    public AnchorPane getanchorPane() {
        return anchorPane;
    }

    @Override
    public void showObject(Player player) {
    }

}
