package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Terrain;
import javafx.scene.shape.Polygon;

/**
 * Tile object representing a tile in the map. Has unique coordinates and can be
 * filled with various terrain.
 *
 * @author Miroslav Falcmann
 */
public class Tile {

    boolean objectStored;
    private Polygon hexagon;
    private double centerX;
    private double centerY;
    private double layoutX;
    private double layoutY;
    private Terrain terrain;

    public Tile(Polygon hexagon, Terrain terrain) {
        this.hexagon = hexagon;
        this.terrain = terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public BaseObject getTerrain() {
        return terrain;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getCenterY() {
        return centerY;
    }

    public Polygon getHexagon() {
        return hexagon;
    }

    public void setHexagon(Polygon hexagon) {
        this.hexagon = hexagon;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

}
