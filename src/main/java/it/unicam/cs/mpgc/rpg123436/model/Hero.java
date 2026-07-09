package it.unicam.cs.mpgc.rpg123436.model;

/**
 * Rappresenta l'Eroe controllato dal giocatore.
 * Tiene traccia delle statistiche vitali e della posizione nel Dungeon.
 */
public class Hero {
    private final String name;
    private int hp;
    private int maxHp;
    private int gold;
    // Coordinate sulla griglia della mappa
    private int x;
    private int y;

    public Hero(String name, int startX, int startY) {
        this.name = name;
        this.maxHp = 100;
        this.hp = maxHp;
        this.gold = 0;
        this.x = startX;
        this.y = startY;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    // Getter e Setter per le statistiche
    public String getName() { return name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }
    public int getGold() { return gold; }
    public void addGold(int amount) { this.gold += amount; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}