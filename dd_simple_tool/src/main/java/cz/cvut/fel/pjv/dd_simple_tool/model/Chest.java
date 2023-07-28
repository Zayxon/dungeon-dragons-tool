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
 * Structure for chest objects. Each created chest is defined by unique name,
 * short description and image.
 *
 * Chest can hold up to 3 items which can be later obtained by players.
 *
 * Chest is shown in map in JavaFX Circle object.
 *
 * @author Miroslav Falcmann
 */
public class Chest extends BaseObject {

    private Item[] container;

    public Chest(String name, String description, String imageFilename) {
        this.name = name;
        this.description = description;
        this.imageFilename = imageFilename;
        img = new Image(imageFilename);
        imgPat = new ImagePattern(img);
        container = new Item[3];
        this.name = name;
    }

    public void setContainer(Item[] container) {
        this.container = container;
    }

    public Item[] getContainer() {
        return container;
    }

    @Override
    public void registerToListener(ShowSelected listener) {
        listener.showObject(this);
    }

    @Override
    public BaseObject createNew() {
        return new Chest(name, description, imageFilename);
    }

    @Override
    public void selectObject(MapCreatorWindow mcWindow) {
        circle.setStroke(Color.YELLOW);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(2);
        mcWindow.setSelectedObject(this);
    }

    @Override
    public void selectObject(GameWindow gWindow) {
        circle.setStroke(Color.YELLOW);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(2);
        gWindow.setSelectedObject(this);
    }

}
