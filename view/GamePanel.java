package view;

import controller.GameController;
import model.Player;
import model.blocks.*;
import model.structures.*;
import model.units.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GamePanel extends JPanel {
    private GameController gameController;
    private BlockView[][] blockViews;
    private Units selectedUnit;
    private Structures selectedStructure;
    private int selectedRow = -1;
    private int selectedCol = -1;

    // Original constructor for backward compatibility
    public static final int ROWS = 10;
    public static final int COLS = 10;

    public GamePanel() {
        setLayout(new GridLayout(ROWS, COLS, 0, 0));
        setPreferredSize(new Dimension(640, 640));
        blockViews = new BlockView[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Block block;
                if (row + col < 7 && row + col > 2)
                    block = new ForestBlock(row, col);
                else if ((row + col) % 7 == 0)
                    block = new VoidBlock(row, col);
                else
                    block = new EmptyBlock(row, col);

                BlockView blockView = new BlockView(block);
                addPlayerZoneBorder(blockView, row, col);
                blockViews[row][col] = blockView;
                add(blockView);
            }
        }
    }

    public GamePanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new GridLayout(ROWS, COLS, 0, 0));
        setPreferredSize(new Dimension(640, 640));
        blockViews = new BlockView[ROWS][COLS];

        initializeGrid();
        placeTownHalls();
    }

    private boolean isPlayerZone(int row, int col) {
        return (row <= 1 && col <= 1) || (row >= 8 && col >= 8);
    }

    private void placeTownHalls() {
        Block block1 = blockViews[0][0].getBlock();
        TownHall townHall1 = new TownHall();
        block1.setStructure(townHall1);
        blockViews[0][0].updateDisplay();

        Block block2 = blockViews[9][9].getBlock();
        TownHall townHall2 = new TownHall();
        block2.setStructure(townHall2);
        blockViews[9][9].updateDisplay();
    }

    private void addClickListener(BlockView blockView, int row, int col) {
        blockView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBlockClick(row, col);
            }
        });
    }


    private void handleBlockClick(int row, int col) {

        Block block = blockViews[row][col].getBlock();
        String currentPlayer = gameController.getCurrentPlayerName();

        clearSelectionHighlights();



//        if (selectedUnit != null) {
//            if (canMoveUnitTo(selectedRow, selectedCol, row, col)) {
//                moveUnit(selectedRow, selectedCol, row, col);
//                selectedUnit = null;
//                selectedRow = -1;
//                selectedCol = -1;
//                return;
//            }
//        }
        selectedUnit = createUnit(gameController.getSelectedUnitType(), gameController.getCurrentPlayer());
        selectedStructure = createStructure(gameController.getSelectedStructureType(), gameController.getCurrentPlayer());

        if (block.hasUnit() && block.getUnit().getOwner().getName().equals(currentPlayer)) {
            selectedUnit = block.getUnit();
            selectedRow = row;
            selectedCol = col;
            blockViews[row][col].setSelected(true);
            highlightMovementRange(row, col, selectedUnit.getMovementRange());

        } else if (block.hasStructure() && block.getStructure().getOwner().getName().equals(currentPlayer)) {
            selectedStructure = block.getStructure();
            selectedRow = row;
            selectedCol = col;
            blockViews[row][col].setSelected(true);

        } else {
            if (gameController.getSelectedUnitType() != null && block.isOwned() &&
                    block.getOwner().equals(currentPlayer) && !block.hasUnit() && !block.hasStructure()) {

                placeUnit(row, col, gameController.getSelectedUnitType());
            } else if (gameController.getSelectedStructureType() != null && block.isBuildable() &&
                    block.getOwner().equals(currentPlayer) && !block.hasStructure()) {
                placeStructure(row, col, gameController.getSelectedStructureType());
            }
        }

        repaint();
    }



    private boolean canMoveUnitTo(int fromRow, int fromCol, int toRow, int toCol) {
        if (selectedUnit == null) return false;

        Block toBlock = blockViews[toRow][toCol].getBlock();

        if (toBlock instanceof VoidBlock) return false;

        if (toBlock.hasUnit() || toBlock.hasStructure()) return false;

        int distance = Math.abs(toRow - fromRow) + Math.abs(toCol - fromCol);
        return distance <= selectedUnit.getMovementRange();
    }


    private void moveUnit(int fromRow, int fromCol, int toRow, int toCol) {
        Block fromBlock = blockViews[fromRow][fromCol].getBlock();
        Block toBlock = blockViews[toRow][toCol].getBlock();

        Units unit = fromBlock.getUnit();
        fromBlock.setUnit(null);
        toBlock.setUnit(unit);

        blockViews[fromRow][fromCol].updateDisplay();
        blockViews[toRow][toCol].updateDisplay();

        gameController.addLogMessage(unit.getClass().getSimpleName() + " moved to (" + toRow + "," + toCol + ")");
    }



    private void placeUnit(int row, int col, String unitType) {
        Player currentPlayer = gameController.getCurrentPlayer();
        Units unit = createUnit(unitType, currentPlayer);

        if (unit != null && gameController.getCurrentPlayer().getKingdom().canCreateUnit(unit)) {
            if (gameController.getCurrentPlayer().getKingdom().createUnit(unit)) {
                Block block = blockViews[row][col].getBlock();
                block.setUnit(unit);
                blockViews[row][col].updateDisplay();
                gameController.addLogMessage(currentPlayer + " created " + unitType + " at (" + row + "," + col + ")");
                gameController.clearSelectedUnitType();
            } else {
                gameController.addLogMessage("Not enough resources to create " + unitType);
            }
        }
    }

    private void placeStructure(int row, int col, String structureType) {
        Player currentPlayer = gameController.getCurrentPlayer();
        Structures structure = createStructure(structureType, currentPlayer);

        if (structure != null && gameController.getCurrentPlayer().getKingdom().canBuildStructure(structure)) {
            if (gameController.getCurrentPlayer().getKingdom().buildStructure(structure)) {
                Block block = blockViews[row][col].getBlock();
                block.setStructure(structure);
                blockViews[row][col].updateDisplay();
                gameController.addLogMessage(currentPlayer + " built " + structureType + " at (" + row + "," + col + ")");
                gameController.clearSelectedStructureType();
            } else {
                gameController.addLogMessage("Not enough gold to build " + structureType);
            }
        }
    }

    private Units createUnit(String unitType, Player owner) {
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
            default: return null;
        }
    }

    private Structures createStructure(String structureType, Player owner) {
        switch (structureType) {
            case "Barrack":
                Barrack newBarrack = new Barrack();
                newBarrack.setOwner(owner);
                return new Barrack();
            case "Farm":
                Farm newFarm = new Farm();
                newFarm.setOwner(owner);
                return new Farm();
            case "Market":
                Market newMarket = new Market();
                newMarket.setOwner(owner);
                return new Market();
            case "Tower":
                Tower newTower = new Tower();
                newTower.setOwner(owner);
                return new Tower();
            default: return null;
        }
    }

    private void clearSelectionHighlights() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                blockViews[row][col].setSelected(false);
                blockViews[row][col].setHighlighted(false);
            }
        }
    }

    private void initializeGrid() {
        Random random = new Random();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
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
                addPlayerZoneBorder(blockView, row, col);
                addClickListener(blockView, row, col);
                blockViews[row][col] = blockView;
                add(blockView);
            }
        }
    }




    private void highlightMovementRange(int centerRow, int centerCol, int range) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int distance = Math.abs(row - centerRow) + Math.abs(col - centerCol);
                if (distance <= range && distance > 0) {
                    Block block = blockViews[row][col].getBlock();
                    if (!(block instanceof VoidBlock) && !block.hasUnit() && !block.hasStructure()) {
                        blockViews[row][col].setHighlighted(true);
                    }
                }
            }
        }
    }


    private void addPlayerZoneBorder(BlockView blockView, int row, int col) {
        if (row >= 0 && row <= 1 && col >= 0 && col <= 1) {
            blockView.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 3),
                    BorderFactory.createLineBorder(new Color(92, 120, 80), 1)
            ));
        }
        else if (row >= 8 && row <= 9 && col >= 8 && col <= 9) {
            blockView.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLUE, 3),
                    BorderFactory.createLineBorder(new Color(92, 120, 80), 1)
            ));
        }
        else {
            blockView.setBorder(BorderFactory.createLineBorder(new Color(92, 120, 80), 1));
        }
    }

    public BlockView getBlockView(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            return blockViews[row][col];
        }
        return null;
    }

}