package it.unicam.cs.mpgc.rpg123436;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Classe principale del gioco. Estende 'Application' di JavaFX
 * per poter usufruire della gestione delle finestre grafiche.
 */
public class Main extends Application {

    // Il metodo 'start' è il punto di ingresso principale per le applicazioni JavaFX
    @Override
    public void start(Stage primaryStage) {
        // Impostiamo il titolo che comparirà in alto sulla finestra del gioco
        primaryStage.setTitle("Easy Dungeon RPG");

        // Creiamo un semplice testo di benvenuto per verificare che la grafica funzioni
        Text infoText = new Text("Benvenuto nel Dungeon! Il gioco sta per iniziare...");

        // Lo StackPane è un contenitore che posiziona gli elementi al centro (in questo caso il testo)
        StackPane root = new StackPane(infoText);

        // Creiamo la scena del gioco impostando le dimensioni della finestra (800 pixel di larghezza, 600 di altezza)
        Scene scene = new Scene(root, 800, 600);

        // Colleghiamo la scena alla finestra principale (Stage)
        primaryStage.setScene(scene);

        // Mostriamo la finestra a schermo
        primaryStage.show();
    }

    // Il metodo main classico che fa partire l'applicazione JavaFX
    public static void main(String[] args) {
        launch(args); // Lancia l'applicazione JavaFX chiamando il metodo start
    }
}