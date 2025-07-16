package model.blocks;

public class VoidBlock extends Block {

    public VoidBlock(int row, int col) {
        super(row,col);
    }

    @Override
    public boolean isBuildable() {
        return false;
    }

    @Override
    public int getGoldGeneration() {
        return 0;
    }

    @Override
    public int getFoodGeneration() {
        return 0;
    }
}
