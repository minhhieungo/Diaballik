package diaballik.model;


import diaballik.modele.Case;
import diaballik.modele.MovePiece;
import diaballik.modele.Piece;
import org.junit.jupiter.api.*;
import diaballik.modele.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGame {

    Game game;
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
        game= new Game();

        game.configureGameWithPlayer("Paul", "BLANC", "Pauline");

    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }

    /**
     *
     */
    @Test
    void R24_1_1_VICTORY() {

        Case newCase = game.getBoard().getCase(4, 4);
        Piece piece = game.getBoard().getPiece(3);
        MovePiece mp = new MovePiece(piece, newCase);
        mp.doo();
        game.getPlayer1().setMyTurn(false);
        game.getPlayer2().setMyTurn(true);
        newCase = game.getBoard().getCase(0, 3);
        piece = game.getBoard().getPiece(2);
        mp = new MovePiece(piece, newCase);
        mp.doo();
        game.updateBall(3,2);
        assertTrue(game.getWin());
    }
}
