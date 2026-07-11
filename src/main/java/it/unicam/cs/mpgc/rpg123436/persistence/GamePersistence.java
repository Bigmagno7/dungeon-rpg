package it.unicam.cs.mpgc.rpg123436.persistence;

import java.io.*;

/**
 * Gestisce il salvataggio e il caricamento dello stato della partita.
 *
 * Utilizza la serializzazione nativa di Java per scrivere e leggere
 * oggetti GameSaveData sotto forma di file binario.
 */
public class GamePersistence {

    private static final String SAVE_FILE_NAME = "dungeon_save.dat";

    /**
     * Salva lo stato corrente della partita su file.
     *
     * @param saveData dati della partita da salvare
     * @return true se il salvataggio è completato correttamente,
     *         false in caso di errore
     */
    public static boolean saveGame(GameSaveData saveData) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_NAME))) {
            oos.writeObject(saveData);
            System.out.println("🎮 Partita salvata correttamente in: " + SAVE_FILE_NAME);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Errore durante il salvataggio su file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carica una partita precedentemente salvata.
     *
     * @return dati salvati della partita oppure null
     *         se il file non esiste o il caricamento fallisce
     */
    public static GameSaveData loadGame() {
        File saveFile = new File(SAVE_FILE_NAME);
        if (!saveFile.exists()) {
            System.out.println("⚠️ Nessun file di salvataggio trovato.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            GameSaveData saveData = (GameSaveData) ois.readObject();
            System.out.println("🚀 Dati di salvataggio caricati correttamente.");
            return saveData;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Errore durante il caricamento del file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}