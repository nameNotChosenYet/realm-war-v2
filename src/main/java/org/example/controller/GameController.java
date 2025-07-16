package controller;

import model.Kingdom;
import model.Player;
import model.blocks.Block;
import model.grid.Grid;
import view.GameFrame;
import view.HUDPanel;
import view.StructureInfoDialog;
import model.structures.Structure;
import model.units.Unit;

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
    private Object selectedEntityOnMap;
    private Block selectedEntityBlock;

    private GameFrame gameFrame;
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

    public void selectBlock(Block block) {
        clearSelectedUnitType();
        clearSelectedStructureType();

        this.selectedEntityBlock = block;

        if (block.hasUnit()) {
            Unit unit = block.getUnit();
            this.selectedEntityOnMap = unit;

            String unitName = unit.getClass().getSimpleName();
            String owner = unit.getOwner().getName();
            int durability = unit.getHitPoint();
            int maxDurability = unit.getHitPoint();
            int level = 1;
            boolean canUpgrade = unit.canUpgrade();
            int upgradeCost = unit.getUpgradeCost();
            String iconPath = getIconPathFor(unitName);

            gameFrame.updateEntityInfo(unitName, iconPath, owner, durability, maxDurability, level, canUpgrade, upgradeCost);

        } else if (block.hasStructure()) {
            Structure structure = block.getStructure();
            this.selectedEntityOnMap = structure;

            String structureName = structure.getClass().getSimpleName();
            String owner = structure.getOwner().getName();
            int durability = structure.getDurability();
            int maxDurability = 40 + (structure.getLevel() * 10);
            int level = structure.getLevel();
            boolean canUpgrade = structure.canLevelUp();
            int upgradeCost = structure.getLevelUpCost();
            String iconPath = getIconPathFor(structureName);

            gameFrame.updateEntityInfo(structureName, iconPath, owner, durability, maxDurability, level, canUpgrade, upgradeCost);

        } else {
            this.selectedEntityOnMap = null;
            this.selectedEntityBlock = null;
            gameFrame.clearEntityInfo();
        }
    }


    public void upgradeSelectedEntity() {
        if (selectedEntityOnMap == null || selectedEntityBlock == null) {
            addLogMessage("No entity selected to upgrade.");
            return;
        }

        if (selectedEntityOnMap instanceof Unit) {
            Unit unit = (Unit) selectedEntityOnMap;
            if (unit.canUpgrade() && currentPlayer.getKingdom().getGold() >= unit.getUpgradeCost()) {
                currentPlayer.getKingdom().setGold(currentPlayer.getKingdom().getGold() - unit.getUpgradeCost());
                Unit nextTierUnit = unit.getNextTierUnit();
                nextTierUnit.setOwner(currentPlayer);
                selectedEntityBlock.setUnit(nextTierUnit);

                addLogMessage(unit.getClass().getSimpleName() + " upgraded to " + nextTierUnit.getClass().getSimpleName());
                selectBlock(selectedEntityBlock);
                updateHUD();
            } else {
                addLogMessage("Not enough gold or max tier.");
            }
        } else if (selectedEntityOnMap instanceof Structure) {
            Structure structure = (Structure) selectedEntityOnMap;
            if (structure.canLevelUp() && currentPlayer.getKingdom().getGold() >= structure.getLevelUpCost()) {
                structure.levelUp();

                addLogMessage(structure.getClass().getSimpleName() + " upgraded to Level " + structure.getLevel());
                selectBlock(selectedEntityBlock);
                updateHUD();
            } else {
                addLogMessage("Not enough gold or max level.");
            }
        }
    }

    public void endTurn() {
        turnCount++;
        currentPlayer.endTurn();
        clearSelectedStructureType();
        clearSelectedUnitType();
        if (gameFrame != null) gameFrame.clearEntityInfo();

        if (currentPlayer == player1) currentPlayer = player2;
        else currentPlayer = player1;

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

        if (turnManager != null) {
            turnManager.restartTurnTimer();
        }
        if (gameFrame != null) {
            gameFrame.onTurnEnded();
        }
    }

    public void updateHUD() {
        if (hudPanel != null) {
            hudPanel.updateStats();
        }
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void selectUnitType(String unitType) {
        this.selectedUnitType = unitType;
        this.selectedStructureType = null;
        if (gameFrame != null) {
            gameFrame.clearEntityInfo();
            String iconPath = getIconPathFor(unitType);
            gameFrame.updateBuildSelectionInfo(unitType, iconPath);
        }
    }

    public void selectStructureType(String structureType) {
        this.selectedStructureType = structureType;
        this.selectedUnitType = null;
        if (gameFrame != null) {
            gameFrame.clearEntityInfo();
            String iconPath = getIconPathFor(structureType);
            gameFrame.updateBuildSelectionInfo(structureType, iconPath);
        }
    }

    public void clearSelectedUnitType() {
        if (this.selectedUnitType != null) {
            this.selectedUnitType = null;
            if (gameFrame != null) gameFrame.clearBuildSelectionInfo();
        }
    }

    public void clearSelectedStructureType() {
        if (this.selectedStructureType != null) {
            this.selectedStructureType = null;
            if (gameFrame != null) gameFrame.clearBuildSelectionInfo();
        }
    }

    private String getIconPathFor(String type) {
        if (type == null) return null;

        String basePath = "/Images/";
        switch (type.toLowerCase()) {
            case "knight": case "peasant": case "spearman": case "swordman":
                return basePath  + type.toLowerCase() + ".png";
            case "barrack": case "farm": case "market": case "tower":
                return basePath  + type.toLowerCase() + ".png";
            default:
                return null;
        }
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
