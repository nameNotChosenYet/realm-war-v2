package org.example.model;

import org.example.model.blocks.Block;
import org.example.model.Grid.Grid;
import org.example.model.structures.*;
import org.example.model.units.Unit;

import java.util.ArrayList;

public class Kingdom {
    private String playerName;
    private Grid grid;
    private int food;
    private int gold;
    private ArrayList<Unit> units;
    private ArrayList<Structure> structures;
    private ArrayList<Block> blocks;

    private static final int MAX_BARRACKS = 3;
    private static final int MAX_FARMS = 3;
    private static final int MAX_MARKETS = 3;
    private static final int MAX_TOWERS = 3;
    private static final int MAX_TOWN_HALLS = 1;

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

        for (int r = 0; r < Grid.getRow(); r++) {
            for (int c = 0; c < Grid.getCol(); c++) {
                Block b = Grid.getBlockViews()[r][c].getBlock();
                if (b.isOwned() && b.getOwner().equals(playerName)) {
                    this.gold += b.getGoldGeneration();
                    this.food += b.getFoodGeneration();
                }
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
        return this.gold >= structure.getBuildingCost() && !isStructureLimitReached(structure);
    }

    public void buildStructure(Structure structure) {
        if (isStructureLimitReached(structure)) {
            return;
        }
        this.gold -= structure.getBuildingCost();
        this.structures.add(structure);
    }

    private long countStructures(Class<? extends Structure> type) {
        return structures.stream().filter(s -> s.getClass().equals(type)).count();
    }

    private int getMaxStructureCount(Class<? extends Structure> type) {
        if (type.equals(Barrack.class)) return MAX_BARRACKS;
        if (type.equals(Farm.class)) return MAX_FARMS;
        if (type.equals(Market.class)) return MAX_MARKETS;
        if (type.equals(Tower.class)) return MAX_TOWERS;
        if (type.equals(TownHall.class)) return MAX_TOWN_HALLS;
        return Integer.MAX_VALUE;
    }

    public boolean isStructureLimitReached(Structure structure) {
        return countStructures(structure.getClass()) >= getMaxStructureCount(structure.getClass());
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

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public void removeStructure(Structure structure) {
        structures.remove(structure);
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



