package org.example.controller;

import javax.swing.Timer;

public class TurnManager {
    private Timer turnTimer;
    private Timer resourceTimer;

    public TurnManager(GameController controller) {
        turnTimer = new Timer(30_000, e -> {
            controller.endTurn();
            restartTurnTimer();
        });
        turnTimer.setRepeats(false);

        resourceTimer = new Timer(5_000, e -> {
            controller.getCurrentPlayer().getKingdom().generateResources();
            controller.updateHUD();
        });
        resourceTimer.setRepeats(true);
        resourceTimer.start();
    }

    public void startTurnTimer() {
        turnTimer.start();
    }

    public void restartTurnTimer() {
        turnTimer.restart();
    }

    public void stopTimers() {
        turnTimer.stop();
        resourceTimer.stop();
    }
}
