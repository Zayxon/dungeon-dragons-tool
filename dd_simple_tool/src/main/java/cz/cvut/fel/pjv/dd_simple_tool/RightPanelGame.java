package cz.cvut.fel.pjv.dd_simple_tool;

import static cz.cvut.fel.pjv.dd_simple_tool.RightPanelMapCreator.addDisplayObjectButtonEvent;
import cz.cvut.fel.pjv.dd_simple_tool.model.Equip;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.dd_simple_tool.view.ConfirmBox;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * VBox showing ListView of objects that can be added to map within the Game
 * window. Objects are sorted into 8 different tabs, after selecting a tab,
 * contents are displayed in the ListView below.
 *
 * 9. tab "Maps" opens a window showing map saves, which can be loaded into the
 * game.
 *
 * Below ListView is "Give panel" where user can input some values to adjust the
 * stats of currently selected player.
 * 
 * Below "Give panel" are showed currently selected objects.
 *
 * @author Miroslav Falcmann
 */
public class RightPanelGame {

    private final FXMLLoader rightPanelLoader;
    private final VBox vBox;

    private final GridPane gridPane;
    private final HBox hBox;
    private final AnchorPane givePanel;
    private final AnchorPane selectedItemPanel;

    private Button saveGameBut;
    private Button exitGameBut;

    private Button bootsBut;
    private Button usablesBut;
    private Button helmetsBut;
    private Button leggingsBut;
    private Button chestsBut;
    private Button junkBut;
    private Button handsBut;
    private Button weaponsBut;
    private Button mapsBut;

    private Button addBut;
    private TextField goldField;
    private TextField xpField;
    private TextField hpField;
    private TextField energyField;

    public ListView objectsList;

    private ObservableList<Equip> weapons;
    private ObservableList<Equip> boots;
    private ObservableList<Equip> helmets;
    private ObservableList<Equip> leggings;
    private ObservableList<Equip> chests;
    private ObservableList<Equip> hands;
    private ObservableList<Item> junk;
    private ObservableList<Item> usables;
    private final SaveAsWindow saveAsWindow;
    private final SelectedItemPanelGame selectedItemPanelGame;
    private GameWindow gWindow;
    private final LoadMapWindow loadMapWindow;

    private final List<ShowSelected> listeners;

    public RightPanelGame() throws IOException {
        this.listeners = new ArrayList<>();

        // initializing objects from FXML file
        rightPanelLoader = new FXMLLoader(getClass().getResource("right_panel_game.fxml"));
        vBox = rightPanelLoader.load();

        selectedItemPanelGame = new SelectedItemPanelGame(this.vBox);
        listeners.add(selectedItemPanelGame);

        hBox = (HBox) vBox.getChildren().get(0);
        gridPane = (GridPane) vBox.getChildren().get(1);
        objectsList = (ListView) vBox.getChildren().get(2);
        givePanel = (AnchorPane) vBox.getChildren().get(3);
        initGivePanelObjects();

        selectedItemPanel = new AnchorPane();
        selectedItemPanel.setMaxSize(255, 175);
        selectedItemPanel.setMinSize(255, 175);
        vBox.getChildren().add(selectedItemPanel);

        loadMapWindow = new LoadMapWindow("game");
        initAllButtons();
        initObservableLists(new RightPanelMapCreator());

        saveAsWindow = new SaveAsWindow("game");
        addEventFilters();
    }

    /**
     * Initialize objects of "give_panel" from FXML file.
     */
    private void initGivePanelObjects() {
        addBut = (Button) givePanel.getChildren().get(0);
        goldField = (TextField) givePanel.getChildren().get(1);
        xpField = (TextField) givePanel.getChildren().get(2);
        hpField = (TextField) givePanel.getChildren().get(3);
        energyField = (TextField) givePanel.getChildren().get(4);
    }

    /**
     * Add events of all buttons.
     */
    private void addButtonEvents() {
        addDisplayObjectButtonEvent(weaponsBut, weapons, objectsList);
        addDisplayObjectButtonEvent(helmetsBut, helmets, objectsList);
        addDisplayObjectButtonEvent(chestsBut, chests, objectsList);
        addDisplayObjectButtonEvent(bootsBut, boots, objectsList);
        addDisplayObjectButtonEvent(leggingsBut, leggings, objectsList);
        addDisplayObjectButtonEvent(handsBut, hands, objectsList);
        addDisplayObjectButtonEvent(usablesBut, usables, objectsList);
        addDisplayObjectButtonEvent(junkBut, junk, objectsList);
        addDisplayLoadedMapsEvent();
        addSaveGameButtonEvent();
        addExitButtonEvent();

    }

    private void addExitButtonEvent() {
        exitGameBut.setOnMouseClicked(e -> ConfirmBox.closeStageConfirmBox(exitGameBut, "Exit the game", "Are you sure you want to exit the game?"));
    }

    /**
     * Add mouse click event to "Save game" button.
     */
    private void addSaveGameButtonEvent() {
        saveGameBut.setOnAction(e -> saveAsWindow.openInputWindow());
    }

    /**
     * Add mouse click event to "Add" button.
     *
     * @param player
     */
    public void addAddButtonEvent(Player player) {
        addBut.setOnAction(e -> increasePlayerStats(player));
    }

    /**
     * Add mouse click event to "Maps" button.
     */
    private void addDisplayLoadedMapsEvent() {
        EventHandler<MouseEvent> loadMap = (MouseEvent e) -> {
            loadMapWindow.openInputWindow();
        };
        mapsBut.addEventFilter(MouseEvent.MOUSE_CLICKED, loadMap);
    }

    /**
     * Increase player stats according to the given parameters
     *
     * @param player Player whose stats will be increased
     */
    private void increasePlayerStats(Player player) {
        if (isDouble(goldField.getText())) {
            player.addGold((int) Double.parseDouble(goldField.getText()));
        }
        if (isDouble(xpField.getText())) {
            player.addXp((int) Double.parseDouble(xpField.getText()));
        }
        if (isDouble(hpField.getText())) {
            player.addHp((int) Double.parseDouble(hpField.getText()));
        }
        if (isDouble(energyField.getText())) {
            player.addEnergy((int) Double.parseDouble(energyField.getText()));
        }
        gWindow.getBottomP().getPlayerPanel().updateView();
    }

    /**
     * Decides whether the given string can be cast to double
     *
     * @param str String that needs to be tested if it can be cast to double
     * @return false if the given string cannot be cast double, true if the
     * selected string can be cast double
     */
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Add mouse click event to ListView displaying Items. Selected item is on
     * click selected and shown in panel within the Game window
     */
    private void addEventFilters() {
        addButtonEvents();

        EventHandler<MouseEvent> listViewClicked = (MouseEvent e) -> {
            for (ShowSelected listener : listeners) {
                ((BaseObject) objectsList.getSelectionModel().getSelectedItem()).registerToListener(listener);
            }
            gWindow.setSelectedFromInventory(false);
            gWindow.setSelectedFromEquip(false);
            if (gWindow.getSelectedObject() instanceof Item) {
                gWindow.removeItemSelection();
                gWindow.setSelectedObject(null);
            }
        };
        objectsList.addEventFilter(MouseEvent.MOUSE_CLICKED, listViewClicked);
    }

    /**
     * Initialize private buttons from FXML file
     */
    private void initAllButtons() {
        // HBox
        saveGameBut = (Button) hBox.getChildren().get(0);
        exitGameBut = (Button) hBox.getChildren().get(1);

        // GridPane
        bootsBut = (Button) gridPane.getChildren().get(0);
        usablesBut = (Button) gridPane.getChildren().get(1);
        helmetsBut = (Button) gridPane.getChildren().get(2);
        leggingsBut = (Button) gridPane.getChildren().get(3);
        chestsBut = (Button) gridPane.getChildren().get(4);
        junkBut = (Button) gridPane.getChildren().get(5);
        handsBut = (Button) gridPane.getChildren().get(6);
        weaponsBut = (Button) gridPane.getChildren().get(7);
        mapsBut = (Button) gridPane.getChildren().get(8);
    }

    /**
     * Initialize ObservableLists with values already defined in
     * RightPanelMapCreator
     *
     * @param rp RightPanelMapCreator pointer to use its already defined values
     */
    private void initObservableLists(RightPanelMapCreator rp) {
        helmets = rp.getHelmets();
        leggings = rp.getLeggings();
        junk = rp.getJunk();
        usables = rp.getUsables();
        chests = rp.getChests();
        hands = rp.getHands();
        weapons = rp.getWeapons();
        boots = rp.getBoots();
    }

    public VBox getvBox() {
        return vBox;
    }

    public void addListener(ShowSelected toAdd) {
        listeners.add(toAdd);
    }

    public ListView getObjectsList() {
        return objectsList;
    }

    public LoadMapWindow getLoadMapWindow() {
        return loadMapWindow;
    }

    public SaveAsWindow getSaveAsWindow() {
        return saveAsWindow;
    }

    public void setgWindow(GameWindow gWindow) {
        this.gWindow = gWindow;
    }

    public SelectedItemPanelGame getSelectedItemPanelGame() {
        return selectedItemPanelGame;
    }
}
