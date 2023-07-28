package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.EnergyPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Equip;
import cz.cvut.fel.pjv.dd_simple_tool.model.Food;
import cz.cvut.fel.pjv.dd_simple_tool.model.HealthPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.ItemTags;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class for saving the current changes in game within the Game window to .json
 * file. Current stats, inventory containers or equip container of all players
 * are saved.
 *
 * @author Miroslav Falcmann
 */
public class SaveGame {

    private ArrayList<Player> playerArr;
    private final String savePath;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public SaveGame() {
        // path where saved games are stored
        this.savePath = "saves/saved_games/";
    }

    /**
     * Save the current game under the given name.
     *
     * @param fileName Name of the game save file.
     */
    public void saveGame(String fileName) {
        JSONObject jsonObject = new JSONObject();
        savePlayers(jsonObject);
        createJsonFile(jsonObject, savePath + fileName + ".json");
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
            LOGGER.info("JSON File created.");
        } catch (IOException e) {
            LOGGER.severe("File cannot be created!");
            e.printStackTrace();
        }
    }

    /**
     * Save all players data (stats, inventory, equip) in given JSONObject
     *
     * @param jsonObject JSONObject where necessary data about players will be
     * saved
     */
    private void savePlayers(JSONObject jsonObject) {
        int i = 0;
        for (Player player : playerArr) {
            JSONArray playersArr = new JSONArray();
            addJSONObject("name", player.getName(), playersArr);
            addJSONObject("class", player.getClassName(), playersArr);
            addJSONObject("lvl", (int) player.getLvl(), playersArr);
            addJSONObject("xp", (int) player.getXp(), playersArr);
            addJSONObject("energy", (int) player.getEnergy(), playersArr);
            addJSONObject("hp", (int) player.getHp(), playersArr);
            addJSONObject("gold", (int) player.getGold(), playersArr);
            addJSONObject("maxHp", (int) player.getMaxHp(), playersArr);
            addJSONObject("maxEnergy", (int) player.getMaxEnergy(), playersArr);
            addJSONObject("maxXp", (int) player.getMaxXp(), playersArr);
            addJSONObject("resist", (int) player.getResist(), playersArr);
            addJSONObject("baseDmg", (int) player.getBaseDmg(), playersArr);
            addJSONObject("baseMaxHp", (int) player.getMaxHp(), playersArr);

            // equip
            JSONArray equipArr = new JSONArray();
            addEquip(player.getEquip(), equipArr);
            addJSONObject("equip", equipArr, playersArr);

            // inventory
            JSONArray inventoryArr = new JSONArray();
            addContainer(player.getContainer(), inventoryArr);
            addJSONObject("inventory", inventoryArr, playersArr);

            jsonObject.put("player" + i, playersArr);
            i++;
            LOGGER.fine("player" + i + " saved.");
        }
    }

    /**
     * Save all items stored in players inventory as JSONObjects and add them to
     * the JSONArray representing players inventory
     *
     * @param container Players game inventory
     * @param containerArr JSONArray where all inventory data will be stored
     */
    private void addContainer(Item[] container, JSONArray containerArr) {
        for (int i = 0; i < 12; i++) {
            if (container[i] != null) {
                addItem(container, i, containerArr);
            }
        }
    }

    /**
     * Save all equip items stored in players equip as JSONObjects and add them
     * to the JSONArray representing players equip
     *
     * @param container Players game equip
     * @param containerArr JSONArray where all equip data will be stored
     */
    private void addEquip(Item[] container, JSONArray containerArr) {
        for (int i = 0; i < 6; i++) {
            if (container[i] != null) {
                addItem(container, i, containerArr);
            }
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

    public void setPlayerList(ArrayList<Player> playerArr) {
        this.playerArr = playerArr;
    }

}
