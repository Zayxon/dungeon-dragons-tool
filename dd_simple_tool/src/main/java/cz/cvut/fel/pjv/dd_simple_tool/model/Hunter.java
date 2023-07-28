package cz.cvut.fel.pjv.dd_simple_tool.model;

import cz.cvut.fel.pjv.dd_simple_tool.GameWindow;
import cz.cvut.fel.pjv.dd_simple_tool.ShowSelected;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.StrokeType;

/**
 * Hunter object is a creatable Player object with some predefined stats and values,
 * which makes him unique.
 *
 * @author Miroslav Falcmann
 */
public class Hunter extends Player {

    public Hunter(String name) {
        super(name);
        //need to be balanced
        baseDmg = 15;
        resist = 10;
        baseMaxHp = 100;
        maxEnergy = 100;
        maxHp = baseMaxHp;
        hp = maxHp;
        energy = maxEnergy;

        className = "Hunter";
        classIconFilename = "hunter_icon_wow.png";
        imgPat = new ImagePattern(new Image(classIconFilename));
        // must be called after initialization of image pattern
        circleInit();
    }

    @Override
    public void selectObject(GameWindow gWindow) {
        circle.setStroke(Color.YELLOW);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(2);
        gWindow.setSelectedPlayer(this);
    }

    @Override
    public void registerToListener(ShowSelected listener) {
        listener.showObject(this);
    }
}
