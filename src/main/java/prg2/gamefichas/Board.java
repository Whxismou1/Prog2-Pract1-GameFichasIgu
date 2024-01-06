package prg2.gamefichas;

/*
    Cambios hechos:
    -Quitar el touppercase
    -Metodo changecols cambiado
    -Metodo checkEntradaSameFilsAndCols
    -Metodo removeRec
    -añadir getPiecesDown y movecols a printSolutiones y a find despues de añadir la solucion

 * Ya me pilla el tablero del test 4 ya me pilla los buenos y no la app sola
 * Test 2: test2Diverso: se montan nuevos grupos al eliminar anteriores. Error en el segundo -> SOLUCIONADO (Metod chaneg cols)
 * Test 4: test4Error: error en el tercer juego, con el 4 bueno -> SOLUCIONADO (Metodo removeWithRec)
 * Test 6: test6Con1Movimiento -> cOMO QUE EL TABLERO NO SE ACTUALIZA el juego 1 y 3 bien
 * Test 7: test7EligiendoCasilla -> Me pilla todo bien y me da bien los juegos menos el primero que me faltan puntos
 * Test 8: test8Largo -> TO DO: se peta y PILLA LOS tableros
 */

/**
 * @author Mouhcine El Oualidi 
 * Clase encargada de jugar
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Board {
    // Numero de juegos que se va a jugar
    int totalGames;
    // Juego actual
    int actualGame;
    private int bestScore;
    private List<List<Integer>> actualSolutions;

    private List<List<Integer>> bestSolutions;

    // Lista donde se obtienen los tableros correctos
    List<List<String>> boardLists;

    // Matriz donde se guarda el tablero de chars
    private char[][] gameBoard;

    // Matriz donde se guarda el tablero de Pieces
    // private Piece[][] pieceGameBoard;

    private boolean isLastGame;

    Board(int realGames, List<List<String>> validList) {
        this.totalGames = realGames;
        this.boardLists = validList;
        this.actualGame = 1;

        // this.actualSolutions = new ArrayList<>();
        this.actualSolutions = new ArrayList<>();

        // this.bestSolutions = new ArrayList<>();
        this.bestSolutions = new ArrayList<>();

        this.bestScore = 0;
        play();
    }

    /***
     * Metodo encargado de obtener de la lista los tableros
     * 
     * @return matrix: Devuelve la matriz de juego actual
     */
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
                fillMatrix(matrix, boardLists.get(i), i);
                boardLists.remove(i);

                break;

            }

            return matrix;
        }

    }

    /**
     * Metodo auxiliar que nos rellena la matriz
     * 
     * @param matrix
     * @param list
     * @param indexActualList
     */
    private void fillMatrix(char[][] matrix, List<String> list, int indexActualList) {
        for (int i = 0; i < list.size(); i++) {
            String linea = list.get(i);
            for (int j = 0; j < linea.length(); j++) {
                matrix[i][j] = linea.charAt(j);
            }
        }
    }

    /***
     * Metodo principal del juego
     */
    private void play() {

        while (this.actualGame <= this.totalGames) {
            // Se obtiene el tablero de chars
            // System.out.println("Lista antes de sacaer:" + boardLists);
            this.gameBoard = getGameBoard();
            // System.out.println("Matriz");

            // for (int i = 0; i < getTotalRows(gameBoard); i++) {
            // for (int j = 0; j < getTotalCols(gameBoard); j++) {
            // System.out.print(gameBoard[i][j]);
            // }
            // System.out.println();
            // }

            // System.out.println("Lista despues de sacaer:" + boardLists);
            // Se agrupa
            // regroupPieces();

            List<List<Integer>> posibleMovesChanged = new ArrayList<>();
            checkMoves(gameBoard, posibleMovesChanged);

            findSolutions4Board(this.gameBoard, posibleMovesChanged);

            // System.out.println("Matriz despues de agrupar");
            // for (int i = 0; i < getTotalRows(gameBoard); i++) {
            // for (int j = 0; j < getTotalCols(gameBoard); j++) {
            // System.out.print(gameBoard[i][j]);
            // }
            // System.out.println();
            // }

            // Se imprimen los mejores movimientos y puntuaciones
            int auxPointsFinal = this.actualGame;
            if (totalGames < auxPointsFinal + 1) {
                this.isLastGame = true;
            } else {
                this.isLastGame = false;
            }

            printActualGameSolution(actualGame, this.gameBoard);
            gameBoard = null;
            bestScore = 0;

            actualSolutions.clear();
            bestSolutions.clear();

            // se incrementa el juego
            this.actualGame++;
        }

    }

    private void printActualGameSolution(int actualGame, char[][] boardGameP) {
        StringBuilder sb = new StringBuilder();
        sb.append("Juego " + actualGame + ":" + "\n");

        int finalScore = 0;
        for (int i = 0; i < this.bestSolutions.size(); i++) {
            List<Integer> arrSolutions = this.bestSolutions.get(i);

            int movesScore = getPointWithDeletedPieces(boardGameP, arrSolutions.get(2));

            finalScore += movesScore;

            int row = arrSolutions.get(0);
            int col = arrSolutions.get(1);

            removeGroup(boardGameP, row, col);
            getPiecesDown(boardGameP);
            movePiecesCol(boardGameP);

            if (movesScore == 1) {
                sb.append("Movimiento ").append(i + 1).append(" en (")
                        .append(getTotalRows(boardGameP) - arrSolutions.get(0))
                        .append(", ")
                        .append(arrSolutions.get(1) + 1).append("): eliminó ").append(arrSolutions.get(2))
                        .append(" fichas de color ")
                        .append((char) arrSolutions.get(3).intValue()).append(" y obtuvo ").append(movesScore)
                        .append(" punto.\n");
            } else {
                sb.append("Movimiento ").append(i + 1).append(" en (")
                        .append(getTotalRows(boardGameP) - arrSolutions.get(0))
                        .append(", ")
                        .append(arrSolutions.get(1) + 1).append("): eliminó ").append(arrSolutions.get(2))
                        .append(" fichas de color ")
                        .append((char) arrSolutions.get(3).intValue()).append(" y obtuvo ").append(movesScore)
                        .append(" puntos.\n");
            }

        }

        finalScore += (getLeftPieces(boardGameP) == 0) ? 1000 : 0;

        if (getLeftPieces(boardGameP) == 1) {
            sb.append("Puntuación final: ").append(finalScore).append(", quedando ")
                    .append(getLeftPieces(boardGameP))
                    .append(" ficha.");
        } else {
            sb.append("Puntuación final: ").append(finalScore).append(", quedando ")
                    .append(getLeftPieces(boardGameP))
                    .append(" fichas.");
        }
        if (!isLastGame) {
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private void checkMoves(char[][] boardGameP, List<List<Integer>> posibleMovesChanged) {
        boolean visited[][] = new boolean[getTotalRows(boardGameP)][getTotalCols(boardGameP)];

        for (int row = getTotalRows(boardGameP) - 1; row >= 0; row--) {
            for (int col = 0; col < getTotalCols(boardGameP); col++) {
                if (!visited[row][col] && isGroupByPos(row, col, boardGameP, boardGameP[row][col])
                        && boardGameP[row][col] != '-') {
                    List<Integer> actualMovelist = new ArrayList<>();
                    actualMovelist.addAll(Arrays.asList(row, col));
                    posibleMovesChanged.add(actualMovelist);
                    checkMovesWithRec(boardGameP, row, col, boardGameP[row][col], visited);
                }
            }
        }

        // boolean visited[][] = new
        // boolean[getTotalRows(boardGameP)][getTotalCols(boardGameP)];

        // for (int row = getTotalRows(boardGameP) - 1; row >= 0; row--) {
        // for (int col = 0; col < getTotalCols(boardGameP); col++) {
        // if (!visited[row][col] && isGroupByPos(row, col, boardGameP,
        // boardGameP[row][col])
        // && boardGameP[row][col] != '-') {
        // posibleMoves.add(new int[] { row, col });
        // checkMovesWithRec(boardGameP, row, col, boardGameP[row][col], visited);
        // }
        // }
        // }

    }

    private boolean isGroupByPos(int row, int col, char[][] boardGameP, char actualColor) {
        // return isGroupByPosWithRec(boardGameP, row - 1, col, actualColor) ||
        // isGroupByPosWithRec(boardGameP, row + 1, col, actualColor) ||
        // isGroupByPosWithRec(boardGameP, row, col - 1, actualColor) ||
        // isGroupByPosWithRec(boardGameP, row, col + 1, actualColor);

        return (row > 0 && boardGameP[row - 1][col] == actualColor)
                || (row < boardGameP.length - 1 && boardGameP[row + 1][col] == actualColor)
                || (col > 0 && boardGameP[row][col - 1] == actualColor)
                || (col < boardGameP[0].length - 1 && boardGameP[row][col + 1] == actualColor);

    }

    // private static boolean isGroupByPosWithRec(char[][] boardGameP, int row, int
    // col, char actualColor) {
    // return row >= 0 && row < boardGameP.length &&
    // col >= 0 && col < boardGameP[0].length &&
    // boardGameP[row][col] == actualColor;
    // }

    private void checkMovesWithRec(char[][] boardGameP, int row, int col, char actualPiece,
            boolean[][] visited) {

        if (row < 0 || row >= getTotalRows(boardGameP) || col < 0 || col >= getTotalCols(boardGameP)
                || visited[row][col]
                || boardGameP[row][col] != actualPiece) {
            return;
        }

        visited[row][col] = true;

        checkMovesWithRec(boardGameP, row - 1, col, actualPiece, visited); // Check UP
        checkMovesWithRec(boardGameP, row + 1, col, actualPiece, visited); // check down
        checkMovesWithRec(boardGameP, row, col - 1, actualPiece, visited); // check left
        checkMovesWithRec(boardGameP, row, col + 1, actualPiece, visited); // check right

    }

    private void findSolutions4Board(char[][] boardGameP, List<List<Integer>> posibleMovesChanged) {

        char[][] copyBoardGame = matrixCopy(boardGameP);

        for (int i = 0; i < posibleMovesChanged.size(); i++) {
            List<Integer> actualMove = posibleMovesChanged.get(i);

            int row = actualMove.get(0);
            int col = actualMove.get(1);

            char actualColor = copyBoardGame[row][col];

            int deletedPieces = removeGroup(copyBoardGame, row, col);

            List<Integer> actualSol = Arrays.asList(row, col, deletedPieces, (int) actualColor);
            this.actualSolutions.add(actualSol);
            getPiecesDown(boardGameP);
            movePiecesCol(boardGameP);

            List<List<Integer>> nextMovesList = new ArrayList<>();
            checkMoves(copyBoardGame, nextMovesList);
            findSolutions4Board(copyBoardGame, nextMovesList);
            copyBoardGame = matrixCopy(boardGameP);
            this.actualSolutions.remove(this.actualSolutions.size() - 1);
        }

        if (posibleMovesChanged.size() == 0) {
            if (bestSolutions.size() == 0) {
                bestSolutions.addAll(actualSolutions);
            }

            checkSolutions(copyBoardGame);
        }

    }

    private void checkSolutions(char[][] boardGameP) {
        int finalScore = getTotalMoveScore(boardGameP);

        if (this.bestScore < finalScore) {
            this.bestScore = finalScore;
            this.bestSolutions.clear();
            this.bestSolutions.addAll(this.actualSolutions);
        }

    }

    private int getTotalMoveScore(char[][] boardGameP) {
        int finalMoveScore = 0;

        for (int i = 0; i < this.actualSolutions.size(); i++) {
            List<Integer> actu = this.actualSolutions.get(i);
            int numPointsMv = getPointWithDeletedPieces(boardGameP, actu.get(2));
            finalMoveScore += numPointsMv;
        }

        finalMoveScore += (getLeftPieces(boardGameP) == 0) ? 1000 : 0;

        return finalMoveScore;
    }

    private int getPointWithDeletedPieces(char[][] boardGameP, int deletedPieces) {
        return (deletedPieces - 2) * (deletedPieces - 2);
    }

    /***
     * Metodo que devuelve el numero de piezas restante
     * 
     */
    private int getLeftPieces(char[][] boardGameP) {
        int piecesLeft = 0;

        for (int row = 0; row < getTotalRows(boardGameP); row++) {
            for (int col = 0; col < getTotalCols(boardGameP); col++) {
                if (boardGameP[row][col] != '-') {
                    piecesLeft++;
                }
            }
        }

        return piecesLeft;
    }

    /***
     * Metodo encargado de eliminar el grupo
     * 
     * @param col
     * @param intento
     */
    private int removeGroup(char[][] boardGameP, int row, int col) {
        char actualColor = boardGameP[row][col];

        boolean[][] visited = new boolean[getTotalRows(boardGameP)][getTotalCols(boardGameP)];

        return removeGroupRec(boardGameP, row, col, actualColor, visited);

    }

    private int removeGroupRec(char[][] boardGameP, int row, int col, char color, boolean[][] visited) {
        // if (row < 0 || row >= boardGameP.length || col < 0 || col >=
        // boardGameP[0].length || visited[row][col]
        // || boardGameP[row][col] != color) {
        // return 0;
        // }

        // visited[row][col] = true;

        // int numPiecesRem = removeGroupRec(boardGameP, row - 1, col, color, visited);
        // // Check Up
        // numPiecesRem += removeGroupRec(boardGameP, row + 1, col, color, visited); //
        // Check DOnw
        // numPiecesRem += removeGroupRec(boardGameP, row, col - 1, color, visited); //
        // Check Left
        // numPiecesRem += removeGroupRec(boardGameP, row, col + 1, color, visited); //
        // CheckRigth

        // boardGameP[row][col] = '-';

        // getPiecesDown(boardGameP);
        // movePiecesCol(boardGameP);
        // return numPiecesRem + 1;
        /* Newww */
        int numPiecesRem = 0;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] { row, col });

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            row = current[0];
            col = current[1];

            if (row < 0 || row >= boardGameP.length || col < 0 || col >= boardGameP[0].length || visited[row][col]
                    || boardGameP[row][col] != color) {
                continue;
            }

            visited[row][col] = true;
            numPiecesRem++;

            stack.push(new int[] { row - 1, col }); // Comprobar arriba
            stack.push(new int[] { row + 1, col }); // Comprobar abajo
            stack.push(new int[] { row, col - 1 }); // Comprobar izquierda
            stack.push(new int[] { row, col + 1 }); // Comprobar derecha

            boardGameP[row][col] = '-';
        }

        getPiecesDown(boardGameP);
        movePiecesCol(boardGameP);

        return numPiecesRem;
    }

    /**
     * Metodo encargado de bajar las piezas
     * 
     * @param boardGameP -> tablero a comprobar
     */
    private void getPiecesDown(char[][] boardGameP) {
        for (int row = 0; row < boardGameP.length - 1; row++) {
            for (int col = 0; col < boardGameP[0].length; col++) {
                if (boardGameP[row + 1][col] == '-' && boardGameP[row][col] != '-') {
                    boardGameP[row + 1][col] = boardGameP[row][col];
                    boardGameP[row][col] = '-';
                    getPiecesDown(boardGameP);
                }

            }
        }

    }

    /**
     * Metodo encargado de mover las piezas en colummnas
     * 
     * @param boardGameP -> tablero a comprobar
     */
    private void movePiecesCol(char[][] boardGameP) {
        for (int row = 0; row < boardGameP[0].length; row++) {
            int numEmptyPieces = 0;
            for (int col = 0; col < boardGameP.length; col++) {
                if (boardGameP[col][row] == '-') {
                    numEmptyPieces++;
                }
            }
            if (numEmptyPieces == boardGameP.length) {
                changeCols(boardGameP, row);
            }
        }

    }

    /**
     * New
     * 
     * @param tablero
     * @param fila
     */
    public void changeCols(char[][] tablero, int fila) {
        for (int j = fila + 1; j < tablero[0].length; j++) {
            int numEspacios = 0;
            for (int i = 0; i < tablero.length; i++) {
                if (tablero[i][j] == '-') {
                    numEspacios++;
                }
            }
            if (numEspacios != tablero.length) {
                for (int i = 0; i < tablero.length; i++) {
                    tablero[i][fila] = tablero[i][j];
                    tablero[i][j] = '-';
                }
                break;
            }
        }
    }

    // /**
    // * Metodo auxiliar para cambiar columnas
    // *
    // * @param boardGameP -> tablero a cambiar
    // * @param col -> columna
    // */
    // private void changeCols(char[][] boardGameP, int col) {
    // for (int destCol = col + 1; destCol < boardGameP[0].length; destCol++) {
    // int numEmptySpaces = countEmptySpacesInColumn(boardGameP, destCol);
    // if (numEmptySpaces != boardGameP.length) {
    // shiftColumnContents(boardGameP, col, destCol);
    // break;
    // }
    // }
    // }

    // private int countEmptySpacesInColumn(char[][] boardGameP, int col) {
    // int emptySpaces = 0;
    // for (int row = 0; row < boardGameP.length; row++) {
    // if (boardGameP[row][col] == '-') {
    // emptySpaces++;
    // }
    // }
    // return emptySpaces;
    // }

    // private void shiftColumnContents(char[][] boardGameP, int srcCol, int
    // destCol) {
    // for (int row = 0; row < boardGameP.length; row++) {
    // boardGameP[row][srcCol] = boardGameP[row][destCol];
    // boardGameP[row][destCol] = '-';
    // }
    // }

    // private void getPiecesDown(char[][] boardGameP) {
    // for (int row = 0; row < getTotalRows(boardGameP) - 1; row++) {
    // for (int col = 0; col < getTotalCols(boardGameP); col++) {
    // if (boardGameP[row + 1][col] == '-' && boardGameP[row][col] != '-') {
    // boardGameP[row + 1][col] = boardGameP[row][col];
    // boardGameP[row][col] = '-';
    // getPiecesDown(boardGameP);
    // }

    // }
    // }

    // }

    // private void movePiecesCol(char[][] boardGameP) {
    // for (int row = 0; row < getTotalCols(boardGameP); row++) {
    // int numEmptyPieces = 0;
    // for (int col = 0; col < getTotalRows(boardGameP); col++) {
    // if (boardGameP[col][row] == '-') {
    // numEmptyPieces++;
    // }
    // }
    // if (numEmptyPieces == getTotalCols(boardGameP)) {
    // changeColsNew(boardGameP, numEmptyPieces);
    // }
    // }

    // }

    // private void changeCols(char[][] boardGameP, int numEmptyPieces) {
    // for (int colLast = numEmptyPieces; colLast < getTotalCols(boardGameP) - 1;
    // colLast++) {
    // for (int colFirst = 0; colFirst < getTotalRows(boardGameP) - 1; colFirst++) {
    // boardGameP[colFirst][colLast] = boardGameP[colFirst][colLast + 1];
    // boardGameP[colFirst][colLast + 1] = '-';
    // }
    // }
    // }

    // /**
    // * Metodo auxiliar para cambiar columnas
    // *
    // * @param boardGameP -> tablero a cambiar
    // * @param col -> columna
    // */
    // private void changeColsNew(char[][] boardGameP, int col) {
    // for (int destCol = col + 1; destCol < boardGameP[0].length; destCol++) {
    // int numEmptySpaces = countEmptySpacesInColumnNew(boardGameP, destCol);
    // if (numEmptySpaces != boardGameP.length) {
    // shiftColumnContentsNew(boardGameP, col, destCol);
    // break;
    // }
    // }
    // }

    // private int countEmptySpacesInColumnNew(char[][] boardGameP, int col) {
    // int emptySpaces = 0;
    // for (int row = 0; row < boardGameP.length; row++) {
    // if (boardGameP[row][col] == '-') {
    // emptySpaces++;
    // }
    // }
    // return emptySpaces;
    // }

    // private void shiftColumnContentsNew(char[][] boardGameP, int srcCol, int
    // destCol) {
    // for (int row = 0; row < boardGameP.length; row++) {
    // boardGameP[row][srcCol] = boardGameP[row][destCol];
    // boardGameP[row][destCol] = '-';
    // }
    // }

    private int getTotalCols(char[][] matrix) {
        return matrix[0].length;
    }

    private int getTotalRows(char[][] matrix) {
        return matrix.length;
    }

    private char[][] matrixCopy(char[][] originalMatrix) {

        char[][] copiedMatrix = new char[getTotalRows(originalMatrix)][getTotalCols(originalMatrix)];

        for (int row = 0; row < getTotalRows(originalMatrix); row++) {
            for (int col = 0; col < getTotalCols(originalMatrix); col++) {
                copiedMatrix[row][col] = originalMatrix[row][col];
            }
        }

        return copiedMatrix;

    }

}