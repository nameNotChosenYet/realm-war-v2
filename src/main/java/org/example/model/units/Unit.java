package org.example.model.units;

import org.example.model.Player;
import org.example.model.structures.Structure;

public abstract class Unit {
    private int hitPoint;
    private int movementRange;
    private int attackPower;
    private final int baseAttackPower;
    private boolean inForest = false;
    private int attackRange;
    private int payment;
    private int ration;
    private int unitSpace;
    private Player Owner;
    protected boolean canMove;
    private final int maxHitPoint;


    public Unit(int hitPoint, int movementRange, int attackPower, int attackRange, int payment, int ration, int unitSpace, boolean canMove) {
        this.hitPoint = hitPoint;
        this.movementRange = movementRange;
        this.attackPower = attackPower;
        this.baseAttackPower = attackPower;
        this.attackRange = attackRange;
        this.payment = payment;
        this.ration = ration;
        this.unitSpace = unitSpace;
        this.canMove = canMove;
        this.maxHitPoint = hitPoint;
    }


    public void attack(Unit target){
        int newHP = target.getHitPoint() - this.getAttackPower();
        target.setHitPoint(Math.max(0, newHP));
    }

    public void structAttack(Structure target){
        int newDurability = target.getDurability() - this.getAttackPower();
        target.setDurability(Math.max(0, newDurability));
    }

    public void enterForest() {
        if (!inForest) {
            attackPower += 2;
            hitPoint += 5;
            inForest = true;
        }
    }

    public void leaveForest() {
        if (inForest) {
            attackPower = baseAttackPower;
            hitPoint = Math.max(hitPoint - 5, 0);
            inForest = false;
        }
    }

    public int getMaxHitPoint() {
        return maxHitPoint;
    }

    public abstract boolean getCanMove();

    public abstract void setCanMove(boolean move);

    public abstract boolean canUpgrade();

    public abstract int getUpgradeCost();

    public abstract Unit getNextTierUnit();

    public Player getOwner() {
        return Owner;
    }

    public void setOwner(Player owner) {
        Owner = owner;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getPayment() {
        return payment;
    }

    public int getRation() {
        return ration;
    }

    public int getUnitSpace() {
        return unitSpace;
    }
}
