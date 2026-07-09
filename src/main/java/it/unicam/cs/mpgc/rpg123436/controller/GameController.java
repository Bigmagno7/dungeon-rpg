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
        this.hero.setHp(hero.getHp());
        this.monster = new Monster("Orco", 30, 6, 7, 2);
        combatLog.add("Benvenuto! Livello " + currentLevel + " - HP: 50");
    }

    public boolean handleHeroMovement(int deltaX, int deltaY) {
        if (isGameOver || isGameWon) return false;

        // 1. Calcoliamo DOVE l'eroe vorrebbe andare
        int nextX = hero.getX() + deltaX;
        int nextY = hero.getY() + deltaY;

        // 2. CONTROLLO ATTACCO IMMEDIATO: Se l'orco è vivo ed è esattamente nella casella dove sto andando, lo MENO subito!
        if (monster.getHp() > 0 && nextX == monster.getX() && nextY == monster.getY()) {
            executeCombatTurn();
            return true; // Turno finito! Ho attaccato io per primo, l'eroe resta fermo e il mostro non si muove!
        }

        // 3. Blocco sui muri (se non sto attaccando)
        if (map.getGrid()[nextY][nextX] == '#') return false;

        // 4. Se la via è libera e non sto attaccando, muovo l'eroe
        hero.setX(nextX);
        hero.setY(nextY);

        // Controllo medikit
        if (map.getGrid()[nextY][nextX] == 'H') {
            hero.setHp(Math.min(50, hero.getHp() + 5));
            map.getGrid()[nextY][nextX] = '.';
            logMessage("❤️ Raccolto Medikit! +5 HP");
        }

        // Controllo porta livello successivo
        if (map.getGrid()[nextY][nextX] == 'E') {
            if (currentLevel == 5) {
                isGameWon = true;
                logMessage("👑 COMPLIMENTI! HAI VINTO IL GIOCO! 👑");
            } else {
                nextLevel();
            }
            return true; // CRUCIALE: Ferma il turno qui! Evita che il nuovo orco si muova nel vecchio turno!
        }

        // 5. SOLO SE NON C'È STATO COMBATTIMENTO, il mostro fa il suo passo verso di te
        if (monster.getHp() > 0) {
            moveMonsterWithCollision();
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
            logMessage("💀 Mostro ucciso! La via è libera!");
            return;
        }

        // Se sopravvive, contrattacca subito
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
    /**
     * Muove il mostro verso l'eroe con la BFS.
     * Se il mostro raggiunge l'eroe, si ferma davanti a lui SENZA attaccare preventivamente!
     */
    private void moveMonsterWithCollision() {
        int startX = monster.getX();
        int startY = monster.getY();
        int targetX = hero.getX();
        int targetY = hero.getY();

        // Se sono già adiacenti o sopra, il mostro non si muove, aspetta solo di essere menato o contrattacca
        if (Math.abs(startX - targetX) + Math.abs(startY - targetY) <= 1) {
            return;
        }

        int rows = map.getRows();
        int cols = map.getCols();
        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] dirs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        queue.add(new Node(startX, startY, null));
        visited[startY][startX] = true;
        Node targetNode = null;

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

        if (targetNode != null) {
            Node p = targetNode;
            while (p.parent != null && (p.parent.x != startX || p.parent.y != startY)) {
                p = p.parent;
            }

            // BLOCCO DI SICUREZZA ASSOLUTO: Se il prossimo passo della BFS coincide con l'eroe,
            // il mostro RESTA FERMO dove si trova. Non gli sale sopra MAI.
            if (p.x == hero.getX() && p.y == hero.getY()) {
                return; // Non ti muovere, resta a distanza 1!
            } else {
                monster.setX(p.x);
                monster.setY(p.y);
            }
        }
    }
    private void nextLevel() {
        currentLevel++;
        combatLog.clear();
        logMessage("🎉 Benvenuto al livello " + currentLevel + "!");

        this.map = new DungeonMap(10, 10, currentLevel);
        hero.setX(1);
        hero.setY(1);

        int newHp = 10 + (currentLevel * 10);
        int newDmg = 4 + currentLevel;

        // COORDINATE BLINDATE: distanze matematicamente dispari da [1,1] per evitare la sovrapposizione nello stesso frame!
        int spawnX = 7;
        int spawnY = 2; // Livello 1 standard

        if (currentLevel == 2) {
            spawnX = 7;
            spawnY = 8; // Distanza dispari garantita
        }
        else if (currentLevel == 3) {
            spawnX = 6;
            spawnY = 2;
        }
        else if (currentLevel == 4) {
            spawnX = 7;
            spawnY = 6; // Modificato per evitare il bug del livello 4!
        }
        else if (currentLevel == 5) {
            spawnX = 7;
            spawnY = 7;
        }

        this.monster = new Monster("Orco Lvl " + currentLevel, newHp, newDmg, spawnX, spawnY);
        logMessage("👹 Apparso " + monster.getType() + " con " + newHp + " HP!");
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