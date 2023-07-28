package cz.cvut.fel.pjv.dd_simple_tool.view;

import cz.cvut.fel.pjv.dd_simple_tool.App;
import cz.cvut.fel.pjv.dd_simple_tool.CreateGameWindow;
import cz.cvut.fel.pjv.dd_simple_tool.LoadGameWindow;
import cz.cvut.fel.pjv.dd_simple_tool.MapCreatorWindow;
import java.io.IOException;
import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main window menu opened after launching the application. User can choose
 * between 4 buttons what his next move will be.
 *
 * @author Miroslav Falcmann
 */
public class StartWindow {

    private final int windowWidth;
    private final int windowHeight;
    private final int buttonWidth;
    private final int buttonHeight;
    private final int buttonStartLayoutX;
    private final int buttonStartLayoutY;
    private final AnchorPane layout;
    private final Button createNewGame;
    private final Button createNewMap;
    private final Button loadGame;
    private final Button exitGame;
    private final Font buttonFont;
    private Scene scene;
    private final Button[] buttons;

    private final CreateGameWindow createGameWindow;
    private final LoadGameWindow loadGameWindow;
    private final MapCreatorWindow mapCreatorWindow;

    public StartWindow() throws IOException {
        // initializing the properties of JavaFX objects
        windowWidth = 400;
        windowHeight = 300;
        buttonWidth = 200;
        buttonHeight = 40;
        buttonStartLayoutX = 100;
        buttonStartLayoutY = 70;
        layout = new AnchorPane();

        createNewGame = new Button("Create new Game");
        createNewMap = new Button("Create new Map");
        loadGame = new Button("Load Game");
        exitGame = new Button("Exit Game");
        buttonFont = Font.font("High Tower Text", 18);
        buttons = new Button[]{createNewGame, createNewMap, loadGame, exitGame};

        // creating the application windows for opening them later
        createGameWindow = new CreateGameWindow();
        loadGameWindow = new LoadGameWindow();
        mapCreatorWindow = new MapCreatorWindow();
        startWindowInit();
    }

    /**
     * Initializes the scene of "Start Window"
     */
    private void startWindowInit() {
        layout.setStyle("-fx-background-color: #44484A;");
        allButtonsInit();
        layout.getChildren().addAll(Arrays.asList(buttons));
        scene = new Scene(layout, windowWidth, windowHeight);
    }

    /**
     * Sets properties of all buttons within the "Start window" and add mouse
     * click events to them.
     */
    private void allButtonsInit() {
        int i = 0;
        for (Button bt : buttons) {
            bt.setMinWidth(buttonWidth);
            bt.setMaxWidth(buttonWidth);
            bt.setMinHeight(buttonHeight);
            bt.setMaxHeight(buttonHeight);
            bt.setFont(buttonFont);
            bt.setLayoutX(buttonStartLayoutX);
            bt.setLayoutY(buttonStartLayoutY + i * buttonHeight + i * 5);
            i++;
        }

        createNewGame.setOnMouseClicked(e -> openCreateGameWindow());
        createNewMap.setOnMouseClicked(e -> openMapCreatorWindow());
        loadGame.setOnMouseClicked(e -> openLoadGameWindow());
        exitGame.setOnMouseClicked(e -> openExitGameWindow());
    }

    private void openMapCreatorWindow() {
        mapCreatorWindow.openInputWindow();
    }

    private void openCreateGameWindow() {
        createGameWindow.openInputWindow();
    }

    private void openLoadGameWindow() {
        loadGameWindow.openInputWindow();
    }

    private void openExitGameWindow() {
        ConfirmBox.closeStageConfirmBox(exitGame, "Exit the game", "Are you sure you want to exit the game?");
    }

    public Scene getScene() {
        return scene;
    }

}
