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
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
 * player. His icon, name, level, gold balance, health points, energy,
 * experience, 3 attack buttons, inventory and equip button.
 *
 * 3 Attack buttons can be used to attack currently selected enemy. Attack
 * button "1" costs no energy, but also deals low damage. Attack button "2"
 * costs 20 energy and deals medium damage. Attack button "3" costs 30 energy
 * and deals high damage.
 *
 * Inventory is an item placeholder for up to 12 items. Items can be added there
 * and also removed.
 *
 * Equip buttons equips the currently selected item on left click (if no item of
 * the same type is equipped) and opens the equip window on right click.
 *
 * @author Miroslav Falcmann
 */
public class PlayerPanel implements ShowSelected {

    private final FXMLLoader playerPanelLoader;
    private final AnchorPane ap;
    private Player player;
    private final HBox hBox;
    private GameWindow gWindow;
    private final GridPane container;
    private final ProgressBar hpBar;
    private final ProgressBar energyBar;
    private final ProgressBar xpBar;
    private final Button attack1But;
    private final Button attack2But;
    private final Button attack3But;
    private final Button equipBut;
    private final Circle classIcon;
    private final Label nameLabel;
    private final Label goldLabel;
    private final Rectangle[] rectangleArr;
    private final Rectangle[] rectangleArrEquip;
    private final EquipWindow equipWindow;
    private final Label lvlLabel;
    private final Label hpLabel;
    private final Label energyLabel;
    private final Label xpLabel;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public PlayerPanel(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        playerPanelLoader = new FXMLLoader(getClass().getResource("player_panel2.fxml"));
        ap = playerPanelLoader.load();

        container = (GridPane) ap.getChildren().get(0);
        hpBar = (ProgressBar) ap.getChildren().get(1);
        energyBar = (ProgressBar) ap.getChildren().get(2);
        xpBar = (ProgressBar) ap.getChildren().get(3);

        attack1But = (Button) ap.getChildren().get(4);
        attack2But = (Button) ap.getChildren().get(5);
        attack3But = (Button) ap.getChildren().get(6);
        equipBut = (Button) ap.getChildren().get(7);
        classIcon = (Circle) ap.getChildren().get(8);
        nameLabel = (Label) ap.getChildren().get(9);
        goldLabel = (Label) ap.getChildren().get(10);
        hpLabel = (Label) ap.getChildren().get(11);
        energyLabel = (Label) ap.getChildren().get(12);
        xpLabel = (Label) ap.getChildren().get(13);
        lvlLabel = (Label) ap.getChildren().get(14);

        rectangleArr = new Rectangle[12];
        rectangleArrEquip = new Rectangle[6];

        equipWindow = new EquipWindow();
        rectangleArrInit();
        addEvents();

    }

    /**
     * Updates view of Health bar, Energy bar and XP bar according to the
     * current values in model.
     */
    private void progressBarsSetProgress() {
        hpBar.setProgress(((double) player.getHp()) / ((double) player.getMaxHp()));
        energyBar.setProgress(((double) player.getEnergy()) / ((double) player.getMaxEnergy()));
        xpBar.setProgress(((double) player.getXp()) / ((double) player.getMaxXp()));
    }

    /**
     * Initializes rectangles from FXML file.
     */
    private void rectangleArrInit() {
        for (int i = 0; i < rectangleArr.length; i++) {
            rectangleArr[i] = (Rectangle) container.getChildren().get(i);
        }
        for (int i = 0; i < 3; i++) {
            rectangleArrEquip[i] = (Rectangle) equipWindow.getItemSlots1().getChildren().get(i);
            rectangleArrEquip[i + 3] = (Rectangle) equipWindow.getItemSlots2().getChildren().get(i);
        }
    }

    /**
     * Updates the label showing players gold balance.
     */
    public void updateGoldLabel() {
        goldLabel.setText("Gold: " + player.getGold());
    }

    /**
     * Updates the view of each component in Player panel.
     */
    public void updateView() {
        player.updateStatsFromEquip();
        classIcon.setFill(player.getImgPat());
        nameLabel.setText(player.getName());
        goldLabel.setText("Gold:    " + player.getGold());
        hpLabel.setText("HP    " + player.getHp() + " / " + player.getMaxHp());
        energyLabel.setText("Energy    " + player.getEnergy() + " / " + player.getMaxEnergy());
        xpLabel.setText("XP    " + player.getXp() + " / " + player.getMaxXp());
        lvlLabel.setText("Lvl. " + player.getLvl());
        int i = 0;
        for (Rectangle rect : rectangleArr) {
            updateDropContainerView(rect, i);
            i++;
        }
        progressBarsSetProgress();
        LOGGER.info("PlayerPanel view updated");
    }

    /**
     * Adds events to players inventory placeholder, equip placeholder and
     * buttons within the player panel.
     */
    private void addEvents() {
        int i = 0;
        for (Rectangle rect : rectangleArr) {
            addRectangleEvent(rect, i);
            i++;
        }
        i = 0;
        for (Rectangle rect : rectangleArrEquip) {
            addRectangleEquipEvent(rect, i);
            i++;
        }
        addButtonEvents();
    }

    /**
     * Adds mouse left click event to players equip placeholder
     *
     * @param rectangle Rectangle representing the view of single equip item
     * placeholder
     * @param itemIndex Index at which the equip item is stored in the equip
     * array.
     */
    private void addRectangleEquipEvent(Rectangle rectangle, int itemIndex) {
        EventHandler<MouseEvent> addAndSelectItem = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.PRIMARY) {
                selectEquipItem(rectangle, itemIndex);
            }
        };
        rectangle.addEventFilter(MouseEvent.MOUSE_CLICKED, addAndSelectItem);
    }

    /**
     * Adds mouse click events to "equip" button and all 3 "attack" buttons.
     */
    private void addButtonEvents() {
        EventHandler<MouseEvent> addAndSelectItem = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            if (button == MouseButton.PRIMARY) {
                addEquipItem();
                updateView();
            } else if (button == MouseButton.SECONDARY) {
                equipWindow.openEquipWindow(player);
            }
        };
        equipBut.addEventFilter(MouseEvent.MOUSE_CLICKED, addAndSelectItem);

        attack1But.setOnAction(e -> attack(1.0, 0));
        attack2But.setOnAction(e -> attack(1.5, 20));
        attack3But.setOnAction(e -> attack(2.0, 40));
    }

    /**
     * Invokes player attack to the enemy according to players damage and enemy
     * resistance. After that, model and view are updated.
     *
     * @param attackPower double constant by which is the attack damage
     * multiplied.
     * @param energyCost int constant representing how much energy is drawn from
     * player after this attack.
     */
    public void attack(double attackPower, int energyCost) {
        if (gWindow.getShowedObject() instanceof Enemy) {
            Enemy enemy = (Enemy) gWindow.getShowedObject();
            enemy.dealMeDmg(player.attack(attackPower, energyCost) * (gWindow.getRolledNumber() * 0.1));
            gWindow.getBottomP().getEnemyPanel().updateView();
            updateView();
        }
    }

    /**
     * Selects equip item from players equip
     *
     * @param rectangle Rectangle showing the view of equip item placeholder
     * @param itemIndex Index of the equip item in Equip array (players equip)
     */
    private void selectEquipItem(Rectangle rectangle, int itemIndex) {
        BaseObject baseObj = ((Player) gWindow.getSelectedPlayer()).getEquip()[itemIndex];
        if (baseObj != null) {
            gWindow.setSelectedEquipIndex(itemIndex);
            baseObj.setRectangle(rectangle);
            gWindow.setSelectedFromEquip(true);
            gWindow.setSelectedFromInventory(false);

            // showing selected equip in item panel
            baseObj.registerToListener(gWindow.getRightP().getSelectedItemPanelGame());
            if (gWindow.getSelectedObject() == null) {
                baseObj.selectObject(gWindow);
            } else {
                baseObj.reselectObject(gWindow);
            }
        }
    }

    /**
     * Adds mouse click event to rectangle representing the inventory item
     * placeholder. On left click, item is selected. On right click, selected
     * item or item from ListView in right panel is added.
     *
     * @param rectangle Rectangle showing the view of inventory item placeholder
     * @param itemIndex Index of the item in Item array (players inventory)
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
     * Selects the item from inventory
     *
     * @param rectangle Rectangle showing the view of inventory item placeholder
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void selectInventoryItem(Rectangle rectangle, int itemIndex) {
        BaseObject baseObj = ((Player) gWindow.getSelectedPlayer()).getContainer()[itemIndex];
        if (baseObj != null) {
            gWindow.setSelectedItemIndex(itemIndex);
            gWindow.setSelectedFromInventory(true);
            gWindow.setSelectedFromEquip(false);
            baseObj.setRectangle(rectangle);

            // showing selected item in item panel
            updateShowedItemPanel(baseObj, gWindow.getRightP().getSelectedItemPanelGame());

            if (gWindow.getSelectedObject() == null) {
                baseObj.selectObject(gWindow);
            } else {
                baseObj.reselectObject(gWindow);
            }
            LOGGER.fine("Inventory item selected.");
        }
    }

    /**
     * Changes picture and item description in the selectedItemPanel according
     * to the currently selected item.
     *
     * @param baseObj currently selected object
     * @param panel AnchorPane showing description of the currently selected
     * item
     */
    private void updateShowedItemPanel(BaseObject baseObj, SelectedItemPanelGame panel) {
        panel.setInventoryItem(true);
        baseObj.registerToListener(panel);
        panel.addEvents(player, gWindow);
        panel.setInventoryItem(false);
    }

    /**
     * Adds currently selected Equip object to players equip
     */
    private void addEquipItem() {
        BaseObject obj = gWindow.getSelectedObject();
        if (obj != null && obj instanceof Equip) {
            Rectangle rectangle = obj.getRectangle();
            int ret = equipIndex((Equip) obj, player);
            if (ret != -1) {
                player.getEquip()[ret] = (Equip) obj;
                gWindow.setSelectedEquipIndex(ret);
                takeItemFromContainer(rectangle, ret);
                gWindow.setSelectedObject(null);
                gWindow.getRightP().getvBox().getChildren().set(4, gWindow.getRightP().getSelectedItemPanelGame().getEmptyAnchorPane());
            }
        }
    }

    /**
     * Decides at which index in players equip array will be the given Equip
     * object stored (according to the item tag). Also updates view of certain
     * equip slot.
     *
     * @param eq Equip object
     * @param player Player whose equip will be updated
     * @return int index of equip item in players equip array
     */
    private int equipIndex(Equip eq, Player player) {
        int ret = -1;
        switch (eq.getTag()) {
            case HEAD:
                if (player.getEquip()[0] == null) {
                    equipWindow.getHeadSlot().setFill(eq.getImgPat());
                    ret = 0;
                }
                break;
            case CHEST:
                if (player.getEquip()[1] == null) {
                    equipWindow.getChestSlot().setFill(eq.getImgPat());
                    ret = 1;
                }
                break;
            case LEGS:
                if (player.getEquip()[2] == null) {
                    equipWindow.getLeggingsSlot().setFill(eq.getImgPat());
                    ret = 2;
                }
                break;
            case FEET:
                if (player.getEquip()[3] == null) {
                    equipWindow.getBootsSlot().setFill(eq.getImgPat());
                    ret = 3;
                }
                break;
            case HAND:
                if (player.getEquip()[4] == null) {
                    equipWindow.getHandsSlot().setFill(eq.getImgPat());
                    ret = 4;
                }
                break;
            case WEAPON:
                if (player.getEquip()[5] == null) {
                    equipWindow.getWeaponSlot().setFill(eq.getImgPat());
                    ret = 5;
                }
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * Adds item to inventory. Either the selected item from enemy/chest drop
     * container or from ListView in right panel.
     *
     * @param rectangle Rectangle showing the view of inventory item placeholder
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void addInventoryItem(Rectangle rectangle, int itemIndex) {
        BaseObject obj = gWindow.getSelectedObject();
        if ((obj == null || obj instanceof Enemy || obj instanceof Terrain) && gWindow.getRightP().getObjectsList().getSelectionModel().getSelectedItem() != null) {
            addInventoryItemFromList(rectangle, itemIndex);
            updateView();
        } else if (obj != null && obj instanceof Item && (player.getContainer()[itemIndex] == null)) {
            player.getContainer()[itemIndex] = (Item) obj;

            takeItemFromContainer(rectangle, itemIndex);
            rectangle.setFill(obj.getImgPat());
        }
    }

    /**
     * Removes the selected item from container and puts it elsewhere (in item
     * placeholder where has been right-clicked).
     *
     * @param rectangle Rectangle showing the view of inventory item placeholder
     * @param itemIndex Index of the item in Item array
     */
    private void takeItemFromContainer(Rectangle rectangle, int itemIndex) {
        if (gWindow.isSelectedFromEquip()) {
            player.getEquip()[gWindow.getSelectedEquipIndex()] = null;
            selectInventoryItem(rectangle, itemIndex);
            equipWindow.updateView(player.getEquip());
        } else if (gWindow.isSelectedFromInventory()) {
            player.getContainer()[gWindow.getSelectedItemIndex()] = null;
            selectInventoryItem(rectangle, itemIndex);
            updateView();
        } else {
            if (gWindow.getShowedObject() instanceof Enemy) {
                ((Enemy) (gWindow.getShowedObject())).getContainer()[gWindow.getSelectedItemIndex()] = null;
                selectInventoryItem(rectangle, itemIndex);
                gWindow.getBottomP().getEnemyPanel().updateView();
            } else if (gWindow.getShowedObject() instanceof Chest) {
                ((Chest) (gWindow.getShowedObject())).getContainer()[gWindow.getSelectedItemIndex()] = null;
                selectInventoryItem(rectangle, itemIndex);
                gWindow.getBottomP().getChestPanel().updateView();
            }
        }
        LOGGER.info("Item from container taken and added to inventory");
    }

    /**
     * Adds the selected Item in ListView in right panel to players inventory
     * (to certain placeholder which has been right-clicked).
     *
     * @param rectangle Rectangle showing the view of inventory item placeholder
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void addInventoryItemFromList(Rectangle rectangle, int itemIndex) {
        Object obj = gWindow.getRightP().getObjectsList().getSelectionModel().getSelectedItem();
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
            LOGGER.info("New inventory item added.");
        }
    }

    /**
     * Adds item of instance "HealthPotion" to players inventory.
     *
     * @param baseObj selected object of instance "HealthPotion"
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void addHealthPotion(BaseObject baseObj, int itemIndex) {
        player.getContainer()[itemIndex] = (HealthPotion) baseObj;
    }

    /**
     * Adds item of instance "EnergyPotion" to players inventory.
     *
     * @param baseObj selected object of instance "EnergyPotion"
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void addEnergyPotion(BaseObject baseObj, int itemIndex) {
        player.getContainer()[itemIndex] = (EnergyPotion) baseObj;
    }

    /**
     * Adds item of instance "Food" to players inventory.
     *
     * @param baseObj selected object of instance "Food"
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void addFood(BaseObject baseObj, int itemIndex) {
        player.getContainer()[itemIndex] = (Food) baseObj;
    }

    /**
     * Adds item of instance "Equip" to players inventory.
     *
     * @param baseObj selected object of instance "Equip"
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void addEquip(BaseObject baseObj, int itemIndex) {
        player.getContainer()[itemIndex] = (Equip) baseObj;
    }

    /**
     * Adds item of instance "Item" to players inventory.
     *
     * @param baseObj selected object of instance "Item"
     * @param itemIndex Index of the item in Item array (players inventory)
     */
    private void addItem(BaseObject baseObj, int itemIndex) {
        player.getContainer()[itemIndex] = (Item) baseObj;
    }

    /**
     * Updates the view of item placeholder. If the item is stored, fills the
     * rectangle view with item image. Otherwise fills the rectangle with black
     * color (representing empty slot)
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param index Index of the item in Item array (players inventory)
     */
    private void updateDropContainerView(Rectangle rectangle, int index) {
        if (player.getContainer()[index] == null) {
            rectangle.setFill(Color.BLACK);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(0.75);
        } else {
            rectangle.setFill(player.getContainer()[index].getImgPat());
        }
    }

    public AnchorPane getAnchorPane() {
        return ap;
    }

    public void setgWindow(GameWindow gWindow) {
        this.gWindow = gWindow;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void showObject(Enemy enemy) {
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
        this.player = player;
        gWindow.getRightP().addAddButtonEvent(player);
        updateView();
        hBox.getChildren().set(1, ap);
    }
}
