package model.blocks;

import model.structures.Structures;
import model.units.Units;

import java.awt.*;

public abstract class Block {
    protected int row, col;
    protected boolean owned = false;
    protected String owner = null;
    protected Units unit = null;
    protected Structures structure = null;


    public Block(int row, int col) {
        this.row = row;
        this.col = col;
    }


    // getters and setters

    public boolean isOwned() {
        return owned;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owned = true;
        this.owner = owner;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public Structures getStructure() {
        return structure;
    }

    public void setStructure(Structures structure) {
        this.structure = structure;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    public boolean hasUnit() {
        return unit != null;
    }

    public abstract String getType();

    public abstract Color getColor();

    public boolean hasStructure() {
        return structure != null;
    }

    public abstract boolean isBuildable();

    public abstract int getGoldGeneration();

    public abstract int getFoodGeneration();
}
