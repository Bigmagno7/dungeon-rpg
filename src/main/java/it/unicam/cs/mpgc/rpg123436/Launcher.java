package it.unicam.cs.mpgc.rpg123436;

/**
 * Classe di avvio (Launcher) principale del progetto.
 * Questa classe non estende 'Application' di JavaFX per evitare problemi
 * di configurazione dei moduli (Java Platform Module System) a runtime,
 * garantendo la massima compatibilità dell'eseguibile su qualsiasi IDE.
 */
public class Launcher {
    public static void main(String[] args) {
        // Delega l'avvio dell'interfaccia grafica alla classe Main di JavaFX
        Main.main(args);
    }
}