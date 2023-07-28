package cz.cvut.fel.pjv.dd_simple_tool;

import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * HBox object filled with all the necessary panels (Tile panel or Enemy panel,
 * Panel for selected object from ListView) for creating the map.
 *
 */
public class BottomPanelMapCreator {

    private final HBox hBox;
    private AnchorPane setObjectPanel;
    private AnchorPane selectedFromListPanel;
    private final AnchorPane saveLoadExitPanel;
    private final SelectedFromListPanelMapCreator selectedFromListPanelMapCreator;
    private final EnemyPanelMapCreator enemyPanelMapCreator;
    private final TilePanelMapCreator tilePanelMapCreator;
    private final SaveLoadExitPanelMapCreator saveLoadExitPanelMapCreator;
    private final ChestPanelMapCreator chestPanelMapCreator;

    public BottomPanelMapCreator(BaseObject selectedObject) throws IOException {

        // creating parts of bottom panel
        hBox = new HBox();
        chestPanelMapCreator = new ChestPanelMapCreator(hBox);
        saveLoadExitPanelMapCreator = new SaveLoadExitPanelMapCreator();
        saveLoadExitPanel = saveLoadExitPanelMapCreator.getAnchorPane();
        selectedFromListPanelMapCreator = new SelectedFromListPanelMapCreator(hBox);
        tilePanelMapCreator = new TilePanelMapCreator(hBox);
        enemyPanelMapCreator = new EnemyPanelMapCreator(hBox);

        setObjectPanel = new AnchorPane();
        setObjectPanel.setMaxSize(575, 160);
        setObjectPanel.setMinSize(575, 160);

        selectedFromListPanel = new AnchorPane();
        selectedFromListPanel.setMaxSize(450, 160);
        selectedFromListPanel.setMinSize(450, 160);

        hBox.getChildren().addAll(setObjectPanel, selectedFromListPanel, saveLoadExitPanel);
    }

    public TilePanelMapCreator getTilePanelMapCreator() {
        return tilePanelMapCreator;
    }

    public ChestPanelMapCreator getChestPanelMapCreator() {
        return chestPanelMapCreator;
    }

    public void setSelectedFromListDefault(AnchorPane selectedFromListDefault) {
        this.selectedFromListPanel = selectedFromListDefault;
    }

    public void setSetObjectPanel(AnchorPane setObjectPanel) {
        this.setObjectPanel = setObjectPanel;
    }

    public SelectedFromListPanelMapCreator getSelectedFromListPanelMapCreator() {
        return selectedFromListPanelMapCreator;
    }

    public EnemyPanelMapCreator getEnemyPanelMapCreator() {
        return enemyPanelMapCreator;
    }

    public HBox gethBox() {
        return hBox;
    }

    public SaveLoadExitPanelMapCreator getSaveLoadExitPanelMapCreator() {
        return saveLoadExitPanelMapCreator;
    }

}
