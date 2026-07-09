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
    private boolean isGameWon = false;

    public GameController() {
        this.map = new DungeonMap(10, 10, currentLevel);
        this.hero = new Hero("Mago", 1, 1);
        this.hero.setHp(50); // Parte con 50 HP reali
        this.monster = new Monster("Orco", 30, 6, 7, 2); // 30 HP liv 1
        combatLog.add("Benvenuto! Livello " + currentLevel + " - HP: 50");
    }

    public boolean handleHeroMovement(int deltaX, int deltaY) {
        if (isGameOver || isGameWon) return false;

        // Coordinate future dove l'eroe vorrebbe andare
        int nextX = hero.getX() + deltaX;
        int nextY = hero.getY() + deltaY;

        // 1. Blocco sui muri
        if (map.getGrid()[nextY][nextX] == '#') return false;

        // 2. CONTROLLO COLLISIONE: Il mostro è nella casella dove voglio andare?
        if (monster.getHp() > 0 && nextX == monster.getX() && nextY == monster.getY()) {
            // Spara il colpo stando FERMO nella posizione attuale!
            executeCombatTurn();

            // Il mostro è ancora vivo? Allora contrattacca subito stando fermo!
            if (monster.getHp() > 0 && !isGameOver) {
                executeMonsterAttack();
            }
            return true; // Blocca il turno qui! L'eroe NON si sposta sulla casella del mostro!
        }

        // 3. Se la casella è libera, allora (e solo allora) l'eroe fa il passo
        hero.setX(nextX);
        hero.setY(nextY);

        // Controllo medikit
        if (map.getGrid()[nextY][nextX] == 'H') {
            hero.setHp(Math.min(50, hero.getHp() + 5));
            map.getGrid()[nextY][nextX] = '.';
            logMessage("❤️ Raccolto Medikit! +5 HP");
        }

        // Controllo porta
        if (map.getGrid()[nextY][nextX] == 'E') {
            if (currentLevel == 5) {
                isGameWon = true;
                logMessage("👑 COMPLIMENTI! HAI VINTO IL GIOCO! 👑");
            } else {
                nextLevel();
            }
            return true;
        }

        // 4. Turno del mostro (si muove verso di te solo se non avete appena combattuto)
        if (monster.getHp() > 0 && !isGameOver && !isGameWon) {
            moveMonsterTowardsHero();
            // Se muovendosi ti viene addosso, ti mena
            if (monster.getX() == hero.getX() && monster.getY() == hero.getY()) {
                executeMonsterAttack();
            }
        }

        return true;
    }

    private void executeCombatTurn() {
        if (monster.getHp() <= 0) return;

        int heroDamage = 10;
        monster.setHp(monster.getHp() - heroDamage);
        logMessage("⚔️ Colpisci il mostro per " + heroDamage + " danni!");

        if (monster.getHp() <= 0) {
            monster.setHp(0);
            monster.setX(-1); // Lanciato fuori mappa per sicurezza assoluta
            monster.setY(-1);
            logMessage("💀 Mostro ucciso! La via è libera!");
            return;
        }

        executeMonsterAttack();
    }

    private void executeMonsterAttack() {
        if (monster.getHp() <= 0) return;

        int monsterDamage = monster.getDamage();
        hero.setHp(hero.getHp() - monsterDamage);
        logMessage("💥 Subisci " + monsterDamage + " danni!");

        if (hero.getHp() <= 0) {
            hero.setHp(0);
            isGameOver = true;
            logMessage("🚨 GAME OVER - SEI MORTO! 🚨");
        }
    }

    private void nextLevel() {
        currentLevel++;
        combatLog.clear();
        logMessage("🎉 Benvenuto al livello " + currentLevel + "!");

        this.map = new DungeonMap(10, 10, currentLevel);
        hero.setX(1);
        hero.setY(1);

        int newHp = 20 + (currentLevel * 5);
        int newDmg = 3 + currentLevel;

        // POSIZIONI DI SPAWN AGGIORNATE: Tutti i mostri nascono nell'angolo opposto [8, 8] o [8, 2] fisse, lontani da te!
        int spawnX = 8;
        int spawnY = 8;
        if (currentLevel == 2) { spawnX = 8; spawnY = 2; }
        else if (currentLevel == 3) { spawnX = 8; spawnY = 7; }
        else if (currentLevel == 4) { spawnX = 2; spawnY = 8; }

        this.monster = new Monster("Orco Lvl " + currentLevel, newHp, newDmg, spawnX, spawnY);
        logMessage("👹 Apparso " + monster.getType() + " con " + newHp + " HP!");
    }

    public void moveMonsterTowardsHero() {
        int startX = monster.getX(); int startY = monster.getY();
        int targetX = hero.getX(); int targetY = hero.getY();
        if (startX == -1 || (startX == targetX && startY == targetY)) return;

        int rows = map.getRows(); int cols = map.getCols();
        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] dirs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        queue.add(new Node(startX, startY, null));
        visited[startY][startX] = true;
        Node targetNode = null;

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            if (curr.x == targetX && curr.y == targetY) { targetNode = curr; break; }
            for (int[] d : dirs) {
                int nextX = curr.x + d[0]; int nextY = curr.y + d[1];
                if (nextX >= 0 && nextX < cols && nextY >= 0 && nextY < rows) {
                    if (map.getGrid()[nextY][nextX] != '#' && !visited[nextY][nextX]) {
                        visited[nextY][nextX] = true;
                        queue.add(new Node(nextX, nextY, curr));
                    }
                }
            }
        }
        if (targetNode != null) {
            Node p = targetNode;
            while (p.parent != null && (p.parent.x != startX || p.parent.y != startY)) p = p.parent;
            monster.setX(p.x); monster.setY(p.y);
        }
    }

    private static class Node {
        int x, y; Node parent;
        Node(int x, int y, Node parent) { this.x = x; this.y = y; this.parent = parent; }
    }

    private void logMessage(String msg) {
        combatLog.add(msg);
        if (combatLog.size() > 5) combatLog.remove(0);
    }

    public List<String> getCombatLog() { return combatLog; }
    public DungeonMap getMap() { return map; }
    public Hero getHero() { return hero; }
    public Monster getMonster() { return monster; }
    public int getCurrentLevel() { return currentLevel; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isGameWon() { return isGameWon; }
}