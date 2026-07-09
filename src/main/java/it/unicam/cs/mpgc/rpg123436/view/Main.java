package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.controller.InputHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Easy Dungeon RPG - Sincronizzato");

        // 1. CREIAMO L'UNICA ISTANZA DEL CONTROLLER
        GameController controller = new GameController();

        // 2. PASSIAMO QUESTA SPECIFICA ISTANZA ALLA VIEW
        DungeonView dungeonView = new DungeonView(controller);

        // 3. CREIAMO LA SCENA
        Scene scene = new Scene(dungeonView, 450, 450);

        // 4. PASSIAMO LA STESSA ISTANZA ALL'INPUT HANDLER
        InputHandler inputHandler = new InputHandler(controller, dungeonView);
        inputHandler.attachToScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}