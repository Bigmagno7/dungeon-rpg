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

        render();
    }

    /**
     * Disegna e aggiorna l'intera griglia di gioco.
     */
    public void render() {
        // Puliamo la griglia prima di ridisegnare per evitare sovrapposizioni
        this.getChildren().clear();

        DungeonMap map = controller.getMap();
        char[][] grid = map.getGrid();

        // Ciclo for per scorrere tutta la matrice della mappa
        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                String element = "."; // Di base è pavimento

                // 1. Controlliamo cosa c'è nella matrice del Model
                if (grid[r][c] == '#') {
                    element = "🧱"; // Muro
                } else {
                    element = "⬛"; // Pavimento calpestabile vuoto
                }

                // 2. Controlliamo se in questa coordinata c'è l'Eroe
                if (c == controller.getHero().getX() && r == controller.getHero().getY()) {
                    element = "🧝‍♂️"; // Eroe
                }
                // 3. Controlliamo se c'è il Mostro (e se è ancora vivo)
                else if (c == controller.getMonster().getX() && r == controller.getMonster().getY() && controller.getMonster().getHp() > 0) {
                    element = "👹"; // Mostro
                }

                // Creiamo il nodo di testo per JavaFX
                Text tile = new Text(element);
                tile.setFont(new Font(tileSize - 10)); // Adattiamo la dimensione dell'emoji

                // Aggiungiamo l'elemento alla griglia di JavaFX (colonna c, riga r)
                this.add(tile, c, r);
            }
        }
    }
}