package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DungeonView extends HBox {

    private final GameController controller;
    private final int tileSize = 40;

    private GridPane mapGrid;
    private VBox hudPanel;
    private VBox leftLayout; // Layout sinistro che contiene Titolo + Mappa
    private Text topStatusText; // Il mega testo del livello
    private Text monsterHpText; // AGGIUNTA: Il testo per la vita dell'orco sotto il livello

    private Image wallImg;
    private Image floorImg;
    private Image doorImg;
    private Image heartImg;
    private Image heroImg;
    private Image monsterImg;
    private Image fogImg; // Texture per la nebbia nei livelli avanzati

    public DungeonView(GameController controller) {
        this.controller = controller;
        this.setStyle("-fx-background-color: #0b0b0d;");

        // 1. Creiamo il layout di sinistra (Titoli sopra + Mappa sotto)
        this.leftLayout = new VBox(5); // Spazio ridotto per tenere tutto compatto
        this.leftLayout.setPadding(new Insets(15));
        this.leftLayout.setAlignment(Pos.TOP_CENTER);

        // Il mega testo sopra la mappa per il Livello
        this.topStatusText = new Text();
        this.topStatusText.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        // Il testo dedicato alla vita dell'orco
        this.monsterHpText = new Text();
        this.monsterHpText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        this.mapGrid = new GridPane();
        this.mapGrid.setHgap(1);
        this.mapGrid.setVgap(1);

        // Inseriamo i titoli e poi la mappa
        this.leftLayout.getChildren().addAll(topStatusText, monsterHpText, mapGrid);

        // 2. Pannello HUD di destra classico per i log
        this.hudPanel = new VBox(15);
        this.hudPanel.setPrefWidth(300);
        this.hudPanel.setStyle("-fx-background-color: #141419; -fx-border-color: #2d2d38; -fx-border-width: 0 0 0 2;");
        this.hudPanel.setPadding(new Insets(20));

        this.getChildren().addAll(leftLayout, hudPanel);

        generateTextures();
        render();
    }

    private void generateTextures() {
        Canvas canvasWall = new Canvas(tileSize, tileSize);
        GraphicsContext gc = canvasWall.getGraphicsContext2D();
        gc.setFill(Color.web("#3A3D40")); gc.fillRect(0, 0, tileSize, tileSize);
        wallImg = canvasWall.snapshot(null, new WritableImage(tileSize, tileSize));

        Canvas canvasFloor = new Canvas(tileSize, tileSize);
        gc = canvasFloor.getGraphicsContext2D();
        gc.setFill(Color.web("#1E2022")); gc.fillRect(0, 0, tileSize, tileSize);
        floorImg = canvasFloor.snapshot(null, new WritableImage(tileSize, tileSize));

        Canvas canvasDoor = new Canvas(tileSize, tileSize);
        gc = canvasDoor.getGraphicsContext2D();
        gc.setFill(Color.web("#8B5A2B")); gc.fillRect(5, 5, tileSize - 10, tileSize - 5);
        gc.setFill(Color.GOLD); gc.fillOval(tileSize - 15, tileSize / 2.0, 6, 6);
        doorImg = canvasDoor.snapshot(null, new WritableImage(tileSize, tileSize));

        Canvas canvasHeart = new Canvas(tileSize, tileSize);
        gc = canvasHeart.getGraphicsContext2D();
        gc.setFill(Color.RED); gc.fillOval(8, 8, tileSize-16, tileSize-16);
        gc.setStroke(Color.WHITE); gc.setLineWidth(3);
        gc.strokeLine(tileSize/2.0, 12, tileSize/2.0, tileSize-12);
        gc.strokeLine(12, tileSize/2.0, tileSize-12, tileSize/2.0);
        heartImg = canvasHeart.snapshot(null, new WritableImage(tileSize, tileSize));

        // GENERAZIONE TEXTURE DELLA NEBBIA (Nero fiammante per oscurare la griglia)
        WritableImage imgFog = new WritableImage(tileSize, tileSize);
        for (int x = 0; x < tileSize; x++) {
            for (int y = 0; y < tileSize; y++) {
                imgFog.getPixelWriter().setColor(x, y, Color.web("#050505"));
            }
        }
        fogImg = imgFog;

        heroImg = new Image("https://img.icons8.com/color/40/wizard.png", tileSize, tileSize, true, true);
        monsterImg = new Image("https://img.icons8.com/color/40/orc.png", tileSize, tileSize, true, true);
    }

    public void render() {
        int currentLevel = controller.getCurrentLevel();
        int monsterHp = controller.getMonster().getHp();

        // 1. AGGIORNAMENTO DEL MEGA TESTO DEL LIVELLO
        if (controller.isGameOver()) {
            topStatusText.setText("🚨 GAME OVER 🚨");
            topStatusText.setFill(Color.RED);
            monsterHpText.setText(""); // Nascondi se hai perso
        } else if (controller.isGameWon()) {
            topStatusText.setText("👑 VITTORIA FINALE! 👑");
            topStatusText.setFill(Color.GREEN);
            monsterHpText.setText(""); // Nascondi se hai vinto
        } else {
            topStatusText.setText("🏰 LIVELLO " + currentLevel);
            topStatusText.setFill(Color.web("#00D2FF"));

            // AGGIORNAMENTO DELLA TABELLA/BARRA DELLA VITA DELL'ORCO
            if (monsterHp > 0) {
                monsterHpText.setText("👹 VITA ORCO: " + monsterHp + " HP");
                monsterHpText.setFill(Color.CRIMSON);
            } else {
                monsterHpText.setText("💀 ORCO SCONFITTO (Via libera!)");
                monsterHpText.setFill(Color.LIGHTGREEN);
            }
        }

        // Render della griglia
        mapGrid.getChildren().clear();
        DungeonMap map = controller.getMap();
        char[][] grid = map.getGrid();

        int heroX = controller.getHero().getX();
        int heroY = controller.getHero().getY();
        int monsterX = controller.getMonster().getX();
        int monsterY = controller.getMonster().getY();

        // Visione: vedi fino a 2 caselle di distanza
        int visionRadius = 2;

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                ImageView tileView = new ImageView();

                int distX = Math.abs(c - heroX);
                int distY = Math.abs(r - heroY);
                boolean isVisible = (distX <= visionRadius && distY <= visionRadius);

                // La nebbia scatta dal livello 3 in su
                if (currentLevel >= 3 && !isVisible) {
                    tileView.setImage(fogImg);
                } else {
                    if (grid[r][c] == '#') {
                        tileView.setImage(wallImg);
                    } else if (grid[r][c] == 'E') {
                        tileView.setImage(doorImg);
                    } else if (grid[r][c] == 'H') {
                        tileView.setImage(heartImg);
                    } else {
                        tileView.setImage(floorImg);
                    }

                    if (c == heroX && r == heroY) {
                        tileView.setImage(heroImg);
                    } else if (monsterHp > 0 && c == monsterX && r == monsterY) {
                        tileView.setImage(monsterImg);
                    }
                }

                mapGrid.add(tileView, c, r);
            }
        }

        // HUD laterale di riepilogo
        hudPanel.getChildren().clear();

        // Sistemata l'icona dello scudo che sballava su Windows
        Text statsTitle = new Text("⚔️ STATUS EROE");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16)); statsTitle.setFill(Color.LIGHTGRAY);

        Text heroInfo = new Text("HP: " + controller.getHero().getHp() + "/50\nPosizione: [" + heroX + "," + heroY + "]");
        heroInfo.setFont(Font.font("Arial", 14)); heroInfo.setFill(Color.WHITE);

        Text logTitle = new Text("\n📜 DIARIO DI GIOCO");
        logTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16)); logTitle.setFill(Color.LIGHTGRAY);

        hudPanel.getChildren().addAll(statsTitle, heroInfo, logTitle);

        for (String log : controller.getCombatLog()) {
            Text logLine = new Text(log);
            logLine.setFont(Font.font("Consolas", 12));
            logLine.setFill(Color.GOLD);
            hudPanel.getChildren().add(logLine);
        }
    }
}