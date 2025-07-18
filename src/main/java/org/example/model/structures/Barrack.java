package org.example.model.structures;

import org.example.model.Kingdom;
import org.example.model.units.Unit;

public class Barrack extends Structure {
    private int UNIT_SPACE;
    private boolean trainedThisTurn;

    public Barrack(){
        super(50, 5, 1, 3, 20, 20);
        this.UNIT_SPACE = 5;
        this.trainedThisTurn = false;
    }


    public void createUnit(Kingdom kingdom, Unit unit){
        kingdom.addUnit(unit);
    }

    public boolean canTrain() {
        return !trainedThisTurn;
    }

    public void markTrained() {
        trainedThisTurn = true;
    }

    public void resetTrainingFlag() {
        trainedThisTurn = false;
    }

}
