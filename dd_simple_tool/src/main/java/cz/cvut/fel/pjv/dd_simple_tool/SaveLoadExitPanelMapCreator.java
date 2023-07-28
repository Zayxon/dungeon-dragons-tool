package cz.cvut.fel.pjv.dd_simple_tool;

import java.io.IOException;

import cz.cvut.fel.pjv.dd_simple_tool.view.ConfirmBox;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * AnchorPane showing the 3 buttons "Save map", "Load Map" and "Exit game"
 * connected with events within the MapCreator window.
 *
 * @author Miroslav Falcmann
 */
public class SaveLoadExitPanelMapCreator {

    private final FXMLLoader saveLoadExitPanelLoader;
    private final AnchorPane anchorPane;
    private final Button saveGameBut;
    private final Button loadGameBut;
    private final Button exitBut;
    private final SaveAsWindow saveAsWindow;
    private final LoadMapWindow loadMapWindow;

    public SaveLoadExitPanelMapCreator() throws IOException {

        // initializing objects from FXML file
        saveLoadExitPanelLoader = new FXMLLoader(getClass().getResource("save_load_exit_map_creator.fxml"));
        anchorPane = saveLoadExitPanelLoader.load();

        // saveLoadExitPanel objects
        saveGameBut = (Button) anchorPane.getChildren().get(0);
        loadGameBut = (Button) anchorPane.getChildren().get(1);
        exitBut = (Button) anchorPane.getChildren().get(2);
        addEventFilters();

        saveAsWindow = new SaveAsWindow("map");
        loadMapWindow = new LoadMapWindow("mapCreator");
    }

    /**
     * Add events to "save game" button, "load game" button and "exit button".
     *
     * @throws IOException if loaded map is not found
     */
    private void addEventFilters() throws IOException {
        EventHandler<MouseEvent> saveGame = (MouseEvent e) -> {
            saveAsWindow.openInputWindow();
        };
        saveGameBut.addEventFilter(MouseEvent.MOUSE_CLICKED, saveGame);

        EventHandler<MouseEvent> loadGame = (MouseEvent e) -> {
            loadMapWindow.openInputWindow();
        };
        loadGameBut.addEventFilter(MouseEvent.MOUSE_CLICKED, loadGame);

        EventHandler<MouseEvent> exit = (MouseEvent e) -> {
            ConfirmBox.closeStageConfirmBox(exitBut, "Exit Map Creator", "Are you sure you want to exit Map Creator?");
        };
        exitBut.addEventFilter(MouseEvent.MOUSE_CLICKED, exit);
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public SaveAsWindow getSaveAsWindow() {
        return saveAsWindow;
    }

    public LoadMapWindow getLoadMapWindow() {
        return loadMapWindow;
    }

}
