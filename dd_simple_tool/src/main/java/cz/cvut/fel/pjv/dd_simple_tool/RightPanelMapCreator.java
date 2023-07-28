package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.EnergyPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Equip;
import cz.cvut.fel.pjv.dd_simple_tool.model.Food;
import cz.cvut.fel.pjv.dd_simple_tool.model.HealthPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.ItemTags;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * VBox showing ListView of objects that can be added to map within the
 * MapCreator window. Objects are sorted into 11 different tabs, after selecting
 * a tab, contents are displayed in the ListView below.
 *
 * @author Miroslav Falcmann
 */
public class RightPanelMapCreator {

    private final VBox vBox;
    private final GridPane gridPane;
    private Button bootsBut;
    private Button usablesBut;
    private Button helmetsBut;
    private Button leggingsBut;
    private Button chestsBut;
    private Button junkBut;
    private Button handsBut;
    private Button weaponsBut;
    private Button terrainBut;
    private Button enemiesBut;
    private Button objectsBut;

    // public to change it in different class
    private final ListView objectsList;

    private ObservableList<Enemy> enemies;
    private ObservableList<Equip> weapons;
    private ObservableList<Equip> boots;
    private ObservableList<Equip> helmets;
    private ObservableList<Equip> leggings;
    private ObservableList<Equip> chests;
    private ObservableList<Equip> hands;
    private ObservableList<Item> junk;
    private ObservableList<Item> usables;
    private ObservableList<Chest> objects;
    private ObservableList<Terrain> terrain;

    private final FXMLLoader rightPanelLoader;
    private final List<ShowSelected> listeners;

    public RightPanelMapCreator() throws IOException {
        this.listeners = new ArrayList<>();

        // initializing objects from FXML file
        rightPanelLoader = new FXMLLoader(getClass().getResource("right_panel_map_creator.fxml"));
        vBox = rightPanelLoader.load();
        gridPane = (GridPane) vBox.getChildren().get(0);
        objectsList = (ListView) vBox.getChildren().get(1);

        // adding created objects
        addListElements();

        //objectsList.setItems(enemies);
        initAllButtons();
        addEventFilters();
    }

    /**
     * Add various enemies to "enemies" ObservableList
     */
    private void addEnemies() {
        enemies = FXCollections.observableArrayList();
        enemies.add(new Enemy("Bear", "Just a common bear", "bear.png", 20, 5, 20));
        enemies.add(new Enemy("Wolf", "Angry wolf", "wolf.jpg", 15, 7, 15));
        enemies.add(new Enemy("Dead pirate", "...", "dead_pirate1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Summoner", "...", "summoner1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Summoned soul", "...", "summoned_soul.jpg", 15, 7, 15));
        enemies.add(new Enemy("Plague Doctor", "...", "plague_doc1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Shark pirate", "...", "shark_pirate1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Goblin witch", "...", "goblin1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Orc1", "...", "orc_sq1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Orc2", "...", "orc_sq2.jpg", 15, 7, 15));
        enemies.add(new Enemy("Orc3", "...", "orc_sq3.jpg", 15, 7, 15));
        enemies.add(new Enemy("Orc pirate", "...", "orc_pirate1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Magic hen", "...", "magic_hen.jpg", 15, 7, 15));
        enemies.add(new Enemy("Bandit1", "Thief, hiding in the woods", "bandit_sq1.jpg", 15, 7, 15));
        enemies.add(new Enemy("Bandit2", "Thief, hiding in the woods", "bandit_sq2.jpg", 15, 7, 15));

    }

    /**
     * Add various chests to "objects" ObservableList
     */
    private void addObjects() {
        objects = FXCollections.observableArrayList();
        objects.add(new Chest("Forest chest", "May contain various items...", "treasureChest1.jpg"));
        objects.add(new Chest("Wooden chest", "May contain various items...", "treasureChest2.jpg"));
    }

    /**
     * Add various terrain tiles to "terrain" ObservableList
     */
    private void addTerrain() {
        terrain = FXCollections.observableArrayList();
        terrain.add(new Terrain("Mountain", "Mountain tile.", "mountain.jpg"));
        terrain.add(new Terrain("Grass", "Grass tile.", "grass.jpg"));
        terrain.add(new Terrain("Water", "Water tile.", "water1.jpg"));
        terrain.add(new Terrain("Snow", "Snow tile.", "snow1.jpg"));
        terrain.add(new Terrain("Bush", "Bush tile.", "bush1.jpg"));
        terrain.add(new Terrain("Sand", "Sand tile.", "sand1.jpg"));
    }

    /**
     * Add various helmets to "helmets" ObservableList
     */
    private void addHelmets() {
        helmets = FXCollections.observableArrayList();
        helmets.add(new Equip("Helmet1", "helmet_01.jpg", ItemTags.HEAD, 10, 0, 2));
        helmets.add(new Equip("Helmet2", "helmet_02.jpg", ItemTags.HEAD, 15, 2, 4));
        helmets.add(new Equip("Helmet3", "helmet_03.jpg", ItemTags.HEAD, 20, 4, 6));
        helmets.add(new Equip("Helmet4", "helmet_04.jpg", ItemTags.HEAD, 25, 6, 8));
        helmets.add(new Equip("Helmet5", "helmet_05.jpg", ItemTags.HEAD, 30, 8, 10));
        helmets.add(new Equip("Helmet6", "helmet_06.jpg", ItemTags.HEAD, 35, 10, 12));
    }

    /**
     * Add various leggings to "leggings" ObservableList
     */
    private void addLeggings() {
        leggings = FXCollections.observableArrayList();
        leggings.add(new Equip("Leggings1", "legs_01.jpg", ItemTags.LEGS, 10, 0, 2));
        leggings.add(new Equip("Leggings2", "legs_02.jpg", ItemTags.LEGS, 15, 2, 4));
        leggings.add(new Equip("Leggings3", "legs_03.jpg", ItemTags.LEGS, 20, 4, 6));
        leggings.add(new Equip("Leggings4", "legs_04.jpg", ItemTags.LEGS, 25, 6, 8));
        leggings.add(new Equip("Leggings5", "legs_05.jpg", ItemTags.LEGS, 30, 8, 10));
        leggings.add(new Equip("Leggings6", "legs_06.jpg", ItemTags.LEGS, 35, 10, 12));
    }

    /**
     * Add various chestplates to "chests" ObservableList
     */
    private void addChests() {
        chests = FXCollections.observableArrayList();
        chests.add(new Equip("Chest1", "chest_01.jpg", ItemTags.CHEST, 15, 2, 5));
        chests.add(new Equip("Chest2", "chest_02.jpg", ItemTags.CHEST, 30, 4, 10));
        chests.add(new Equip("Chest3", "chest_03.jpg", ItemTags.CHEST, 45, 6, 15));
        chests.add(new Equip("Chest4", "chest_04.jpg", ItemTags.CHEST, 60, 8, 20));
        chests.add(new Equip("Chest5", "chest_05.jpg", ItemTags.CHEST, 75, 10, 25));
        chests.add(new Equip("Chest6", "chest_06.jpg", ItemTags.CHEST, 90, 12, 30));
        chests.add(new Equip("Chest7", "chest_07.jpg", ItemTags.CHEST, 105, 14, 35));
        chests.add(new Equip("Chest8", "chest_08.jpg", ItemTags.CHEST, 120, 16, 40));
    }

    /**
     * Add various boots to "boots" ObservableList
     */
    private void addBoots() {
        boots = FXCollections.observableArrayList();
        boots.add(new Equip("Boots1", "boots_02.jpg", ItemTags.FEET, 10, 0, 2));
        boots.add(new Equip("Boots2", "boots_03.jpg", ItemTags.FEET, 15, 0, 4));
        boots.add(new Equip("Boots3", "boots_04.jpg", ItemTags.FEET, 20, 0, 6));
        boots.add(new Equip("Boots4", "boots_05.jpg", ItemTags.FEET, 25, 0, 8));
        boots.add(new Equip("Boots5", "boots_06.jpg", ItemTags.FEET, 30, 0, 10));
        boots.add(new Equip("Boots6", "boots_07.jpg", ItemTags.FEET, 35, 0, 12));
        boots.add(new Equip("Boots7", "boots_08.jpg", ItemTags.FEET, 40, 0, 14));
    }

    /**
     * Add various gloves to "hands" ObservableList
     */
    private void addHands() {
        hands = FXCollections.observableArrayList();
        hands.add(new Equip("Gloves1", "hands_01.jpg", ItemTags.HAND, 10, 2, 2));
        hands.add(new Equip("Gloves2", "hands_02.jpg", ItemTags.HAND, 15, 4, 4));
        hands.add(new Equip("Gloves3", "hands_03.jpg", ItemTags.HAND, 20, 6, 6));
        hands.add(new Equip("Gloves4", "hands_04.jpg", ItemTags.HAND, 25, 8, 8));
        hands.add(new Equip("Gloves5", "hands_05.jpg", ItemTags.HAND, 30, 10, 10));
        hands.add(new Equip("Gloves6", "hands_06.jpg", ItemTags.HAND, 35, 12, 12));
    }

    /**
     * Add various weapons to "weapons" ObservableList
     */
    private void addWeapons() {
        weapons = FXCollections.observableArrayList();
        weapons.add(new Equip("Sword1", "sword_01.jpg", ItemTags.WEAPON, 10, 5, 10));
        weapons.add(new Equip("Sword2", "sword_02.jpg", ItemTags.WEAPON, 20, 10, 20));
        weapons.add(new Equip("Sword3", "sword_03.jpg", ItemTags.WEAPON, 30, 15, 30));
        weapons.add(new Equip("Sword4", "sword_04.jpg", ItemTags.WEAPON, 40, 20, 40));
        weapons.add(new Equip("Sword5", "sword_05.jpg", ItemTags.WEAPON, 50, 25, 50));
        weapons.add(new Equip("Sword6", "sword_06.jpg", ItemTags.WEAPON, 60, 30, 60));
        weapons.add(new Equip("Sword7", "sword_07.jpg", ItemTags.WEAPON, 70, 35, 70));

        weapons.add(new Equip("Axe1", "axe_01.jpg", ItemTags.WEAPON, 10, 5, 10));
        weapons.add(new Equip("Axe2", "axe_02.jpg", ItemTags.WEAPON, 20, 10, 20));
        weapons.add(new Equip("Axe3", "axe_03.jpg", ItemTags.WEAPON, 30, 15, 30));
        weapons.add(new Equip("Axe4", "axe_04.jpg", ItemTags.WEAPON, 40, 20, 40));
        weapons.add(new Equip("Axe5", "axe_05.jpg", ItemTags.WEAPON, 50, 25, 50));
        weapons.add(new Equip("Axe6", "axe_06.jpg", ItemTags.WEAPON, 60, 30, 60));
        weapons.add(new Equip("Axe7", "axe_07.jpg", ItemTags.WEAPON, 70, 35, 70));

        weapons.add(new Equip("mace1", "mace_01.jpg", ItemTags.WEAPON, 10, 5, 10));
        weapons.add(new Equip("Mace2", "mace_02.jpg", ItemTags.WEAPON, 20, 10, 20));
        weapons.add(new Equip("Mace3", "mace_03.jpg", ItemTags.WEAPON, 30, 15, 30));
        weapons.add(new Equip("Mace4", "mace_04.jpg", ItemTags.WEAPON, 40, 20, 40));
        weapons.add(new Equip("Mace5", "mace_05.jpg", ItemTags.WEAPON, 50, 25, 50));
        weapons.add(new Equip("Mace6", "mace_06.jpg", ItemTags.WEAPON, 60, 30, 60));
        weapons.add(new Equip("Mace7", "mace_07.jpg", ItemTags.WEAPON, 70, 35, 70));

        weapons.add(new Equip("Hammer1", "hammer_01.jpg", ItemTags.WEAPON, 10, 5, 10));
        weapons.add(new Equip("Hammer2", "hammer_02.jpg", ItemTags.WEAPON, 20, 10, 20));
        weapons.add(new Equip("Hammer3", "hammer_03.jpg", ItemTags.WEAPON, 30, 15, 30));
        weapons.add(new Equip("Hammer4", "hammer_04.jpg", ItemTags.WEAPON, 40, 20, 40));
        weapons.add(new Equip("Hammer5", "hammer_05.jpg", ItemTags.WEAPON, 50, 25, 50));
        weapons.add(new Equip("Hammer6", "hammer_06.jpg", ItemTags.WEAPON, 60, 30, 60));
        weapons.add(new Equip("Hammer7", "hammer_07.jpg", ItemTags.WEAPON, 70, 35, 70));

        weapons.add(new Equip("Dagger1", "dagger_01.jpg", ItemTags.WEAPON, 10, 5, 10));
        weapons.add(new Equip("Dagger2", "dagger_02.jpg", ItemTags.WEAPON, 20, 10, 20));
        weapons.add(new Equip("Dagger3", "dagger_03.jpg", ItemTags.WEAPON, 30, 15, 30));
        weapons.add(new Equip("Dagger4", "dagger_04.jpg", ItemTags.WEAPON, 40, 20, 40));
        weapons.add(new Equip("Dagger5", "dagger_05.jpg", ItemTags.WEAPON, 50, 25, 50));
        weapons.add(new Equip("Dagger6", "dagger_06.jpg", ItemTags.WEAPON, 60, 30, 60));
        weapons.add(new Equip("Dagger7", "dagger_07.jpg", ItemTags.WEAPON, 70, 35, 70));

        weapons.add(new Equip("Staff1", "staff_01.jpg", ItemTags.WEAPON, 10, 5, 10));
        weapons.add(new Equip("Staff2", "staff_02.jpg", ItemTags.WEAPON, 20, 10, 20));
        weapons.add(new Equip("Staff3", "staff_03.jpg", ItemTags.WEAPON, 30, 15, 30));
        weapons.add(new Equip("Staff4", "staff_04.jpg", ItemTags.WEAPON, 40, 20, 40));
        weapons.add(new Equip("Staff5", "staff_05.jpg", ItemTags.WEAPON, 50, 25, 50));
        weapons.add(new Equip("Staff6", "staff_06.jpg", ItemTags.WEAPON, 60, 30, 60));
        weapons.add(new Equip("Staff7", "staff_07.jpg", ItemTags.WEAPON, 70, 35, 70));

        weapons.add(new Equip("Bow1", "bow_01.jpg", ItemTags.WEAPON, 10, 5, 10));
        weapons.add(new Equip("Bow2", "bow_02.jpg", ItemTags.WEAPON, 20, 10, 20));
        weapons.add(new Equip("Bow3", "bow_03.jpg", ItemTags.WEAPON, 30, 15, 30));
        weapons.add(new Equip("Crossbow1", "crossbow_01.jpg", ItemTags.WEAPON, 40, 20, 40));
        weapons.add(new Equip("Crossbow2", "crossbow_02.jpg", ItemTags.WEAPON, 50, 25, 50));
        weapons.add(new Equip("Gun1", "gun_01.jpg", ItemTags.WEAPON, 60, 30, 60));
        weapons.add(new Equip("Gun2", "gun_02.jpg", ItemTags.WEAPON, 70, 35, 70));
    }

    /**
     * Add various junk items to "junk" ObservableList
     */
    private void addJunk() {
        junk = FXCollections.observableArrayList();
        junk.add(new Item("Wolf hide", "junk_wolf_hide.jpg", ItemTags.JUNK, 40));
        junk.add(new Item("Bear hide", "junk_bear_hide.jpg", ItemTags.JUNK, 50));
        junk.add(new Item("Eye", "junk_eye.jpg", ItemTags.JUNK, 10));
    }

    /**
     * Add various usable items to "usables" ObservableList
     */
    private void addUsables() {
        usables = FXCollections.observableArrayList();
        usables.add(new HealthPotion("Health Potion1", "health_potion1.jpg", ItemTags.USABLE, 5, 50));
        usables.add(new EnergyPotion("Energy Potion1", "energy_potion1.jpg", ItemTags.USABLE, 5, 50));
        usables.add(new EnergyPotion("Energy Potion2", "energy_potion2.jpg", ItemTags.USABLE, 10, 70));
        usables.add(new Food("Cheese", "food_cheese.jpg", ItemTags.USABLE, 5, 10));
        usables.add(new Food("Ham", "food_ham.jpg", ItemTags.USABLE, 10, 20));
    }

    /**
     * Add all objects to their lists in one function
     */
    private void addListElements() {
        addEnemies();
        addObjects();
        addTerrain();
        addHelmets();
        addHands();
        addBoots();
        addChests();
        addWeapons();
        addLeggings();
        addJunk();
        addUsables();
    }

    /**
     * Add mouse click event to given button. On click given ListView is set by
     * given ObservableList
     *
     * @param button Button to which will be the event added.
     * @param objects ObservableList which will be added to ListView
     * @param objectsList ListView whose items will be set
     */
    public static void addDisplayObjectButtonEvent(Button button, ObservableList objects, ListView objectsList) {
        EventHandler<MouseEvent> displayObjects = (MouseEvent e) -> {
            objectsList.setItems(objects);
        };
        button.addEventFilter(MouseEvent.MOUSE_CLICKED, displayObjects);
    }

    /**
     * Call addDisplayObjectButtonEvent function for each button
     */
    private void addButtonEvents() {
        addDisplayObjectButtonEvent(enemiesBut, enemies, objectsList);
        addDisplayObjectButtonEvent(weaponsBut, weapons, objectsList);
        addDisplayObjectButtonEvent(terrainBut, terrain, objectsList);
        addDisplayObjectButtonEvent(objectsBut, objects, objectsList);
        addDisplayObjectButtonEvent(helmetsBut, helmets, objectsList);
        addDisplayObjectButtonEvent(chestsBut, chests, objectsList);
        addDisplayObjectButtonEvent(bootsBut, boots, objectsList);
        addDisplayObjectButtonEvent(leggingsBut, leggings, objectsList);
        addDisplayObjectButtonEvent(handsBut, hands, objectsList);
        addDisplayObjectButtonEvent(usablesBut, usables, objectsList);
        addDisplayObjectButtonEvent(junkBut, junk, objectsList);
    }

    /**
     * Add mouse click event to ListView displaying Items, Terrain, Chests and
     * Enemies. Selected object is on click selected and shown in panel within
     * the Map Creator window
     */
    private void addEventFilters() {
        addButtonEvents();

        EventHandler<MouseEvent> listViewClicked = (MouseEvent e) -> {
            for (ShowSelected listener : listeners) {
                ((BaseObject) objectsList.getSelectionModel().getSelectedItem()).registerToListener(listener);
            }
        };
        objectsList.addEventFilter(MouseEvent.MOUSE_CLICKED, listViewClicked);
    }

    /**
     * Initialize buttons from FXML file
     */
    private void initAllButtons() {
        bootsBut = (Button) gridPane.getChildren().get(0);
        usablesBut = (Button) gridPane.getChildren().get(1);
        helmetsBut = (Button) gridPane.getChildren().get(2);
        leggingsBut = (Button) gridPane.getChildren().get(3);
        chestsBut = (Button) gridPane.getChildren().get(4);
        junkBut = (Button) gridPane.getChildren().get(5);
        handsBut = (Button) gridPane.getChildren().get(6);
        weaponsBut = (Button) gridPane.getChildren().get(7);
        terrainBut = (Button) gridPane.getChildren().get(8);
        enemiesBut = (Button) gridPane.getChildren().get(9);
        objectsBut = (Button) gridPane.getChildren().get(10);
    }

    public VBox getVBox() {
        return vBox;
    }

    public void addListener(ShowSelected toAdd) {
        listeners.add(toAdd);
    }

    public ListView getObjectsList() {
        return objectsList;
    }

    public ObservableList<Equip> getWeapons() {
        return weapons;
    }

    public ObservableList<Equip> getBoots() {
        return boots;
    }

    public ObservableList<Equip> getHelmets() {
        return helmets;
    }

    public ObservableList<Equip> getLeggings() {
        return leggings;
    }

    public ObservableList<Equip> getChests() {
        return chests;
    }

    public ObservableList<Equip> getHands() {
        return hands;
    }

    public ObservableList<Item> getJunk() {
        return junk;
    }

    public ObservableList<Item> getUsables() {
        return usables;
    }
}
