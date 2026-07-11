package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.view.DungeonView;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

/**
 * Gestisce l'interazione tra gli input dell'utente
 * e il motore di gioco.
 *
 * Traduce la pressione dei tasti della tastiera
 * in movimenti dell'eroe, delegando l'elaborazione
 * al GameController e aggiornando successivamente
 * la rappresentazione grafica tramite DungeonView.
 */
public class InputHandler {

    private final GameController gameController;
    private final DungeonView dungeonView;

    public InputHandler(GameController gameController, DungeonView dungeonView) {
        this.gameController = gameController;
        this.dungeonView = dungeonView;
    }
    /**
     * Collega il gestore degli eventi alla scena JavaFX.
     *
     * @param scene scena principale del gioco
     */
    public void attachToScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }
    /**
     * Elabora la pressione di un tasto e aggiorna lo stato del gioco.
     *
     * @param event evento generato dalla tastiera
     */
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

        // Gestisce il movimento dell'eroe e l'aggiornamento del turno di gioco
        gameController.handleHeroMovement(deltaX, deltaY);

        dungeonView.render();

    }
}