package org.example.model.units;

import org.example.model.Kingdom;
import org.example.model.structures.Structure;

public class SwordMan extends Unit {

    public SwordMan(){
        super(30,4, 4, 4, 15, 4, 1, true);
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
        return true;
    }

    @Override
    public int getUpgradeCost() {
        return 50;
    }

    @Override
    public Unit getNextTierUnit() {
        return new Knight();
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
