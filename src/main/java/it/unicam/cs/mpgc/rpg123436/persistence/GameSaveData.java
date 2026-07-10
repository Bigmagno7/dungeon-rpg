package it.unicam.cs.mpgc.rpg123436.persistence;

import java.io.Serializable;
import java.util.List;

public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // Dati del Livello e della Mappa
    private final int currentLevel;
    private final char[][] mapGrid;
    private final int rows;
    private final int cols;

    // Dati dell'Eroe
    private final int heroX;
    private final int heroY;
    private final int heroHp;

    // Dati del Mostro
    private final int monsterX;
    private final int monsterY;
    private final int monsterHp;

    // Diario di gioco
    private final List<String> combatLog;

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