package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Rappresenta la 'View' nell'architettura MVC del progetto.
 * Punto di ingresso dell'applicazione grafica JavaFX, responsabile del rendering complessivo.
 */
public class Main extends Application {

    private GameController controller;
    private DungeonView dungeonView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Easy Dungeon RPG");

        // 1. Inizializziamo il Controller (che a sua volta crea il Model)
        this.controller = new GameController();

        // 2. Creiamo la View della mappa passandogli il controller come regista
        this.dungeonView = new DungeonView(controller);

        // 3. Impostiamo la scena inserendo la griglia del dungeon appena creata
        // Dimensioni della finestra (800x600) adatte a contenere comodamente la griglia
        Scene scene = new Scene(dungeonView, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show(); // Mostra la finestra a schermo
    }

    public static void main(String[] args) {
        launch(args);
    }
}