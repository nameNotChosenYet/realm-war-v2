package controller;

import model.Kingdom;
import model.Player;
import model.blocks.Block;
import model.grid.Grid;
import model.units.Unit;
import view.GamePanel;
import view.HUDPanel;
import view.StructureInfoDialog;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Kingdom kingdom1;
    private Kingdom kingdom2;
    private HUDPanel hudPanel;
    private List<StructureInfoDialog> structureInfoDialogs = new ArrayList<>();
    private String selectedUnitType;
    private String selectedStructureType;
    private int turnCount = 0;

    private TurnManager turnManager;

    public GameController() {
        initializeGame();
    }

    private void initializeGame() {
        kingdom1 = new Kingdom();
        kingdom2 = new Kingdom();
        player1 = new Player(kingdom1, "Player 1");
        player2 = new Player(kingdom2, "Player 2");

        currentPlayer = player1;
        currentPlayer.setTurn(true);

        turnManager = new TurnManager(this);
        turnManager.startTurnTimer();
    }

    public void endTurn() {
        turnCount++;

        currentPlayer.endTurn();
        clearSelectedStructureType();
        clearSelectedUnitType();

        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
        currentPlayer.startTurn(turnCount);

        for (int row = 0; row < Grid.getRow(); row++) {
            for (int col = 0; col < Grid.getCol(); col++) {
                Block block = Grid.getBlockViews()[row][col].getBlock();
                if (block.hasUnit() && block.getUnit().getOwner().equals(currentPlayer)) {
                    block.getUnit().setCanMove(true);
                }
            }
        }
        for (StructureInfoDialog s : structureInfoDialogs) {
            s.dispose();
        }
        structureInfoDialogs.clear();

        if (hudPanel != null) {
            hudPanel.setCurrentPlayer(currentPlayer.getName());
            hudPanel.updateStats();
            hudPanel.addLogMessage(currentPlayer.getName() + "'s turn started");
        }

        turnManager.restartTurnTimer();
    }

    public void updateHUD() {
        if (hudPanel != null) {
            hudPanel.updateStats();
        }
    }

    public void selectUnitType(String unitType) {
        this.selectedUnitType = unitType;
        this.selectedStructureType = null;
    }

    public void selectStructureType(String structureType) {
        this.selectedStructureType = structureType;
        this.selectedUnitType = null;
    }

    public void clearSelectedUnitType() {
        this.selectedUnitType = null;
    }

    public void clearSelectedStructureType() {
        this.selectedStructureType = null;
    }

    public void addLogMessage(String message) {
        if (hudPanel != null) {
            hudPanel.addLogMessage(message);
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public String getCurrentPlayerName() {
        return currentPlayer.getName();
    }
    public Kingdom getKingdom1() { return player1.getKingdom(); }
    public Kingdom getKingdom2() { return player2.getKingdom(); }
    public String getSelectedUnitType() { return selectedUnitType; }
    public String getSelectedStructureType() { return selectedStructureType; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public void setHudPanel(HUDPanel hudPanel) {
        this.hudPanel = hudPanel;
    }
    public HUDPanel getHudPanel() {
        return hudPanel;
    }

    public List<StructureInfoDialog> getStructureInfoDialogs() {
        return structureInfoDialogs;
    }
}
