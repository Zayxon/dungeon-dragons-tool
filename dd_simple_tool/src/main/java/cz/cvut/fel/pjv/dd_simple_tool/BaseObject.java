package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import cz.cvut.fel.pjv.dd_simple_tool.model.Item;
import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Abstract structure for enemy, chest and also item objects. Defines only the
 * most basic functions and values, that can be used by extended objects.
 *
 * @author Miroslav Falcmann
 */
public abstract class BaseObject {

    protected Circle circle;
    protected Polygon hexagon;
    protected Rectangle rectangle;
    protected String name;
    protected ImagePattern imgPat;
    protected String description;
    protected String imageFilename;
    protected Image img;

    /**
     * Function invokes a listener function which is showing the BaseObject on
     * certain panel within the Game or MapCreator window.
     *
     * @param listener listener object that will show BaseObject in certain
     * panel.
     */
    public abstract void registerToListener(ShowSelected listener);

    /**
     * Creates a new instance of class extending BaseObject
     *
     * @return new instance of class extending BaseObject
     */
    public abstract BaseObject createNew();

    /**
     * Selects the object. To let the user know something is selected, stroke of
     * the selected object is yellow instead of black.
     *
     * @param mcWindow pointer to MapCreatorWindow
     */
    public abstract void selectObject(MapCreatorWindow mcWindow);

    /**
     * Selects the object. To let the user know something is selected, stroke of
     * the selected object is yellow instead of black.
     *
     * @param gWindow pointer to GameWindow
     */
    public abstract void selectObject(GameWindow gWindow);

    /**
     * Removes selection of previous objects and selects a new one. To let the
     * user know something is selected, stroke of the selected object is yellow
     * instead of black.
     *
     * @param mcWindow pointer to MapCreatorWindow
     */
    public void reselectObject(MapCreatorWindow mcWindow) {
        if (mcWindow.getSelectedObject() instanceof Enemy || mcWindow.getSelectedObject() instanceof Chest) {
            mcWindow.getSelectedObject().getCircle().setStroke(Color.BLACK);
            mcWindow.getSelectedObject().getCircle().setStrokeWidth(0.75);
        } else if (mcWindow.getSelectedObject() instanceof Terrain) {
            mcWindow.getSelectedObject().getHexagon().setStroke(Color.BLACK);
            mcWindow.getSelectedObject().getHexagon().setStrokeWidth(0.75);
        } else if (mcWindow.getSelectedObject() instanceof Item) {
            mcWindow.getSelectedObject().getRectangle().setStroke(Color.BLACK);
            mcWindow.getSelectedObject().getRectangle().setStrokeWidth(0.75);
        }
        selectObject(mcWindow);
    }

    /**
     * Removes selection of previous objects and selects a new one. To let the
     * user know something is selected, stroke of the selected object is yellow
     * instead of black.
     *
     * @param gWindow pointer to GameWindow
     */
    public void reselectObject(GameWindow gWindow) {
        if (gWindow.getSelectedObject() instanceof Enemy || gWindow.getSelectedObject() instanceof Chest) {
            gWindow.getSelectedObject().getCircle().setStroke(Color.BLACK);
            gWindow.getSelectedObject().getCircle().setStrokeWidth(0.75);
        } else if (gWindow.getSelectedObject() instanceof Terrain) {
            gWindow.getSelectedObject().getHexagon().setStroke(Color.BLACK);
            gWindow.getSelectedObject().getHexagon().setStrokeWidth(0.75);
        } else if (gWindow.getSelectedObject() instanceof Item) {
            gWindow.getSelectedObject().getRectangle().setStroke(Color.BLACK);
            gWindow.getSelectedObject().getRectangle().setStrokeWidth(0.75);
        }
        selectObject(gWindow);
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgPat(ImagePattern imgPat) {
        this.imgPat = imgPat;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public ImagePattern getImgPat() {
        return imgPat;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public String getDescription() {
        return description;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setHexagon(Polygon hexagon) {
        this.hexagon = hexagon;
    }

    public Circle getCircle() {
        return circle;
    }

    public Polygon getHexagon() {
        return hexagon;
    }

}
