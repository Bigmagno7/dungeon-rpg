package it.unicam.cs.mpgc.rpg123436.model;

import java.io.Serializable;
/**
 * Rappresenta un mostro nel dungeon.
 */
public class Monster implements Serializable {

    private final String type;
    private int hp;
    private final int damage;

    // ATTENZIONE: NON devono essere final, altrimenti non si muoverà mai!
    private int x;
    private int y;

    public Monster(String type, int hp, int damage, int x, int y) {
        this.type = type;
        this.hp = hp;
        this.damage = damage;
        this.x = x;
        this.y = y;
    }

    // GETTER
    public String getType() { return type; }
    public int getHp() { return hp; }
    public int getDamage() { return damage; }
    public int getX() { return x; }
    public int getY() { return y; }

    // SETTER (Controlla che assegnino a this.x e this.y)
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