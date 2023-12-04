package prg2.gamefichas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    int juegosTotales;
    int juegoActual;
    List<List<String>> boardLists;
    List<Integer> solutions;
    private char[][] gameBoard;
    private int[] fichasLeftGame;
    private Piece[][] pieceGameBoard;
    private int numMovs;

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
                printMatrixChar(matrix);
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
            pieceGameBoard = getGameBoardWithPieces(gameBoard);

            System.out.println("Matriz de piezas:");
            printMatrixPiece(pieceGameBoard);

            // findSolutions4Board(gameBoard);
            // printSolutionsList();
            this.juegoActual++;
        }

    }

    private Piece[][] getGameBoardWithPieces(char[][] gameBoard2) {
        Piece[][] aux = new Piece[gameBoard2.length][gameBoard2[0].length];

        for (int i = 0; i < gameBoard2.length; i++) {
            for (int j = 0; j < gameBoard2[0].length; j++) {
                char color = gameBoard2[i][j];

                aux[i][j] = new Piece(color);
            }
        }

        return aux;
    }

    private int getLeftPieces() {
        int piecesLeft = 0;

        for (int i = 0; i < pieceGameBoard.length; i++) {
            for (int j = 0; j < pieceGameBoard[0].length; j++) {
                if (getPosPiece(i, j).getColorP() != '-') {
                    piecesLeft++;
                }
            }
        }

        return piecesLeft;
    }

    private int removeGroup(int numGroup) {
        int piecesDeleted = 0;

        if (getGroupSize(numGroup) > 1) {

            for (int i = 0; i < pieceGameBoard.length; i++) {
                for (int j = 0; j < pieceGameBoard[0].length; j++) {
                    if (numGroup == getPosPiece(i, j).getGroupNumP()) {
                        piecesDeleted++;
                        getPosPiece(i, j).setGroupNumP(0);
                        setPosPiece(i, j, '-');
                    }
                }
            }

        }

        return piecesDeleted;
    }

    private int getBiggerGroup() {
        int biggerGroup = 0;

        for (int i = 0; i < pieceGameBoard.length; i++) {
            for (int j = 0; j < pieceGameBoard[0].length; j++) {
                int actualGroup = getPosPiece(i, j).getGroupNumP();
                if (actualGroup > biggerGroup) {
                    biggerGroup = actualGroup;
                }
            }
        }

        return biggerGroup;
    }

    private int getGroupSizeByPos(int i, int j) {
        return getGroupSize(getPosPiece(i, j).getGroupNumP());
    }

    private int getGroupSize(int numGroup) {
        int size = 0;

        for (int i = 0; i < pieceGameBoard.length; i++) {
            for (int j = 0; j < pieceGameBoard[0].length; j++) {
                Piece actualPiece = getPosPiece(i, j);
                if (actualPiece.getGroupNumP() == numGroup) {
                    size++;
                }
            }
        }

        return size;
    }

    private Piece getPosPiece(int i, int j) {

        if (i < 0 || i > pieceGameBoard.length) {
            return null;
        } else if (j < 0 || j > pieceGameBoard[0].length) {
            return null;
        } else {

            return pieceGameBoard[i][j];
        }

    }

    private void setPosPiece(int i, int j, char color) {
        if (i < 0 || i > pieceGameBoard.length) {
            return;
        } else if (j < 0 || j > pieceGameBoard[0].length) {
            return;
        } else {

            pieceGameBoard[i][j].setColorP(color);
        }
    }

    private void printSolutionsList() {
        int i = 0;
        while (solutions.size() > 0) {
            System.out.println("Puntuacion final: " + solutions.get(i) + "quedando ");
        }
    }

    private void findSolutions4Board(char[][] matrixBoard) {

        for (int i = 0; i < matrixBoard.length; i++) {
            for (int j = 0; j < matrixBoard[0].length; j++) {
                if (movementsAvailable()) {
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

    private boolean movementsAvailable() {
        if (this.numMovs > 0) {
            return true;
        }
        return false;
    }

    private void printMatrixPiece(Piece[][] pieceGameBoard) {
        for (int i = 0; i < pieceGameBoard.length; i++) {
            for (int j = 0; j < pieceGameBoard[0].length; j++) {
                System.out.print(pieceGameBoard[i][j].getColorP());
            }
            System.out.println();
        }
    }

    private void printMatrixChar(char[][] matrix) {
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

    // private void findSolutions4Board(char[][] matrixBoard) {

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
    // return;
    // }

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

}
