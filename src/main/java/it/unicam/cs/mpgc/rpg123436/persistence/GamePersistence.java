package it.unicam.cs.mpgc.rpg123436.persistence;

import java.io.*;

public class GamePersistence {

    private static final String SAVE_FILE_NAME = "dungeon_save.dat";

    /**
     * Scrive i dati di salvataggio su file binarizzato
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
     * Legge il file di salvataggio se esiste
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