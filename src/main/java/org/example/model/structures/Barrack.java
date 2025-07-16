package model.structures;

import model.Kingdom;
import model.units.Unit;

public class Barrack extends Structure {
    private int UNIT_SPACE;

    public Barrack(){
        super(50, 5, 1, 3, 20, 5);
        this.UNIT_SPACE = 5;

    }


    public void createUnit(Kingdom kingdom, Unit unit){
        kingdom.addUnit(unit);
    }


}
