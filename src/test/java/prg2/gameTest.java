package prg2;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prg2.gamefichas.Coordinator;

public class gameTest {
    private static InputStream originalSystemIn;
    private static PrintStream originalSystemOut;
    private static ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        originalSystemIn = System.in;
        originalSystemOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() throws Exception {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    public void testNumTablerosIgualJuegosCorrecto() {
        String entrada1 = "1\nRRV\nRRV\nRRV\n\n";

        System.setIn(new ByteArrayInputStream(entrada1.getBytes()));

        Coordinator coordinator = new Coordinator();
        coordinator.start();

        String expectedsalida1 = "Numero de juegos pasados: 1\nNumero tableros pasado: 1\nLista pasada por parametro: [[RRV, RRV, RRV]]\n[false]\nLista sin tableros malos(RVA bien falta columnas y filas)[[RRV, RRV, RRV]]\nLista sin tableros malos[[RRV, RRV, RRV]]\nJuego 1:\nMatriz qeuda\nRRV\nRRV\nRRV\nLista despues de rellenar matriz:[]\n";

        assertEquals(expectedsalida1, outputStream.toString());

        outputStream.reset();

        String entrada2 = "2\nRRV\nRRV\nRRV\n\nAAV\nAAA\nRRR\n\n";

        System.setIn(new ByteArrayInputStream(entrada2.getBytes()));

        Coordinator coordinator2 = new Coordinator();
        coordinator2.start();

        String expectedsalid2 = "Numero de juegos pasados: 2\n" + //
                "Numero tableros pasado: 2\n" + //
                "Lista pasada por parametro: [[RRV, RRV, RRV], [AAV, AAA, RRR]]\n" + //
                "[false, false]\n" + //
                "Lista sin tableros malos(RVA bien falta columnas y filas)[[RRV, RRV, RRV], [AAV, AAA, RRR]]\n" + //
                "Lista sin tableros malos[[RRV, RRV, RRV], [AAV, AAA, RRR]]\n" + //
                "Juego 1:\n" + //
                "Matriz qeuda\n" + //
                "RRV\n" + //
                "RRV\n" + //
                "RRV\n" + //
                "Lista despues de rellenar matriz:[[AAV, AAA, RRR]]\n" + //
                "Juego 2:\n" + //
                "Matriz qeuda\n" + //
                "AAV\n" + //
                "AAA\n" + //
                "RRR\n" + //
                "Lista despues de rellenar matriz:[]\n";

        assertEquals(expectedsalid2, outputStream.toString());

    }

}
