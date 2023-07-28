package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.BaseObject;
import cz.cvut.fel.pjv.dd_simple_tool.GameWindow;
import cz.cvut.fel.pjv.dd_simple_tool.MapCreatorWindow;
import cz.cvut.fel.pjv.dd_simple_tool.ShowSelected;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.StrokeType;

/**
 * Structure for creating item objects. Items are defined by unique name, image,
 * tag and sell value.
 * 
 * Each item can be sold.
 *
 * @author Miroslav Falcmann
 */
public class Item extends BaseObject {

    protected int sellValue;
    protected ItemTags tag;

    public Item(String name, String imageFilename, ItemTags tag, int sellValue) {
        this.tag = tag;
        this.imageFilename = imageFilename;
        img = new Image(imageFilename);
        imgPat = new ImagePattern(img);
        this.name = name;
        this.sellValue = sellValue;
        description = "Sell value: " + sellValue + " gold\n";
    }

    /**
     * Method can be invoked, however items of class Item cannot be used, so
     * method is empty.
     *
     * @param player Player who will obtain some bonuses from the used item.
     */
    public void use(Player player) {
    }

    /**
     * Given string is added to the old description.
     *
     * @param newDescription String value of new description to add.
     */
    public void adjustDescription(String newDescription) {
        description = newDescription + description;
    }

    public ItemTags getTag() {
        return tag;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getSellValue() {
        return sellValue;
    }

    @Override
    public ImagePattern getImgPat() {
        return imgPat;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void registerToListener(ShowSelected listener) {
        listener.showObject(this);
    }

    @Override
    public BaseObject createNew() {
        return new Item(name, imageFilename, tag, sellValue);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void selectObject(MapCreatorWindow mcWindow) {
        rectangle.setStroke(Color.YELLOW);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(2);
        mcWindow.setSelectedObject(this);
    }

    @Override
    public void selectObject(GameWindow gWindow) {
        rectangle.setStroke(Color.YELLOW);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(2);
        gWindow.setSelectedObject(this);
    }

    @Override
    public String getImageFilename() {
        return imageFilename;
    }

}
