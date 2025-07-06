package model.structures;

import model.Player;

public abstract class Structure {
    private int durability;
    private int maintenanceCost;
    private int level;
    private int maxLevel;
    private int levelUpCost;
    private int buildingCost;
    private Player Owner;


    public Structure (int durability, int maintenanceCost, int level, int maxLevel, int levelUpCost, int buildingCost) {
        this.durability = durability;
        this.maintenanceCost = maintenanceCost;
        this.level = level;
        this.maxLevel = maxLevel;
        this.levelUpCost = levelUpCost;
        this.buildingCost = buildingCost;
    }

    public boolean canLevelUp() {
        return level < maxLevel;
    }

    public void levelUp() {
        this.level++;
        durability += 10;
        Owner.getKingdom().setGold(Owner.getKingdom().getGold() - levelUpCost);
        if (level != maxLevel)
            levelUpCost *= 2;
    }


    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public Player getOwner() {
        return Owner;
    }

    public void setOwner(Player owner) {
        Owner = owner;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    public int getBuildingCost() {
        return buildingCost;
    }

    public int getLevelUpCost() {
        return levelUpCost;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

}
