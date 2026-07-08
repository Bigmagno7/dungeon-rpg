package it.unicam.cs.mpgc.rpg123436.model;

/**
 * Rappresenta la mappa del Dungeon.
 * Gestisce la griglia del gioco, i muri e i confini calpestabili.
 */
public class DungeonMap {
    private final int rows;
    private final int cols;
    private final char[][] grid;

    public DungeonMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        generateDefaultMap();
    }

    private void generateDefaultMap() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Muri sui bordi esterni, spazio libero dentro
                if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                    grid[r][c] = '#'; // Muro
                } else {
                    grid[r][c] = '.'; // Pavimento
                }
            }
        }
        // Aggiungiamo un pilastro di ostacolo al centro per test
        grid[4][4] = '#';
    }

    public boolean isWalkable(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return false;
        }
        return grid[r][c] != '#';
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public char[][] getGrid() { return grid; }
}