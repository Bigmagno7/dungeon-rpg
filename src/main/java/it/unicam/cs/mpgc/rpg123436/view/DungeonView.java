package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Gestisce il rendering grafico della mappa del dungeon su una griglia JavaFX.
 * Legge i dati dal Model tramite il GameController e li trasforma in elementi visivi.
 */
public class DungeonView extends GridPane {

    private final GameController controller;
    private final int tileSize = 40; // Dimensione in pixel di ogni cella della griglia

    public DungeonView(GameController controller) {
        this.controller = controller;
        // Spaziatura tra le celle della griglia
        this.setHgap(2);
        this.setVgap(2);
        this.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 10;"); // Sfondo scuro da dungeon

        public void rendering(){}
    }


}