package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * AnchorPane showing ListView of all saved maps that can be loaded into Game
 * window or MapCreator window. User can choose one of the maps and load it by
 * clicking the "Load" button.
 *
 * @author Miroslav Falcmann
 */
public class LoadMapWindow {

    private final FXMLLoader loadMapWindowLoader;
    private final Button loadBut;
    private final AnchorPane ap;
    private Scene scene;
    private final Stage stage;

    private boolean hasBeenOpened;
    private LoadMap loadMap;
    private MapCreatorWindow mcWindow;
    private GameWindow gWindow;
    private ListView listView;

    public LoadMapWindow(String type) throws IOException {

        this.hasBeenOpened = false;

        // initializing objects from FXML file
        loadMapWindowLoader = new FXMLLoader(getClass().getResource("load_map_window.fxml"));
        ap = loadMapWindowLoader.load();

        listView = (ListView) ap.getChildren().get(0);
        listView.setItems(getSavedMaps());
        loadBut = (Button) ap.getChildren().get(1);
        addEventFilters(type);

        stage = new Stage();
        stage.setTitle("Load map");
    }

    /**
     * Creates a list of filenames in path "saves/saved_maps".
     *
     * @return ObservableList of filenames.
     */
    private ObservableList<String> getSavedMaps() {
        ObservableList<String> savedMaps = FXCollections.observableArrayList();
        File[] files = new File("saves/saved_maps").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
            if (file.isFile()) {
                savedMaps.add(file.getName());
            }
        }
        return savedMaps;
    }

    /**
     * Add events to objects in loaded map according to whether the function was
     * called in MapCreator window or Game window.
     *
     * @param type String deciding which events need to be added.
     */
    private void addEventFilters(String type) {
        EventHandler<MouseEvent> loadGame = (MouseEvent e) -> {
            try {
                loadMap = new LoadMap();
                loadMap.loadMap(listView.getSelectionModel().getSelectedItem().toString());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (type.equals("mapCreator")) {
                MapCreatorEvents mcEvents = new MapCreatorEvents(loadMap.getTilesArr(), loadMap.getObjectsArr(), loadMap.getAnchorPane(), mcWindow);
                mcEvents.addCircleEvents();
                mcEvents.addHexagonEvents();
                mcWindow.getBorderP().setCenter(loadMap.getAnchorPane());
                mcWindow.getMp().setTilesArr(loadMap.getTilesArr());
                mcWindow.getMp().setCircleObjectsArr(loadMap.getObjectsArr());
            } else if (type.equals("game")) {
                GameEvents gEvents = new GameEvents(loadMap.getTilesArr(), loadMap.getObjectsArr(), loadMap.getAnchorPane(), gWindow);
                gEvents.addCircleEvents();
                gEvents.addHexagonEvents();
                gWindow.getBorderP().setCenter(loadMap.getAnchorPane());
                gWindow.setTilesArr(loadMap.getTilesArr());
                gWindow.setObjectsArr(loadMap.getObjectsArr());
                setPlayersAddedToMap(false);
            }

        };
        loadBut.addEventFilter(MouseEvent.MOUSE_CLICKED, loadGame);
    }

    /**
     * Set the boolean value "playerAddedToMap" to true or false for each player
     * in PlayerList. Also add the circle mouse click event for the circle
     * object od each player.
     *
     * @param bool true or false, whether the player is already added to map or
     * not
     */
    private void setPlayersAddedToMap(boolean bool) {
        List<Player> pList = gWindow.getBottomP().getPlayerList().getItems();
        for (Player player : pList) {
            player.setAddedToMap(bool);
            player.addCircleEvent(gWindow, player);
        }
    }

    /**
     * Initialize the stage
     */
    private void stageInit() {
        ap.setStyle("-fx-background-color: #44484A;");
        scene = new Scene(ap);
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Open the window to show the user loaded games and let him choose which he
     * wants to load. (On first function call, stage is initialized)
     */
    public void openInputWindow() {
        if (!hasBeenOpened) {
            stageInit();
            hasBeenOpened = true;
        }
        listView.setItems(getSavedMaps());
        stage.show();
    }

    public void setMcWindow(MapCreatorWindow mcWindow) {
        this.mcWindow = mcWindow;
    }

    public void setgWindow(GameWindow gWindow) {
        this.gWindow = gWindow;
    }

    public LoadMap getLoadMap() {
        return loadMap;
    }

    public ListView getListView() {
        return listView;
    }
    
}
