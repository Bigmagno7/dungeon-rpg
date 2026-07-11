package it.unicam.cs.mpgc.rpg123436;

import it.unicam.cs.mpgc.rpg123436.view.Main;

/**
 * Classe di avvio principale del progetto.
 *
 * Delega l'avvio dell'applicazione JavaFX alla classe Main,
 * separando il punto di ingresso del programma dalla gestione
 * della finestra grafica.
 */
public class Launcher {
    public static void main(String[] args) {
        // Delega l'avvio dell'interfaccia grafica alla classe Main di JavaFX
        Main.main(args);
    }
}