package org.example.model.units;

import org.example.model.Kingdom;
import org.example.model.structures.Structure;

public class Knight extends Unit {

    public Knight(){
        super(40,4, 5, 5, 5, 6, 1, true);
    }

    @Override
    public void attack(Unit target){
        super.attack(target);
    }

    @Override
    public void structAttack(Structure target){
        super.structAttack(target);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public int getUpgradeCost() {
        return 0;
    }

    @Override
    public Unit getNextTierUnit() {
        return null;
    }



    @Override
    public boolean getCanMove() {
        return canMove;
    }

    @Override
    public void setCanMove(boolean move) {
        canMove = move;
    }
}
