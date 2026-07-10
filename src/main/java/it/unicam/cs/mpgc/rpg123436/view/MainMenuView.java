package it.unicam.cs.mpgc.rpg123436.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainMenuView extends VBox {

    public MainMenuView(Runnable onNewGame, Runnable onLoadGame, Runnable onExit) {
        this.setSpacing(20);
        this.setPadding(new Insets(60, 50, 60, 50));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #0b0b0d;"); // Stesso sfondo scuro del gioco

        // Titolo del Gioco spaziale
        Text title = new Text("🏰 DUNGEON WIZARD 🔮");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        title.setFill(Color.web("#00D2FF"));

        Text subtitle = new Text("Unicam Chronicles\n");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setFill(Color.LIGHTGRAY);

        // Bottone Nuova Partita
        Button btnNew = createMenuButton("NUOVA PARTITA", "#00D2FF");
        btnNew.setOnAction(e -> onNewGame.run());

        // Bottone Carica Salvataggio
        Button btnLoad = createMenuButton("CARICA PARTITA", "#FFD700"); // Colore Oro fiammante
        btnLoad.setOnAction(e -> onLoadGame.run());

        // Bottone Esci
        Button btnExit = createMenuButton("ESCI DAL GIOCO", "#FF4C4C"); // Rosso
        btnExit.setOnAction(e -> onExit.run());

        this.getChildren().addAll(title, subtitle, btnNew, btnLoad, btnExit);
    }

    private Button createMenuButton(String text, String colorHex) {
        Button btn = new Button(text);
        btn.setPrefWidth(260);
        btn.setPrefHeight(45);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setStyle(
                "-fx-background-color: #141419; " +
                        "-fx-text-fill: " + colorHex + "; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );

        // Effetto hover base via codice per renderlo super responsive al passaggio del mouse
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + colorHex + "; " +
                        "-fx-text-fill: #0b0b0d; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #141419; " +
                        "-fx-text-fill: " + colorHex + "; " +
                        "-fx-border-color: " + colorHex + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        ));

        return btn;
    }
}