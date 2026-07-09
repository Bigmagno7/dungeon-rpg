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

        int newX = hero.getX() + deltaX;
        int newY = hero.getY() + deltaY;

        // Blocco sui muri
        if (map.getGrid()[newY][newX] == '#') return false;

        // Se vai contro il mostro vivo: COMBATTIMENTO (l'eroe non avanza!)
        if (monster.getHp() > 0 && newX == monster.getX() && newY == monster.getY()) {
            executeCombatTurn();
            return true;
        }

        // Muove l'eroe solo se la casella è calpestabile
        hero.setX(newX);
        hero.setY(newY);

        // Controllo Medikit
        if (map.getGrid()[newY][newX] == 'H') {
            hero.setHp(Math.min(50, hero.getHp() + 5));
            map.getGrid()[newY][newX] = '.';
            logMessage("❤️ Raccolto Medikit! +5 HP");
        }

        // Controllo Porta
        if (map.getGrid()[newY][newX] == 'E') {
            if (currentLevel == 5) {
                isGameWon = true;
                logMessage("👑 COMPLIMENTI! HAI VINTO IL GIOCO! 👑");
            } else {
                nextLevel();
            }
            return true;
        }

        // Turno del mostro intelligente (solo se vivo)
        if (monster.getHp() > 0 && !isGameOver && !isGameWon) {
            moveMonsterTowardsHero();
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

        // HP crescenti per i livelli
        int newHp = 10 + (currentLevel * 10); // Liv 2 = 30 HP, Liv 3 = 40 HP...
        int newDmg = 4 + currentLevel;

        // Spawn sicuro per non nascere incastrato
        int spawnX = 7; int spawnY = 7;
        if (currentLevel == 2) { spawnX = 7; spawnY = 3; }
        else if (currentLevel == 3) { spawnX = 6; spawnY = 2; }
        else if (currentLevel == 4) { spawnX = 5; spawnY = 5; }
        else if (currentLevel == 5) { spawnX = 4; spawnY = 5; }

        this.monster = new Monster("Orco Lvl " + currentLevel, newHp, newDmg, spawnX, spawnY);
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