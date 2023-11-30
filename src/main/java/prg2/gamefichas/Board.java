package prg2.gamefichas;

import java.util.ArrayList;
import java.util.List;

public class Board {
    int juegosTotales;
    int juegoActual;
    List<List<String>> boardLists;
    List<Integer> solutions;
    private char[][] gameBoard;

    Board(int juegosReales, List<List<String>> validList) {
        this.juegosTotales = juegosReales;
        this.boardLists = validList;
        this.juegoActual = 0;
        this.solutions = new ArrayList<>();
        play();
    }

    private char[][] getGameBoard() {

        if (this.boardLists.isEmpty()) {
            return null;
        } else {
            int row;
            int col;
            char[][] matrix = null;
            for (int i = 0; i < this.boardLists.size();) {
                row = this.boardLists.get(i).size();
                col = this.boardLists.get(i).get(0).length();
                matrix = new char[row][col];
                rellenarMatriz(matrix, boardLists.get(i), i);
                System.out.println("Matriz qeuda");
                printMatrix(matrix);
                boardLists.remove(i);
                System.out.println("Lista despues de rellenar matriz:" + boardLists);
                break;

            }

            return matrix;
        }

    }

    private void rellenarMatriz(char[][] matrix, List<String> list, int indexActualList) {
        for (int i = 0; i < list.size(); i++) {
            String linea = list.get(i);
            for (int j = 0; j < linea.length(); j++) {
                matrix[i][j] = linea.charAt(j);
            }
        }
    }

    private void play() {

        while (this.juegoActual < this.juegosTotales) {
            System.out.println("Juego " + (juegoActual + 1) + ":");
            gameBoard = getGameBoard();

            findSolutions4Board(gameBoard);
            this.juegoActual++;
        }

    }

    private void findSolutions4Board(char[][] matrixBoard) {

        // for (int i = matrixBoard.length - 1; i >= 0; i--) {
        // for (int j = matrixBoard[0].length - 1; j >= 0; j--) {
        // if (matrixBoard[i][j] != '.') { // Ignorar fichas vacías
        // char[][] copyBoard = matrixCopy(matrixBoard);
        // int eliminated = eliminateGroup(matrixBoard, i, j);
        // if (eliminated >= 2) {
        // int score = (int) Math.pow(eliminated - 2, 2);
        // solutions.add(score);
        // System.out.println("Movimiento " + (solutions.size()) + " en (" + (i + 1) +
        // ", " + (j + 1) +
        // "): eliminó " + eliminated + " fichas de color " + matrixBoard[i][j] +
        // " y obtuvo " + score + " puntos.");
        // }
        // printMatrix(matrixBoard);
        // System.out.println("Puntuación final: " + calculateTotalScore() + ", quedando
        // " +
        // countRemainingPieces(matrixBoard) + " fichas.");
        // matrixBoard = matrixCopy(copyBoard);
        // }
        // }
        // }

        // if (!notMovementsAvailable()) {
        //     return;
        // }

        for (int i = 0; i < matrixBoard.length; i++) {
            for (int j = 0; j < matrixBoard[0].length; j++) {
                if (isValidMove(matrixBoard, i, j)) {
                    char[][] copyBoard = matrixCopy(matrixBoard);

                    // Hacemos el movimiento
                    // Move columns & rows
                    // makemove(board)

                    findSolutions4Board(matrixBoard);

                    matrixBoard = matrixCopy(copyBoard);

                }
            }
        }


    }

    private boolean isValidMove(char[][] matrixBoard, int i, int j) {

        char color = matrixBoard[i][j];

        // Verificamos filas
        if (checkConnection(matrixBoard, i, j, 0, 1, color) + checkConnection(matrixBoard, i, j, 0, -1, color) >= 2) {
            return true;
        }

        // Verificamos columnas
        if (checkConnection(matrixBoard, i, j, 1, 0, color) + checkConnection(matrixBoard, i, j, -1, 0, color) >= 2) {
            return true;
        }

        return false;
    }


    private int checkConnection(char[][] matrixBoard, int x, int y, int dx, int dy, char color) {

        int count = 0;

        while (x >= 0 && x < matrixBoard.length && y >= 0 && y < matrixBoard[0].length && matrixBoard[x][y] == color) {
            count++;
            x += dx;
            y += dy;
        }

        return count;
    }

    private boolean notMovementsAvailable() {
        return false;
    }

    // private int eliminateGroup(char[][] matrixBoard, int row, int col) {
    // char color = matrixBoard[row][col];
    // int eliminated = 0;
    // if (color != '.') {
    // eliminated = dfs(matrixBoard, row, col, color);
    // for (int i = matrixBoard.length - 1; i >= 0; i--) {
    // for (int j = matrixBoard[0].length - 1; j >= 0; j--) {
    // if (matrixBoard[i][j] == 'X') {
    // matrixBoard[i][j] = '.'; // Mover la eliminación aquí
    // }
    // }
    // }
    // }
    // return eliminated;
    // }

    // private int dfs(char[][] matrixBoard, int row, int col, char color) {
    // if (row < 0 || col < 0 || row >= matrixBoard.length || col >=
    // matrixBoard[0].length ||
    // matrixBoard[row][col] != color) {
    // return 0;
    // }
    // matrixBoard[row][col] = 'X'; // Mark as eliminated
    // int count = 1;
    // count += dfs(matrixBoard, row + 1, col, color);
    // count += dfs(matrixBoard, row - 1, col, color);
    // count += dfs(matrixBoard, row, col + 1, color);
    // count += dfs(matrixBoard, row, col - 1, color);
    // return count;
    // }

    // private int calculateTotalScore() {
    // return solutions.stream().mapToInt(Integer::intValue).sum() + 1000;
    // }

    // private int countRemainingPieces(char[][] matrixBoard) {
    // int count = 0;
    // for (char[] row : matrixBoard) {
    // for (char piece : row) {
    // if (piece != '.') {
    // count++;
    // }
    // }
    // }
    // return count;
    // }

    private void printMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    private char[][] matrixCopy(char[][] matrixOriginal) {
        char[][] matrixCopy = new char[matrixOriginal.length][matrixOriginal[0].length];

        for (int i = 0; i < matrixOriginal.length; i++) {
            for (int j = 0; j < matrixOriginal[0].length; j++) {
                matrixCopy[i][j] = matrixOriginal[i][j];
            }
        }

        return matrixCopy;
    }

    private List<Movements> getMovements(char[][] matrixBoard) {
        List<Movements> movements = new ArrayList<>();

        for (int i = matrixBoard.length - 1; i >= 0; i--) {
            for (int j = 0; j < matrixBoard[0].length; j++) {
                if (isValidMove(matrixBoard, i, j)) {
                    int startRow = findStartRow(matrixBoard, i, j);
                    movements.add(new Movements(startRow, j, i, j));
                }
            }
        }

        return movements;
    }

    private int findStartRow(char[][] matrixBoard, int row, int col) {
        int startRow = row;
        while (startRow > 0 && matrixBoard[startRow - 1][col] == matrixBoard[row][col]) {
            startRow--;
        }
        return startRow;
    }

    private class Movements {
        private final int startRow;
        private final int startCol;
        private final int endRow;
        private final int endCol;

        public Movements(int startRow, int startCol, int endRow, int endCol) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }

        public int getStartRow() {
            return startRow;
        }

        public int getStartCol() {
            return startCol;
        }

        public int getEndRow() {
            return endRow;
        }

        public int getEndCol() {
            return endCol;
        }
    }
}
