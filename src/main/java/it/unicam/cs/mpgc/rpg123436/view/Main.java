package it.unicam.cs.mpgc.rpg123436.view;

import it.unicam.cs.mpgc.rpg123436.controller.GameController;
import it.unicam.cs.mpgc.rpg123436.controller.InputHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private VBox hudPanel;
    private GameController controller;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Easy Dungeon RPG - Combat & HUD");

        this.controller = new GameController();
        DungeonView dungeonView = new DungeonView(controller);

        // 1. CREIAMO IL PANNELLO LATERALE DELL'HUD (Destra)
        hudPanel = new VBox(15);
        hudPanel.setPrefWidth(300);
        hudPanel.setStyle("-fx-background-color: #141419; -fx-border-color: #2d2d38; -fx-border-width: 0 0 0 2;");
        hudPanel.setPadding(new Insets(20));

        // Aggiorniamo i testi dentro l'HUD
        updateHUD();

        // 2. CREIAMO IL CONTENITORE PRINCIPALE ORIZZONTALE
        HBox root = new HBox();
        root.setStyle("-fx-background-color: #0b0b0d;");
        // Mettiamo la mappa a sinistra e il pannello statistiche a destra
        root.getChildren().addAll(dungeonView, hudPanel);

        // Allarghiamo la Scene a 760 di larghezza per farci stare tutto comodamente
        Scene scene = new Scene(root, 760, 460);

        // 3. AGGANCIAMO L'INPUT HANDLER
        // Trucco: modifichiamo la logica dell'input per aggiornare anche l'HUD grafico oltre alla mappa
        InputHandler inputHandler = new InputHandler(controller, dungeonView) {
            @Override
            public void attachToScene(Scene s) {
                s.setOnKeyPressed(event -> {
                    // Chiamiamo il vecchio movimento standard
                    super.attachToScene(s);
                    // Forza l'aggiornamento scritte dell'HUD ad ogni tasto premuto
                    updateHUD();
                });
            }
        };
        inputHandler.attachToScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Svuota e ridisegna le scritte di testo dell'HUD laterale prendendo i dati dal controller.
     */
    private void updateHUD() {
        hudPanel.getChildren().clear();

        // Titolo Statistiche
        Text statsTitle = new Text("🛡️ STATUS EROE");
        statsTitle.setFont(Font.font("Arial", 18));
        statsTitle.setFill(Color.web("#00D2FF"));

        Text heroInfo = new Text("Nome: " + controller.getHero().getName() + "\nPosizione: [" + controller.getHero().getX() + "," + controller.getHero().getY() + "]");
        heroInfo.setFont(Font.font("Arial", 14));
        heroInfo.setFill(Color.WHITE);

        Text monsterTitle = new Text("\n👹 STATO MOSTRO");
        monsterTitle.setFont(Font.font("Arial", 18));
        monsterTitle.setFill(Color.web("#FF1744"));

        String monsterStatus = controller.getMonster().getHp() > 0
                ? "Nome: " + controller.getMonster().getType() + "\nHP: " + controller.getMonster().getHp()
                : "STATO: DEFUNTO 💀";
        Text monsterInfo = new Text(monsterStatus);
        monsterInfo.setFont(Font.font("Arial", 14));
        monsterInfo.setFill(Color.WHITE);

        // Sezione Log dei Combattimenti
        Text logTitle = new Text("\n📜 DIARIO DI GIOCO");
        logTitle.setFont(Font.font("Arial", 16));
        logTitle.setFill(Color.LIGHTGRAY);

        hudPanel.getChildren().addAll(statsTitle, heroInfo, monsterTitle, monsterInfo, logTitle);

        // Stampiamo le righe di testo dei danni passati dal controller
        for (String log : controller.getCombatLog()) {
            Text logLine = new Text(log);
            logLine.setFont(Font.font("Consolas", 12));
            logLine.setFill(Color.GOLD);
            hudPanel.getChildren().add(logLine);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}