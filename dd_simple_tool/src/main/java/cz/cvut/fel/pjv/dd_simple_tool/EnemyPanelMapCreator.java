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
import javafx.scene.control.Button;
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
 * enemy. Its icon, name, level, health points, resistance, damage, 2 buttons to
 * adjust the selected enemy level and drop container for storing up to 3 items.
 *
 * Button "Level up" for increasing enemy level.
 *
 * Button "Level down" for decreasing enemy level.
 *
 * Selected item from ListView in right panel is added to the drop container by
 * right click into empty slot.
 *
 * @author Miroslav Falcmann
 */
public class EnemyPanelMapCreator implements ShowSelected {

    private final HBox hBox;
    private final FXMLLoader enemyPanelLoader;
    private final AnchorPane anchorPane;
    private final Circle icon;
    private final Label name;
    private final Label hp;
    private final Label lvl;
    private final Button lvlUp;
    private final Button lvlDown;
    private final Label dmg;
    private final Label resist;
    private final Rectangle rectangle1;
    private final Rectangle rectangle2;
    private final Rectangle rectangle3;
    private final GridPane itemDrop;
    private Enemy enemy;
    private MapCreatorWindow mcWindow;
    private ListView objectsList;

    public EnemyPanelMapCreator(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        enemyPanelLoader = new FXMLLoader(getClass().getResource("enemy_panel_map_creator.fxml"));
        anchorPane = enemyPanelLoader.load();

        // enemy panel map creator
        icon = (Circle) anchorPane.getChildren().get(0);
        name = (Label) anchorPane.getChildren().get(1);
        lvl = (Label) anchorPane.getChildren().get(2);
        // drop label
        itemDrop = (GridPane) anchorPane.getChildren().get(3);
        rectangle1 = (Rectangle) itemDrop.getChildren().get(0);
        rectangle2 = (Rectangle) itemDrop.getChildren().get(1);
        rectangle3 = (Rectangle) itemDrop.getChildren().get(2);
        lvlUp = (Button) anchorPane.getChildren().get(5);
        lvlDown = (Button) anchorPane.getChildren().get(6);
        hp = (Label) anchorPane.getChildren().get(7);
        resist = (Label) anchorPane.getChildren().get(8);
        dmg = (Label) anchorPane.getChildren().get(9);
        addEvents();
    }

    /**
     * Add mouse click events to "Level Up" button and "Level Down" button.
     */
    private void addLvlButtonsEvents() {
        EventHandler<MouseEvent> increaseLevel = (MouseEvent e) -> {
            enemy.setLvl(enemy.getLvl() + 1);
            updateView();
        };
        lvlUp.addEventFilter(MouseEvent.MOUSE_CLICKED, increaseLevel);

        EventHandler<MouseEvent> decreaseLevel = (MouseEvent e) -> {
            enemy.setLvl(enemy.getLvl() - 1);
            updateView();
        };
        lvlDown.addEventFilter(MouseEvent.MOUSE_CLICKED, decreaseLevel);
    }

    /**
     * Add mouse left click and right click event to the rectangle
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (enemy drop container)
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
     * Select the item (showed in rectangle) in enemy drop container
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (enemy drop container)
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
     * Add currently selected Item to Item array (enemy drop container)
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (enemy drop container)
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
     * Add HealthPotion object to Item array (enemy drop container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (enemy drop container)
     */
    private void addHealthPotion(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (HealthPotion) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (HealthPotion) baseObj;
        }
    }

    /**
     * Add EnergyPotion object to Item array (enemy drop container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (enemy drop container)
     */
    private void addEnergyPotion(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (EnergyPotion) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (EnergyPotion) baseObj;
        }
    }

    /**
     * Add Food object to Item array (enemy drop container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (enemy drop container)
     */
    private void addFood(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Food) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Food) baseObj;
        }
    }

    /**
     * Add Equip object to Item array (enemy drop container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (enemy drop container)
     */
    private void addEquip(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Equip) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Equip) baseObj;
        }
    }

    /**
     * Add Item object to Item array (enemy drop container)
     *
     * @param baseObj Currently selected item to add
     * @param itemIndex Index of the item in Item array (enemy drop container)
     */
    private void addItem(BaseObject baseObj, int itemIndex) {
        if (mcWindow.getShowedObject() instanceof Enemy) {
            ((Enemy) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Item) baseObj;
        } else if (mcWindow.getShowedObject() instanceof Chest) {
            ((Chest) mcWindow.getShowedObject()).getContainer()[itemIndex] = (Item) baseObj;
        }
    }

    /**
     * Add mouse left click events to 3 rectangles, each representing an item
     * placeholder and to "Level Up" and "Level Down" buttons.
     */
    private void addEvents() {
        addLvlButtonsEvents();
        addRectangleEvent(rectangle1, 0);
        addRectangleEvent(rectangle2, 1);
        addRectangleEvent(rectangle3, 2);
    }

    /**
     * Updates the view of item placeholder. If the item is stored, fills the
     * rectangle view with item image. Otherwise fills the rectangle with black
     * color (representing empty slot)
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (enemy drop container)
     */
    private void updateDropContainerView(Rectangle rectangle, int index) {
        if (enemy.getContainer()[index] == null) {
            rectangle.setFill(Color.BLACK);
        } else {
            rectangle.setFill(enemy.getContainer()[index].getImgPat());
        }
    }

    /**
     * Update the view of each component in Enemy Panel according to the current
     * model values
     */
    private void updateView() {
        updateDropContainerView(rectangle1, 0);
        updateDropContainerView(rectangle2, 1);
        updateDropContainerView(rectangle3, 2);
        lvl.setText("Lvl. " + enemy.getLvl());
        hp.setText("HP: " + enemy.getHp());
        resist.setText("Resist: " + enemy.getResist());
        dmg.setText("Attack damage: " + enemy.getDmg());
    }

    public ListView getObjectsList() {
        return objectsList;
    }

    public void setObjectsList(ListView objectsList) {
        this.objectsList = objectsList;
    }

    public void setMcWindow(MapCreatorWindow mcWindow) {
        this.mcWindow = mcWindow;
    }

    @Override
    public void showObject(Enemy enemy) {
        mcWindow.setShowedObject(enemy);
        this.enemy = enemy;
        icon.setFill(enemy.getImgPat());
        name.setText(enemy.getName());
        updateView();

        hBox.getChildren().set(0, anchorPane);
    }

    @Override
    public void showObject(Chest chest) {
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
