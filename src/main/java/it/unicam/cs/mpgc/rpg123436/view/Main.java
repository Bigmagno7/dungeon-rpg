package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.controller.InputHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Easy Dungeon RPG - Combat & HUD");

        // 1. Unica istanza del controller
        GameController controller = new GameController();

        // 2. Creiamo la View principale (che ora gestirà sia mappa che statistiche!)
        DungeonView dungeonView = new DungeonView(controller);

        Scene scene = new Scene(dungeonView, 760, 460);

        // 3. Input handler standard, senza strani override
        InputHandler inputHandler = new InputHandler(controller, dungeonView);
        inputHandler.attachToScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}