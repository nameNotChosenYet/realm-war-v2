package org.example.model.units;

import org.example.model.Kingdom;
import org.example.model.structures.Structure;

public class Peasant extends Unit {
    public Peasant() {
        super(10 , 4, 2, 2, 2, 3, 1, true);
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
        return new SpearMan();
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
