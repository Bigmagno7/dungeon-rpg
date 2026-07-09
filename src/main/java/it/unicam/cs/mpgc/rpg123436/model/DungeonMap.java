package it.unicam.cs.mpgc.rpg123436.model;

public class DungeonMap {

    private final int rows;
    private final int cols;
    private char[][] grid;

    public DungeonMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        generateMaze();
    }

    /**
     * Genera un labirinto predefinito con stanze, corridoi e una porta di uscita 'E'.
     */
    public void generateMaze() {
        this.grid = new char[rows][cols];

        // 1. Riempiamo di pavimento ('.') e creiamo il perimetro di muri ('#')
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                    grid[r][c] = '#';
                } else {
                    grid[r][c] = '.';
                }
            }
        }

        // 2. Aggiungiamo dei muri interni per fare un labirinto/corridoi
        // Muro verticale
        for (int r = 1; r < 6; r++) grid[r][3] = '#';
        // Muro orizzontale
        for (int c = 3; c < 8; c++) grid[5][c] = '#';
        // Un altro blocco di muro isolato
        grid[2][6] = '#';
        grid[3][6] = '#';

        // 3. Posizioniamo la PORTA DI USCITA (Exit) in basso a destra
        grid[8][8] = 'E';
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public char[][] getGrid() { return grid; }
}