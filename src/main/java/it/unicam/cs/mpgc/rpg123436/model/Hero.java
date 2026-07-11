package it.unicam.cs.mpgc.rpg123436.model;

import java.io.Serializable;

/**
 * Rappresenta l'eroe controllato dal giocatore.
 *
 * La classe appartiene al Model e contiene lo stato del personaggio
 * principale: nome, punti vita e posizione all'interno della griglia.
 *
 * Implementa Serializable per permettere il salvataggio e il ripristino
 * dello stato della partita.
 */
public class Hero implements Serializable {

    private final String name;
    private final int maxHp;
    private int hp;
    private int x;
    private int y;

    /**
     * Costruttore dell'Eroe.
     * Inizializza il nome, la vita iniziale a 100 e la posizione sulla griglia.
     */
    public Hero(String name, int startX, int startY) {
        this.name = name;
        this.maxHp=100;
        this.hp = maxHp;
        this.x = startX;
        this.y = startY;
    }

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

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMaxHp() {
        return maxHp;
    }
}