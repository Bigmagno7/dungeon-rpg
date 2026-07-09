package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.model.Hero;
import it.unicam.cs.mpgc.rpg123436.model.Monster;

/**
 * Fa da ponte (Controller) tra il Model (Dati) e la View (Grafica).
 * Gestisce i turni di movimento dell'eroe e l'intelligenza artificiale del mostro.
 */
public class GameController {

    private final DungeonMap map;
    private final Hero hero;
    private final Monster monster;

    public GameController() {
        // Inizializziamo il mondo di gioco (Mappa 10x10, Eroe e un Mostro)
        this.map = new DungeonMap(10, 10);

        // Passiamo solo: Nome, X iniziale, Y iniziale
        this.hero = new Hero("Cavaliere", 1, 1);

        // Passiamo: Tipo, HP, Danno, X iniziale, Y iniziale (ne mancava uno!)
        this.monster = new Monster("Goblin", 30, 5, 2, 2);
    }

    /**
     * Gestisce il movimento dell'eroe. Se si muove, subito dopo fa muovere il mostro.
     */
    public boolean handleHeroMovement(int deltaX, int deltaY) {
        int newX = hero.getX() + deltaX;
        int newY = hero.getY() + deltaY;

        // Controlliamo se la cella d'arrivo è calpestabile (non è un muro '#')
        if (map.getGrid()[newY][newX] != '#') {
            hero.setX(newX);
            hero.setY(newY);

            // SISTEMA A TURNI: subito dopo l'eroe, si muove l'orco!
            moveMonsterTowardsHero();

            return true;
        }
        return false;
    }

    /**
     * Intelligenza artificiale del mostro: calcola la posizione dell'eroe e lo insegue.
     */
    public void moveMonsterTowardsHero() {
        // Se il mostro è morto, non fa nulla
        if (monster.getHp() <= 0) {
            return;
        }

        int monsterX = monster.getX();
        int monsterY = monster.getY();
        int heroX = hero.getX();
        int heroY = hero.getY();

        int nextX = monsterX;
        int nextY = monsterY;

        // Inseguimento sull'asse X
        if (monsterX < heroX) {
            nextX++;
        } else if (monsterX > heroX) {
            nextX--;
        }

        // Inseguimento sull'asse Y
        if (monsterY < heroY) {
            nextY++;
        } else if (monsterY > heroY) {
            nextY--;
        }

        // Muoviamo l'orco solo se la casella di destinazione non è un muro
        if (map.getGrid()[monsterY][nextX] != '#') {
            monster.setX(nextX);
        }
        if (map.getGrid()[nextY][monster.getX()] != '#') {
            monster.setY(nextY);
        }
    }

    // Metodi Getter per dare i dati alla View
    public DungeonMap getMap() { return map; }
    public Hero getHero() { return hero; }
    public Monster getMonster() { return monster; }
}