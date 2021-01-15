package diaballik.model;
import diaballik.modele.*;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestBoard {

        Board b;
        Player j1;
        Player j2;
        /**
         * @throws java.lang.Exception
         */
        @BeforeAll
        static void setUpBeforeClass() throws Exception {
        }

        /**
         * @throws java.lang.Exception
         */
        @AfterAll
        static void tearDownAfterClass() throws Exception {
        }

        /**
         * @throws java.lang.Exception
         */
        @BeforeEach
        void setUp() throws Exception {
                j1 = new Human("Paul", "BLANC");
                j2 = new Human("Pauline", "NOIR");
                b = new Board(j1, j2);
        }

        /**
         * @throws java.lang.Exception
         */
        @AfterEach
        void tearDown() throws Exception {
        }


        @Test
        final void testGetPiece() {

                assertEquals(b.getPiece(0).getId(),0);
        }
        /**
         *
         */
        @Test
        final void R21_2_GAME_BOARD() {

                b.getPiecesPlayer1().forEach(i -> assertTrue(i.getCase().getIsAPiece()));
                assertEquals(7, b.getWidth());
                assertEquals(7, b.getHeight());

                assertEquals(b.getPiece(0).getCase().getX(),0);
                assertEquals(b.getPiece(0).getCase().getY(),0);
        }

        @Test
        final void R21_3_GAME_PIECES() {

                assertEquals(b.getPiecesPlayer1().size(), b.getPiecesPlayer2().size());
                assertEquals(b.getPiecesPlayer1().size(), b.getWidth());
        }

       /* @Test
        final void R21_3_GAME_PIECES() {

                assertEquals(b.getPiecesPlayer1().size(), b.getPiecesPlayer2().size());
        }

        */



}
