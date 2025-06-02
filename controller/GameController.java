package controller;

import model.Kingdom;
import model.Player;
import view.HUDPanel;

public class GameController {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Kingdom kingdom1;
    private Kingdom kingdom2;
    private HUDPanel hudPanel;
    private String selectedUnitType;
    private String selectedStructureType;

    public GameController() {
        this.kingdom1 = new Kingdom();
        this.kingdom2 = new Kingdom();
        this.player1 = new Player(kingdom1, "Player 1");
        this.player2 = new Player(kingdom2, "Player 2");
        this.currentPlayer = player1;
    }

    private void initializeGame() {
        kingdom1 = new Kingdom();
        kingdom2 = new Kingdom();
        player1 = new Player(kingdom1, "Player 1");
        player2 = new Player(kingdom2, "Player 2");

        currentPlayer = player1;
        currentPlayer.setTurn(true);
    }

    public void endTurn() {
        currentPlayer.endTurn();

        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }

        currentPlayer.startTurn();

        if (hudPanel != null) {
            hudPanel.setCurrentPlayer(currentPlayer.getName());
            hudPanel.updateStats();
            hudPanel.addLogMessage(currentPlayer.getName() + "'s turn started");
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

    public boolean levelUpUnit(model.units.Units unit) {
        if (currentPlayer.getKingdom().getGold() >= unit.getLevelUpCost() && unit.canLevelUp()) {
            currentPlayer.getKingdom().setGold(currentPlayer.getKingdom().getGold() - unit.getLevelUpCost());
            unit.levelUp();
            addLogMessage(currentPlayer.getName() + " leveled up " + unit.getClass().getSimpleName());
            return true;
        }
        return false;
    }


    public boolean levelUpStructure(model.structures.Structures structure) {
        if (currentPlayer.getKingdom().getGold() >= structure.getLevelUpCost() && structure.canLevelUp()) {
            currentPlayer.getKingdom().setGold(currentPlayer.getKingdom().getGold() - structure.getLevelUpCost());
            structure.levelUp();
            addLogMessage(currentPlayer.getName() + " leveled up " + structure.getClass().getSimpleName() + " to level " + structure.getLevel());
            if (hudPanel != null) {
                hudPanel.updateStats();
            }
            return true;
        } else {
            addLogMessage("Cannot level up: insufficient gold or max level reached");
            return false;
        }
    }


    public void newGame() {
        initializeGame();
        if (hudPanel != null) {
            hudPanel.setCurrentPlayer(currentPlayer.getName());
            hudPanel.updateStats();
            hudPanel.addLogMessage("New game started!");
        }
    }







    public Player getCurrentPlayer() { return currentPlayer; }
    public String getCurrentPlayerName() { return currentPlayer.getName(); }
    public Kingdom getKingdom1() { return player1.getKingdom(); }
    public Kingdom getKingdom2() { return player2.getKingdom(); }
    public String getSelectedUnitType() { return selectedUnitType; }
    public String getSelectedStructureType() { return selectedStructureType; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
}
