package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Gestisce il rendering grafico del dungeon auto-generando le texture in memoria
 * per evitare l'uso di file PNG esterni e garantire la massima portabilità.
 */
public class DungeonView extends GridPane {

    private final GameController controller;
    private final int tileSize = 40;

    private Image wallImg;
    private Image floorImg;
    private Image heroImg;
    private Image monsterImg;

    public DungeonView(GameController controller) {
        this.controller = controller;
        this.setStyle("-fx-background-color: #0b0b0d; -fx-padding: 20;");
        this.setHgap(1); // Griglia compatta da vero gioco retro
        this.setVgap(1);

        generateTextures();
        render();
    }

    /**
     * Disegna e genera le immagini in memoria usando un Canvas di JavaFX.
     */
    /**
     * Disegna i muri in memoria e carica l'eroe e il mostro direttamente dal web.
     */
    private void generateTextures() {
        // 1. TEXTURE MURO (Mattoni di pietra sfumati - Rimane questa che andava bene!)
        Canvas canvasWall = new Canvas(tileSize, tileSize);
        GraphicsContext gc = canvasWall.getGraphicsContext2D();
        gc.setFill(Color.web("#3A3D40"));
        gc.fillRect(0, 0, tileSize, tileSize);
        gc.setStroke(Color.web("#1A1C1E"));
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, tileSize, tileSize);
        gc.strokeLine(0, tileSize / 2.0, tileSize, tileSize / 2.0);
        gc.strokeLine(tileSize / 2.0, 0, tileSize / 2.0, tileSize / 2.0);
        gc.strokeLine(tileSize / 4.0, tileSize / 2.0, tileSize / 4.0, tileSize);
        wallImg = canvasWall.snapshot(null, new WritableImage(tileSize, tileSize));

        // 2. TEXTURE PAVIMENTO (Lastre di pietra scura)
        Canvas canvasFloor = new Canvas(tileSize, tileSize);
        gc = canvasFloor.getGraphicsContext2D();
        gc.setFill(Color.web("#1E2022"));
        gc.fillRect(0, 0, tileSize, tileSize);
        gc.setStroke(Color.web("#2D3033"));
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, tileSize, tileSize);
        floorImg = canvasFloor.snapshot(null, new WritableImage(tileSize, tileSize));

        // 3. SPRITE EROE (Caricato dal web - Icona Cavaliere RPG)
        String heroUrl = "https://img.icons8.com/color/40/knight-helmet.png";
        heroImg = new Image(heroUrl, tileSize, tileSize, true, true);

        // 4. SPRITE MOSTRO (Caricato dal web - Icona Orco/Goblin)
        String monsterUrl = "https://img.icons8.com/color/40/orc.png";
        monsterImg = new Image(monsterUrl, tileSize, tileSize, true, true);
    }

    public void render() {
        this.getChildren().clear();
        DungeonMap map = controller.getMap();
        char[][] grid = map.getGrid();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                ImageView tileView = new ImageView();

                if (grid[r][c] == '#') {
                    tileView.setImage(wallImg);
                } else {
                    tileView.setImage(floorImg);
                }

                if (c == controller.getHero().getX() && r == controller.getHero().getY()) {
                    tileView.setImage(heroImg);
                } else if (c == controller.getMonster().getX() && r == controller.getMonster().getY() && controller.getMonster().getHp() > 0) {
                    tileView.setImage(monsterImg);
                }

                this.add(tileView, c, r);
            }
        }
    }
}