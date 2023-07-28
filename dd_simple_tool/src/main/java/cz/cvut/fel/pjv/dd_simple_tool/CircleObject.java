package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * Class for creating JavaFX Circle object showing an enemy, a chest or a player
 * added to map. Circles are added on certain tiles within the map. By left
 * clicking the circle, object showed by the circle is selected and is shown in
 * special panel within the Game window or MapCreator window for further use.
 *
 * @author Miroslav Falcmann
 */
public class CircleObject {

    private Circle circ;
    private ImagePattern imgPat;
    private BaseObject baseObj;
    private final double centerX;
    private final double centerY;
    private final List<ShowSelected> listeners;
    private Player player;

    // constructor for creating CircleObject in MapCreator window
    public CircleObject(double centerX, double centerY, BaseObject baseObj) {
        this.listeners = new ArrayList<>();
        this.baseObj = baseObj;
        this.centerX = centerX;
        this.centerY = centerY;
        if (baseObj != null) {
            imgPat = baseObj.getImgPat();
            createCircle(centerX, centerY);
        }
    }

    // constructor for creating CircleObject in Game window
    public CircleObject(double centerX, double centerY, Player player) {
        this.listeners = new ArrayList<>();
        this.player = player;
        this.centerX = centerX;
        this.centerY = centerY;
        if (player != null) {
            imgPat = player.getImgPat();
            createCircle(centerX, centerY);
        }
    }

    /**
     * Creates a circle object and initialize it
     *
     * @param centerX X coordinate of the circle center
     * @param centerY Y coordinate of the circle center
     */
    private void createCircle(double centerX, double centerY) {
        circ = new Circle();
        circ.setCenterX(centerX);
        circ.setCenterY(centerY);
        circ.setStroke(Color.BLACK);
        circ.setStrokeWidth(0.75);
        circ.setRadius(25);
        circ.setFill(imgPat);
    }

    public Circle getCirc() {
        return circ;
    }

    public List<ShowSelected> getListeners() {
        return listeners;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public BaseObject getBaseObj() {
        return baseObj;
    }

    public Player getPlayer() {
        return player;
    }

}
