package model;


import model.structures.*;
import model.units.*;


public class Player {
    private String name;
    private Kingdom kingdom;
    private boolean isTurn;
    private boolean isGameOver;
    private Units selectedUnit;
    private Structures selectedStructure;

//    public Player(Kingdom kingdom) {
//        this.kingdom = kingdom;
//        this.isTurn = false;
//    }

    public Player(Kingdom kingdom, String name) {
        this.kingdom = kingdom;
        this.name = name;
        this.kingdom.setPlayerName(name);
        this.isTurn = false;
    }


    public boolean createKnight() {
        return kingdom.createUnit(new Knight());
    }

    public boolean createPeasant() {
        return kingdom.createUnit(new Peasant());
    }

    public boolean createSpearMan() {
        return kingdom.createUnit(new SpearMan());
    }

    public boolean createSwordMan() {
        return kingdom.createUnit(new SwordMan());
    }

    public boolean buildBarrack() {
        return kingdom.buildStructure(new Barrack());
    }

    public boolean buildFarm() {
        return kingdom.buildStructure(new Farm());
    }

    public boolean buildMarket() {
        return kingdom.buildStructure(new Market());
    }

    public boolean buildTower() {
        return kingdom.buildStructure(new Tower());
    }

    public void startTurn() {
        this.isTurn = true;
        kingdom.generateResources();
    }

    public void endTurn() {
        this.isTurn = false;
        this.selectedUnit = null;
        this.selectedStructure = null;
    }

    public void selectUnit(Units unit) {
        this.selectedUnit = unit;
        this.selectedStructure = null;
    }

    public void selectStructure(Structures structure) {
        this.selectedStructure = structure;
        this.selectedUnit = null;
    }

    public void clearSelection() {
        this.selectedUnit = null;
        this.selectedStructure = null;
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

    public Units getSelectedUnit() {
        return selectedUnit;
    }

    public Structures getSelectedStructure() {
        return selectedStructure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
