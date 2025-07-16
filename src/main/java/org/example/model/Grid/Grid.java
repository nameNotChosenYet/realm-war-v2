package model.grid;

import model.blocks.Block;
import view.BlockView;

public class Grid {

    private static int row = 10;
    private static int col = 10;
    private static BlockView[][] blockViews = new BlockView[row][col];

    public static int getRow() {
        return row;
    }

    public static int getCol() {
        return col;
    }

    public static BlockView[][] getBlockViews() {
        return blockViews;
    }
}
