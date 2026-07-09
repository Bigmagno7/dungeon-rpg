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
        moveMonsterRandom();

        return true;
    }

    public void moveMonsterRandom() {
        int deltaX = rand.nextInt(3) - 1; // -1, 0, o 1
        int deltaY = rand.nextInt(3) - 1; // -1, 0, o 1

        // Evita movimenti diagonali fulminei
        if (deltaX != 0 && deltaY != 0) {
            deltaY = 0;
        }

        int nextX = monster.getX() + deltaX;
        int nextY = monster.getY() + deltaY;

        // Controlla che non esca dai bordi e non colpisca muri
        if (nextY >= 0 && nextY < map.getRows() && nextX >= 0 && nextX < map.getCols()) {
            if (map.getGrid()[nextY][nextX] != '#') {
                monster.setX(nextX);
                monster.setY(nextY);
                System.out.println("LOG -> Mostro si è mosso a: X=" + nextX + " Y=" + nextY);
                return;
            }
        }
        System.out.println("LOG -> Il mostro ha provato a muoversi ma ha sbattuto contro un muro!");
    }

    public DungeonMap getMap() { return this.map; }
    public Hero getHero() { return this.hero; }
    public Monster getMonster() { return this.monster; }
}