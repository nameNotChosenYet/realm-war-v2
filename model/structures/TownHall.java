package model.structures;

public class TownHall extends Structures {
    private int goldPerTurn;
    private int foodPerTurn;
    private int unitSpace;

    public TownHall() {
        super(50, 0, 1, 1, 0, 0);
        this.goldPerTurn = 5;
        this.foodPerTurn = 5;
        this.unitSpace = 5;
    }


    public int getGoldPerTurn() {
        return goldPerTurn;
    }

    public int getFoodPerTurn() {
        return foodPerTurn;
    }

    public int getUnitSpace() {
        return unitSpace;
    }
}
