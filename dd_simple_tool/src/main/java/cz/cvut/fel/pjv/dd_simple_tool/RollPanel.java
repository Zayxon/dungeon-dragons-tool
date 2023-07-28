package cz.cvut.fel.pjv.dd_simple_tool;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * AnchorPane showing a picture of 20-sided dice and a Label with last rolled
 * number.
 *
 * @author Miroslav Falcmann
 */
public class RollPanel {

    private final FXMLLoader rollPanelLoader;
    private final AnchorPane ap;
    private final Polygon hexagon;
    private final Label rolledNumLabel;
    private GameWindow gWindow;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public RollPanel() throws IOException {

        // initializing objects from FXML file
        rollPanelLoader = new FXMLLoader(getClass().getResource("roll_panel.fxml"));
        ap = rollPanelLoader.load();

        hexagon = (Polygon) ap.getChildren().get(0);
        rolledNumLabel = (Label) ap.getChildren().get(1);
        createHexagon(80, -10, 0);
        addHexagonAndLabelEvent();

    }

    /**
     * Create hexagon (Polygon) with proper size and layout
     *
     * @param size Integer number defining the size of the hexagon
     * @param layoutX X coordinate of the hexagon layout
     * @param layoutY Y coordinate of the hexagon layout
     */
    private void createHexagon(double size, double layoutX, double layoutY) {
        double w = Math.sqrt(3) * size;
        double h = 2 * size;
        hexagon.getPoints().setAll(new Double[]{
            w, 0.0,
            1.5 * w, 0.25 * h,
            1.5 * w, 0.75 * h,
            w, h,
            0.5 * w, 0.75 * h,
            0.5 * w, 0.25 * h,});
        hexagon.setStroke(Color.BLACK);
        hexagon.setStrokeWidth(0.75);
        hexagon.setLayoutX(layoutX);
        hexagon.setLayoutY(layoutY);

        // filling the hexagon with 20-sided dice image
        Image image = new Image("roll_img2.png");
        ImagePattern imgPat = new ImagePattern(image);
        hexagon.setFill(imgPat);
    }

    /**
     * Add mouse click event on Polygon (hexagon) and Label showing rolled
     * number
     */
    private void addHexagonAndLabelEvent() {
        EventHandler<MouseEvent> rollDice = (MouseEvent e) -> {
            roll();
        };
        hexagon.addEventFilter(MouseEvent.MOUSE_CLICKED, rollDice);
        rolledNumLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, rollDice);
    }

    /**
     * Roll a number and show it in text Label
     */
    private void roll() {
        int rolledNum = getRandomNumber(1, 20);
        gWindow.setRolledNumber(rolledNum);
        rolledNumLabel.setText("" + rolledNum);
        LOGGER.info("Number " + rolledNum + " has been rolled.");
    }

    /**
     * Get random number between the given "min" integer value and the given
     * "max" integer value
     *
     * @param min Minimal integer number, which can be returned from the
     * function
     * @param max Maximal integer number, which can be returned from the
     * function
     * @return Random integer number between "min" and "max".
     */
    private int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public void setgWindow(GameWindow gWindow) {
        this.gWindow = gWindow;
    }

    public AnchorPane getAnchorPane() {
        return ap;
    }

}
