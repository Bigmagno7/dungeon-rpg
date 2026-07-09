package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.model.Hero;
import it.unicam.cs.mpgc.rpg123436.model.Monster;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private DungeonMap map;
    private final Hero hero;
    private Monster monster;
    private final List<String> combatLog = new ArrayList<>();

    private int currentLevel = 1;
    private boolean isGameOver = false;

    public GameController() {
        this.map = new DungeonMap(10, 10);
        this.hero = new Hero("Mago", 1, 1);
        // Il mostro parte in una stanza del labirinto
        this.monster = new Monster("Orco", 30, 8, 7, 2);
        combatLog.add("Benvenuto nel Labirinto! Livello " + currentLevel);
    }

    public boolean handleHeroMovement(int deltaX, int deltaY) {
        // Se è Game Over, i tasti non fanno muovere nessuno!
        if (isGameOver) return false;

        int newX = hero.getX() + deltaX;
        int newY = hero.getY() + deltaY;

        // Blocco sui muri
        if (map.getGrid()[newY][newX] == '#') {
            return false;
        }

        // SE L'EROE VA SOPRA IL MOSTRO VIVO -> COMBATTIMENTO
        if (monster.getHp() > 0 && newX == monster.getX() && newY == monster.getY()) {
            executeCombatTurn();
            return true;
        }

        // Muoviamo l'eroe
        hero.setX(newX);
        hero.setY(newY);

        // CONTROLLO USCITA: Se l'eroe va sulla porta 'E'
        if (map.getGrid()[newY][newX] == 'E') {
            nextLevel();
            return true;
        }

        // Turno del Mostro (solo se è vivo ed l'eroe non è morto)
        if (monster.getHp() > 0 && !isGameOver) {
            moveMonsterTowardsHero();
            // Se il mostro raggiunge l'eroe dopo essersi mosso, lo attacca
            if (monster.getX() == hero.getX() && monster.getY() == hero.getY()) {
                executeMonsterAttack();
            }
        }

        return true;
    }

    private void executeCombatTurn() {
        // L'eroe fa 10 danni
        int heroDamage = 10;
        monster.setHp(monster.getHp() - heroDamage);
        logMessage("⚔️ Hai colpito l'Orco per " + heroDamage + " danni!");

        if (monster.getHp() <= 0) {
            logMessage("💀 Orco sconfitto! La porta per il prossimo livello è aperta!");
            return;
        }

        // Contrattacco del mostro
        executeMonsterAttack();
    }

    private void executeMonsterAttack() {
        int monsterDamage = monster.getDamage();
        hero.setHp(hero.getHp() - monsterDamage);
        logMessage("💥 L'Orco ti fa " + monsterDamage + " danni!");

        // Controllo della morte dell'Eroe (GAME OVER)
        if (hero.getHp() <= 0) {
            hero.setHp(0);
            isGameOver = true;
            logMessage("🚨 SEI MORTO! GAME OVER 🚨");
        }
    }

    /**
     * Passaggio al livello successivo: rigenera labirinto e potenzia il mostro.
     */
    private void nextLevel() {
        currentLevel++;
        logMessage("🎉 Sei passato al livello " + currentLevel + "!");

        // Rigeneriamo il labirinto
        this.map = new DungeonMap(10, 10);

        // Resettiamo la posizione dell'eroe all'inizio
        hero.setX(1);
        hero.setY(1);

        // Creiamo un mostro nuovo e più forte in base al livello!
        int newHp = 20 + (currentLevel * 10);
        int newDmg = 6 + (currentLevel * 2);
        this.monster = new Monster("Orco Elite", newHp, newDmg, 7, 2);

        logMessage("👹 Un nuovo " + monster.getType() + " è apparso (HP: " + newHp + ")!");
    }

    public void moveMonsterTowardsHero() {
        int monsterX = monster.getX();
        int monsterY = monster.getY();
        int heroX = hero.getX();
        int heroY = hero.getY();

        int deltaX = 0, deltaY = 0;
        if (Math.abs(heroX - monsterX) > Math.abs(heroY - monsterY)) {
            deltaX = (heroX > monsterX) ? 1 : -1;
        } else {
            deltaY = (heroY > monsterY) ? 1 : -1;
        }

        int nextX = monsterX + deltaX;
        int nextY = monsterY + deltaY;

        if (map.getGrid()[nextY][nextX] != '#') {
            monster.setX(nextX);
            monster.setY(nextY);
        }
    }

    private void logMessage(String msg) {
        combatLog.add(msg);
        if (combatLog.size() > 6) combatLog.remove(0);
    }

    public List<String> getCombatLog() { return combatLog; }
    public DungeonMap getMap() { return map; }
    public Hero getHero() { return hero; }
    public Monster getMonster() { return monster; }
    public int getCurrentLevel() { return currentLevel; }
    public boolean isGameOver() { return isGameOver; }
}