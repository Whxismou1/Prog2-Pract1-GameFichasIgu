package prg2.gamefichas;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Coordinator {
    private final int MINIMUM_ROWS = 1;
    private final int MAXIMUM_COLS = 20;
    List<List<String>> inputList = new ArrayList<>();
    List<List<String>> validList = new ArrayList<>();
    List<List<String>> validDefinitiveList = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private int numJuegos;
    private int numTablerosPasados;
    private boolean[] notValid;

    public void start() {
        numJuegos = getNumJuegos();
        getEntrada();
        numTablerosPasados = countNumTablerosPasados();
        // System.out.println("Primera lista: " + inputList);
        checkEntrada(numJuegos, numTablerosPasados);
        // System.out.println("Lista valida: " + validList);
        checkEntradaSameFilsAndCols();

        checkLimitRowsAndCols();

        int juegosReales = validDefinitiveList.size();

        // System.out.println("Juegos reales: " + juegosReales);
        // System.out.println("Lista valida final: " + validDefinitiveList);

        new Board(juegosReales, validDefinitiveList);

    }

    private void checkLimitRowsAndCols() {

        for (int i = 0; i < validList.size(); i++) {
            List<String> actualList = validList.get(i);

            int nRows = actualList.size();
            int nCols = actualList.get(0).length();

            if ((nRows >= MINIMUM_ROWS && nRows <= MAXIMUM_COLS) && (nCols >= MINIMUM_ROWS && nCols <= MAXIMUM_COLS)) {
                validDefinitiveList.add(actualList);
            }

        }
    }

    private void checkEntradaSameFilsAndCols() {

        if (validList.size() > 0) {
            /** Old */
            // for (int i = 0; i < validList.size(); i++) {

            // if (checkRow(validList.get(i)) != true) {
            // validList.remove(i);
            // }

            // }
            /** New */
            for (int i = validList.size() - 1; i >= 0; i--) {
                if (checkRow(validList.get(i)) != true) {
                    validList.subList(i, validList.size()).clear();
                }
            }

        } else {

            exitGame(1);
        }

    }

    private boolean checkRow(List<String> matrix) {

        List<Integer> filas = new ArrayList<>();
        boolean valid = true;

        for (int i = 0; i < matrix.size(); i++) {
            String fila = matrix.get(i);
            filas.add(fila.length());

        }

        int col = filas.get(0);

        for (int colum : filas) {
            if (colum != col) {
                valid = false;
                break;
            }
        }

        return valid;

    }

    private void checkEntrada(int numJuegos, int numTablerosPasados) {
        if (numJuegos == numTablerosPasados) {
            isValid(numTablerosPasados);
        } else if (numJuegos < numTablerosPasados) {
            isValid(numJuegos);
        } else {
            // numJuegos > numTablerosPasados;
            isValid(numTablerosPasados);
        }

        getValidBoards();
    }

    private void getValidBoards() {

        for (int i = 0; i < notValid.length; i++) {
            if (notValid[i] == false) {
                validList.add(inputList.get(i));
            } else {
                inputList.clear();
                break;
            }
        }

    }

    private void isValid(int param) {
        notValid = new boolean[param];

        for (int i = 0; i < notValid.length; i++) {
            for (int j = 0; j < inputList.get(i).size(); j++) {
                String actualRow = inputList.get(i).get(j);

                for (int k = 0; k < actualRow.length(); k++) {
                    char currentChar = actualRow.charAt(k);

                    if (currentChar != 'R' && currentChar != 'V' && currentChar != 'A') {
                        notValid[i] = true;
                        break;
                    }
                }
            }
        }

    }

    private int countNumTablerosPasados() {
        return inputList.size();
    }

    private void getEntrada() {
        List<String> currentTablero = new ArrayList<>();
        int numSaltosLinea = 0;

        while (sc.hasNextLine()) {
            String ln = sc.nextLine().trim();

            if (!ln.isEmpty()) {
                currentTablero.add(ln);
                numSaltosLinea = 0;
            } else {
                numSaltosLinea++;
                if (numSaltosLinea >= 2) {
                    break;
                }
                if (!currentTablero.isEmpty()) {
                    inputList.add(new ArrayList<>(currentTablero));
                    currentTablero.clear();
                }
            }
        }

        if (!currentTablero.isEmpty()) {
            inputList.add(new ArrayList<>(currentTablero));
        }
    }

    private int getNumJuegos() {
        int numJuegos = 0;
        if (sc.hasNextInt()) {
            numJuegos = sc.nextInt();
            sc.nextLine();

        } else {

            exitGame(1);
        }

        if (numJuegos <= 0) {

            exitGame(2);
        }

        return numJuegos;
    }

    private void exitGame(int statusCode) {
        System.exit(statusCode);
    }

}