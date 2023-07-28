package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Player;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * HBox object filled with all the necessary panels (ListView showing players in
 * the game, Player panel, enemy or chest panel, roll-dice panel) for the game.
 *
 */
public class BottomPanelGame {

    private final HBox hBox;
    private VBox switchPlayers;
    private final AnchorPane setPlayerPanel;
    private final AnchorPane setObjectPanel;
    private AnchorPane selectedObjectPanel;
    private AnchorPane itemPanel;
    private ListView playerList;
    private final BorderPane bp;
    private final PlayerPanel playerPanel;
    private final EnemyPanel enemyPanel;
    private final ChestPanel chestPanel;
    private final SelectedTilePanelGame selectedTilePanel;
    private final RollPanel rollDicePanel;
    private GameWindow gWindow;
    private final FXMLLoader bottomPanelLoader;

    public BottomPanelGame() throws IOException {

        // initializing objects from FXML file
        bottomPanelLoader = new FXMLLoader(getClass().getResource("game_interface_game.fxml"));
        bp = bottomPanelLoader.load();
        hBox = (HBox) bp.getBottom();
        playerList = (ListView) hBox.getChildren().get(0);

        // creating parts of bottom panel
        playerPanel = new PlayerPanel(this.hBox);
        enemyPanel = new EnemyPanel(this.hBox);
        chestPanel = new ChestPanel(this.hBox);
        selectedTilePanel = new SelectedTilePanelGame(this.hBox);
        rollDicePanel = new RollPanel();

        setObjectPanel = new AnchorPane();
        setObjectPanel.setMaxSize(350, 160);
        setObjectPanel.setMinSize(350, 160);

        setPlayerPanel = new AnchorPane();
        setPlayerPanel.setMaxSize(575, 160);
        setPlayerPanel.setMinSize(575, 160);

        hBox.getChildren().addAll(setPlayerPanel, setObjectPanel, rollDicePanel.getAnchorPane());
    }

    /**
     * Add mouse click event to ListView showing players. Selected player is
     * showed in Player Panel.
     */
    public void addPlayerListEvent() {
        EventHandler<MouseEvent> setSelectedPlayer = (MouseEvent e) -> {
            MouseButton button = e.getButton();
            Player player = (Player) playerList.getSelectionModel().getSelectedItem();
            if (button == MouseButton.PRIMARY && (player != null)) {
                player.registerToListener(gWindow.getBottomP().getPlayerPanel());
                if (gWindow.getSelectedPlayer() == null) {
                    player.selectObject(gWindow);
                } else {
                    player.reselectObject(gWindow, player);
                }
                gWindow.setSelectedPlayer(player);
            }
        };
        playerList.addEventFilter(MouseEvent.MOUSE_CLICKED, setSelectedPlayer);
    }

    public void setgWindow(GameWindow gWindow) {
        this.gWindow = gWindow;
    }

    public ListView getPlayerList() {
        return playerList;
    }

    public HBox gethBox() {
        return hBox;
    }

    public void setSelectedObjectPanel(AnchorPane selectedObjectPanel) {
        this.selectedObjectPanel = selectedObjectPanel;
    }

    public VBox getSwitchPlayers() {
        return switchPlayers;
    }

    public AnchorPane getSelectedObjectPanel() {
        return selectedObjectPanel;
    }

    public AnchorPane getItemPanel() {
        return itemPanel;
    }

    public PlayerPanel getPlayerPanel() {
        return playerPanel;
    }

    public EnemyPanel getEnemyPanel() {
        return enemyPanel;
    }

    public SelectedTilePanelGame getSelectedTilePanel() {
        return selectedTilePanel;
    }

    public ChestPanel getChestPanel() {
        return chestPanel;
    }

    public RollPanel getRollDicePanel() {
        return rollDicePanel;
    }

    public void setPlayerList(ListView playerList) {
        this.playerList = playerList;
    }

}
