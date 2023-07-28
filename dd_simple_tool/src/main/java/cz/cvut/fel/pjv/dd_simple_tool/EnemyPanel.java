package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.io.IOException;
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
 * enemy. Its icon, name, level, health points, resistance, damage, attack
 * button to attack currently selected player and drop container for storing up
 * to 3 items.
 *
 * Button "Attack" deals damage to the currently selected player according to
 * players resistance and enemy damage.
 *
 * Selected item within the drop container can be added to players inventory.
 *
 * @author Miroslav Falcmann
 */
public class EnemyPanel implements ShowSelected {

    private final Circle icon;
    private final AnchorPane ap;
    private final HBox hBox;
    private final FXMLLoader enemyPanelLoader;
    private GameWindow gWindow;
    private Enemy enemy;

    private final Label name;
    private final ProgressBar hpBar;
    private final Button attackBut;
    private final GridPane itemDrop;

    private final Rectangle rectangle1;
    private final Rectangle rectangle2;
    private final Rectangle rectangle3;

    private final Label dmgLabel;
    private final Label resistLabel;
    private final Label lvlLabel;
    private final Label hpLabel;

    public EnemyPanel(HBox hBox) throws IOException {
        this.hBox = hBox;

        // initializing objects from FXML file
        enemyPanelLoader = new FXMLLoader(getClass().getResource("enemy_panel.fxml"));
        ap = enemyPanelLoader.load();

        icon = (Circle) ap.getChildren().get(0);
        name = (Label) ap.getChildren().get(1);
        lvlLabel = (Label) ap.getChildren().get(2);
        hpBar = (ProgressBar) ap.getChildren().get(3);
        attackBut = (Button) ap.getChildren().get(4);
        itemDrop = (GridPane) ap.getChildren().get(5);
        hpLabel = (Label) ap.getChildren().get(6);
        dmgLabel = (Label) ap.getChildren().get(7);
        resistLabel = (Label) ap.getChildren().get(8);

        rectangle1 = (Rectangle) itemDrop.getChildren().get(0);
        rectangle2 = (Rectangle) itemDrop.getChildren().get(1);
        rectangle3 = (Rectangle) itemDrop.getChildren().get(2);

        addEvents();
    }

    /**
     * Add mouse click "attack" event to the "attack" button.
     */
    private void addAttackEvent() {
        attackBut.setOnAction(e -> attack());
    }

    /**
     * Deal damage to the player according to the enemy and player stats.
     */
    public void attack() {
        if (gWindow.getSelectedPlayer() != null) {
            Player player = (Player) gWindow.getSelectedPlayer();
            double dealtDmg = enemy.attack() * (gWindow.getRolledNumber() * 0.1);
            player.dealMeDmg(dealtDmg);
            gWindow.getBottomP().getPlayerPanel().updateView();
        }
    }

    /**
     * Add mouse left click event to the rectangle
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (enemy drop container)
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
     * Select the item (showed in rectangle) in enemy drop container
     *
     * @param rectangle Rectangle showing the view of item placeholder
     * @param itemIndex Index of the item in Item array (enemy drop container)
     */
    private void selectInventoryItem(Rectangle rectangle, int itemIndex) {
        BaseObject baseObj = null;
        if (gWindow.getShowedObject() instanceof Enemy) {
            baseObj = ((Enemy) gWindow.getShowedObject()).getContainer()[itemIndex];
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
     * Add mouse left click events to 3 rectangles, each representing an item
     * placeholder.
     */
    private void addEvents() {
        addAttackEvent();
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
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(0.75);
        } else {
            rectangle.setFill(enemy.getContainer()[index].getImgPat());
        }
    }

    /**
     * Update the view of each component in Enemy Panel according to the current
     * model values
     */
    public void updateView() {
        updateDropContainerView(rectangle1, 0);
        updateDropContainerView(rectangle2, 1);
        updateDropContainerView(rectangle3, 2);
        hpBar.setProgress(((double) enemy.getHp()) / ((double) enemy.getMaxHp()));
        hpLabel.setText("HP:    " + enemy.getHp() + " / " + enemy.getMaxHp());
        dmgLabel.setText("Damage:    " + enemy.getDmg());
        resistLabel.setText("Resist:    " + enemy.getResist());
        lvlLabel.setText("Lvl. " + enemy.getLvl());
        System.out.println();
    }

    public void setgWindow(GameWindow gWindow) {
        this.gWindow = gWindow;
    }

    public AnchorPane getAnchorPane() {
        return ap;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void showObject(Enemy enemy) {
        gWindow.setShowedObject(enemy);
        this.enemy = enemy;
        icon.setFill(enemy.getImgPat());
        name.setText(enemy.getName());
        updateView();

        hBox.getChildren().set(2, ap);
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
