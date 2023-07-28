package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.EnergyPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Food;
import cz.cvut.fel.pjv.dd_simple_tool.model.HealthPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * AnchorPane showing the selected Item within the Game window. If the item is
 * selected from inventory, panel also offers some functions, such as using the
 * item, selling the item or discarding the item from the game.
 *
 * @author Miroslav Falcmann
 */
public class SelectedItemPanelGame implements ShowSelected {

    private final VBox vBox;

    private FXMLLoader selectedItemPanelLoader;
    private AnchorPane anchorPane;
    private AnchorPane emptyAnchorPane;
    private Circle icon;
    private Label name;
    private Label description;

    private FXMLLoader selectedItemPanelSellButtonLoader;
    private AnchorPane anchorPaneSellButton;
    private Circle iconSellBut;
    private Label nameSellBut;
    private Label descriptionSellBut;
    private Button sellBut;
    private Button discardBut;
    private Button useBut;
    private final boolean eventsAdded;
    private boolean inventoryItem;

    public SelectedItemPanelGame(VBox vBox) throws IOException {
        this.inventoryItem = false;
        this.eventsAdded = false;
        this.vBox = vBox;
        initBasicAnchorPane();
        initSellButtonAnchorPane();
        initEmptyAnchorPane();
    }

    /**
     * Initialize an empty AnchorPane with proper size.
     */
    private void initEmptyAnchorPane() {
        emptyAnchorPane = new AnchorPane();
        emptyAnchorPane.setMaxSize(255, 175);
        emptyAnchorPane.setMinSize(255, 175);
    }

    /**
     * Add events to "sell" button, "discard" button and "use" button.
     *
     * @param player Currently selected player
     * @param gWindow pointer to GameWindow to get currently selected object
     */
    public void addEvents(Player player, GameWindow gWindow) {
        sellBut.setOnAction(e -> sellItem(player, gWindow));
        discardBut.setOnAction(e -> discardItem(player, gWindow));
        useBut.setOnAction(e -> useItem(player, gWindow));
    }

    /**
     * Invoke the "sell" method of currently selected Item from players
     * inventory.
     *
     * @param player Currently selected player whose item will be used
     * @param gWindow pointer to GameWindow to get currently selected object
     */
    public void sellItem(Player player, GameWindow gWindow) {
        player.sellItem((Item) gWindow.getSelectedObject());
        gWindow.getBottomP().getPlayerPanel().updateGoldLabel();
        discardItem(player, gWindow);
    }

    /**
     * Invoke the "use" method of currently selected Item from players
     * inventory.
     *
     * @param player Currently selected player whose item will be used
     * @param gWindow pointer to GameWindow to get currently selected object
     */
    public void useItem(Player player, GameWindow gWindow) {
        if (gWindow.getSelectedObject() instanceof HealthPotion) {
            ((HealthPotion) gWindow.getSelectedObject()).use(player);
            gWindow.getBottomP().getPlayerPanel().updateView();
            discardItem(player, gWindow);
        } else if (gWindow.getSelectedObject() instanceof EnergyPotion) {
            ((EnergyPotion) gWindow.getSelectedObject()).use(player);
            gWindow.getBottomP().getPlayerPanel().updateView();
            discardItem(player, gWindow);
        } else if (gWindow.getSelectedObject() instanceof Food) {
            ((Food) gWindow.getSelectedObject()).use(player);
            gWindow.getBottomP().getPlayerPanel().updateView();
            discardItem(player, gWindow);
        }
    }

    /**
     * Remove the currently selected item from game.
     *
     * @param player Currently selected player whose item will be discarded
     * @param gWindow pointer to GameWindow to get currently selected object
     */
    public void discardItem(Player player, GameWindow gWindow) {
        gWindow.getSelectedObject().rectangle.setFill(Color.BLACK);
        gWindow.getSelectedObject().rectangle.setStroke(Color.BLACK);
        gWindow.getSelectedObject().rectangle.setStrokeWidth(0.75);
        player.getContainer()[gWindow.getSelectedItemIndex()] = null;
        gWindow.setSelectedObject(null);
        vBox.getChildren().set(4, emptyAnchorPane);
    }

    /**
     * Initialize basic AnchorPane objects from FXML file
     *
     * @throws IOException if FXML file is not found
     */
    private void initBasicAnchorPane() throws IOException {
        selectedItemPanelLoader = new FXMLLoader(getClass().getResource("selected_item_panel_game.fxml"));
        anchorPane = selectedItemPanelLoader.load();
        icon = (Circle) anchorPane.getChildren().get(0);
        name = (Label) anchorPane.getChildren().get(1);
        description = (Label) anchorPane.getChildren().get(2);
    }

    /**
     * Initialize special AnchorPane objects from FXML file
     *
     * @throws IOException if FXML file is not found
     */
    private void initSellButtonAnchorPane() throws IOException {
        selectedItemPanelSellButtonLoader = new FXMLLoader(getClass().getResource("selected_item_panel_game_sell_button.fxml"));
        anchorPaneSellButton = selectedItemPanelSellButtonLoader.load();
        iconSellBut = (Circle) anchorPaneSellButton.getChildren().get(0);
        nameSellBut = (Label) anchorPaneSellButton.getChildren().get(1);
        descriptionSellBut = (Label) anchorPaneSellButton.getChildren().get(2);
        useBut = (Button) anchorPaneSellButton.getChildren().get(3);
        sellBut = (Button) anchorPaneSellButton.getChildren().get(4);
        discardBut = (Button) anchorPaneSellButton.getChildren().get(5);
    }

    /**
     * If the item is selected from inventory, panel showing selected item has
     * also buttons connected with functions.
     *
     * @param item selected Item
     */
    @Override
    public void showObject(Item item) {
        if (inventoryItem) {
            iconSellBut.setFill(item.getImgPat());
            nameSellBut.setText(item.getName());
            descriptionSellBut.setText(item.getDescription());
            vBox.getChildren().set(4, anchorPaneSellButton);
        } else {
            icon.setFill(item.getImgPat());
            name.setText(item.getName());
            description.setText(item.getDescription());
            vBox.getChildren().set(4, anchorPane);
        }
    }

    public AnchorPane getanchorPane() {
        return anchorPane;
    }

    public void setInventoryItem(boolean inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public boolean isEventsAdded() {
        return eventsAdded;
    }

    public AnchorPane getEmptyAnchorPane() {
        return emptyAnchorPane;
    }

    @Override
    public void showObject(Enemy enemy) {
    }

    @Override
    public void showObject(Chest chest) {
    }

    @Override
    public void showObject(Terrain terrain) {
    }

    @Override
    public void showObject(Player player) {
    }

}
