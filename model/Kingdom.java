package model;

import model.structures.Farm;
import model.structures.Market;
import model.structures.TownHall;
import model.units.Units;
import model.blocks.Block;
import model.structures.Structures;
import model.Grid.Grid;

import java.util.ArrayList;

public class Kingdom {
    private String playerName;
    private Grid grid;
    private int food;
    private int gold;
    private ArrayList<Units> units;
    private ArrayList<Structures> structures;
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
        for (Structures structure : structures) {
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
        for (Units u : units) {
            this.gold -= u.getPayment();
            this.food -= u.getRation();
        }
    }


    public ArrayList<Structures> getStructures() {
        return structures;
    }

    public ArrayList<Units> getUnits() {
        return units;
    }

    public void addUnit(Units unit) {
        this.units.add(unit);
    }


    public boolean canCreateUnit(Units unit) {
        return this.gold >= unit.getPayment() && this.food >= unit.getRation();
    }

    public boolean createUnit(Units unit) {
        if (canCreateUnit(unit)) {
            this.gold -= unit.getPayment();
            this.food -= unit.getRation();
            this.units.add(unit);
            return true;
        }
        return false;
    }

    public void addStructure(Structures structure) {
        this.structures.add(structure);
    }


    public boolean canBuildStructure(Structures structure) {
        return this.gold >= structure.getBuildingCost();
    }

    public boolean buildStructure(Structures structure) {
        if (canBuildStructure(structure)) {
            this.gold -= structure.getBuildingCost();
            this.structures.add(structure);
            return true;
        }
        return false;
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

    public void deleteUnit(Units unit) {
        units.remove(unit);
    }

    public void deleteStructure(Structures structure) {
        structures.remove(structure);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}



