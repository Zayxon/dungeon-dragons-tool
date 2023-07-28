package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * AnchorPane showing all the important information about currently selected
 * chest. Its icon, name and drop container for storing up to 3 items.
 *
 * Selected item within the drop container can be added to players inventory.
 *
 * @author Miroslav Falcmann
 */
public class ChestPanel implements ShowSelected {

    private final AnchorPane ap;
    private final Circle icon;
    private final Label name;
    private final GridPane gridPane;
    private Chest chest;
    private final HBox hBox;
    private final Rectangle rectangle1;
    private final Rectangle rectangle2;
    private final Rectangle rectangle3;
    private final Label description;
    private GameWindow gWindow;

    public ChestPanel(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chest_panel.fxml"));
        ap = loader.load();
        icon = (Circle) ap.getChildren().get(0);
        name = (Label) ap.getChildren().get(1);
        description = (Label) ap.getChildren().get(2);
        gridPane = (GridPane) ap.getChildren().get(3);
        rectangle1 = (Rectangle) gridPane.getChildren().get(0);
        rectangle2 = (Rectangle) gridPane.getChildren().get(1);
        rectangle3 = (Rectangle) gridPane.getChildren().get(2);
        addEvents();
    }

    /**
     * Adding click events to 3 Rectangles representing item placeholders
     */
    private void addEvents() {
        addRectangleEvent(rectangle1, 0);
        addRectangleEvent(rectangle2, 1);
        addRectangleEvent(rectangle3, 2);
    }

    /**
     * Add rectangle event on mouse left click
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void addRectangleEvent(Rectangle rectangle, int itemIndex) {
        EventHandler<MouseEvent> addAndSelectItem = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.PRIMARY) {
                selectInventoryItem(rectangle, itemIndex);
            }
        };
        rectangle.addEventFilter(MouseEvent.MOUSE_CLICKED, addAndSelectItem);
    }

    /**
     * Select item from inventory
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void selectInventoryItem(Rectangle rectangle, int itemIndex) {
        BaseObject baseObj = null;
        if (gWindow.getShowedObject() instanceof Chest) {
            baseObj = ((Chest) gWindow.getShowedObject()).getContainer()[itemIndex];
            gWindow.setSelectedItemIndex(itemIndex);
            gWindow.setSelectedFromInventory(false);
            gWindow.setSelectedFromEquip(false);
        }
        if (baseObj != null) {
            baseObj.setRectangle(rectangle);
            baseObj.registerToListener(gWindow.getRightP().getSelectedItemPanelGame());
            if (gWindow.getSelectedObject() == null) {
                baseObj.selectObject(gWindow);
            } else {
                baseObj.reselectObject(gWindow);
            }
        }
    }

    /**
     * Updates the view of item placeholder. If the item is stored, fills the
     * rectangle view with item image. Otherwise fills the rectangle with black
     * color (representing empty slot)
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void updateDropContainerView(Rectangle rectangle, int index) {
        if (chest.getContainer()[index] == null) {
            rectangle.setFill(Color.BLACK);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(0.75);
        } else {
            rectangle.setFill(chest.getContainer()[index].getImgPat());
        }
    }

    /**
     * updates the view of all 3 rectangles, each representing an item
     * placeholder
     */
    public void updateView() {
        updateDropContainerView(rectangle1, 0);
        updateDropContainerView(rectangle2, 1);
        updateDropContainerView(rectangle3, 2);
    }

    @Override
    public void showObject(Enemy enemy) {
    }

    @Override
    public void showObject(Chest chest) {
        gWindow.setShowedObject(chest);
        this.chest = chest;
        icon.setFill(chest.getImgPat());
        name.setText(chest.getName());
        updateView();
        hBox.getChildren().set(2, ap);
    }

    @Override
    public void showObject(Item item) {
    }

    @Override
    public void showObject(Terrain terrain) {
    }

    @Override
    public void showObject(Player player) {
    }

    public AnchorPane getAnchorPane() {
        return ap;
    }

    public void setgWindow(GameWindow gWindow) {
        this.gWindow = gWindow;
    }
}
