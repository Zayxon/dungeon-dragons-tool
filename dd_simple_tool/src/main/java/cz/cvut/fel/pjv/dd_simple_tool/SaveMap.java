package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.EnergyPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Equip;
import cz.cvut.fel.pjv.dd_simple_tool.model.Food;
import cz.cvut.fel.pjv.dd_simple_tool.model.HealthPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.ItemTags;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class for saving the current changes on created map within the MapCreator
 * window to .json file.
 *
 * @author Miroslav Falcmann
 */
public class SaveMap {

    private ArrayList<Tile> tilesArr;
    private ArrayList<CircleObject> circleObjectsArr;
    private final String savePath;
    private MapCreatorWindow mcWindow;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public SaveMap() {
        this.savePath = "saves/saved_maps/";
    }

    /**
     * Save the current map under the given name.
     *
     * @param fileName Name of the game save file.
     */
    public void saveMap(String fileName) {
        tilesArr = mcWindow.getMp().getTilesArr();
        circleObjectsArr = mcWindow.getMp().getCircleObjectsArr();
        JSONObject jsonObject = new JSONObject();
        saveTiles(jsonObject);
        saveObjects(jsonObject);
        createJsonFile(jsonObject, savePath + fileName + ".json");
    }

    /**
     * Save all tiles data (layout, name, fill...) in given JSONObject
     *
     * @param jsonObject JSONObject where necessary data about tiles will be
     * saved
     */
    private void saveTiles(JSONObject jsonObject) {
        int i = 0;
        for (Tile tile : tilesArr) {
            if (tile.getTerrain() == null) {
                LOGGER.warning("Tile with no terrain!");
                continue;
            }
            JSONArray hexagonArr = new JSONArray();
            addJSONObject("name", tile.getTerrain().getName(), hexagonArr);
            addJSONObject("description", tile.getTerrain().getDescription(), hexagonArr);
            addJSONObject("image", tile.getTerrain().getImageFilename(), hexagonArr);
            addJSONObject("layoutX", (double) tile.getLayoutX(), hexagonArr);
            addJSONObject("layoutY", (double) tile.getLayoutY(), hexagonArr);
            addJSONObject("centerX", (double) tile.getCenterX(), hexagonArr);
            addJSONObject("centerY", (double) tile.getCenterY(), hexagonArr);
            jsonObject.put("Tile" + i, hexagonArr);
            i++;
            LOGGER.fine("Tile" + i + " saved.");
        }
    }

    /**
     * Write all data in JSONObject into json file. Creates new json file in
     * given path.
     *
     * @param jsonObject JSONObject which will be written to the file
     * @param path Path where the json file will be created
     */
    private void createJsonFile(JSONObject jsonObject, String path) {
        try {
            FileWriter file = new FileWriter(path);
            file.write(jsonObject.toString(2));
            file.close();
            LOGGER.info("File created.");
        } catch (IOException e) {
            LOGGER.severe("File cannot be created!");
            e.printStackTrace();
        }
    }

    /**
     * Save all game objects (enemies, chests) data in given JSONObject
     *
     * @param jsonObject JSONObject where necessary data about game objects will
     * be saved
     */
    private void saveObjects(JSONObject jsonObject) {
        int i = 0;
        for (CircleObject circObj : circleObjectsArr) {
            JSONArray circObjectArr = new JSONArray();

            addJSONObject("centerX", (double) circObj.getCenterX(), circObjectArr);
            addJSONObject("centerY", (double) circObj.getCenterY(), circObjectArr);

            JSONArray objArr = new JSONArray();

            addJSONObject("name", circObj.getBaseObj().getName(), objArr);
            addJSONObject("description", circObj.getBaseObj().getDescription(), objArr);
            addJSONObject("image", circObj.getBaseObj().getImageFilename(), objArr);

            JSONArray containerArr = new JSONArray();
            if (circObj.getBaseObj() instanceof Enemy) {
                addJSONObject("class", "Enemy", objArr);
                addEnemyStats(((Enemy) circObj.getBaseObj()), objArr);
                addContainer(((Enemy) circObj.getBaseObj()).getContainer(), containerArr);
            } else {
                addJSONObject("class", "Chest", objArr);
                addContainer(((Chest) circObj.getBaseObj()).getContainer(), containerArr);
            }

            addJSONObject("container", containerArr, objArr);
            addJSONObject("object", objArr, circObjectArr);

            jsonObject.put("CircleObject" + i, circObjectArr);
            i++;
            LOGGER.fine("CircleObject" + i + "saved");
        }
    }

    /**
     * Add necessary data that are unique for enemies
     *
     * @param enemy given enemy whose necessary stats will be saved
     * @param objArr JSONArray to which enemy data will be added
     */
    private void addEnemyStats(Enemy enemy, JSONArray objArr) {
        addJSONObject("level", (int) enemy.getLvl(), objArr);
        addJSONObject("hp", (int) enemy.getHp(), objArr);
        addJSONObject("dmg", (int) enemy.getDmg(), objArr);
        addJSONObject("resist", (int) enemy.getResist(), objArr);
    }

    /**
     * Save all items stored in enemy drop container as JSONObjects and add them
     * to the JSONArray representing enemy drop container
     *
     * @param container Enemy drop container
     * @param containerArr JSONArray where all drop container data will be
     * stored
     */
    private void addContainer(Item[] container, JSONArray containerArr) {
        if (container[0] != null) {
            addItem(container, 0, containerArr);
        }
        if (container[1] != null) {
            addItem(container, 1, containerArr);
        }
        if (container[2] != null) {
            addItem(container, 2, containerArr);
        }
    }

    /**
     * Item in given Item container will be saved as JSONObject and added to
     * JSONArray storing data about all items
     *
     * @param container Player game Item container
     * @param itemIndex Index at which the item is stored in Item container
     * @param containerArr JSONArray to which the item data will be appended
     */
    private void addItem(Item[] container, int itemIndex, JSONArray containerArr) {
        JSONArray itemArr = new JSONArray();

        addJSONObject("name", container[itemIndex].getName(), itemArr);
        addJSONObject("image", container[itemIndex].getImageFilename(), itemArr);
        addJSONObject("sellValue", (int) container[itemIndex].getSellValue(), itemArr);
        addJSONObject("tag", container[itemIndex].getTag(), itemArr);

        if ((container[itemIndex]) instanceof Equip) {
            addEquipSpecifics(container, itemIndex, itemArr);
        } else if ((container[itemIndex]) instanceof HealthPotion) {
            addHealthPotionSpecifics(container, itemIndex, itemArr);
        } else if ((container[itemIndex]) instanceof EnergyPotion) {
            addEnergyPotionSpecifics(container, itemIndex, itemArr);
        } else if ((container[itemIndex]) instanceof Food) {
            addFoodSpecifics(container, itemIndex, itemArr);
        } else {
            addItemSpecifics(itemArr);
        }

        addJSONObject("Item" + itemIndex, itemArr, containerArr);
    }

    /**
     * Add necessary data that are unique for Items of instance "Equip"
     *
     * @param container Player game Item container
     * @param itemIndex Index at which the item is stored in Item container
     * @param itemArr JSONArray storing all data about a single item
     */
    private void addEquipSpecifics(Item[] container, int itemIndex, JSONArray itemArr) {
        addJSONObject("class", "Equip", itemArr);
        addJSONObject("damage", (int) ((Equip) (container[itemIndex])).getAttackDmg(), itemArr);
        addJSONObject("healthPoints", (int) ((Equip) (container[itemIndex])).getHealthPoints(), itemArr);
    }

    /**
     * Add necessary data that are unique for Items of instance "HealthPotion"
     *
     * @param container Player game Item container
     * @param itemIndex Index at which the item is stored in Item container
     * @param itemArr JSONArray storing all data about a single item
     */
    private void addHealthPotionSpecifics(Item[] container, int itemIndex, JSONArray itemArr) {
        addJSONObject("class", "HealthPotion", itemArr);
        addJSONObject("healAmount", (int) ((HealthPotion) (container[itemIndex])).getHealAmount(), itemArr);
    }

    /**
     * Add necessary data that are unique for Items of instance "EnergyPotion"
     *
     * @param container Player game Item container
     * @param itemIndex Index at which the item is stored in Item container
     * @param itemArr JSONArray storing all data about a single item
     */
    private void addEnergyPotionSpecifics(Item[] container, int itemIndex, JSONArray itemArr) {
        addJSONObject("class", "EnergyPotion", itemArr);
        addJSONObject("healAmount", (int) ((EnergyPotion) (container[itemIndex])).getHealAmount(), itemArr);
    }

    /**
     * Add necessary data that are unique for Items of instance "Food"
     *
     * @param container Player game Item container
     * @param itemIndex Index at which the item is stored in Item container
     * @param itemArr JSONArray storing all data about a single item
     */
    private void addFoodSpecifics(Item[] container, int itemIndex, JSONArray itemArr) {
        addJSONObject("class", "Food", itemArr);
        addJSONObject("healAmount", (int) ((Food) (container[itemIndex])).getHealAmount(), itemArr);
    }

    /**
     * Add necessary data that are unique for Items of instance "Item"
     *
     * @param itemArr JSONArray storing all data about a single item
     */
    private void addItemSpecifics(JSONArray itemArr) {
        addJSONObject("class", "Item", itemArr);
    }

    /**
     * Creates new JSONObject and set its key and value. Add this JSONObjects to
     * the given JSONArray.
     *
     * @param key (String) key to the value in JSONObject.
     * @param value String value of created JSONObject.
     * @param objArr JSONArray to which created JSONObject will be added.
     */
    private void addJSONObject(String key, String value, JSONArray objArr) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        objArr.put(obj);
    }

    /**
     * Creates new JSONObject and set its key and value. Add this JSONObjects to
     * the given JSONArray.
     *
     * @param key (String) key to the value in JSONObject.
     * @param value double value of created JSONObject.
     * @param objArr JSONArray to which created JSONObject will be added.
     */
    private void addJSONObject(String key, double value, JSONArray objArr) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        objArr.put(obj);
    }

    /**
     * Creates new JSONObject and set its key and value. Add this JSONObjects to
     * the given JSONArray.
     *
     * @param key (String) key to the value in JSONObject.
     * @param value int value of created JSONObject.
     * @param objArr JSONArray to which created JSONObject will be added.
     */
    private void addJSONObject(String key, int value, JSONArray objArr) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        objArr.put(obj);
    }

    /**
     * Creates new JSONObject and set its key and value. Add this JSONObjects to
     * the given JSONArray.
     *
     * @param key (String) key to the value in JSONObject.
     * @param value ItemTags value of created JSONObject.
     * @param objArr JSONArray to which created JSONObject will be added.
     */
    private void addJSONObject(String key, ItemTags value, JSONArray objArr) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        objArr.put(obj);
    }

    /**
     * Creates new JSONObject and set its key and value. Add this JSONObjects to
     * the given JSONArray.
     *
     * @param key (String) key to the value in JSONObject.
     * @param value JSONArray value of created JSONObject.
     * @param objArr JSONArray to which created JSONObject will be added.
     */
    private void addJSONObject(String key, JSONArray value, JSONArray objArr) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        objArr.put(obj);
    }

    public void setMcWindow(MapCreatorWindow mcWindow) {
        this.mcWindow = mcWindow;
    }
}
