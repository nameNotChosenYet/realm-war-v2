package org.example.model.blocks;

import org.example.model.structures.Structure;
import org.example.model.units.Unit;

public abstract class Block {

    protected int row, col;
    protected boolean owned = false;
    protected String owner = null;
    protected Unit unit = null;
    protected Structure structure = null;

    public Block(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwner(String owner) {
        this.owned = true;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public boolean hasStructure() {
        return structure != null;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Structure getStructure() {
        return structure;
    }

    public boolean hasUnit() {
        return unit != null;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public abstract boolean isBuildable();

    public abstract int getGoldGeneration();

    public abstract int getFoodGeneration();

}
