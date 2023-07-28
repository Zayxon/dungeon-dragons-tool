package cz.cvut.fel.pjv.dd_simple_tool;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * AnchorPane showing the currently selected item from the ListView in right
 * panel within the MapCreator window.
 *
 * @author Miroslav Falcmann
 */
public class ItemPanel {

    private final AnchorPane ap;
    private final double width;
    private final double height;

    public ItemPanel() throws IOException {
        this.height = 160;
        this.width = 350;

        // initializing objects from FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("item_panel.fxml"));
        ap = loader.load();
        ap.setMaxSize(width, height);
        ap.setMinSize(width, height);
    }

    public AnchorPane getAnchorPane() {
        return ap;
    }
}
