package org.example.model.blocks;

public class EmptyBlock extends Block {

    public EmptyBlock(int row, int col) {
        super(row,col);
    }

    @Override
    public boolean isBuildable() {
        return owned && !hasStructure();
    } // need to fix this

    @Override
    public int getGoldGeneration() {
        return owned ? 3 : 0;
    }

    @Override
    public int getFoodGeneration() {
        return owned ? 10 : 0;
    }
}
