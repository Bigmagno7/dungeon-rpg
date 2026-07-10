package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.view.DungeonView;
import it.unicam.cs.mpgc.rpg123436.view.Main;
import it.unicam.cs.mpgc.rpg123436.persistence.GamePersistence;
import it.unicam.cs.mpgc.rpg123436.persistence.GameSaveData;
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

        // --- INTERCETTAZIONE TASTI SPECIALI DI SISTEMA ---

        // TASTO P -> SALVATAGGIO RAPIDO (Evita conflitti con la S di WASD!)
        if (event.getCode() == javafx.scene.input.KeyCode.P) {
            GameSaveData snapshot = gameController.createSaveSnapshot();
            boolean successo = GamePersistence.saveGame(snapshot);
            if (successo) {
                gameController.getCombatLog().add("💾 PARTITA SALVATA CON SUCCESSO (Tasto P)!");
            } else {
                gameController.getCombatLog().add("❌ ERRORE DURANTE IL SALVATAGGIO!");
            }
            dungeonView.render();
            return; // Fine del comando, non è un movimento
        }

        // TASTO M -> TORNA AL MENU PRINCIPALE
        if (event.getCode() == javafx.scene.input.KeyCode.M) {
            javafx.stage.Stage stage = (javafx.stage.Stage) dungeonView.getScene().getWindow();
            if (stage.getUserData() instanceof Main) {
                Main mainApp = (Main) stage.getUserData();
                mainApp.showMainMenu();
            }
            return; // Esce e interrompe la sessione di gioco corrente
        }

        // --- GESTIONE MOVIMENTO STANDARD (WASD + FRECCE) ---
        int deltaX = 0;
        int deltaY = 0;

        switch (event.getCode()) {
            case W:
            case UP:    deltaY = -1; break;
            case S:
            case DOWN:  deltaY = 1;  break; // S resta per andare GIÙ!
            case A:
            case LEFT:  deltaX = -1; break;
            case D:
            case RIGHT: deltaX = 1;  break;
            default: return; // Se preme altro, ignora
        }

        // 1. Muove l'eroe ed esegue il turno del mostro
        gameController.handleHeroMovement(deltaX, deltaY);

        // 2. FORZA IL RENDER COMPLETO DELLA MAPPA
        System.out.println("LOG -> Richiesto aggiornamento grafico della mappa");
        dungeonView.render();
    }
}