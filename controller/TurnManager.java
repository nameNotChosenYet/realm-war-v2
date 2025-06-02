package controller;

import model.Kingdom;
import model.Player;
import model.Player.*;

public class TurnManager {
    boolean player1Turn;
    boolean player2Turn;

    public TurnManager(GameController gameController) {
        player1Turn = true;
        player2Turn = false;
    }


    public void changeTurn() {
        if (player1Turn) {
            player1Turn = false;
            player2Turn = true;
        }else {
            player1Turn = true;
            player2Turn = false;
        }
    }

    public void turn(Player player) {

    }


}
