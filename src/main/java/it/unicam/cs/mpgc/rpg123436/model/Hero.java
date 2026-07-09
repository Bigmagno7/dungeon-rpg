package it.unicam.cs.mpgc.rpg123436.model;

/**
 * Rappresenta l'eroe controllato dal giocatore.
 */
public class Hero {

    private final String name;
    private int hp;
    private int x;
    private int y;

    /**
     * Costruttore dell'Eroe.
     * Inizializza il nome, la vita iniziale a 100 e la posizione sulla griglia.
     */
    public Hero(String name, int startX, int startY) {
        this.name = name;
        this.hp = 100; // Parte con 100 HP di default
        this.x = startX;
        this.y = startY;
    }

    // --- GETTER ---
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // --- SETTER ---
    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}