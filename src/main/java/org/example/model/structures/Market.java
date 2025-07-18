package org.example.model.structures;

import org.example.model.Kingdom;

public class Market extends Structure {
    private int goldPerTurn;

    public Market() {
        super(50, 5, 1, 3, 5, 10);
        this.goldPerTurn = 8;

    }

    public void produce_gold(Kingdom kingdom){
        int newGold = kingdom.getGold() + goldPerTurn;
        kingdom.setGold(newGold);
    }

    public int getGoldPerTurn() {
        return goldPerTurn;
    }

}
