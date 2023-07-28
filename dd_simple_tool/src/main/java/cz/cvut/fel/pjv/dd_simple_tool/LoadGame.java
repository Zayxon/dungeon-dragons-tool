package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.DeathKnight;
import cz.cvut.fel.pjv.dd_simple_tool.model.Druid;
import cz.cvut.fel.pjv.dd_simple_tool.model.EnergyPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Equip;
import cz.cvut.fel.pjv.dd_simple_tool.model.Food;
import cz.cvut.fel.pjv.dd_simple_tool.model.HealthPotion;
import cz.cvut.fel.pjv.dd_simple_tool.model.Hunter;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.ItemTags;
import cz.cvut.fel.pjv.dd_simple_tool.model.Mage;
import cz.cvut.fel.pjv.dd_simple_tool.model.Paladin;
import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import cz.cvut.fel.pjv.dd_simple_tool.model.Priest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Shaman;
import cz.cvut.fel.pjv.dd_simple_tool.model.Warlock;
import cz.cvut.fel.pjv.dd_simple_tool.model.Warrior;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Class for loading previously created games that are saved in .json files.
 *
 * @author Miroslav Falcmann
 */
public class LoadGame {

    private final List<Player> playerList;
    private final ObservableList<Player> classList;
    private final ListView playerListView;
    private final GameWindow gWindow;

    private final String pathToJSON = "saves/saved_games/";

    private String name;
    private String className;
    private Integer lvl;
    private Integer xp;
    private Integer energy;
    private Integer hp;
    private Integer gold;
    private Integer maxHp;
    private Integer maxEnergy;
    private Integer maxXp;
    private Integer resist;
    private Integer baseDmg;
    private Integer baseMaxHp;
    private Equip[] playerEquip;
    private Item[] playerInventory;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public LoadGame() throws IOException {
        this.playerListView = new ListView();
        this.playerList = new ArrayList<>();
        gWindow = new GameWindow();
        classList = FXCollections.observableArrayList();
    }

    /**
     * Load the selected game save and open the game window
     *
     * @param fileName Name of the game save file.
     * @throws IOException if the selected game save is not found
     */
    public void loadGame(String fileName) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(pathToJSON + fileName)));
        JSONObject obj = new JSONObject(contents);
        loadPlayers(obj, 20);
        openGameWindow();
    }

    /**
     * Load all players from JSONObject. All their stats, equip and inventory.
     *
     * @param obj JSONObject which contains all the saved data about players
     * @param maxNumberOfPlayers maximal number of players that can be loaded
     */
    private void loadPlayers(JSONObject obj, int maxNumberOfPlayers) {
        for (int i = 0; i < maxNumberOfPlayers; i++) {
            JSONArray playersArr;
            try {
                playersArr = obj.getJSONArray("player" + i);
            } catch (JSONException e) {
                LOGGER.severe("player" + i + " not found.");
                continue;
            }

            name = (String) playersArr.getJSONObject(0).getString("name");
            className = (String) playersArr.getJSONObject(1).getString("class");
            lvl = (Integer) playersArr.getJSONObject(2).getInt("lvl");
            xp = (Integer) playersArr.getJSONObject(3).getInt("xp");
            energy = (Integer) playersArr.getJSONObject(4).getInt("energy");
            hp = (Integer) playersArr.getJSONObject(5).getInt("hp");
            gold = (Integer) playersArr.getJSONObject(6).getInt("gold");
            maxHp = (Integer) playersArr.getJSONObject(7).getInt("maxHp");
            maxEnergy = (Integer) playersArr.getJSONObject(8).getInt("maxEnergy");
            maxXp = (Integer) playersArr.getJSONObject(9).getInt("maxXp");
            resist = (Integer) playersArr.getJSONObject(10).getInt("resist");
            baseDmg = (Integer) playersArr.getJSONObject(11).getInt("baseDmg");
            baseMaxHp = (Integer) playersArr.getJSONObject(12).getInt("baseMaxHp");

            JSONArray equip = playersArr.getJSONObject(13).getJSONArray("equip");
            playerEquip = addEquip(equip);

            JSONArray inventory = playersArr.getJSONObject(14).getJSONArray("inventory");
            playerInventory = addInventory(inventory);

            addNewPlayerToArrays();
            LOGGER.fine("player" + i + " loaded into arrays.");
        }
        playerListView.setItems(classList);
    }

    /**
     * Use to loaded game contents to set up a game and open the game window
     */
    private void openGameWindow() {
        gWindow.setPlayerListView(playerListView);
        gWindow.getBottomP().setPlayerList(playerListView);
        gWindow.getRightP().getSaveAsWindow().getSaveGame().setPlayerList((ArrayList) playerList);
        gWindow.getBottomP().addPlayerListEvent();
        gWindow.getBottomP().gethBox().getChildren().set(0, playerListView);
        Stage stage = new Stage();
        stage.setTitle("Dungeons and Dragons simple tool");
        stage.setScene(gWindow.getScene());
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Load given equip data, according to them create Equip items and add them
     * to the equip container.
     *
     * @param equip JSONArray storing all saved data about players equip
     * @return Equip array of players equipped items
     */
    private Equip[] addEquip(JSONArray equip) {
        JSONArray eq;
        Equip[] equipContainer = new Equip[6];
        int i = 0;
        for (int j = 0; j < 6; j++) {
            try {
                eq = equip.getJSONObject(i).getJSONArray("Item" + j);
            } catch (JSONException e) {
                continue;
            }
            String name = eq.getJSONObject(0).getString("name");
            String image = eq.getJSONObject(1).getString("image");
            int sellValue = eq.getJSONObject(2).getInt("sellValue");
            ItemTags tag = ItemTags.valueOf(eq.getJSONObject(3).getString("tag"));

            int attackDmg = eq.getJSONObject(5).getInt("damage");
            int healthPoints = eq.getJSONObject(6).getInt("healthPoints");
            Equip equipNew = new Equip(name, image, tag, sellValue, attackDmg, healthPoints);
            equipContainer[j] = equipNew;
            i++;
        }
        return equipContainer;
    }

    /**
     * Load given inventory data, according to them create items and add them to
     * the item container.
     *
     * @param inventory JSONArray storing all saved data about players inventory
     * @return Item array of items stored in players inventory
     */
    private Item[] addInventory(JSONArray inventory) {
        JSONArray item;
        Item[] dropContainer = new Item[12];
        int i = 0;
        for (int j = 0; j < 12; j++) {
            try {
                item = inventory.getJSONObject(i).getJSONArray("Item" + j);
            } catch (JSONException e) {
                continue;
            }
            String name = item.getJSONObject(0).getString("name");
            String image = item.getJSONObject(1).getString("image");
            int sellValue = item.getJSONObject(2).getInt("sellValue");
            ItemTags tag = ItemTags.valueOf(item.getJSONObject(3).getString("tag"));
            String itemClass = item.getJSONObject(4).getString("class");

            // create specific item according to the item tag
            switch (itemClass) {
                case "Item":
                    Item newItem = new Item(name, image, tag, sellValue);
                    dropContainer[j] = newItem;
                    break;
                case "Equip":
                    int attackDmg = item.getJSONObject(5).getInt("damage");
                    int healthPoints = item.getJSONObject(6).getInt("healthPoints");
                    Equip eq = new Equip(name, image, tag, sellValue, attackDmg, healthPoints);
                    dropContainer[j] = eq;
                    break;
                case "HealthPotion":
                    int healAmount = item.getJSONObject(5).getInt("healAmount");
                    HealthPotion hPot = new HealthPotion(name, image, tag, sellValue, healAmount);
                    dropContainer[j] = hPot;
                    break;
                case "EnergyPotion":
                    healAmount = item.getJSONObject(5).getInt("healAmount");
                    EnergyPotion ePot = new EnergyPotion(name, image, tag, sellValue, healAmount);
                    dropContainer[j] = ePot;
                    break;
                case "Food":
                    healAmount = item.getJSONObject(5).getInt("healAmount");
                    Food food = new Food(name, image, tag, sellValue, healAmount);
                    dropContainer[j] = food;
                    break;
                default:
                    break;
            }
            i++;
        }
        return dropContainer;
    }

    /**
     * According to loaded data create player with proper stats, values and add
     * the player to the player list
     */
    private void addNewPlayerToArrays() {
        switch (className) {
            case "Warrior":
                Warrior warr = new Warrior(name);
                setPlayersStats(warr);
                classList.add(warr);
                playerList.add(warr);
                break;
            case "Hunter":
                Hunter hunt = new Hunter(name);
                setPlayersStats(hunt);
                classList.add(hunt);
                playerList.add(hunt);
                break;
            case "Mage":
                Mage mage = new Mage(name);
                setPlayersStats(mage);
                classList.add(mage);
                playerList.add(mage);
                break;
            case "Druid":
                Druid druid = new Druid(name);
                setPlayersStats(druid);
                classList.add(druid);
                playerList.add(druid);
                break;
            case "Paladin":
                Paladin pala = new Paladin(name);
                setPlayersStats(pala);
                classList.add(pala);
                playerList.add(pala);
                break;
            case "Warlock":
                Warlock war = new Warlock(name);
                setPlayersStats(war);
                classList.add(war);
                playerList.add(war);
                break;
            case "Priest":
                Priest priest = new Priest(name);
                setPlayersStats(priest);
                classList.add(priest);
                playerList.add(priest);
                break;
            case "Shaman":
                Shaman shaman = new Shaman(name);
                setPlayersStats(shaman);
                classList.add(shaman);
                playerList.add(shaman);
                break;
            case "Death Knight":
                DeathKnight dk = new DeathKnight(name);
                setPlayersStats(dk);
                classList.add(dk);
                playerList.add(dk);
                break;
            default:
                break;
        }
    }

    /**
     * Set the player stats according to acquired values
     * @param player Player object whose stats needs to be set.
     */
    private void setPlayersStats(Player player) {
        player.setLvl(lvl);
        player.setXp(xp);
        player.setEnergy(energy);
        player.setHp(hp);
        player.setGold(gold);
        player.setMaxHp(maxHp);
        player.setMaxEnergy(maxEnergy);
        player.setMaxXp(maxXp);
        player.setResist(resist);
        player.setBaseDmg(baseDmg);
        player.setBaseMaxHp(baseMaxHp);
        player.setEquip(playerEquip);
        player.setContainer(playerInventory);
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
