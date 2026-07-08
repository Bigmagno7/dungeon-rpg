package it.unicam.cs.mpgc.rpg123436.controller;

import it.unicam.cs.mpgc.rpg123436.model.DungeonMap;
import it.unicam.cs.mpgc.rpg123436.model.Hero;
import it.unicam.cs.mpgc.rpg123436.model.Monster;

/**
 * Rappresenta il 'Controller' principale nell'architettura MVC.
 * Coordina lo stato del gioco, riceve gli input dell'utente e aggiorna il Model di conseguenza.
 */
public class GameController {
    private final DungeonMap map;
    private final Hero hero;
    private final Monster monster; // Per ora gestiamo un singolo mostro di test

    /**
     * Inizializza i componenti del gioco posizionando l'eroe e il mostro sulla mappa.
     */
    public GameController() {
        // Creiamo una mappa di 10 righe x 10 colonne
        this.map = new DungeonMap(10, 10);

        // Posizioniamo l'eroe alle coordinate iniziali (1, 1) che sono libere da muri
        this.hero = new Hero("Guerriero", 1, 1);

        // Piazziamo un Goblin di test in posizione (2, 2)
        this.monster = new Monster("Goblin", 30, 5, 2, 2);
    }

    /**
     * Tenta di muovere l'eroe nelle direzioni specificate.
     * Controlla prima se la mossa è valida tramite le regole della mappa.
     * * @param deltaX Movimento sull'asse X (es. +1 a destra, -1 a sinistra)
     * @param deltaY Movimento sull'asse Y (es. +1 in basso, -1 in alto)
     * @return true se il movimento è avvenuto con successo, false altrimenti
     */
    public boolean handleHeroMovement(int deltaX, int deltaY) {
        int nextX = hero.getX() + deltaX;
        int nextY = hero.getY() + deltaY;

        // Chiediamo al Model (DungeonMap) se la cella d'arrivo è calpestabile
        if (map.isWalkable(nextY, nextX)) {
            // Controlliamo se c'è il mostro in quella posizione prima di muoverci
            if (nextX == monster.getX() && nextY == monster.getY() && monster.getHp() > 0) {
                System.out.println("Incontro con un " + monster.getType() + "! Inizia il combattimento!");
                // Qui in futuro chiameremo la logica di combattimento
                return false;
            }

            // Se la via è libera, aggiorniamo la posizione dell'eroe nel Model
            hero.move(deltaX, deltaY);
            System.out.println("Eroe mosso in posizione: (" + hero.getX() + ", " + hero.getY() + ")");
            return true;
        }

        System.out.println("Movimento bloccato! C'è un muro in posizione: (" + nextX + ", " + nextY + ")");
        return false;
    }

    // Getter per permettere alla View di leggere lo stato aggiornato e fare il rendering
    public DungeonMap getMap() { return map; }
    public Hero getHero() { return hero; }
    public Monster getMonster() { return monster; }
}