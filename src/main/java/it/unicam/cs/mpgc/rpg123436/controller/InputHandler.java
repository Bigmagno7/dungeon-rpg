package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.view.DungeonView;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class InputHandler {

    private final GameController gameController;
    private final DungeonView dungeonView;

    public InputHandler(GameController gameController, DungeonView dungeonView) {
        this.gameController = gameController;
        this.dungeonView = dungeonView;
    }

    public void attachToScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent event) {
        // Se il gioco è finito, blocchiamo i movimenti standard
        if (gameController.isGameOver() || gameController.isGameWon()) {
            return;
        }

        int deltaX = 0;
        int deltaY = 0;

        switch (event.getCode()) {
            case W:
            case UP:    deltaY = -1; break;
            case S:
            case DOWN:  deltaY = 1;  break;
            case A:
            case LEFT:  deltaX = -1; break;
            case D:
            case RIGHT: deltaX = 1;  break;
            default: return;
        }

        // 1. Muove l'eroe ed esegue il turno del mostro
        gameController.handleHeroMovement(deltaX, deltaY);

        // 2. FORZA IL RENDER COMPLETO DELLA MAPPA
        System.out.println("LOG -> Richiesto aggiornamento grafico della mappa");
        dungeonView.render();
    }
}