package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.model.Hero;
import it.unicam.cs.mpgc.rpg123436.model.Monster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    private DungeonMap map;
    private final Hero hero;
    private Monster monster;
    private final List<String> combatLog = new ArrayList<>();
    private final Random rand = new Random();

    private int currentLevel = 1;
    private boolean isGameOver = false;

    public GameController() {
        this.map = new DungeonMap(10, 10, currentLevel);
        this.hero = new Hero("Mago", 1, 1);
        this.monster = new Monster("Orco", 30, 8, 7, 2);
        combatLog.add("Benvenuto nel Labirinto! Livello " + currentLevel);
    }

    public boolean handleHeroMovement(int deltaX, int deltaY) {
        if (isGameOver) return false;

        int newX = hero.getX() + deltaX;
        int newY = hero.getY() + deltaY;

        if (map.getGrid()[newY][newX] == '#') {
            return false;
        }

        // Verifica scontro con l'orco VIVO
        if (monster.getHp() > 0 && newX == monster.getX() && newY == monster.getY()) {
            executeCombatTurn();
            return true;
        }

        hero.setX(newX);
        hero.setY(newY);

        if (map.getGrid()[newY][newX] == 'E') {
            nextLevel();
            return true;
        }

        // Turno del mostro
        if (monster.getHp() > 0 && !isGameOver) {
            moveMonsterTowardsHero();
            if (monster.getX() == hero.getX() && monster.getY() == hero.getY()) {
                executeMonsterAttack();
            }
        }

        return true;
    }

    private void executeCombatTurn() {
        int heroDamage = 10;
        monster.setHp(monster.getHp() - heroDamage);
        logMessage("⚔️ Hai colpito il mostro per " + heroDamage + " danni!");

        if (monster.getHp() <= 0) {
            monster.setHp(0);
            logMessage("💀 Mostro sconfitto! Raggiungi l'uscita!");
            return;
        }

        executeMonsterAttack();
    }

    private void executeMonsterAttack() {
        int monsterDamage = monster.getDamage();
        hero.setHp(hero.getHp() - monsterDamage);
        logMessage("💥 Subisci " + monsterDamage + " danni!");

        if (hero.getHp() <= 0) {
            hero.setHp(0);
            isGameOver = true;
            logMessage("🚨 SEI MORTO! GAME OVER 🚨");
        }
    }

    private void nextLevel() {
        currentLevel++;
        logMessage("🎉 Sei passato al livello " + currentLevel + "!");

        // Genera la nuova mappa basata sul livello!
        this.map = new DungeonMap(10, 10, currentLevel);

        hero.setX(1);
        hero.setY(1);

        // Nuovo orco con vita resettata e potenziata
        int newHp = 20 + (currentLevel * 10);
        int newDmg = 5 + (currentLevel * 2);

        // Cambiamo posizione di spawn al livello 2 per non farlo incastrare
        int spawnX = (currentLevel % 2 == 0) ? 2 : 7;
        int spawnY = (currentLevel % 2 == 0) ? 7 : 2;

        this.monster = new Monster("Orco Elite v" + currentLevel, newHp, newDmg, spawnX, spawnY);
        logMessage("👹 Apparso " + monster.getType() + " con " + newHp + " HP!");
    }

    /**
     * Muove il mostro verso l'eroe usando l'algoritmo BFS per aggirare i muri.
     */
    public void moveMonsterTowardsHero() {
        int startX = monster.getX();
        int startY = monster.getY();
        int targetX = hero.getX();
        int targetY = hero.getY();

        // Se sono già sulla stessa casella, non muoverti
        if (startX == targetX && startY == targetY) return;

        int rows = map.getRows();
        int cols = map.getCols();

        // Strutture per la BFS
        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        // Direzioni possibili (Su, Giù, Sinistra, Destra)
        int[][] dirs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        // Partiamo dalla posizione attuale del mostro
        queue.add(new Node(startX, startY, null));
        visited[startY][startX] = true;

        Node targetNode = null;

        // Ciclo della BFS per trovare l'eroe
        while (!queue.isEmpty()) {
            Node curr = queue.poll();

            if (curr.x == targetX && curr.y == targetY) {
                targetNode = curr;
                break;
            }

            for (int[] d : dirs) {
                int nextX = curr.x + d[0];
                int nextY = curr.y + d[1];

                if (nextX >= 0 && nextX < cols && nextY >= 0 && nextY < rows) {
                    if (map.getGrid()[nextY][nextX] != '#' && !visited[nextY][nextX]) {
                        visited[nextY][nextX] = true;
                        queue.add(new Node(nextX, nextY, curr));
                    }
                }
            }
        }

        // Se abbiamo trovato un percorso, risaliamo al PRIMO passo da fare
        if (targetNode != null) {
            Node p = targetNode;
            // Risaliamo la catena dei nodi fino a trovare quello subito dopo l'inizio
            while (p.parent != null && (p.parent.x != startX || p.parent.y != startY)) {
                p = p.parent;
            }
            // Facciamo fare il passo intelligente al mostro
            monster.setX(p.x);
            monster.setY(p.y);
        }
    }

    // Classe di supporto interna da mettere IN FONDO a GameController.java (prima dell'ultima })
    private static class Node {
        int x, y;
        Node parent;

        Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }
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
}