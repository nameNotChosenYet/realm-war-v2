package org.example.model.units;

import org.example.model.Kingdom;
import org.example.model.structures.Structure;

public class Knight extends Unit {

    public Knight(){
        super(40,4, 5, 5, 5, 6, 1, true);
    }

    @Override
    public void attack(Unit target){
        int hP = target.getHitPoint() - this.getAttackPower();
        if( hP < 0) {
            hP = 0;
            new Kingdom().deleteUnit(target);
        }
        target.setHitPoint(hP);
    }

    @Override
    public void structAttack(Structure target){
        int newDurability = target.getDurability() - this.getAttackPower();
        if( newDurability < 0 ){
            newDurability = 0;
            new Kingdom().deleteStructure(target);
        }
        target.setDurability(newDurability);
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
