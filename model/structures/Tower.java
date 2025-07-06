package model.structures;

public class Tower extends Structure {
    private int defendRange;

    public Tower() {
        super(50, 5, 1, 3, 5, 5);
        this.defendRange = 5;

    }
}
