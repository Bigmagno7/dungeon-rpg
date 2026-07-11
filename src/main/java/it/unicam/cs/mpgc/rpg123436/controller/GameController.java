package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.model.Hero;
import it.unicam.cs.mpgc.rpg123436.model.Monster;
import it.unicam.cs.mpgc.rpg123436.persistence.GameSaveData;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller principale dell'applicazione.
 *
 * Gestisce il ciclo di gioco coordinando le interazioni tra il modello
 * (Hero, Monster, DungeonMap) e la logica applicativa.
 *
 * Si occupa della gestione dei movimenti del giocatore, dei combattimenti,
 * della progressione dei livelli, del comportamento dei nemici tramite BFS
 * e delle operazioni di salvataggio e caricamento dello stato di gioco.
 */
public class GameController {

    private DungeonMap map;
    private final Hero hero;
    private Monster monster;
    private final List<String> combatLog = new ArrayList<>();

    private int currentLevel = 1;
    private boolean isGameOver = false;
    private boolean isGameWon = false;

    /**
     * Inizializza una nuova partita creando la mappa iniziale,
     * il personaggio principale e il primo nemico del dungeon.
     */
    public GameController() {
        this.map = new DungeonMap(10, 10, currentLevel);
        this.hero = new Hero("Mago", 1, 1);
        this.hero.setHp(hero.getHp());
        this.monster = new Monster("Orco", 30, 6, 7, 2);
        combatLog.add("Benvenuto! Livello " + currentLevel + " - HP: 50");
    }
    /**
     * Gestisce il movimento dell'eroe all'interno della mappa.
     *
     * Il metodo controlla:
     * - collisioni con ostacoli;
     * - combattimento con i nemici;
     * - raccolta dei medikit;
     * - cambio livello;
     * - aggiornamento del turno del mostro.
     *
     * @param deltaX variazione orizzontale della posizione
     * @param deltaY variazione verticale della posizione
     * @return true se il movimento viene eseguito correttamente
     */
    public boolean handleHeroMovement(int deltaX, int deltaY) {
        if (isGameOver || isGameWon) return false;

        int nextX = hero.getX() + deltaX;
        int nextY = hero.getY() + deltaY;

        if (monster.getHp() > 0 && nextX == monster.getX() && nextY == monster.getY()) {
            executeCombatTurn();
            return true;
        }

        if (map.getGrid()[nextY][nextX] == '#') return false;

        hero.setX(nextX);
        hero.setY(nextY);

        if (map.getGrid()[nextY][nextX] == 'H') {
            hero.setHp(Math.min(hero.getMaxHp(), hero.getHp() + 5));
            map.getGrid()[nextY][nextX] = '.';
            logMessage("❤️ Raccolto Medikit! +5 HP");
        }

        if (map.getGrid()[nextY][nextX] == 'E') {
            if (currentLevel == 5) {
                isGameWon = true;
                logMessage("👑 COMPLIMENTI! HAI VINTO IL GIOCO! 👑");
            } else {
                nextLevel();
            }
            return true;
        }

        if (monster.getHp() > 0) {
            moveMonsterWithCollision();
        }

        return true;
    }
    /**
     * Gestisce un turno di combattimento tra eroe e mostro.
     *
     * L'eroe attacca per primo e, se il nemico sopravvive,
     * viene eseguito il contrattacco del mostro.
     */
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
    /**
     * Applica il danno del mostro all'eroe e verifica
     * l'eventuale conclusione della partita.
     */
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
     * Calcola il movimento del mostro verso l'eroe utilizzando
     * l'algoritmo Breadth First Search (BFS).
     *
     * L'algoritmo permette al nemico di trovare il percorso minimo
     * evitando muri e ostacoli presenti nella mappa.
     */
    private void moveMonsterWithCollision() {
        int startX = monster.getX();
        int startY = monster.getY();
        int targetX = hero.getX();
        int targetY = hero.getY();

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


            if (p.x == hero.getX() && p.y == hero.getY()) {
                return; // Non ti muovere, resta a distanza 1!
            } else {
                monster.setX(p.x);
                monster.setY(p.y);
            }
        }
    }
    /**
     * Gestisce il passaggio al livello successivo.
     *
     * Aggiorna la mappa, incrementa la difficoltà del mostro
     * e ricrea l'ambiente di gioco.
     */
    private void nextLevel() {
        currentLevel++;
        combatLog.clear();
        logMessage("🎉 Benvenuto al livello " + currentLevel + "!");

        this.map = new DungeonMap(10, 10, currentLevel);
        hero.setX(1);
        hero.setY(1);

        int newHp = 10 + (currentLevel * 10);
        int newDmg = 4 + currentLevel;

        int spawnX = 7;
        int spawnY = 2;

        if (currentLevel == 2) {
            spawnX = 7;
            spawnY = 8;
        }
        else if (currentLevel == 3) {
            spawnX = 6;
            spawnY = 2;
        }
        else if (currentLevel == 4) {
            spawnX = 7;
            spawnY = 6;
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
    /**
     * Crea una copia dello stato corrente della partita
     * utilizzata per il sistema di persistenza.
     *
     * @return snapshot serializzabile del gioco
     */
    public GameSaveData createSaveSnapshot() {
        return new it.unicam.cs.mpgc.rpg123436.persistence.GameSaveData(
                this.currentLevel,
                this.map.getGrid(),
                this.map.getRows(),
                this.map.getCols(),
                this.hero.getX(),
                this.hero.getY(),
                this.hero.getHp(),
                this.monster.getX(),
                this.monster.getY(),
                this.monster.getHp(),
                new java.util.ArrayList<>(this.combatLog)
        );
    }
    /**
     * Ripristina lo stato della partita partendo
     * dai dati contenuti in un salvataggio precedente.
     *
     * @param data dati salvati della partita
     */
    public void restoreFromSnapshot(GameSaveData data) {
        this.currentLevel = data.getCurrentLevel();
        this.map = new DungeonMap(data.getRows(), data.getCols(), this.currentLevel);

        for (int r = 0; r < data.getRows(); r++) {
            System.arraycopy(data.getMapGrid()[r], 0, this.map.getGrid()[r], 0, data.getCols());
        }

        this.hero.setX(data.getHeroX());
        this.hero.setY(data.getHeroY());
        this.hero.setHp(data.getHeroHp());

        int monsterMaxHp = 10 + (this.currentLevel * 10);
        int monsterDmg = 4 + this.currentLevel;
        this.monster = new Monster("Orco Lvl " + this.currentLevel, monsterMaxHp, monsterDmg, data.getMonsterX(), data.getMonsterY());
        this.monster.setHp(data.getMonsterHp());

        this.combatLog.clear();
        this.combatLog.addAll(data.getCombatLog());
        this.combatLog.add("✨ Partita caricata con successo!");

        this.isGameOver = false;
        this.isGameWon = false;
    }
}