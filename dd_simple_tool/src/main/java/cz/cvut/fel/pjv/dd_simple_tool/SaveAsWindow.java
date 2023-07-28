package cz.cvut.fel.pjv.dd_simple_tool;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * AnchorPane which let the user enter under which name he/she wishes to save
 * the current game/map and saves the game/map after the "save" button is
 * clicked.
 *
 * @author Miroslav Falcmann
 */
public class SaveAsWindow {

    private final FXMLLoader inputWindowLoader;
    private final TextField textField;
    private final Button saveBut;
    private final AnchorPane ap;
    private Scene scene;
    private final Stage stage;

    private final SaveMap saveMap;
    private final SaveGame saveGame;
    private boolean hasBeenOpened = false;
    private final String mode;

    public SaveAsWindow(String mode) throws IOException {
        this.mode = mode;
        saveMap = new SaveMap();
        saveGame = new SaveGame();

        // initializing objects from FXML file
        inputWindowLoader = new FXMLLoader(getClass().getResource("save_as_window.fxml"));
        ap = inputWindowLoader.load();

        textField = (TextField) ap.getChildren().get(0);
        saveBut = (Button) ap.getChildren().get(1);
        addEventFilters();

        stage = new Stage();
        stage.setTitle("Save game as...");
    }

    /**
     * Add mouse click event to the "save" button according to whether the
     * function was called from Game window or MapCreator window
     */
    private void addEventFilters() {
        EventHandler<MouseEvent> save = (MouseEvent e) -> {
            if (mode.equals("map")) {
                saveMap.saveMap(textField.getText());
            } else if (mode.equals("game")) {
                saveGame.saveGame(textField.getText());
            }
        };
        saveBut.addEventFilter(MouseEvent.MOUSE_CLICKED, save);
    }

    /**
     * Initialize the stage
     */
    private void stageInit() {
        ap.setStyle("-fx-background-color: #44484A;");
        scene = new Scene(ap);
        stage.setScene(scene);
    }

    /**
     * Open the window to show the user saved games or maps and let him choose
     * which he/she wants to load. (On first function call, stage is
     * initialized)
     */
    public void openInputWindow() {
        if (!hasBeenOpened) {
            stageInit();
            hasBeenOpened = true;
        }
        stage.show();
    }

    public SaveMap getSaveMap() {
        return saveMap;
    }

    public SaveGame getSaveGame() {
        return saveGame;
    }

}
