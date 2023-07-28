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
 * Structure for terrain objects. Each created Terrain is defined by unique name,
 * short description and image.
 *
 * Terrain objects are used to fill Map tiles with certain image.
 *
 * @author Miroslav Falcmann
 */
public class Terrain extends BaseObject {

    public Terrain(String name, String description, String imageFilename) {
        this.name = name;
        this.description = description;
        this.imageFilename = imageFilename;
        img = new Image(imageFilename);
        imgPat = new ImagePattern(img);
    }
    
    @Override
    public void registerToListener(ShowSelected listener) {
        listener.showObject(this);
    }

    @Override
    public BaseObject createNew() {
        return new Terrain(name, description, imageFilename);
    }

    @Override
    public void selectObject(MapCreatorWindow mcWindow) {
        hexagon.setStroke(Color.YELLOW);
        hexagon.setStrokeType(StrokeType.INSIDE);
        hexagon.setStrokeWidth(2);
        mcWindow.setSelectedObject(this);
    }

    @Override
    public void selectObject(GameWindow gWindow) {
        hexagon.setStroke(Color.YELLOW);
        hexagon.setStrokeType(StrokeType.INSIDE);
        hexagon.setStrokeWidth(2);
        gWindow.setSelectedObject(this);
    }
}
