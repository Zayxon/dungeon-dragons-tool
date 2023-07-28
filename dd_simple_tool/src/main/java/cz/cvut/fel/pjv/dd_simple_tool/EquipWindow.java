package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Equip;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * AnchorPane showing the equip of currently selected player. Also shows player
 * class icon, name and stats. Equip items can be taken off and stored back in
 * the players inventory.
 *
 * @author Miroslav Falcmann
 */
public class EquipWindow {

    private boolean hasBeenOpened = false;
    private final FXMLLoader equipWindowLoader;
    private final AnchorPane ap;
    private final Circle classIcon;
    private final Label nameLabel;
    private final Label stats1Label;
    private final Label stats2Label;
    private final GridPane itemSlots1;
    private final GridPane itemSlots2;
    private final Rectangle headSlot;
    private final Rectangle chestSlot;
    private final Rectangle leggingsSlot;
    private final Rectangle bootsSlot;
    private final Rectangle handsSlot;
    private final Rectangle weaponSlot;

    private Scene scene;
    private final Stage stage;

    public EquipWindow() throws IOException {

        // initializing objects from FXML file
        equipWindowLoader = new FXMLLoader(getClass().getResource("equip_window.fxml"));
        ap = equipWindowLoader.load();

        classIcon = (Circle) ap.getChildren().get(0);
        nameLabel = (Label) ap.getChildren().get(1);
        stats1Label = (Label) ap.getChildren().get(2);
        stats2Label = (Label) ap.getChildren().get(3);
        itemSlots1 = (GridPane) ap.getChildren().get(4);
        itemSlots2 = (GridPane) ap.getChildren().get(5);

        headSlot = (Rectangle) itemSlots1.getChildren().get(0);
        chestSlot = (Rectangle) itemSlots1.getChildren().get(1);
        leggingsSlot = (Rectangle) itemSlots1.getChildren().get(2);

        bootsSlot = (Rectangle) itemSlots2.getChildren().get(0);
        handsSlot = (Rectangle) itemSlots2.getChildren().get(1);
        weaponSlot = (Rectangle) itemSlots2.getChildren().get(2);

        stage = new Stage();
    }

    /**
     * Initialize the stage
     */
    private void stageInit() {
        ap.setStyle("-fx-background-color: #44484A;");
        scene = new Scene(ap);
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Updates the view of Equip window according to the players current stats
     * and equip. Then opens the window showing the players stats and equip
     *
     * @param player Currently selected player
     */
    public void openEquipWindow(Player player) {
        if (!hasBeenOpened) {
            stageInit();
            hasBeenOpened = true;
        }
        updateView(player.getEquip());
        nameLabel.setText(player.getName());
        classIcon.setFill(player.getImgPat());
        stats1Label.setText("Level: " + player.getLvl() + "\nXP: " + player.getXp()
                + "\nEnergy: " + player.getMaxEnergy());
        stats2Label.setText("HP: " + player.getMaxHp() + "\nDamage: " + (player.getBaseDmg() + player.getDmg())
                + "\nResist: " + player.getResist());
        stage.show();
    }

    /**
     * Update the view of players equip slots according to the equip the player
     * is currently wearing
     *
     * @param eq Equip array storing players equip
     */
    public void updateView(Equip[] eq) {
        resetSlot(eq, headSlot, 0);
        resetSlot(eq, chestSlot, 1);
        resetSlot(eq, leggingsSlot, 2);
        resetSlot(eq, bootsSlot, 3);
        resetSlot(eq, handsSlot, 4);
        resetSlot(eq, weaponSlot, 5);
    }

    /**
     * Update the view of a single equip slot according to the players currently
     * equipped items
     *
     * @param eq Equip array storing players equip
     * @param slot Rectangle representing equip placeholder
     * @param index Index of Equip object in the Equip array
     */
    private void resetSlot(Equip[] eq, Rectangle slot, int index) {
        if (eq[index] == null) {
            emptySlot(slot);
        } else {
            slot.setFill(eq[index].getImgPat());
        }
    }

    /**
     * Fill the rectangle with black color to make it look "empty"
     *
     * @param slot Rectangle representing equip placeholder
     */
    private void emptySlot(Rectangle slot) {
        slot.setFill(Color.BLACK);
        slot.setStroke(Color.BLACK);
        slot.setStrokeWidth(0.75);
    }

    public void setHasBeenOpened(boolean hasBeenOpened) {
        this.hasBeenOpened = hasBeenOpened;
    }

    public GridPane getItemSlots1() {
        return itemSlots1;
    }

    public GridPane getItemSlots2() {
        return itemSlots2;
    }

    public Rectangle getHeadSlot() {
        return headSlot;
    }

    public Rectangle getChestSlot() {
        return chestSlot;
    }

    public Rectangle getLeggingsSlot() {
        return leggingsSlot;
    }

    public Rectangle getBootsSlot() {
        return bootsSlot;
    }

    public Rectangle getHandsSlot() {
        return handsSlot;
    }

    public Rectangle getWeaponSlot() {
        return weaponSlot;
    }

}
