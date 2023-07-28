package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.EnergyPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Equip;
import cz.cvut.fel.pjv.dd_simple_tool.model.Food;
import cz.cvut.fel.pjv.dd_simple_tool.model.HealthPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
 * enemy. Its icon, name and drop container for storing up to 3 items.
 *
 * Selected item from ListView in right panel is added to the drop container by
 * right click into empty slot.
 *
 * @author Miroslav Falcmann
 */
public class ChestPanelMapCreator implements ShowSelected {

    private final FXMLLoader chestPanelLoader;
    private final AnchorPane anchorPane;
    private final Circle icon;
    private final Label name;
    private GridPane gridPane;
    private Label dropLabel;
    private Chest chest;
    private HBox hBox;
    private Rectangle rectangle1;
    private Rectangle rectangle2;
    private Rectangle rectangle3;
    private ListView objectsList;
    private MapCreatorWindow mcWindow;

    public ChestPanelMapCreator(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        chestPanelLoader = new FXMLLoader(getClass().getResource("chest_panel_map_creator.fxml"));
        anchorPane = chestPanelLoader.load();

        // chestPanel objects
        icon = (Circle) anchorPane.getChildren().get(0);
        name = (Label) anchorPane.getChildren().get(1);
        gridPane = (GridPane) anchorPane.getChildren().get(2);
        rectangle1 = (Rectangle) gridPane.getChildren().get(0);
        rectangle2 = (Rectangle) gridPane.getChildren().get(1);
        rectangle3 = (Rectangle) gridPane.getChildren().get(2);
        dropLabel = (Label) anchorPane.getChildren().get(3);
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
            } else if (button == MouseButton.SECONDARY) {
                addInventoryItem(rectangle, itemIndex);
            }
        };
        rectangle.addEventFilter(MouseEvent.MOUSE_CLICKED, addAndSelectItem);
    }

    /**
     * Select item from Item array (chest container)
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void selectInventoryItem(Rectangle rectangle, int itemIndex) {
        BaseObject baseObj = null;
        if (mcWindow.getShowedObject() instanceof Enemy) {
            baseObj = ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex];
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            baseObj = ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex];
        }
        if (baseObj != null) {
            baseObj.setRectangle(rectangle);
            if (mcWindow.getSelectedObject() == null) {
                baseObj.selectObject(mcWindow);
            } else {
                baseObj.reselectObject(mcWindow);
            }
        }

    }

    /**
     * Add currently selected Item to Item array (chest container)
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void addInventoryItem(Rectangle rectangle, int itemIndex) {
        Object obj = objectsList.getSelectionModel().getSelectedItem();
        if (obj != null && obj instanceof Item) {
            BaseObject baseObj = ((BaseObject) obj).createNew();
            if (baseObj instanceof Equip) {
                addEquip(baseObj, itemIndex);
            } else if (baseObj instanceof HealthPotion) {
                addHealthPotion(baseObj, itemIndex);
            } else if (baseObj instanceof EnergyPotion) {
                addEnergyPotion(baseObj, itemIndex);
            } else if (baseObj instanceof Food) {
                addFood(baseObj, itemIndex);
            } else {
                addItem(baseObj, itemIndex);
            }
            rectangle.setFill(baseObj.getImgPat());
        }
    }

    /**
     * Add HealthPotion object to Item array (chest container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void addHealthPotion(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (HealthPotion) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (HealthPotion) baseObj;
        }
    }

    /**
     * Add EnergyPotion object to Item array (chest container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void addEnergyPotion(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (EnergyPotion) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (EnergyPotion) baseObj;
        }
    }

    /**
     * Add Food object to Item array (chest container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void addFood(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Food) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Food) baseObj;
        }
    }

    /**
     * Add Equip object to Item array (chest container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void addEquip(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Equip) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Equip) baseObj;
        }
    }

    /**
     * Add Item object to Item array (chest container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (chest container)
     */
    private void addItem(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Item) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Item) baseObj;
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
        } else {
            rectangle.setFill(chest.getContainer()[index].getImgPat());
        }
    }

    /**
     * updates the view of all 3 rectangles, each representing an item
     * placeholder
     */
    private void updateView() {
        updateDropContainerView(rectangle1, 0);
        updateDropContainerView(rectangle2, 1);
        updateDropContainerView(rectangle3, 2);
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setMcWindow(MapCreatorWindow mcWindow) {
        this.mcWindow = mcWindow;
    }

    public void setObjectsList(ListView objectsList) {
        this.objectsList = objectsList;
    }

    @Override
    public void showObject(Enemy enemy) {
    }

    @Override
    public void showObject(Chest chest) {
        mcWindow.setShowedObject(chest);
        this.chest = chest;
        icon.setFill(chest.getImgPat());
        name.setText(chest.getName());
        updateView();
        hBox.getChildren().set(0, anchorPane);
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

}
