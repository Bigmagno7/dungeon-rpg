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
        this.setHgap(1);
        this.setVgap(1);

        generateTextures();
        render();
    }

    private void generateTextures() {
        // Texture Muro
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

        // Texture Pavimento
        Canvas canvasFloor = new Canvas(tileSize, tileSize);
        gc = canvasFloor.getGraphicsContext2D();
        gc.setFill(Color.web("#1E2022"));
        gc.fillRect(0, 0, tileSize, tileSize);
        gc.setStroke(Color.web("#2D3033"));
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, tileSize, tileSize);
        floorImg = canvasFloor.snapshot(null, new WritableImage(tileSize, tileSize));

        // Sprite Eroe (Mago)
        String heroUrl = "https://img.icons8.com/color/40/wizard.png";
        heroImg = new Image(heroUrl, tileSize, tileSize, true, true);

        // Sprite Mostro (Orco)
        String monsterUrl = "https://img.icons8.com/color/40/orc.png";
        monsterImg = new Image(monsterUrl, tileSize, tileSize, true, true);
    }

    /**
     * Ridisegna l'intera griglia leggendo le posizioni AGGIORNATE dei personaggi.
     */
    public void render() {
        this.getChildren().clear();

        DungeonMap map = controller.getMap();
        char[][] grid = map.getGrid();

        // Recuperiamo le posizioni correnti direttamente dal controller a ogni ciclo
        int heroX = controller.getHero().getX();
        int heroY = controller.getHero().getY();
        int monsterX = controller.getMonster().getX();
        int monsterY = controller.getMonster().getY();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                ImageView tileView = new ImageView();

                // 1. Disegna lo sfondo (Muro o Pavimento)
                if (grid[r][c] == '#') {
                    tileView.setImage(wallImg);
                } else {
                    tileView.setImage(floorImg);
                }

                // 2. Controllo Eroe (c è la X/Colonna, r è la Y/Riga)
                if (c == heroX && r == heroY) {
                    tileView.setImage(heroImg);
                }
                // 3. Controllo Mostro (confronto dinamico)
                else if (c == monsterX && r == monsterY) {
                    tileView.setImage(monsterImg);
                }

                this.add(tileView, c, r);
            }
        }
    }
}