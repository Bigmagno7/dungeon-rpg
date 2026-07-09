package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Gestisce il rendering grafico della mappa del dungeon su una griglia JavaFX.
 * Utilizza forme geometriche colorate stabili per garantire la massima compatibilità.
 */
public class DungeonView extends GridPane {

    private final GameController controller;
    private final int tileSize = 40; // Dimensione in pixel di ogni cella

    public DungeonView(GameController controller) {
        this.controller = controller;
        // Spaziatura tra i quadratini
        this.setHgap(2);
        this.setVgap(2);
        this.setStyle("-fx-background-color: #111111; -fx-padding: 20;"); // Sfondo finestra scuro

        render();
    }

    /**
     * Disegna e aggiorna l'intera griglia di gioco basandosi sulle forme geometriche.
     */
    public void render() {
        this.getChildren().clear();

        DungeonMap map = controller.getMap();
        char[][] grid = map.getGrid();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {

                // Creiamo un quadratino della dimensione scelta
                Rectangle tile = new Rectangle(tileSize, tileSize);

                // 1. Colore di base (Muro o Pavimento)
                if (grid[r][c] == '#') {
                    tile.setFill(Color.web("#555555")); // Grigio scuro per i muri
                } else {
                    tile.setFill(Color.web("#222222")); // Grigio molto scuro per il pavimento calpestabile
                }

                // 2. Sovrascriviamo il colore se c'è l'Eroe
                if (c == controller.getHero().getX() && r == controller.getHero().getY()) {
                    tile.setFill(Color.DODGERBLUE); // Blu acceso per l'Eroe
                }
                // 3. Sovrascriviamo il colore se c'è il Mostro vivo
                else if (c == controller.getMonster().getX() && r == controller.getMonster().getY() && controller.getMonster().getHp() > 0) {
                    tile.setFill(Color.RED); // Rosso acceso per il Mostro
                }

                // Aggiungiamo il quadratino alla griglia (colonna c, riga r)
                this.add(tile, c, r);
            }
        }
    }
}