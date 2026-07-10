package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.persistence.GamePersistence;
import it.unicam.cs.mpgc.rpg123436.persistence.GameSaveData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
import javafx.stage.Stage;

public class DungeonView extends HBox {

    private final GameController controller;
    private final int tileSize = 40;

    private GridPane mapGrid;
    private VBox hudPanel;
    private VBox leftLayout;
    private Text topStatusText;

    private Image wallImg;
    private Image floorImg;
    private Image doorImg;
    private Image heartImg;
    private Image heroImg;
    private Image monsterImg;
    private Image fogImg;

    public DungeonView(GameController controller) {
        this.controller = controller;
        this.setStyle("-fx-background-color: #0b0b0d;");

        // 1. Layout sinistro compatto (padding ridotto a 10 per non tagliare i muri sotto!)
        this.leftLayout = new VBox(5);
        this.leftLayout.setPadding(new Insets(10, 15, 10, 15));
        this.leftLayout.setAlignment(Pos.TOP_CENTER);

        this.topStatusText = new Text();
        this.topStatusText.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        this.mapGrid = new GridPane();
        this.mapGrid.setHgap(1);
        this.mapGrid.setVgap(1);

        // --- CREAZIONE PULSANTI IN BASSO A SINISTRA (SOTTO LA MAPPA) ---
        HBox bottomButtons = new HBox(15);
        bottomButtons.setAlignment(Pos.CENTER_LEFT);
        bottomButtons.setPadding(new Insets(8, 0, 0, 0));

        Button btnSave = new Button("💾 SALVA");
        styleGameButton(btnSave, "#FFD700");
        btnSave.setFocusTraversable(false); // CRUCIALE: impedisce al bottone di rubare l'input alla tastiera!
        btnSave.setOnAction(e -> {
            GameSaveData snapshot = controller.createSaveSnapshot();
            if (GamePersistence.saveGame(snapshot)) {
                controller.getCombatLog().add("💾 Partita salvata!");
                render();
            }
        });

        Button btnExit = new Button("🚪 ESCI");
        styleGameButton(btnExit, "#FF4C4C");
        btnExit.setFocusTraversable(false); // CRUCIALE: impedisce di bloccare la tastiera!
        btnExit.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            if (stage.getUserData() instanceof Main) {
                ((Main) stage.getUserData()).showMainMenu();
            }
        });

        bottomButtons.getChildren().addAll(btnSave, btnExit);

        // Ora sopra la mappa c'è SOLO il livello gigante, sotto ci sono i bottoni
        this.leftLayout.getChildren().addAll(topStatusText, mapGrid, bottomButtons);

        // 2. Pannello HUD di destra
        this.hudPanel = new VBox(12); // Spazio verticale ottimizzato
        this.hudPanel.setPrefWidth(300);
        this.hudPanel.setStyle("-fx-background-color: #141419; -fx-border-color: #2d2d38; -fx-border-width: 0 0 0 2;");
        this.hudPanel.setPadding(new Insets(15));

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

        // Testo sopra la mappa
        if (controller.isGameOver()) {
            topStatusText.setText("🚨 GAME OVER 🚨");
            topStatusText.setFill(Color.RED);
        } else if (controller.isGameWon()) {
            topStatusText.setText("👑 VITTORIA FINALE! 👑");
            topStatusText.setFill(Color.GREEN);
        } else {
            topStatusText.setText("🏰 LIVELLO " + currentLevel);
            topStatusText.setFill(Color.web("#00D2FF"));
        }

        // Render griglia mappa
        mapGrid.getChildren().clear();
        DungeonMap map = controller.getMap();
        char[][] grid = map.getGrid();
        int heroX = controller.getHero().getX();
        int heroY = controller.getHero().getY();
        int monsterX = controller.getMonster().getX();
        int monsterY = controller.getMonster().getY();

        int visionRadius = 2;

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                ImageView tileView = new ImageView();

                int distX = Math.abs(c - heroX);
                int distY = Math.abs(r - heroY);
                boolean isVisible = (distX <= visionRadius && distY <= visionRadius);

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

        // AGGIORNAMENTO HUD LATERALE (TUTTO SULLA DESTRA)
        hudPanel.getChildren().clear();

        // 1. Sezione Eroe
        Text statsTitle = new Text("⚔️ STATUS EROE");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16)); statsTitle.setFill(Color.web("#00D2FF"));
        Text heroInfo = new Text("HP: " + controller.getHero().getHp() + "/"+ controller.getHero().getMaxHp()+"\nPosizione: [" + heroX + "," + heroY + "]");
        heroInfo.setFont(Font.font("Arial", 14)); heroInfo.setFill(Color.WHITE);
        hudPanel.getChildren().addAll(statsTitle, heroInfo);

        // 2. Sezione Mostro inserita stabilmente a DESTRA!
        Text monsterTitle = new Text("\n👹 STATUS MOSTRO");
        monsterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16)); monsterTitle.setFill(Color.CRIMSON);

        int maxMonsterHp = (currentLevel == 1) ? 30 : (10 + (currentLevel * 10));

        String monsterText = monsterHp > 0 ? "HP: " + monsterHp + " / " + maxMonsterHp : "HP: 0 💀 (Sconfitto!)";
        Text monsterInfo = new Text(monsterText);
        monsterInfo.setFont(Font.font("Arial", 14));
        monsterInfo.setFill(monsterHp > 0 ? Color.WHITE : Color.LIGHTGREEN);
        hudPanel.getChildren().addAll(monsterTitle, monsterInfo);

        // 3. Diario di gioco
        Text logTitle = new Text("\n📜 DIARIO DI GIOCO");
        logTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16)); logTitle.setFill(Color.LIGHTGRAY);
        hudPanel.getChildren().add(logTitle);

        for (String log : controller.getCombatLog()) {
            Text logLine = new Text(log);
            logLine.setFont(Font.font("Consolas", 12));
            logLine.setFill(Color.GOLD);
            hudPanel.getChildren().add(logLine);
        }
    }

    private void styleGameButton(Button btn, String colorHex) {
        btn.setPrefWidth(100);
        btn.setPrefHeight(30);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        btn.setStyle(
                "-fx-background-color: #141419; " +
                        "-fx-text-fill: " + colorHex + "; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 4; " +
                        "-fx-background-radius: 4; " +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + colorHex + "; " +
                        "-fx-text-fill: #0b0b0d; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 4; " +
                        "-fx-background-radius: 4; " +
                        "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #141419; " +
                        "-fx-text-fill: " + colorHex + "; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 4; " +
                        "-fx-background-radius: 4; " +
                        "-fx-cursor: hand;"
        ));
    }
}