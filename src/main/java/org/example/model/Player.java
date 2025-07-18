package org.example.model;

public class Player {
    private String name;
    private Kingdom kingdom;
    private boolean isTurn;
    private boolean isGameOver;
    private int score;

    public Player(Kingdom kingdom, String name) {
        this.kingdom = kingdom;
        this.name = name;
        this.kingdom.setPlayerName(name);
        this.isTurn = false;
        this.score = 0;
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

    public int getScore() {
        return score;
    }

    public void addScore(int amount) {
        this.score += amount;
    }


}
