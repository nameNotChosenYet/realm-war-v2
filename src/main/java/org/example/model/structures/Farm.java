package org.example.model.structures;

import org.example.model.Kingdom;

public class Farm extends Structure {
    private int foodPerTurn;

    public Farm() {
        super(50, 5, 1, 3, 5, 5);
        this.foodPerTurn = 5;

    }


    public void produce_food(Kingdom kingdom){
        int newFood = kingdom.getFood() + foodPerTurn;
        kingdom.setFood(newFood);
    }


}
