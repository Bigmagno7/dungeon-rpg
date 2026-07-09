package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.model.Hero;
import it.unicam.cs.mpgc.rpg123436.model.Monster;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private final DungeonMap map;
    private final Hero hero;
    private final Monster monster;
    // Lista di stringhe per memorizzare i messaggi di combattimento da mostrare nell'HUD
    private final List<String> combatLog = new ArrayList<>();

    public GameController() {
        this.map = new DungeonMap(10, 10);
        this.hero = new Hero("Mago", 1, 1);
        this.monster = new Monster("Orco", 30, 8, 7, 2);
        combatLog.add("Benvenuto nel Dungeon! Trova l'Orco.");
    }

    public boolean handleHeroMovement(int deltaX, int deltaY) {
        int newX = hero.getX() + deltaX;
        int newY = hero.getY() + deltaY;

        if (map.getGrid()[newY][newX] == '#') {
            return false;
        }

        // SE L'EROE VA SOPRA IL MOSTRO -> PARTE IL COMBATTIMENTO!
        if (monster.getHp() > 0 && newX == monster.getX() && newY == monster.getY()) {
            executeCombatTurn();
            return true;
        }

        hero.setX(newX);
        hero.setY(newY);

        // Se il mostro è vivo, si muove dopo il passo dell'eroe
        if (monster.getHp() > 0) {
            moveMonsterTowardsHero();
            // Controlliamo se muovendosi il mostro ha raggiunto l'eroe per attaccarlo
            if (monster.getX() == hero.getX() && monster.getY() == hero.getY()) {
                executeMonsterAttack();
            }
        }

        return true;
    }

    private void executeCombatTurn() {
        // 1. L'eroe attacca il mostro (danno fisso di esempio: 10)
        int heroDamage = 10;
        monster.setHp(monster.getHp() - heroDamage);
        logMessage("⚔️ Hai colpito l'Orco per " + heroDamage + " danni!");

        // 2. Controlliamo se il mostro è morto
        if (monster.getHp() <= 0) {
            logMessage("💀 L'Orco è stato sconfitto! Vittoria!");
            return;
        }

        // 3. Se il mostro è ancora vivo, contrattacca immediatamente
        executeMonsterAttack();
    }

    private void executeMonsterAttack() {
        int monsterDamage = monster.getDamage();
        // Nota: Assicurati che la tua classe Hero abbia un metodo setHp o un modo per gestire la vita.
        // Se non ce l'ha ancora, stampiamo solo il log per evitare crash.
        logMessage("💥 L'Orco ti contrattacca e ti fa " + monsterDamage + " danni!");
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
        // Teniamo solo gli ultimi 5 messaggi per non intasare la schermata
        if (combatLog.size() > 5) {
            combatLog.remove(0);
        }
    }

    public List<String> getCombatLog() { return combatLog; }
    public DungeonMap getMap() { return map; }
    public Hero getHero() { return hero; }
    public Monster getMonster() { return monster; }
}