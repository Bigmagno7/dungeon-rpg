package it.unicam.cs.mpgc.rpg123436.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Rappresenta la 'View' nell'architettura MVC del progetto.
 * Punto di ingresso effettivo dell'applicazione grafica JavaFX, responsabile
 * esclusivamente della gestione e del rendering dell'interfaccia utente (finestra, scene e nodi).
 */
public class Main extends Application {

    // Il metodo 'start' viene invocato dal Launcher tramite il framework JavaFX
    @Override
    public void start(Stage primaryStage) {
        // Impostiamo il titolo che comparirà in alto sulla finestra del gioco
        primaryStage.setTitle("Easy Dungeon RPG");

        // Creiamo un semplice testo di benvenuto per verificare che la grafica funzioni
        Text infoText = new Text("Benvenuto nel Dungeon! Il gioco sta per iniziare...");

        // Lo StackPane è un contenitore che posiziona gli elementi al centro
        StackPane root = new StackPane(infoText);

        // Creiamo la scena impostando le dimensioni della finestra (800x600)
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show(); // Mostra la finestra a schermo
    }

    public static void main(String[] args) {
        launch(args);
    }
}