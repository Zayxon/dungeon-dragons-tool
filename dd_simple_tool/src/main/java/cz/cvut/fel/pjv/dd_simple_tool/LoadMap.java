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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for loading previously created maps that are saved in .json files.
 *
 * @author Miroslav Falcmann
 */
public class LoadMap {

    private final ArrayList<Tile> tilesArr;
    private final ArrayList<CircleObject> objectsArr;
    private AnchorPane anchorPane;
    private final double hexSize;
    private final double hexLayoutXShift;
    private final double hexLayoutYShift;
    private final int numOfTiles;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final String pathToJSON = "saves/saved_maps/";

    public LoadMap() {
        this.numOfTiles = 144; // proper number map tiles
        this.hexLayoutYShift = 0; // to fit perfectly in the center of AnchorPane
        this.hexLayoutXShift = 25; // to fit perfectly in the center of AnchorPane
        this.hexSize = 34; // size of one tile
        this.objectsArr = new ArrayList<>();
        this.tilesArr = new ArrayList<>();
    }

    /**
     * Load the selected map save and display the map within the opened
     * MapCreator/Game window
     *
     * @param fileName Name of the game save file.
     * @throws IOException if the selected map save is not found
     */
    public void loadMap(String fileName) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(pathToJSON + fileName)));
        JSONObject obj = new JSONObject(contents);
        loadTiles(obj);
        loadObjects(obj);
        setUpAnchorPane();
    }

    /**
     * Set up a new AnchorPane (displaying the loaded map contents) within the
     * opened MapCreator/Game window.
     */
    private void setUpAnchorPane() {
        anchorPane = new AnchorPane();
        anchorPane.setMaxSize(1025, 490);
        anchorPane.setMinSize(1025, 490);
        for (Tile tile : tilesArr) {
            anchorPane.getChildren().add(tile.getHexagon());
        }
        for (CircleObject obj : objectsArr) {
            anchorPane.getChildren().add(obj.getCirc());
        }
    }

    /**
     * Load given tiles data, according to them create new Tiles and add them
     * to the tiles array.
     * @param o JSONObject storing all the saved tiles data
     */
    private void loadTiles(JSONObject o) {
        for (int i = 0; i < numOfTiles; i++) {

            JSONArray tile;
            try {
                tile = o.getJSONArray("Tile" + i);
            } catch (JSONException e) {
                LOGGER.severe("Tile" + i + " not found.");
                continue;
            }

            Double layoutX = tile.getJSONObject(3).getDouble("layoutX");
            Double layoutY = tile.getJSONObject(4).getDouble("layoutY");
            Double centerX = tile.getJSONObject(5).getDouble("centerX");
            Double centerY = tile.getJSONObject(6).getDouble("centerY");

            Polygon hexagon = createHexagon(hexSize, layoutX, layoutY);

            String name = tile.getJSONObject(0).getString("name");
            String description = tile.getJSONObject(1).getString("description");
            String image = tile.getJSONObject(2).getString("image");

            Terrain terrain = new Terrain(name, description, image);

            hexagon.setFill(terrain.getImgPat());
            terrain.setHexagon(hexagon);

            Tile newTile = new Tile(hexagon, terrain);
            newTile.setLayoutX(layoutX);
            newTile.setLayoutY(layoutY);
            newTile.setCenterX(centerX);
            newTile.setCenterY(centerY);
            tilesArr.add(newTile);
            LOGGER.fine("Tile" + i + " loaded into tilesArr.");
        }
    }

    /**
     * Create hexagon (Polygon) with proper size and layout
     * @param size Integer number defining the size of the hexagon
     * @param layoutX X coordinate of the hexagon layout
     * @param layoutY Y coordinate of the hexagon layout
     * @return Polygon - created hexagon
     */
    private Polygon createHexagon(double size, double layoutX, double layoutY) {
        Polygon hexagon = new Polygon();
        double w = Math.sqrt(3) * size;
        double h = 2 * size;
        hexagon.getPoints().addAll(w, 0.0,
                1.5 * w, 0.25 * h,
                1.5 * w, 0.75 * h,
                w, h,
                0.5 * w, 0.75 * h,
                0.5 * w, 0.25 * h);
        hexagon.setFill(Color.TRANSPARENT);
        hexagon.setStroke(Color.BLACK);
        hexagon.setStrokeWidth(0.75);
        hexagon.setLayoutX(layoutX + hexLayoutXShift);
        hexagon.setLayoutY(layoutY + hexLayoutYShift);
        return hexagon;
    }

    /**
     * Load given CircleObjects data, according to them create new CircleObjects and add them
     * to the CircleObjects array. CircleObjects store data about enemies/chests.
     * @param o JSONObject storing all the saved CircleObjects data
     */
    private void loadObjects(JSONObject o) {
        for (int i = 0; i < numOfTiles; i++) {
            JSONArray cObj;
            try {
                cObj = o.getJSONArray("CircleObject" + i);
            } catch (JSONException e) {
                LOGGER.severe("CircleObject" + i + " not found.");
                continue;
            }

            Double centerX = cObj.getJSONObject(0).getDouble("centerX");
            Double centerY = cObj.getJSONObject(1).getDouble("centerY");

            JSONArray object = cObj.getJSONObject(2).getJSONArray("object");
            String name = object.getJSONObject(0).getString("name");
            String description = object.getJSONObject(1).getString("description");
            String image = object.getJSONObject(2).getString("image");

            String oClass = object.getJSONObject(3).getString("class");

            Item[] dropContainer;
            CircleObject circObj;
            if (oClass.equals("Enemy")) { // Enemy
                int level = object.getJSONObject(4).getInt("level");
                int hp = object.getJSONObject(5).getInt("hp");
                int dmg = object.getJSONObject(6).getInt("dmg");
                int resist = object.getJSONObject(7).getInt("resist");

                dropContainer = addContainer(object.getJSONObject(8).getJSONArray("container"));

                Enemy enemy = new Enemy(name, description, image, hp, dmg, resist);
                enemy.setContainer(dropContainer);
                enemy.setLvl(level);

                circObj = new CircleObject(centerX, centerY, enemy);

            } else { // Chest
                dropContainer = addContainer(object.getJSONObject(4).getJSONArray("container"));
                Chest chest = new Chest(name, description, image);
                chest.setContainer(dropContainer);

                circObj = new CircleObject(centerX, centerY, chest);
            }
            objectsArr.add(circObj);
            LOGGER.fine("CircleObject" + i + " loaded into objectsArr.");
        }
    }

    /**
     * Load given container data, according to them create items and add them to
     * the item container.
     *
     * @param container JSONArray storing all saved data enemy/chest drop container
     * @return Item array of items stored in enemy/chest drop container
     */
    private Item[] addContainer(JSONArray container) {
        JSONArray item;
        Item[] dropContainer = new Item[3];
        int i = 0;
        for (int j = 0; j < 3; j++) {
            try {
                item = container.getJSONObject(i).getJSONArray("Item" + j);
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

    public ArrayList<Tile> getTilesArr() {
        return tilesArr;
    }

    public ArrayList<CircleObject> getObjectsArr() {
        return objectsArr;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

}
