package org.example.model.structures;

public class Tower extends Structure {
    private int defendRange;

    public Tower() {
        super(50, 5, 1, 3, 5, 10);
        this.defendRange = 1;

    }

    public int getDefendRange() {
        return defendRange;
    }


}
