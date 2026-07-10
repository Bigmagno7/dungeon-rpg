package it.unicam.cs.mpgc.rpg123436.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class InstructionsView extends VBox {

    public InstructionsView(Runnable onStartGame) {
        this.setSpacing(20);
        this.setPadding(new Insets(40));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #0b0b0d;"); // Stesso sfondo scuro del gioco

        // Titolo della Guida
        Text title = new Text("📜 GUIDA DI GIOCO");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web("#00D2FF"));

        // Sezione Comandi
        VBox commandsBox = createSection("🎮 COMANDI DI MOVIMENTO",
                "• Usa le FRECCE TASTIERA o i tasti W, A, S, D per muoverti nella mappa.\n" +
                        "• Andare contro l'Orco farà partire automaticamente un turno di combattimento!");

        // Sezione Oggetti e Legenda
        VBox elementsBox = createSection("🏰 ELEMENTI DEL DUNGEON",
                "• 🚪 PORTA (E): Rappresenta l'uscita. Raggiungila per passare al livello successivo. Arriva al Livello 5 per vincere!\n" +
                        "• ❤️ MEDIKIT (H): Raccoglilo per recuperare +5 HP fino a un massimo di 100 HP.\n" +
                        "• 🧱 MURI (#): Bloccano il passaggio, pianifica bene i tuoi movimenti.\n" +
                        "• 🌁 NEBBIA DI GUERRA: Dal Livello 3 la visibilità sarà ridotta! Vedrai solo le caselle vicine.");

        // Bottone per Avviare il gioco
        Button btnStart = new Button("INIZIA L'AVVENTURA ⚔️");
        btnStart.setPrefWidth(250);
        btnStart.setPrefHeight(45);
        btnStart.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnStart.setStyle(
                "-fx-background-color: #141419; " +
                        "-fx-text-fill: #FFD700; " +
                        "-fx-border-color: #FFD700; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-cursor: hand;"
        );

        btnStart.setOnMouseEntered(e -> btnStart.setStyle(
                "-fx-background-color: #FFD700; " +
                        "-fx-text-fill: #0b0b0d; " +
                        "-fx-border-color: #FFD700; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-cursor: hand;"
        ));

        btnStart.setOnMouseExited(e -> btnStart.setStyle(
                "-fx-background-color: #141419; " +
                        "-fx-text-fill: #FFD700; " +
                        "-fx-border-color: #FFD700; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-cursor: hand;"
        ));

        btnStart.setOnAction(e -> onStartGame.run());

        this.getChildren().addAll(title, commandsBox, elementsBox, btnStart);
    }

    private VBox createSection(String sectionTitle, String content) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(550);
        box.setStyle("-fx-background-color: #141419; -fx-padding: 15; -fx-border-color: #2d2d38; -fx-border-width: 1; -fx-border-radius: 5;");

        Text tTitle = new Text(sectionTitle);
        tTitle.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        tTitle.setFill(Color.web("#00D2FF"));

        Text tContent = new Text(content);
        tContent.setFont(Font.font("Arial", 13));
        tContent.setFill(Color.WHITE);
        tContent.setWrappingWidth(520);

        box.getChildren().addAll(tTitle, tContent);
        return box;
    }
}