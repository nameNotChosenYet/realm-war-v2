package model;

public class Player {
    private String name;
    private Kingdom kingdom;
    private boolean isTurn;
    private boolean isGameOver;

    public Player(Kingdom kingdom, String name) {
        this.kingdom = kingdom;
        this.name = name;
        this.kingdom.setPlayerName(name);
        this.isTurn = false;
    }

    public void startTurn(int turnCount) {
        this.isTurn = true;
        if (turnCount > 1)
            kingdom.generateResources();
    }

    public void endTurn() {
        this.isTurn = false;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
