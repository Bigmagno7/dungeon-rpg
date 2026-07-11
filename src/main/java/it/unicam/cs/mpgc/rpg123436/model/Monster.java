package it.unicam.cs.mpgc.rpg123436.model;

import java.io.Serializable;

/**
 * Rappresenta un mostro presente all'interno del dungeon.
 *
 * La classe appartiene al Model e contiene solamente i dati
 * relativi allo stato del nemico: tipologia, punti vita,
 * danno inflitto e posizione sulla mappa.
 *
 * Implementa Serializable per permettere il salvataggio
 * dello stato della partita.
 */
public class Monster implements Serializable {

    private final String type;
    private int hp;
    private final int damage;

    private int x;
    private int y;

    /**
     * Crea un nuovo mostro con le caratteristiche iniziali specificate.
     *
     * @param type tipologia del mostro
     * @param hp punti vita iniziali
     * @param damage danno inflitto negli attacchi
     * @param x posizione iniziale sull'asse orizzontale
     * @param y posizione iniziale sull'asse verticale
     */
    public Monster(String type, int hp, int damage, int x, int y) {
        this.type = type;
        this.hp = hp;
        this.damage = damage;
        this.x = x;
        this.y = y;
    }

    public String getType() { return type; }
    public int getHp() { return hp; }
    public int getDamage() { return damage; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}