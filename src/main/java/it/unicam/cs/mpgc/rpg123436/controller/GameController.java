package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.model.Hero;
import it.unicam.cs.mpgc.rpg123436.model.Monster;
import java.util.Random;

public class GameController {

    private final DungeonMap map;
    private final Hero hero;
    private final Monster monster;
    private final Random rand = new Random();

    public GameController() {
        this.map = new DungeonMap(10, 10);
        this.hero = new Hero("Cavaliere", 1, 1);
        // Lo spawniamo a (7, 2) così è sicuramente sul pavimento libero
        this.monster = new Monster("Goblin", 30, 5, 7, 2);
    }

    public boolean handleHeroMovement(int deltaX, int deltaY) {
        int newX = hero.getX() + deltaX;
        int newY = hero.getY() + deltaY;

        // Blocco sui muri esterni o interni
        if (map.getGrid()[newY][newX] == '#') {
            return false;
        }

        // Blocco se va sopra il mostro
        if (newX == monster.getX() && newY == monster.getY()) {
            System.out.println("LOG -> collisione con il mostro!");
            return false;
        }

        hero.setX(newX);
        hero.setY(newY);

        // FORZIAMO il movimento random del mostro qui dentro
        moveMonsterTowardsHero();

        return true;
    }

    /**
     * Muove il mostro verso l'eroe in modo fluido e intelligente (solo Nord, Sud, Est, Ovest).
     */
    public void moveMonsterTowardsHero() {
        int monsterX = monster.getX();
        int monsterY = monster.getY();
        int heroX = hero.getX();
        int heroY = hero.getY();

        int deltaX = 0;
        int deltaY = 0;

        // Scegliamo se muoverci prima su X o su Y in base a dove la distanza è maggiore
        if (Math.abs(heroX - monsterX) > Math.abs(heroY - monsterY)) {
            // Conviene muoversi in orizzontale
            deltaX = (heroX > monsterX) ? 1 : -1;
        } else {
            // Conviene muoversi in verticale
            deltaY = (heroY > monsterY) ? 1 : -1;
        }

        int nextX = monsterX + deltaX;
        int nextY = monsterY + deltaY;

        // CONTROLLO COLLISIONE: Se la casella verso l'eroe è libera, si muove lì
        if (nextY >= 0 && nextY < map.getRows() && nextX >= 0 && nextX < map.getCols()) {
            if (map.getGrid()[nextY][nextX] != '#') {
                monster.setX(nextX);
                monster.setY(nextY);
                return;
            }
        }

        // SE LA VIA PRINCIPALE È BLOCCATA DA UN MURO, prova l'altra direzione (Aggiramento)
        if (deltaX != 0) { // Stava provando ad andare in orizzontale
            deltaY = (heroY > monsterY) ? 1 : -1;
            deltaX = 0;
        } else { // Stava provando ad andare in verticale
            deltaX = (heroX > monsterX) ? 1 : -1;
            deltaY = 0;
        }

        nextX = monsterX + deltaX;
        nextY = monsterY + deltaY;

        if (nextY >= 0 && nextY < map.getRows() && nextX >= 0 && nextX < map.getCols()) {
            if (map.getGrid()[nextY][nextX] != '#') {
                monster.setX(nextX);
                monster.setY(nextY);
            }
        }
    }

    public DungeonMap getMap() { return this.map; }
    public Hero getHero() { return this.hero; }
    public Monster getMonster() { return this.monster; }
}