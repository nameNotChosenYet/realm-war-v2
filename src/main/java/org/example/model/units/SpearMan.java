package org.example.model.units;

import org.example.model.Kingdom;
import org.example.model.structures.Structure;

public class SpearMan extends Unit {
    public SpearMan(){
        super(20,4, 3, 3, 10, 3, 1, true);
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
        return new SwordMan();
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
