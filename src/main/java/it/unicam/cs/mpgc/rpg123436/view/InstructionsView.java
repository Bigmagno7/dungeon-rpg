package it.unicam.cs.mpgc.rpg123436.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Rappresenta la schermata delle istruzioni di gioco.
 *
 * Mostra al giocatore i comandi, gli elementi del dungeon
 * e permette di iniziare una nuova partita.
 */
public class InstructionsView extends VBox {

    /**
     * Costruisce la schermata delle istruzioni.
     *
     * @param onStartGame azione eseguita quando il giocatore
     *                    avvia una nuova partita
     */
    public InstructionsView(Runnable onStartGame) {
        this.setSpacing(20);
        this.setPadding(new Insets(35));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #0b0b0d;");

        // Titolo della Guida
        Text title = new Text("GUIDA DI GIOCO");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web("#00D2FF"));

        // 1. Sezione Comandi (Aggiornata con la spiegazione dell'attacco direzionale)
        VBox commandsBox = new VBox(10);
        initSectionStyle(commandsBox, "COMANDI DI MOVIMENTO");

        TextFlow cmdContent = new TextFlow(
                createNormalText("1. Usa le "),
                createColoredText("FRECCE TASTIERA", "#00D2FF"),
                createNormalText(" o i tasti "),
                createColoredText("W, A, S, D", "#00D2FF"),
                createNormalText(" per muoverti nella mappa.\n\n"),
                createNormalText("2. "),
                createColoredText("COME ATTACCARE: ", "#FF4C4C"),
                createNormalText("Non ci sono tasti di attacco! Per colpire l'Orco devi semplicemente "),
                createColoredText("muoverti e camminare verso la casella dove si trova lui", "#FFD700"),
                createNormalText(". Andandogli contro, farai partire in automatico il tuo turno di combattimento!")
        );
        commandsBox.getChildren().add(cmdContent);

        // 2. Sezione Elementi
        VBox elementsBox = new VBox(12);
        initSectionStyle(elementsBox, "ELEMENTI DEL DUNGEON");

        // Riga Porta
        TextFlow rowDoor = new TextFlow(
                createColoredText(" [=] ", "#8B5A2B"),
                createColoredText("PORTA [E]: ", "#00D2FF"),
                createNormalText("Rappresenta l'uscita. Raggiungila per passare al livello successivo. Arriva al Livello 5 per vincere!")
        );

        // Riga Medikit
        TextFlow rowHeart = new TextFlow(
                createColoredText(" [+] ", "#FF4C4C"),
                createColoredText("MEDIKIT [H]: ", "#FF4C4C"),
                createNormalText("Raccoglilo per recuperare +5 HP fino a un massimo di 100 HP.")
        );

        // Riga Muri
        TextFlow rowWall = new TextFlow(
                createColoredText(" [#] ", "#3A3D40"),
                createColoredText("MURI [#]: ", "#7A7A7A"),
                createNormalText("Bloccano il passaggio, pianifica bene i tuoi movimenti.")
        );

        // Riga Nebbia
        TextFlow rowFog = new TextFlow(
                createColoredText(" [::] ", "#555555"),
                createColoredText("NEBBIA DI GUERRA: ", "#999999"),
                createNormalText("Dal Livello 3 la visibilita' sara' ridotta! Vedrai solo le caselle adiacenti all'Eroe.")
        );

        elementsBox.getChildren().addAll(rowDoor, rowHeart, rowWall, rowFog);

        // Bottone Inizia
        Button btnStart = new Button("INIZIA L'AVVENTURA");
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
    /**
     * Applica lo stile grafico alle sezioni della guida.
     *
     * @param box contenitore della sezione
     * @param titleText titolo della sezione
     */
    private void initSectionStyle(VBox box, String titleText) {
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(580);
        box.setStyle("-fx-background-color: #141419; -fx-padding: 15; -fx-border-color: #2d2d38; -fx-border-width: 1; -fx-border-radius: 5;");

        Text title = new Text(titleText);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        title.setFill(Color.web("#00D2FF"));

        VBox.setMargin(title, new Insets(0, 0, 8, 0));
        box.getChildren().add(title);
    }

    private Text createNormalText(String content) {
        Text text = new Text(content);
        text.setFont(Font.font("Arial", 13));
        text.setFill(Color.WHITE);
        return text;
    }

    private Text createColoredText(String content, String colorHex) {
        Text text = new Text(content);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        text.setFill(Color.web(colorHex));
        return text;
    }
}