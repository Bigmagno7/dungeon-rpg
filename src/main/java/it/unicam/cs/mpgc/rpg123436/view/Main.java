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
    private final int height = 520;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Easy Dungeon RPG - Combat & HUD");

        // All'avvio mostriamo sempre il Menu Principale
        showMainMenu();
        this.primaryStage.show();
    }

    /**
     * Carica e mostra la Schermata Iniziale
     */
    public void showMainMenu() {
        MainMenuView menuView = new MainMenuView(
                this::startNewGame,    // Quando clicca Nuova Partita
                this::loadSavedGame,   // Quando clicca Carica Partita
                () -> primaryStage.close() // Quando clicca Esci
        );

        Scene menuScene = new Scene(menuView, width, height);
        primaryStage.setScene(menuScene);
    }

    /**
     * Fila via liscio con una nuova partita da zero
     */
    private void startNewGame() {
        GameController controller = new GameController();
        launchGameSession(controller);
    }

    /**
     * Carica i dati dal file binario e ripristina la sessione
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
     * Avvia la partita vera e propria usando la tua struttura originale
     */
    private void launchGameSession(GameController controller) {
        DungeonView dungeonView = new DungeonView(controller);
        Scene scene = new Scene(dungeonView, width, height);

        // Agganciamo il TUO InputHandler originale senza romperlo
        InputHandler inputHandler = new InputHandler(controller, dungeonView);
        inputHandler.attachToScene(scene);

        // AGGIUNTA DI SICUREZZA: se premi un tasto a gioco finito (Win/Lose), torna al menu
        scene.setOnKeyPressed(e -> {
            if (controller.isGameOver() || controller.isGameWon()) {
                showMainMenu();
            }
        });

        primaryStage.setScene(scene);
        dungeonView.render(); // Render iniziale per aggiornare la nebbia/mappa
    }

    public static void main(String[] args) {
        launch(args);
    }
}