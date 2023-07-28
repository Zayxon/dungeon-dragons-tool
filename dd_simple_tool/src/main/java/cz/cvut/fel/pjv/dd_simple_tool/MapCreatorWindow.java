package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.view.MapPanel;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * BorderPane with all the necessary panels for creating various maps.
 *
 * @author Miroslav Falcmann
 */
public class MapCreatorWindow {

    private Scene scene;
    private final Stage stage;
    private boolean hasBeenOpened;
    private final BorderPane borderP;

    private final RightPanelMapCreator rightP;
    private final BottomPanelMapCreator bottomP;
    private final MapPanel mp;
    private BaseObject selectedObject;
    private BaseObject showedObject;

    private final FXMLLoader borderPaneLoader;

    public MapCreatorWindow() throws IOException {

        // initializing objects from FXML file
        this.borderPaneLoader = new FXMLLoader(getClass().getResource("game_interface_raw.fxml"));
        this.hasBeenOpened = false;
        borderP = borderPaneLoader.load();

        // set necessary values to BottomPanel
        rightP = new RightPanelMapCreator();
        bottomP = new BottomPanelMapCreator(this.getSelectedObject());
        rightP.addListener(bottomP.getSelectedFromListPanelMapCreator());
        bottomP.getChestPanelMapCreator().setObjectsList(rightP.getObjectsList());
        bottomP.getChestPanelMapCreator().setMcWindow(this);
        bottomP.getEnemyPanelMapCreator().setObjectsList(rightP.getObjectsList());
        bottomP.getEnemyPanelMapCreator().setMcWindow(this);

        // set necessary values to MapPanel
        mp = new MapPanel(this);
        mp.setListView(rightP.getObjectsList());
        mp.setChestPanelListener(bottomP.getChestPanelMapCreator());
        mp.setEnemyPanelListener(bottomP.getEnemyPanelMapCreator());
        mp.setTilePanelListener(bottomP.getTilePanelMapCreator());

        bottomP.getSaveLoadExitPanelMapCreator().getSaveAsWindow().getSaveMap().setMcWindow(this);

        // set necessary values to BottomPanel
        bottomP.getTilePanelMapCreator().setMcWindow(this);
        bottomP.getTilePanelMapCreator().setTilesArr(mp.getTilesArr());
        bottomP.getSaveLoadExitPanelMapCreator().getLoadMapWindow().setMcWindow(this);

        // set up the BorderPane
        borderP.setCenter(mp.getAnchorPane());
        borderP.setRight(rightP.getVBox());
        borderP.setBottom(bottomP.gethBox());

        stage = new Stage();
    }

    /**
     * Initialize the stage
     */
    private void stageInit() {
        scene = new Scene(borderP);
        stage.setTitle("Map Creator");
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Open the window to show the user loaded games and let him choose which he
     * wants to load. (On first function call, stage is initialized)
     */
    public void openInputWindow() {
        if (!hasBeenOpened) {
            stageInit();
            hasBeenOpened = true;
        }
        stage.show();
    }

    public void setShowedObject(BaseObject showedObject) {
        this.showedObject = showedObject;
    }

    public BaseObject getShowedObject() {
        return showedObject;
    }

    public BottomPanelMapCreator getBottomP() {
        return bottomP;
    }

    public BorderPane getBorderP() {
        return borderP;
    }

    public Scene getScene() {
        return scene;
    }

    public void setSelectedObject(BaseObject selectedObject) {
        this.selectedObject = selectedObject;
    }

    public BaseObject getSelectedObject() {
        return selectedObject;
    }

    public RightPanelMapCreator getRightP() {
        return rightP;
    }

    public MapPanel getMp() {
        return mp;
    }
}
