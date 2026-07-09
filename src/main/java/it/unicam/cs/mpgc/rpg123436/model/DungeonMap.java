package it.unicam.cs.mpgc.rpg123436.model;

public class DungeonMap {

    private final int rows;
    private final int cols;
    private char[][] grid;

    public DungeonMap(int rows, int cols, int level) {
        this.rows = rows;
        this.cols = cols;
        generateMaze(level);
    }

    private void generateMaze(int level) {
        this.grid = new char[rows][cols];

        // Muri perimetrali
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                    grid[r][c] = '#';
                } else {
                    grid[r][c] = '.';
                }
            }
        }

        // MAPPA LIVELLO 1
        if (level % 2 != 0) {
            for (int r = 1; r < 6; r++) grid[r][3] = '#';
            for (int c = 3; c < 8; c++) grid[5][c] = '#';
            grid[8][8] = 'E'; // Porta in basso a destra
        }
        // MAPPA LIVELLO 2 (Completamente diversa!)
        else {
            for (int c = 1; c < 7; c++) grid[3][c] = '#';
            for (int r = 4; r < 9; r++) grid[r][6] = '#';
            grid[1][8] = 'E'; // Porta in alto a destra!
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public char[][] getGrid() { return grid; }
}