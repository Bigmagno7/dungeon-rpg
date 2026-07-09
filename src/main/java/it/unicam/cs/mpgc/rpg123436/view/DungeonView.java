package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DungeonView extends HBox {

    private final GameController controller;
    private final int tileSize = 40;

    private GridPane mapGrid;
    private VBox hudPanel;

    private Image wallImg;
    private Image floorImg;
    private Image doorImg; // Texture nuova per l'uscita
    private Image heroImg;
    private Image monsterImg;

    public DungeonView(GameController controller) {
        this.controller = controller;
        this.setStyle("-fx-background-color: #0b0b0d;");

        this.mapGrid = new GridPane();
        this.mapGrid.setStyle("-fx-padding: 20;");
        this.mapGrid.setHgap(1);
        this.mapGrid.setVgap(1);

        this.hudPanel = new VBox(15);
        this.hudPanel.setPrefWidth(300);
        this.hudPanel.setStyle("-fx-background-color: #141419; -fx-border-color: #2d2d38; -fx-border-width: 0 0 0 2;");
        this.hudPanel.setPadding(new Insets(20));

        this.getChildren().addAll(mapGrid, hudPanel);

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
        gc.strokeRect(0, 0, tileSize, tileSize);
        wallImg = canvasWall.snapshot(null, new WritableImage(tileSize, tileSize));

        // Texture Pavimento
        Canvas canvasFloor = new Canvas(tileSize, tileSize);
        gc = canvasFloor.getGraphicsContext2D();
        gc.setFill(Color.web("#1E2022"));
        gc.fillRect(0, 0, tileSize, tileSize);
        floorImg = canvasFloor.snapshot(null, new WritableImage(tileSize, tileSize));

        // Texture Porta/Uscita 'E'
        Canvas canvasDoor = new Canvas(tileSize, tileSize);
        gc = canvasDoor.getGraphicsContext2D();
        gc.setFill(Color.web("#8B5A2B")); // Marrone legno
        gc.fillRect(5, 5, tileSize - 10, tileSize - 5);
        gc.setFill(Color.GOLD); // Maniglia dorata
        gc.fillOval(tileSize - 15, tileSize / 2.0, 6, 6);
        doorImg = canvasDoor.snapshot(null, new WritableImage(tileSize, tileSize));

        heroImg = new Image("https://img.icons8.com/color/40/wizard.png", tileSize, tileSize, true, true);
        monsterImg = new Image("https://img.icons8.com/color/40/orc.png", tileSize, tileSize, true, true);
    }

    public void render() {
        mapGrid.getChildren().clear();
        DungeonMap map = controller.getMap();
        char[][] grid = map.getGrid();

        int heroX = controller.getHero().getX();
        int heroY = controller.getHero().getY();
        int monsterX = controller.getMonster().getX();
        int monsterY = controller.getMonster().getY();
        int monsterHp = controller.getMonster().getHp();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                ImageView tileView = new ImageView();

                // Disegno sfondo in base al carattere della matrice
                if (grid[r][c] == '#') {
                    tileView.setImage(wallImg);
                } else if (grid[r][c] == 'E') {
                    tileView.setImage(doorImg);
                } else {
                    tileView.setImage(floorImg);
                }

                // Sovrapposizione entità
                if (c == heroX && r == heroY) {
                    tileView.setImage(heroImg);
                } else if (c == monsterX && r == monsterY && monsterHp > 0) {
                    tileView.setImage(monsterImg);
                }

                mapGrid.add(tileView, c, r);
            }
        }

        // AGGIORNAMENTO HUD LATERALE
        hudPanel.getChildren().clear();

        // Livello Corrente
        Text lvlText = new Text("🏰 DUNGEON LIVELLO: " + controller.getCurrentLevel());
        lvlText.setFont(Font.font("Arial", 16));
        lvlText.setFill(Color.PURPLE);

        // Status Eroe
        Text statsTitle = new Text("🛡️ STATUS EROE");
        statsTitle.setFont(Font.font("Arial", 18));
        statsTitle.setFill(Color.web("#00D2FF"));

        // Mostriamo gli HP dell'Eroe!
        Color hpColor = controller.getHero().getHp() > 30 ? Color.GREEN : Color.RED;
        Text heroInfo = new Text("Nome: " + controller.getHero().getName() + "\nHP: " + controller.getHero().getHp() + "/100\nPosizione: [" + heroX + "," + heroY + "]");
        heroInfo.setFont(Font.font("Arial", 14));
        heroInfo.setFill(Color.WHITE);

        // Status Mostro
        Text monsterTitle = new Text("\n👹 STATO MOSTRO");
        monsterTitle.setFont(Font.font("Arial", 18));
        monsterTitle.setFill(Color.web("#FF1744"));

        String monsterStatus = monsterHp > 0
                ? "Tipo: " + controller.getMonster().getType() + "\nHP: " + monsterHp
                : "STATO: DEFUNTO 💀";
        Text monsterInfo = new Text(monsterStatus);
        monsterInfo.setFont(Font.font("Arial", 14));
        monsterInfo.setFill(Color.WHITE);

        Text logTitle = new Text("\n📜 DIARIO DI GIOCO");
        logTitle.setFont(Font.font("Arial", 16));
        logTitle.setFill(Color.LIGHTGRAY);

        hudPanel.getChildren().addAll(lvlText, statsTitle, heroInfo, monsterTitle, monsterInfo, logTitle);

        for (String log : controller.getCombatLog()) {
            Text logLine = new Text(log);
            logLine.setFont(Font.font("Consolas", 12));
            // Se c'è scritto GAME OVER coloriamo la scritta di rosso acceso
            if (log.contains("GAME OVER")) logLine.setFill(Color.RED);
            else logLine.setFill(Color.GOLD);
            hudPanel.getChildren().add(logLine);
        }
    }
}