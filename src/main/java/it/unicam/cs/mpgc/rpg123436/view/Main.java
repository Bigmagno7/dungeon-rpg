package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.controller.InputHandler;
import it.unicam.cs.mpgc.rpg123436.persistence.GamePersistence;
import it.unicam.cs.mpgc.rpg123436.persistence.GameSaveData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private final int width = 750;
    private final int height = 520; // Altezza perfetta per non tagliare la mappa in basso

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Easy Dungeon RPG - Combat & HUD");

        // Salviamo l'istanza del Main dentro lo Stage così la View può richiamare il menu iniziale
        this.primaryStage.setUserData(this);

        // All'avvio mostriamo sempre il Menu Principale
        showMainMenu();
        this.primaryStage.show();
    }

    /**
     * Carica e mostra la Schermata Iniziale del Menu
     */
    public void showMainMenu() {
        MainMenuView menuView = new MainMenuView(
                this::startNewGame,    // Azione per Nuova Partita
                this::loadSavedGame,   // Azione per Carica Partita
                () -> primaryStage.close() // Azione per Uscire dal gioco
        );

        Scene menuScene = new Scene(menuView, width, height);
        primaryStage.setScene(menuScene);
    }

    /**
        Inizializza una nuova partita da zero (Livello 1)
        e
        mostra la guida di gioco prima di far partire una nuova partita
     */
    private void startNewGame() {
        GameController controller = new GameController();

        // Invece di lanciare subito il gioco, carichiamo la vista delle istruzioni
        InstructionsView instructionsView = new InstructionsView(() -> {
            launchGameSession(controller); // Questo parte quando clicchi "INIZIA L'AVVENTURA"
        });

        Scene instructionsScene = new Scene(instructionsView, width, height);
        primaryStage.setScene(instructionsScene);
    }

    /**
     * Carica i dati dal file binario tramite il sistema di persistenza
     */
    private void loadSavedGame() {
        GameSaveData savedData = GamePersistence.loadGame();
        if (savedData != null) {
            GameController controller = new GameController();
            controller.restoreFromSnapshot(savedData); // Travasa i dati salvati nel controller
            launchGameSession(controller);
        } else {
            System.out.println("⚠️ Nessun file di salvataggio trovato locale.");
        }
    }

    /**
     * Avvia la sessione di gioco vera e propria usando la tua struttura originale
     */
    private void launchGameSession(GameController controller) {
        DungeonView dungeonView = new DungeonView(controller);
        Scene scene = new Scene(dungeonView, width, height);

        // Colleghiamo il tuo InputHandler nativo (WASD + Frecce)
        InputHandler inputHandler = new InputHandler(controller, dungeonView);
        inputHandler.attachToScene(scene);

        primaryStage.setScene(scene);
        dungeonView.render(); // Render iniziale per stampare la mappa
    }

    public static void main(String[] args) {
        launch(args);
    }
}