package it.unicam.cs.mpgc.rpg123436.model;

import java.io.Serializable;
/**
 * Rappresenta la struttura della mappa di gioco.
 *
 * Gestisce la griglia bidimensionale del dungeon, contenente
 * muri, celle attraversabili, porta di uscita e medikit.
 *
 * La classe appartiene al Model e non contiene alcun riferimento
 * alla componente grafica, occupandosi solamente della gestione
 * dello stato logico della mappa.
 */
public class DungeonMap implements Serializable {

    private final int rows;
    private final int cols;
    private char[][] grid;
    /**
     * Crea una nuova mappa generando il layout relativo al livello indicato.
     *
     * @param rows numero di righe della griglia
     * @param cols numero di colonne della griglia
     * @param level livello del dungeon da generare
     */
    public DungeonMap(int rows, int cols, int level) {
        this.rows = rows;
        this.cols = cols;
        generateMaze(level);
    }
    /**
     * Genera la configurazione della mappa in base al livello corrente.
     *
     * Inizialmente vengono creati i muri perimetrali e le celle libere,
     * successivamente vengono aggiunti gli elementi specifici del livello
     * come ostacoli, porta di uscita e medikit.
     *
     * @param level livello del dungeon da configurare
     */
    private void generateMaze(int level) {
        this.grid = new char[rows][cols];

        // Muri perimetrali esterni
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                    grid[r][c] = '#';
                } else {
                    grid[r][c] = '.';
                }
            }
        }

        // Layout per i 5 livelli singoli
        switch (level) {
            case 1:
                for (int r = 1; r < 6; r++) grid[r][3] = '#';
                for (int c = 3; c < 8; c++) grid[5][c] = '#';
                grid[8][8] = 'E'; // Porta
                grid[7][7] = 'H'; // Medikit
                break;
            case 2:
                for (int r = 2; r < 8; r++) grid[r][5] = '#';
                grid[3][2] = '#'; grid[3][3] = '#';
                grid[1][8] = 'E';
                grid[7][2] = 'H';
                break;
            case 3:
                for (int r = 1; r < 8; r++) grid[r][3] = '#';
                grid[8][1] = 'E';
                grid[2][7] = 'H';
                break;
            case 4:
                grid[3][3] = '#'; grid[3][6] = '#';
                grid[6][3] = '#'; grid[6][6] = '#';
                grid[8][8] = 'E';
                grid[1][8] = 'H';
                break;
            case 5:
                for (int c = 2; c < 8; c++) grid[2][c] = '#';
                for (int r = 3; r < 8; r++) grid[r][7] = '#';
                for (int c = 2; c < 7; c++) grid[7][c] = '#';
                grid[4][4] = 'E';
                grid[5][4] = 'H';
                break;
            default:
                grid[8][8] = 'E';
                break;
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public char[][] getGrid() { return grid; }
}