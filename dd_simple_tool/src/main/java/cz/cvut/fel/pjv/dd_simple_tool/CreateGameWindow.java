package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.DeathKnight;
import cz.cvut.fel.pjv.dd_simple_tool.model.Druid;
import cz.cvut.fel.pjv.dd_simple_tool.model.Hunter;
import cz.cvut.fel.pjv.dd_simple_tool.model.Mage;
import cz.cvut.fel.pjv.dd_simple_tool.model.Paladin;
import cz.cvut.fel.pjv.dd_simple_tool.model.Warlock;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Priest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Warrior;
import cz.cvut.fel.pjv.dd_simple_tool.model.Shaman;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * AnchorPane displaying all the important objects connected with events to
 * create players, add them to game and start the game.
 *
 * Each player is given a name through TextLabel, class through ComboBox and
 * then added to game by clicking the "Add" button. Added player will be shown
 * in ListView on the left. By left-clicking the player within the ListView, the
 * player is selected and can be also removed from the game.
 *
 * "Start Game" buttons starts the game with all the players added to the ListView.
 *
 * @author Miroslav Falcmann
 */
public class CreateGameWindow {

    private AnchorPane ap;
    private FXMLLoader createGameWindowLoader;
    private ListView playerListView;
    private Button addPlayerBut;
    private Button startGameBut;
    private Circle classIcon;
    private TextField playerNameField;
    private ComboBox playerClassBox;
    private Button removePlayerBut;
    private Stage stage;
    private Scene scene;
    private boolean hasBeenOpened;
    private final List<Player> playerList;
    private ObservableList<String> classNamesList;
    private ObservableList<Player> classList;
    private GameWindow gWindow;

    public CreateGameWindow() throws IOException {
        this.hasBeenOpened = false;
        this.playerList = new ArrayList<>();
        createGameWindow();
    }

    /**
     * Initializing objects from FXML file
     *
     * @throws IOException if the FXML file is not found
     */
    private void createGameWindow() throws IOException {

        // initializing objects from FXML file
        createGameWindowLoader = new FXMLLoader(getClass().getResource("create_game_final.fxml"));
        initObjectsFromFXML();
        initObjects();
        addEvents();

        gWindow = new GameWindow();

        stage = new Stage();
    }

    private void initObjectsFromFXML() {
        try {
            ap = createGameWindowLoader.load();
            playerListView = (ListView) ap.getChildren().get(0);
            addPlayerBut = (Button) ap.getChildren().get(1);
            startGameBut = (Button) ap.getChildren().get(2);
            classIcon = (Circle) ap.getChildren().get(3);
            playerNameField = (TextField) ap.getChildren().get(4);
            playerClassBox = (ComboBox) ap.getChildren().get(5);
            removePlayerBut = (Button) ap.getChildren().get(6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialization of private objects Adds values (class names) to the List
     * of class names and to ComboBox
     */
    private void initObjects() {
        classNamesList = FXCollections.observableArrayList();
        classList = FXCollections.observableArrayList();
        classNamesList.addAll("Warrior", "Druid", "Mage", "Paladin", "Hunter",
                "Shaman", "Death Knight", "Warlock", "Priest");
        playerClassBox.setPromptText("Select your class");
        playerClassBox.getItems().addAll(classNamesList);
        classIcon.setFill(Color.TRANSPARENT);
    }

    /**
     * Add mouse click events to: "Add player" button - adds created player to
     * ListView. "Remove" button - removes selected player from ListView.
     * "Classes" ComboBox - shows picture of currently selected class."Players"
     * ListView - shows picture and name of player selected in ListView. "Start
     * game" button - starts the game with players which has been added to the
     * "Players" ListView
     */
    private void addEvents() {
        EventHandler<MouseEvent> addPlayer = (MouseEvent e) -> {
            if (playerClassBox.getValue() != null) {
                addPlayerClassToList((String) playerClassBox.getValue());
            }
        };
        addPlayerBut.addEventFilter(MouseEvent.MOUSE_CLICKED, addPlayer);

        EventHandler<MouseEvent> removePlayer = (MouseEvent e) -> {
            if (playerListView.getSelectionModel().getSelectedItem() != null) {
                classList.remove((Player) playerListView.getSelectionModel().getSelectedItem());
                playerList.remove((Player) playerListView.getSelectionModel().getSelectedItem());
            }
        };
        removePlayerBut.addEventFilter(MouseEvent.MOUSE_CLICKED, removePlayer);

        playerClassBox.setOnAction(e -> showSelectedClassIcon((String) playerClassBox.getValue()));

        EventHandler<MouseEvent> playerListClicked = (MouseEvent e) -> {
            Player pl = (Player) playerListView.getSelectionModel().getSelectedItem();
            if (pl != null) {
                classIcon.setFill(pl.getImgPat());
                playerClassBox.setValue(pl.getClassName());
                playerNameField.setText(pl.getName());
            }
        };
        playerListView.addEventFilter(MouseEvent.MOUSE_CLICKED, playerListClicked);

        EventHandler<MouseEvent> startGame = (MouseEvent e) -> {
            openGameWindow();
        };
        startGameBut.addEventFilter(MouseEvent.MOUSE_CLICKED, startGame);
    }

    /**
     * Fill the circle with class picture according to the given class name.
     *
     * @param className Name of the selected class
     */
    private void showSelectedClassIcon(String className) {
        switch (className) {
            case "Warrior":
                classIcon.setFill(new ImagePattern(new Image("warr_icon_wow.png")));
                break;
            case "Hunter":
                classIcon.setFill(new ImagePattern(new Image("hunter_icon_wow.png")));
                break;
            case "Mage":
                classIcon.setFill(new ImagePattern(new Image("mage_icon_wow.png")));
                break;
            case "Druid":
                classIcon.setFill(new ImagePattern(new Image("druid_icon_wow.png")));
                break;
            case "Paladin":
                classIcon.setFill(new ImagePattern(new Image("paladin_icon_wow.png")));
                break;
            case "Warlock":
                classIcon.setFill(new ImagePattern(new Image("warlock_icon_wow.png")));
                break;
            case "Priest":
                classIcon.setFill(new ImagePattern(new Image("priest_icon_wow.png")));
                break;
            case "Shaman":
                classIcon.setFill(new ImagePattern(new Image("shaman_icon_wow.png")));
                break;
            case "Death Knight":
                classIcon.setFill(new ImagePattern(new Image("death_knight_icon_wow2.jpg")));
                break;
            default:
                break;
        }
    }

    /**
     * Create a player according to the given class name and add him to the
     * private List and "Players" ListView
     *
     * @param className name of the class
     */
    private void addPlayerClassToList(String className) {
        switch (className) {
            case "Warrior":
                Warrior warr = new Warrior(playerNameField.getText());
                classList.add(warr);
                playerList.add(warr);
                break;
            case "Hunter":
                Hunter hunt = new Hunter(playerNameField.getText());
                classList.add(hunt);
                playerList.add(hunt);
                break;
            case "Mage":
                Mage mage = new Mage(playerNameField.getText());
                classList.add(mage);
                playerList.add(mage);
                break;
            case "Druid":
                Druid druid = new Druid(playerNameField.getText());
                classList.add(druid);
                playerList.add(druid);
                break;
            case "Paladin":
                Paladin pala = new Paladin(playerNameField.getText());
                classList.add(pala);
                playerList.add(pala);
                break;
            case "Warlock":
                Warlock war = new Warlock(playerNameField.getText());
                classList.add(war);
                playerList.add(war);
                break;
            case "Priest":
                Priest priest = new Priest(playerNameField.getText());
                classList.add(priest);
                playerList.add(priest);
                break;
            case "Shaman":
                Shaman shaman = new Shaman(playerNameField.getText());
                classList.add(shaman);
                playerList.add(shaman);
                break;
            case "Death Knight":
                DeathKnight dk = new DeathKnight(playerNameField.getText());
                classList.add(dk);
                playerList.add(dk);
                break;
            default:
                break;
        }
        playerListView.setItems(classList);
    }

    /**
     * Initialize necessary components and open new window for the game.
     */
    private void openGameWindow() {
        gWindow.setPlayerListView(playerListView);
        gWindow.getBottomP().setPlayerList(playerListView);
        gWindow.getRightP().getSaveAsWindow().getSaveGame().setPlayerList((ArrayList) playerList);
        gWindow.getBottomP().addPlayerListEvent();
        gWindow.getBottomP().gethBox().getChildren().set(0, playerListView);
        Stage stage = new Stage();
        stage.setTitle("Dungeons and Dragons simple tool");
        stage.setResizable(false);
        stage.setScene(gWindow.getScene());
        stage.show();
    }

    /**
     * Initialize the stage
     */
    private void sceneInit() {
        ap.setStyle("-fx-background-color: #44484A;");
        scene = new Scene(ap);
        stage.setTitle("Create game");
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Opens the window for setting the game parameters.
     */
    public void openInputWindow() {
        if (!hasBeenOpened) {
            sceneInit();
            hasBeenOpened = true;
        } else {
            stage.close();
        }
        stage.setTitle("Create game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }

    public ListView getPlayerListView() {
        return playerListView;
    }

}
