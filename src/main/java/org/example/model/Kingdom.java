package model;

import model.blocks.Block;
import model.grid.Grid;
import model.structures.Farm;
import model.structures.Market;
import model.structures.Structure;
import model.structures.TownHall;
import model.units.Unit;

import java.util.ArrayList;

public class Kingdom {
    private String playerName;
    private Grid grid;
    private int food;
    private int gold;
    private ArrayList<Unit> units;
    private ArrayList<Structure> structures;
    private ArrayList<Block> blocks;

    public Kingdom() {
        this.gold = 100;
        this.food = 50;
        this.units = new ArrayList<>();
        this.structures = new ArrayList<>();
        blocks = new ArrayList<>();
        this.structures.add(new TownHall());
    }


    public void generateResources() {
        for (Structure structure : structures) {
            if (structure instanceof TownHall) {
                TownHall townHall = (TownHall) structure;
                this.gold += townHall.getGoldPerTurn();
                this.food += townHall.getFoodPerTurn();
            } else if (structure instanceof Farm) {
                Farm farm = (Farm) structure;
                farm.produce_food(this);
            } else if (structure instanceof Market) {
                Market market = (Market) structure;
                market.produce_gold(this);
            }
        }
        for (Unit u : units) {
            this.food -= u.getRation();
        }
    }


    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }


    public boolean canCreateUnit(Unit unit) {
        return this.gold >= unit.getPayment() && this.food >= unit.getRation();
    }

    public void createUnit(Unit unit) {
        this.gold -= unit.getPayment();
        this.food -= unit.getRation();
        this.units.add(unit);
    }

    public void addStructure(Structure structure) {
        this.structures.add(structure);
    }


    public boolean canBuildStructure(Structure structure) {
        return this.gold >= structure.getBuildingCost();
    }

    public void buildStructure(Structure structure) {
        this.gold -= structure.getBuildingCost();
        this.structures.add(structure);
    }

    public void addGold(int add) {
        gold += add;
    }

    public void addFood(int add) {
        food += add;
    }

    public int getFood() {
        return this.food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void deleteUnit(Unit unit) {
        units.remove(unit);
    }

    public void deleteStructure(Structure structure) {
        structures.remove(structure);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}



