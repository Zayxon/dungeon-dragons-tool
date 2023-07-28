package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.view.ConfirmBox;
import cz.cvut.fel.pjv.dd_simple_tool.view.StartWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
//import cz.cvut.fel.pjv.dd_simple_tool.tests.MainTests;

/**
 * Main class launching the JavaFX application
 *
 * @author Miroslav Falcmann
 */
public class App extends Application {

    private static Scene scene;
    //private final static Logger LOGGER = Logger.

    @Override
    public void start(Stage stage) throws IOException {

// for testing the single game components-----------------------------------------------------     
        // showing Map Creator Window
//        MapCreatorWindow mapC = new MapCreatorWindow();
//        scene = mapC.getScene();
        // showing Game Window
//        GameWindow game = new GameWindow();
//        scene = game.getScene();
        // showing Create Game Window
//        CreateGameWindow cg = new CreateGameWindow();
//        scene = cg.getScene();
// for testing the single game components-----------------------------------------------------  



        // setting Start Window scene
        StartWindow start = new StartWindow();
        scene = start.getScene();

        // showing scene
        stage.setTitle("D&D Simple Tool");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
