package org.example.controller;

import org.example.model.Kingdom;
import org.example.model.Player;
import org.example.model.blocks.Block;
import org.example.model.Grid.Grid;
import org.example.view.GameFrame;
import org.example.view.HUDPanel;
import org.example.view.StructureInfoDialog;
import org.example.model.structures.Structure;
import org.example.model.units.Unit;
import org.example.model.structures.Barrack;
import org.example.model.blocks.VoidBlock;
import org.example.model.units.SpearMan;
import org.example.model.units.SwordMan;
import org.example.model.units.Knight;
import org.example.view.GameResultDialog;
import org.example.database.DatabaseManager;
import org.example.utils.LogManager;
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
        generateBarrackUnits(currentPlayer);
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
            int maxDurability = unit.getMaxHitPoint();
            int level = 1;
            boolean canUpgrade = unit.canUpgrade();
            int upgradeCost = unit.getUpgradeCost();
            String iconPath = getIconPathFor(unitName);

            gameFrame.updateEntityInfo(unitName, iconPath, owner, durability, maxDurability, level, canUpgrade, upgradeCost, false);

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

            boolean canTrain = false;
            if (structure instanceof Barrack) {
                Barrack b = (Barrack) structure;
                canTrain = currentPlayer.equals(b.getOwner()) && b.canTrain();
            }
            gameFrame.updateEntityInfo(structureName, iconPath, owner, durability, maxDurability, level, canUpgrade, upgradeCost, canTrain);

        } else {
            this.selectedEntityOnMap = null;
            this.selectedEntityBlock = null;
            gameFrame.clearEntityInfo();
        }
    }

    public void removeUnitFromGame(Unit unitToRemove, Block blockContainingUnit) {
        if (unitToRemove == null || blockContainingUnit == null) return;

        if (unitToRemove.getOwner() != null) {
            unitToRemove.getOwner().getKingdom().removeUnit(unitToRemove);
        }
        blockContainingUnit.setUnit(null);
        Grid.getBlockViews()[blockContainingUnit.getRow()][blockContainingUnit.getCol()].updateDisplay();
        updateHUD();
    }

    public void removeStructureFromGame(Structure structureToRemove, Block blockContainingStructure) {
        if (structureToRemove == null || blockContainingStructure == null) return;


        if (structureToRemove.getOwner() != null) {
            structureToRemove.getOwner().getKingdom().removeStructure(structureToRemove);
        }

        blockContainingStructure.setStructure(null);

        Grid.getBlockViews()[blockContainingStructure.getRow()][blockContainingStructure.getCol()].updateDisplay();
        updateHUD();
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
                Grid.getBlockViews()[selectedEntityBlock.getRow()][selectedEntityBlock.getCol()].updateDisplay();

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
                Grid.getBlockViews()[selectedEntityBlock.getRow()][selectedEntityBlock.getCol()].updateDisplay();

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
        resetBarrackTraining(currentPlayer);

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

    private void generateBarrackUnits(Player player) {
        Kingdom kingdom = player.getKingdom();
        for (Structure structure : kingdom.getStructures()) {
            if (structure instanceof Barrack) {
                Barrack barrack = (Barrack) structure;
                if (!barrack.canTrain()) continue;
                int[] loc = findStructureLocation(barrack);
                if (loc == null) continue;

                Unit unit;
                switch (barrack.getLevel()) {
                    case 1:
                        unit = new SpearMan();
                        break;
                    case 2:
                        unit = new SwordMan();
                        break;
                    default:
                        unit = new Knight();
                        break;
                }

                unit.setOwner(player);

                int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
                boolean placed = false;
                for (int[] d : dirs) {
                    int r = loc[0] + d[0];
                    int c = loc[1] + d[1];
                    if (r >= 0 && r < Grid.getRow() && c >= 0 && c < Grid.getCol()) {
                        Block b = Grid.getBlockViews()[r][c].getBlock();
                        if (!(b instanceof VoidBlock) && b.isOwned() &&
                                player.getName().equals(b.getOwner()) &&
                                !b.hasUnit() && !b.hasStructure()) {
                            b.setUnit(unit);
                            kingdom.addUnit(unit);
                            Grid.getBlockViews()[r][c].updateDisplay();
                            addLogMessage(player.getName() + " received a free " +
                                    unit.getClass().getSimpleName() + " from Barrack at (" +
                                    loc[0] + "," + loc[1] + ")");
                            placed = true;
                            barrack.markTrained();
                            break;
                        }
                    }
                }
                if (!placed) {
                    addLogMessage("No space near Barrack at (" + loc[0] + "," + loc[1] + ") to place free unit");
                }
            }
        }
        updateHUD();
    }

    private int[] findStructureLocation(Structure structure) {
        for (int r = 0; r < Grid.getRow(); r++) {
            for (int c = 0; c < Grid.getCol(); c++) {
                Block b = Grid.getBlockViews()[r][c].getBlock();
                if (b.getStructure() == structure) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }

    public void trainUnitFromSelectedBarrack() {
        if (!(selectedEntityOnMap instanceof Barrack) || selectedEntityBlock == null) {
            addLogMessage("Select a Barrack first.");
            return;
        }

        Barrack barrack = (Barrack) selectedEntityOnMap;
        if (!currentPlayer.equals(barrack.getOwner())) {
            addLogMessage("Cannot train units from an enemy Barrack.");
            return;
        }
        if (!barrack.canTrain()) {
            addLogMessage("This Barrack has already trained a unit this turn.");
            return;
        }

        Unit unit;
        switch (barrack.getLevel()) {
            case 1:
                unit = new SpearMan();
                break;
            case 2:
                unit = new SwordMan();
                break;
            default:
                unit = new Knight();
                break;
        }

        Kingdom kingdom = currentPlayer.getKingdom();
        if (!kingdom.canCreateUnit(unit)) {
            addLogMessage("Not enough resources to train " + unit.getClass().getSimpleName());
            return;
        }

        int row = selectedEntityBlock.getRow();
        int col = selectedEntityBlock.getCol();
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
        for (int[] d : dirs) {
            int r = row + d[0];
            int c = col + d[1];
            if (r >= 0 && r < Grid.getRow() && c >= 0 && c < Grid.getCol()) {
                Block b = Grid.getBlockViews()[r][c].getBlock();
                if (!(b instanceof VoidBlock) && b.isOwned() && currentPlayer.getName().equals(b.getOwner()) &&
                        !b.hasUnit() && !b.hasStructure()) {
                    unit.setOwner(currentPlayer);
                    b.setUnit(unit);
                    kingdom.createUnit(unit);
                    Grid.getBlockViews()[r][c].updateDisplay();
                    addLogMessage(currentPlayer.getName() + " trained a " + unit.getClass().getSimpleName() + " at (" + r + "," + c + ")");
                    barrack.markTrained();
                    updateHUD();
                    selectBlock(selectedEntityBlock);
                    return;
                }
            }
        }

        addLogMessage("No space near Barrack to train unit.");
    }

    public boolean canTrainFromSelectedBarrack() {
        if (!(selectedEntityOnMap instanceof Barrack)) return false;
        Barrack b = (Barrack) selectedEntityOnMap;
        return currentPlayer.equals(b.getOwner()) && b.canTrain();
    }

    private void resetBarrackTraining(Player player) {
        for (Structure s : player.getKingdom().getStructures()) {
            if (s instanceof Barrack) {
                ((Barrack) s).resetTrainingFlag();
            }
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
            case"townhall": case "barrack": case "farm": case "market": case "tower":
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

    public void endGame(Player winner, Player loser) {
        if (turnManager != null) {
            turnManager.stopTimers();
        }
        LogManager.writeFinalScores(player1, player2);
        DatabaseManager.saveGameResult(winner.getName(), loser.getName(), winner.getScore(), loser.getScore());
        if (hudPanel != null) {
            hudPanel.addLogMessage("Game Over! " + winner.getName() + " wins.");
        }
        new GameResultDialog(gameFrame, winner.getName(), loser.getName(), winner.getScore(), loser.getScore()).setVisible(true);
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
