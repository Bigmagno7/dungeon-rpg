package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.view.DungeonView;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

/**
 * Gestisce gli input della tastiera (W,A,S,D / Frecce) e aggiorna la View.
 */
public class InputHandler {

    private final GameController gameController;
    private final DungeonView dungeonView;

    public InputHandler(GameController gameController, DungeonView dungeonView) {
        this.gameController = gameController;
        this.dungeonView = dungeonView;
    }

    /**
     * Collega l'ascoltatore dei tasti alla scena di gioco.
     */
    public void attachToScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent event) {
        int deltaX = 0;
        int deltaY = 0;

        switch (event.getCode()) {
            case W:
            case UP:
                deltaY = -1; // Su
                break;
            case S:
            case DOWN:
                deltaY = 1;  // Giù
                break;
            case A:
            case LEFT:
                deltaX = -1; // Sinistra
                break;
            case D:
            case RIGHT:
                deltaX = 1;  // Destra
                break;
            default:
                return; // Ignora gli altri tasti
        }

        // Muoviamo l'eroe sul Model tramite il GameController
        boolean moved = gameController.handleHeroMovement(deltaX, deltaY);

        // Se si è mosso, ridisegniamo la mappa con la nuova posizione!
        if (moved) {
            dungeonView.render();
        }
    }
}