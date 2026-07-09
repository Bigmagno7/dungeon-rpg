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

        // Muri esterni
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                    grid[r][c] = '#';
                } else {
                    grid[r][c] = '.';
                }
            }
        }

        // DESIGN DEI 5 LIVELLI
        switch (level) {
            case 1:
                for (int r = 1; r < 6; r++) grid[r][3] = '#';
                for (int c = 3; c < 8; c++) grid[5][c] = '#';
                grid[8][8] = 'E'; // Porta
                grid[7][7] = 'H'; // Oggetto +5 HP
                break;
            case 2:
                for (int r = 2; r < 8; r++) grid[r][5] = '#';
                grid[3][2] = '#'; grid[3][3] = '#';
                grid[1][8] = 'E';
                grid[7][2] = 'H';
                break;
            case 3:
                // Livello 3: Stanze separate da una linea orizzontale con un buco
                for (int c = 1; c < 9; c++) if (c != 4) grid[4][c] = '#';
                grid[8][1] = 'E';
                grid[2][7] = 'H';
                break;
            case 4:
                // Livello 4: I quattro pilastri d'angolo
                grid[3][3] = '#'; grid[3][6] = '#';
                grid[6][3] = '#'; grid[6][6] = '#';
                grid[8][8] = 'E';
                grid[1][8] = 'H';
                break;
            case 5:
                // Livello 5: La sfida finale, corridoio a spirale!
                for (int c = 2; c < 8; c++) grid[2][c] = '#';
                for (int r = 3; r < 8; r++) grid[r][7] = '#';
                for (int c = 2; c < 7; c++) grid[7][c] = '#';
                grid[4][4] = 'E'; // La porta finale è al centro della spirale!
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