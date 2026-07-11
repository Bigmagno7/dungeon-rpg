package it.unicam.cs.mpgc.rpg123436.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * Rappresenta una copia dello stato della partita utilizzata
 * per il sistema di persistenza.
 *
 * Contiene tutte le informazioni necessarie per ricostruire
 * una partita precedentemente salvata: livello corrente,
 * configurazione della mappa, stato dell'eroe, stato del mostro
 * e log degli eventi di gioco.
 *
 * Implementa Serializable per permettere la conversione
 * dell'oggetto in un flusso di byte salvabile su file.
 */
public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // Informazioni relative al livello e alla configurazione della mappa
    private final int currentLevel;
    private final char[][] mapGrid;
    private final int rows;
    private final int cols;

    // Informazioni relative allo stato dell'eroe
    private final int heroX;
    private final int heroY;
    private final int heroHp;

    // Informazioni relative allo stato del mostro
    private final int monsterX;
    private final int monsterY;
    private final int monsterHp;

    // Cronologia degli eventi mostrati al giocatore
    private final List<String> combatLog;

    /**
     * Costruisce un nuovo oggetto contenente lo stato
     * corrente della partita.
     *
     * @param currentLevel livello raggiunto dal giocatore
     * @param mapGrid configurazione della mappa corrente
     * @param rows numero di righe della mappa
     * @param cols numero di colonne della mappa
     * @param heroX posizione orizzontale dell'eroe
     * @param heroY posizione verticale dell'eroe
     * @param heroHp punti vita attuali dell'eroe
     * @param monsterX posizione orizzontale del mostro
     * @param monsterY posizione verticale del mostro
     * @param monsterHp punti vita attuali del mostro
     * @param combatLog lista degli eventi di gioco
     */
    public GameSaveData(int currentLevel, char[][] mapGrid, int rows, int cols,
                        int heroX, int heroY, int heroHp,
                        int monsterX, int monsterY, int monsterHp,
                        List<String> combatLog) {
        this.currentLevel = currentLevel;
        this.mapGrid = mapGrid;
        this.rows = rows;
        this.cols = cols;
        this.heroX = heroX;
        this.heroY = heroY;
        this.heroHp = heroHp;
        this.monsterX = monsterX;
        this.monsterY = monsterY;
        this.monsterHp = monsterHp;
        this.combatLog = combatLog;
    }

    // Getters per ricostruire il gioco al caricamento
    public int getCurrentLevel() { return currentLevel; }
    public char[][] getMapGrid() { return mapGrid; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getHeroX() { return heroX; }
    public int getHeroY() { return heroY; }
    public int getHeroHp() { return heroHp; }
    public int getMonsterX() { return monsterX; }
    public int getMonsterY() { return monsterY; }
    public int getMonsterHp() { return monsterHp; }
    public List<String> getCombatLog() { return combatLog; }
}