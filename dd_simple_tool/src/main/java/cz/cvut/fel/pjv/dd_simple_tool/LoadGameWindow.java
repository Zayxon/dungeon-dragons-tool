package cz.cvut.fel.pjv.dd_simple_tool;

import java.io.File;
import java.io.IOException;
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
 * AnchorPane showing ListView of all saved games that can be loaded into Game
 * window. User can choose one of the games and load it by clicking the "Load"
 * button.
 *
 * @author Miroslav Falcmann
 */
public class LoadGameWindow {

    private final FXMLLoader loadGameWindowLoader;
    private final Button loadBut;
    private final AnchorPane ap;
    private Scene scene;
    private final Stage stage;

    private boolean hasBeenOpened;
    private LoadGame loadGame;

    private final ListView listView;

    public LoadGameWindow() throws IOException {
        this.hasBeenOpened = false;

        // initializing objects from FXML file
        loadGameWindowLoader = new FXMLLoader(getClass().getResource("load_game_window.fxml"));
        ap = loadGameWindowLoader.load();

        listView = (ListView) ap.getChildren().get(0);
        listView.setItems(getSavedGames());
        loadBut = (Button) ap.getChildren().get(1);
        addEventFilters();

        stage = new Stage();
    }

    /**
     * Creates a list of filenames in path "saves/saved_games".
     *
     * @return ObservableList of filenames.
     */
    private ObservableList<String> getSavedGames() {
        ObservableList<String> savedGames = FXCollections.observableArrayList();
        File[] files = new File("saves/saved_games").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
            if (file.isFile()) {
                savedGames.add(file.getName());
            }
        }
        return savedGames;
    }

    /**
     * Add mouse click event to "Load Game" button
     */
    private void addEventFilters() {
        EventHandler<MouseEvent> load = (MouseEvent e) -> {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            try {
                loadGame = new LoadGame();
                loadGame.loadGame(listView.getSelectionModel().getSelectedItem().toString());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        loadBut.addEventFilter(MouseEvent.MOUSE_CLICKED, load);
    }

    /**
     * Initialize the stage
     */
    private void stageInit() {
        ap.setStyle("-fx-background-color: #44484A;");
        scene = new Scene(ap);
        stage.setTitle("Load game");
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Open the window to show the user loaded games and let him choose which
     * he/she wants to load. (On first function call, stage is initialized)
     */
    public void openInputWindow() {
        if (!hasBeenOpened) {
            stageInit();
            hasBeenOpened = true;
        }
        listView.setItems(getSavedGames());
        stage.show();
    }
}
