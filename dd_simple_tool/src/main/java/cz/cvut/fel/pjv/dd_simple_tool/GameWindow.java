package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * BorderPane with all the necessary panels for playing the game.
 *
 * @author Miroslav Falcmann
 */
public class GameWindow {

    private final Scene scene;
    private final BorderPane borderP;

    private final RightPanelGame rightP;
    private final BottomPanelGame bottomP;
    private BaseObject selectedObject;
    private BaseObject showedObject;
    private ListView playerListView;
    private int rolledNumber;
    private Player selectedPlayer;

    private int selectedItemIndex;
    private int selectedEquipIndex;
    private boolean selectedFromInventory;
    private boolean selectedFromEquip;

    private ArrayList<Tile> tilesArr;
    private ArrayList<CircleObject> objectsArr;

    private final FXMLLoader borderPaneLoader;

    public GameWindow() throws IOException {
        this.borderPaneLoader = new FXMLLoader(getClass().getResource("game_interface_game.fxml"));
        this.rolledNumber = 20;
        this.selectedFromInventory = false;
        this.selectedFromEquip = false;
        this.objectsArr = new ArrayList<>();
        this.tilesArr = new ArrayList<>();

        borderP = borderPaneLoader.load();
        rightP = new RightPanelGame();
        bottomP = new BottomPanelGame();

        bottomP.getChestPanel().setgWindow(this);
        bottomP.getEnemyPanel().setgWindow(this);
        bottomP.setgWindow(this);
        bottomP.getPlayerPanel().setgWindow(this);

        rightP.getLoadMapWindow().setgWindow(this);
        rightP.setgWindow(this);

        AnchorPane center = new AnchorPane();
        center.setMaxSize(1000, 200);
        center.setMinSize(1000, 200);

        borderP.setCenter(center);
        borderP.setBottom(bottomP.gethBox());
        borderP.setRight(rightP.getvBox());
        bottomP.getRollDicePanel().setgWindow(this);

        scene = new Scene(borderP);

    }
    
    /**
     * Remove the "stroke view" of currently selected item 
     */
    public void removeItemSelection() {
        if (selectedObject != null) {
            getSelectedObject().rectangle.setStroke(Color.BLACK);
            getSelectedObject().rectangle.setStrokeWidth(0.75);
        }
    }
    
    public int getSelectedEquipIndex() {
        return selectedEquipIndex;
    }

    public void setSelectedEquipIndex(int selectedEquipIndex) {
        this.selectedEquipIndex = selectedEquipIndex;
    }

    public Scene getScene() {
        return scene;
    }

    public int getRolledNumber() {
        return rolledNumber;
    }

    public void setRolledNumber(int rolledNumber) {
        this.rolledNumber = rolledNumber;
    }

    public RightPanelGame getRightP() {
        return rightP;
    }

    public BottomPanelGame getBottomP() {
        return bottomP;
    }

    public BorderPane getBorderP() {
        return borderP;
    }

    public ArrayList<Tile> getTilesArr() {
        return tilesArr;
    }

    public void setTilesArr(ArrayList<Tile> tilesArr) {
        this.tilesArr = tilesArr;
    }

    public ArrayList<CircleObject> getObjectsArr() {
        return objectsArr;
    }

    public void setObjectsArr(ArrayList<CircleObject> objectsArr) {
        this.objectsArr = objectsArr;
    }

    public BaseObject getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(BaseObject selectedObject) {
        this.selectedObject = selectedObject;
    }

    public BaseObject getShowedObject() {
        return showedObject;
    }

    public void setShowedObject(BaseObject showedObject) {
        this.showedObject = showedObject;
    }

    public void setPlayerListView(ListView playerListView) {
        this.playerListView = playerListView;
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setSelectedPlayer(Player selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }
    
    public boolean isSelectedFromEquip() {
        return selectedFromEquip;
    }

    public void setSelectedFromEquip(boolean selectedFromEquip) {
        this.selectedFromEquip = selectedFromEquip;
    }

    public boolean isSelectedFromInventory() {
        return selectedFromInventory;
    }

    public void setSelectedFromInventory(boolean selectedFromInventory) {
        this.selectedFromInventory = selectedFromInventory;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

}
