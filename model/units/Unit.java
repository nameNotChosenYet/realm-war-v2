package model.units;

import model.Player;
import model.structures.Structure;

public abstract class Unit {
    private int hitPoint;
    private int movementRange;
    private int attackPower;
    private int attackRange;
    private int payment;
    private int ration;
    private int unitSpace;
    private Player Owner;
    protected boolean canMove;


    public Unit(int hitPoint, int movementRange, int attackPower, int attackRange, int payment, int ration, int unitSpace, boolean canMove) {
        this.hitPoint = hitPoint;
        this.movementRange = movementRange;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.payment = payment;
        this.ration = ration;
        this.unitSpace = unitSpace;
        this.canMove = canMove;
    }


    public abstract void attack(Unit target);

    public abstract void structAttack(Structure target);

    public abstract boolean getCanMove();

    public abstract void setCanMove(boolean move);

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
