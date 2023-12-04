package prg2.gamefichas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Coordinator {
    List<List<String>> inputList = new ArrayList<>();
    List<List<String>> validList = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private int numJuegos;
    private int numTablerosPasados;
    private boolean[] notValid;

    public void start() {
        numJuegos = getNumJuegos();
        getEntrada();
        numTablerosPasados = countNumTablerosPasados();
        System.out.println("Numero de juegos pasados: " + numJuegos);
        System.out.println("Numero tableros pasado: " + numTablerosPasados);
        System.out.println("Lista pasada por parametro: " + inputList);

        checkEntrada(numJuegos, numTablerosPasados);

        System.out.println("Lista sin tableros malos(RVA bien falta columnas y filas)" + validList);

        checkEntradaSameFilsAndCols();
        System.out.println("Lista sin tableros malos" + validList);

        int juegosReales = validList.size();

        new Board(juegosReales, validList);

    }

    private void checkEntradaSameFilsAndCols() {

        if (validList.size() > 0) {
            // System.out.println(validList.get(0));
            // System.out.println(validList.get(0).get(0));
            for (int i = 0; i < validList.size(); i++) {

                if (checkRow(validList.get(i)) != true) {
                    validList.remove(i);
                }

            }
        } else {
            System.out.println("ERROR: no hay tableros validos en la lista despues de comrpobar filas y columnas");
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
            }
        }

    }

    private void isValid(int param) {
        notValid = new boolean[param];

        // for (int i = 0; i < notValid.length; i++) {
        // String a = inputList.get(i);
        // for (int j = 0; j < inputList.get(i).size(); j++) { // Corregir aquí
        // if (inputList.get(i).get(j).charAt(0) != 'R' &&
        // inputList.get(i).get(j).charAt(0) != 'V' &&
        // inputList.get(i).get(j).charAt(0) != 'A') {
        // notValid[i] = true;
        // }
        // }
        // }
        // int indexExterior = 0, j=0, k=0;
        // while (indexExterior < notValid.length) {
        // List<String> currentTableroARevisar = inputList.get(indexExterior);

        // for (int i = 0; i < currentTableroARevisar.size(); i++) {

        // }

        // indexExterior++;
        // }
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

        System.out.println(Arrays.toString(notValid));

    }

    private int countNumTablerosPasados() {
        return inputList.size();
    }

    private void getEntrada() {
        List<String> currentTablero = new ArrayList<>();
        int numSaltosLinea = 0;
        while (sc.hasNextLine()) {
            String ln = sc.nextLine().toUpperCase().trim();

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
            System.out.println("ERROR: numjuegos no valido");
            exitGame(1);
        }

        if (numJuegos <= 0) {
            System.out.println("ERROR: numjuegos 0 o menor");
            exitGame(2);
        }

        return numJuegos;
    }

    private void exitGame(int statusCode) {
        if (statusCode == 0) {
            System.out.println("Exit successfully");
        } else {
            System.out.println("Error en el programa");
        }
        System.exit(statusCode);
    }

}