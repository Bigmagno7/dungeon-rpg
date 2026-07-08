package it.unicam.cs.mpgc.rpg123436.model;

/**
 * Rappresenta i mostri ostili presenti nel Dungeon.
 */
public class Monster {
    private final String type;
    private int hp;
    private final int damage;
    private final int x;
    private final int y;

    public Monster(String type, int hp, int damage, int x, int y) {
        this.type = type;
        this.hp = hp;
        this.damage = damage;
        this.x = x;
        this.y = y;
    }

    public String getType() { return type; }
    public int getHp() { return hp; }
    public void takeDamage(int amount) { this.hp = Math.max(0, this.hp - amount); }
    public int getDamage() { return damage; }
    public int getX() { return x; }
    public int getY() { return y; }
}