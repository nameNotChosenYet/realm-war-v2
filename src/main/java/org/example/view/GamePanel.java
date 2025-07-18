package org.example.view;

import org.example.controller.GameController;
import org.example.model.Player;
import org.example.model.blocks.Block;
import org.example.model.blocks.EmptyBlock;
import org.example.model.blocks.ForestBlock;
import org.example.model.blocks.VoidBlock;
import org.example.model.Grid.Grid;
import org.example.model.structures.*;
import org.example.model.units.*;
import org.example.utils.LogManager;
import org.example.model.Kingdom;

import javax.swing.*;
import java.awt.*;
import java.util.Random;



public class GamePanel extends JPanel {

    private GameController gameController;
    private Unit selectedUnit;
    private Structure selectedStructure;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GamePanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new GridLayout(Grid.getRow(), Grid.getCol(), 0, 0));
        setPreferredSize(new Dimension(640, 640));

        initializeGrid();
        placeTownHalls();
    }

    private void initializeGrid() {
        Random random = new Random();

        for (int row = 0; row < Grid.getRow(); row++) {
            for (int col = 0; col < Grid.getCol(); col++) {
                Block block;

                if (isPlayerZone(row, col)) {
                    block = new EmptyBlock(row, col);

                    if (row <= 1 && col <= 1) {
                        block.setOwner("Player 1");
                    } else if (row >= 8 && col >= 8) {
                        block.setOwner("Player 2");
                    }
                } else {
                    int blockType = random.nextInt(10);
                    if (blockType < 3) {
                        block = new ForestBlock(row, col);
                    } else if (blockType == 9) {
                        block = new VoidBlock(row, col);
                    } else {
                        block = new EmptyBlock(row, col);
                    }
                }
                BlockView blockView = new BlockView(block);
                Grid.getBlockViews()[row][col] = blockView;
                addPlayerZoneBorder(blockView, row, col);
                addClickListener(blockView);
                add(blockView);
            }
        }
    }

    private boolean isPlayerZone(int row, int col) {
        return (row <= 1 && col <= 1) || (row >= 8 && col >= 8);
    }

    private void addPlayerZoneBorder(BlockView blockView, int row, int col) {
        if (row >= 0 && row <= 1 && col >= 0 && col <= 1)
            blockView.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 3),
                    BorderFactory.createLineBorder(new Color(92, 120, 80), 1)
            ));
        else if (row >= 8 && row <= 9 && col >= 8 && col <= 9)
            blockView.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLUE, 3),
                    BorderFactory.createLineBorder(new Color(92, 120, 80), 1)
            ));
        else
            blockView.setBorder(BorderFactory.createLineBorder(new Color(92, 120, 80), 1));
    }

    private void addClickListener(BlockView blockView) {
        blockView.addActionListener(e -> {
            handleBlockClick(blockView.getBlock());
        });
    }

    private void handleBlockClick(Block block) {
        int row = block.getRow();
        int col = block.getCol();
        String currentPlayerName = gameController.getCurrentPlayerName();

        if (gameController.getSelectedUnitType() != null) {
            if (block.isOwned() && block.getOwner().equals(currentPlayerName) && !block.hasUnit() && !block.hasStructure()) {
                placeUnit(row, col, gameController.getSelectedUnitType());
            } else {
                gameController.addLogMessage("Cannot place unit here!");
            }
            return;
        }
        if (gameController.getSelectedStructureType() != null) {
            if (block.isBuildable() && block.isOwned() && block.getOwner().equals(currentPlayerName) && !block.hasStructure() && !block.hasUnit()) {
                placeStructure(row, col, gameController.getSelectedStructureType());
            } else {
                gameController.addLogMessage("Cannot build structure here!");
            }
            return;
        }

        if (Grid.getBlockViews()[row][col].isHighlighted()) {
            Unit unitOnSelected = Grid.getBlockViews()[selectedRow][selectedCol].getBlock().getUnit();
            if (unitOnSelected != null && unitOnSelected.getCanMove()) {
                moveUnit(selectedRow, selectedCol, row, col);
                unitOnSelected.setCanMove(false);
                clearSelectionHighlights();
                repaint();
                return;
            }
        }

        gameController.selectBlock(block);

        if (row == selectedRow && col == selectedCol) {
            clearSelectionHighlights();
            selectedRow = -1;
            selectedCol = -1;
            selectedUnit = null;
            repaint();
            return;
        }

        clearSelectionHighlights();

        if (block.hasUnit() && block.getUnit().getOwner().getName().equals(currentPlayerName) && block.getUnit().getCanMove()) {
            selectedUnit = block.getUnit();
            selectedRow = row;
            selectedCol = col;
            Grid.getBlockViews()[row][col].setSelected(true);
            highlightMovementRange();
        } else if (block.hasStructure() && block.getStructure().getOwner().getName().equals(currentPlayerName)) {
            selectedRow = row;
            selectedCol = col;
            Grid.getBlockViews()[row][col].setSelected(true);
        }
        repaint();
    }

    private void moveUnit(int fromRow, int fromCol, int toRow, int toCol) {
        Block fromBlock = Grid.getBlockViews()[fromRow][fromCol].getBlock();
        Block toBlock = Grid.getBlockViews()[toRow][toCol].getBlock();
        Unit unit = fromBlock.getUnit();
        boolean targetDestroyed = false;
        if (isUnderTowerProtection(toRow, toCol, gameController.getCurrentPlayer())) {
            gameController.addLogMessage("Target is protected by a Tower. Destroy the tower first!");
            Grid.getBlockViews()[fromRow][fromCol].updateDisplay();
            Grid.getBlockViews()[toRow][toCol].updateDisplay();
            gameController.updateHUD();
            clearSelectionHighlights();
            repaint();
            return;
        }
        if (toBlock.hasStructure() && !toBlock.getStructure().getOwner().getName().equals(gameController.getCurrentPlayerName())) {
            Structure targetStructure = toBlock.getStructure();
            unit.structAttack(targetStructure);
            org.example.utils.LogManager.logAttack(gameController.getCurrentPlayerName());
            gameController.addLogMessage(unit.getClass().getSimpleName() + " attacked " + targetStructure.getClass().getSimpleName() + " at (" + toRow + "," + toCol + ")");
            Grid.getBlockViews()[toRow][toCol].updateDisplay();

            if (targetStructure.getDurability() <= 0) {
                gameController.addLogMessage(targetStructure.getClass().getSimpleName() + " at (" + toRow + "," + toCol + ") was destroyed!");
                gameController.removeStructureFromGame(targetStructure, toBlock);
                gameController.getCurrentPlayer().addScore(3);
                targetDestroyed = true;
                if (targetStructure instanceof TownHall) {
                    gameController.endGame(gameController.getCurrentPlayer(), targetStructure.getOwner());
                }
            }
        } else if (toBlock.hasUnit() && !toBlock.getUnit().getOwner().getName().equals(gameController.getCurrentPlayerName())) {
            Unit enemyUnit = toBlock.getUnit();
            unit.attack(enemyUnit);
            org.example.utils.LogManager.logAttack(gameController.getCurrentPlayerName());
            gameController.addLogMessage(unit.getClass().getSimpleName() + " attacked " + enemyUnit.getClass().getSimpleName() + " at (" + toRow + "," + toCol + ")");
            Grid.getBlockViews()[toRow][toCol].updateDisplay();

            if (enemyUnit.getHitPoint() <= 0) {
                gameController.addLogMessage(enemyUnit.getClass().getSimpleName() + " at (" + toRow + "," + toCol + ") was defeated!");
                gameController.removeUnitFromGame(enemyUnit, toBlock);
                gameController.getCurrentPlayer().addScore(1);
                targetDestroyed = true;
            }
        }
        if (targetDestroyed || (!toBlock.hasUnit() && !toBlock.hasStructure())) {
            boolean fromForest = fromBlock instanceof ForestBlock && ((ForestBlock) fromBlock).hasForest();
            boolean toForest = toBlock instanceof ForestBlock && ((ForestBlock) toBlock).hasForest();
            if (fromForest && !toForest)
                unit.leaveForest();
            else if (!fromForest && toForest)
                unit.enterForest();
            fromBlock.setUnit(null);
            toBlock.setUnit(unit);
            takeOwnershipOfBlock(toBlock, toRow, toCol);
            gameController.addLogMessage(unit.getClass().getSimpleName() + " moved to (" + toRow + "," + toCol + ")");
        } else {

            gameController.addLogMessage(unit.getClass().getSimpleName() + " remained at (" + fromRow + "," + fromCol + ")");
        }

        Grid.getBlockViews()[fromRow][fromCol].updateDisplay();
        Grid.getBlockViews()[toRow][toCol].updateDisplay();
        gameController.updateHUD();
        clearSelectionHighlights();
        repaint();
    }

    private void takeOwnershipOfBlock(Block block, int row, int col) {
        block.setOwner(gameController.getCurrentPlayerName());
        if (gameController.getCurrentPlayer().getName().equals("Player 1")) {
            Grid.getBlockViews()[row][col].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 3),
                    BorderFactory.createLineBorder(new Color(92, 120, 80), 1)
            ));
        } else {
            Grid.getBlockViews()[row][col].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLUE, 3),
                    BorderFactory.createLineBorder(new Color(92, 120, 80), 1)
            ));
        }

    }

    private boolean isUnderTowerProtection(int row, int col, Player attacker) {
        for (int r = 0; r < Grid.getRow(); r++) {
            for (int c = 0; c < Grid.getCol(); c++) {
                if (r == row && c == col) continue;
                Block b = Grid.getBlockViews()[r][c].getBlock();
                if (b.hasStructure() && b.getStructure() instanceof Tower) {
                    Tower tower = (Tower) b.getStructure();
                    if (!tower.getOwner().getName().equals(attacker.getName())) {
                        int dr = Math.abs(r - row);
                        int dc = Math.abs(c - col);
                        if (dr <= tower.getDefendRange() && dc <= tower.getDefendRange()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public void clearSelectionHighlights() {
        for (int row = 0; row < Grid.getRow(); row++) {
            for (int col = 0; col < Grid.getCol(); col++) {
                Grid.getBlockViews()[row][col].setSelected(false);
                Grid.getBlockViews()[row][col].setHighlighted(false);
            }
        }
    }

    private Unit createUnit(String unitType, Player owner) {
        switch (unitType) {
            case "Knight":
                Knight newKnight = new Knight();
                newKnight.setOwner(owner);
                return newKnight;
            case "Peasant":
                Peasant newPeasant = new Peasant();
                newPeasant.setOwner(owner);
                return newPeasant;

            case "SpearMan":
                SpearMan newSpearMan = new SpearMan();
                newSpearMan.setOwner(owner);
                return newSpearMan;
            case "SwordMan":
                SwordMan newSwordMan = new SwordMan();
                newSwordMan.setOwner(owner);
                return newSwordMan;
            default:
                return null;
        }
    }

    private Structure createStructure(String structureType, Player owner) {
        switch (structureType) {
            case "Barrack":
                Barrack newBarrack = new Barrack();
                newBarrack.setOwner(owner);
                return newBarrack;
            case "Farm":
                Farm newFarm = new Farm();
                newFarm.setOwner(owner);
                return newFarm;
            case "Market":
                Market newMarket = new Market();
                newMarket.setOwner(owner);
                return newMarket;
            case "Tower":
                Tower newTower = new Tower();
                newTower.setOwner(owner);
                return newTower;
            case "townhall":
                TownHall newTownhall = new TownHall();
                newTownhall.setOwner(owner);
                return newTownhall;
            default:
                return null;
        }
    }

    private void highlightMovementRange() {
        Player currentPlayer = gameController.getCurrentPlayer();
        for (int row = 0; row < Grid.getRow(); row++) {
            for (int col = 0; col < Grid.getCol(); col++) {
                Block block = Grid.getBlockViews()[row][col].getBlock();
                if (block.isOwned() && block.getOwner().equals(currentPlayer.getName())) {
                    int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
                    for (int[] d : dirs) {
                        int newRow = row + d[0];
                        int newCol = col + d[1];
                        if (newRow >= 0 && newRow < Grid.getRow() && newCol >= 0 && newCol < Grid.getCol()) {
                            Block neighbor = Grid.getBlockViews()[newRow][newCol].getBlock();
                            if (((neighbor.isOwned() && !neighbor.getOwner().equals(currentPlayer.getName())) || (!neighbor.isOwned()))
                                    && !(neighbor instanceof VoidBlock) && canTakeEnemyBlock(newRow, newCol, row, col)) {
                                Grid.getBlockViews()[newRow][newCol].setHighlighted(true);
                                Grid.getBlockViews()[newRow][newCol].repaint();
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeUnit(int row, int col, String unitType) {
        Player currentPlayer = gameController.getCurrentPlayer();
        Unit unit = createUnit(unitType, currentPlayer);
        if (gameController.getCurrentPlayer().getKingdom().canCreateUnit(unit)) {
            Block block = Grid.getBlockViews()[row][col].getBlock();
            block.setUnit(unit);
            if (block instanceof ForestBlock && ((ForestBlock) block).hasForest()) {
                unit.enterForest();
            }
            Grid.getBlockViews()[row][col].updateDisplay();
            gameController.addLogMessage(currentPlayer.getName() + " created " + unitType + " at (" + row + "," + col + ")");
            gameController.getCurrentPlayer().getKingdom().createUnit(unit);
            gameController.getHudPanel().updateStats();
            gameController.clearSelectedUnitType();
        } else
            gameController.addLogMessage("Not enough resources to create " + unitType);
    }

    private void placeStructure(int row, int col, String structureType) {
        Player currentPlayer = gameController.getCurrentPlayer();
        Structure structure = createStructure(structureType, currentPlayer);
        Kingdom kingdom = gameController.getCurrentPlayer().getKingdom();
        if (kingdom.canBuildStructure(structure)) {
            Block block = Grid.getBlockViews()[row][col].getBlock();
            if (block instanceof ForestBlock && ((ForestBlock) block).hasForest()) {
                ((ForestBlock) block).cutForest();
            }
            block.setStructure(structure);
            Grid.getBlockViews()[row][col].updateDisplay();
            gameController.addLogMessage(currentPlayer.getName() + " built " + structureType + " at (" + row + "," + col + ")");
            kingdom.buildStructure(structure);
            gameController.getHudPanel().updateStats();
            gameController.clearSelectedStructureType();
        } else {
            if (kingdom.isStructureLimitReached(structure))
                gameController.addLogMessage("Maximum number of " + structureType + " reached");
            else
                gameController.addLogMessage("Not enough gold to build " + structureType);
        }
    }

    private void placeTownHalls() {

        Player player1 = gameController.getPlayer1();
        Player player2 = gameController.getPlayer2();
        Structure townHall1 = createStructure("townhall", player1);
        Structure townHall2 = createStructure("townhall", player2);

        Block block1 = Grid.getBlockViews()[0][0].getBlock();
        Block block2 = Grid.getBlockViews()[9][9].getBlock();
        block1.setStructure(townHall1);
        block2.setStructure(townHall2);
        Grid.getBlockViews()[0][0].updateDisplay();
        Grid.getBlockViews()[9][9].updateDisplay();


    }

    private boolean canTakeEnemyBlock(int enemyRow, int enemyCol, int row, int col) {
        Block enemyBlock = Grid.getBlockViews()[enemyRow][enemyCol].getBlock();
        if (enemyBlock.hasUnit()) {
            Unit enemyUnit = enemyBlock.getUnit();
            Unit myUnit = Grid.getBlockViews()[row][col].getBlock().getUnit();
            if (myUnit == null) return false;
            return enemyUnit.getAttackPower() <= myUnit.getAttackPower();
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
