package diaballik.model;

import com.github.hanleyt.JerseyExtension;
import diaballik.modele.*;
import diaballik.resource.GameResource;
import diaballik.resource.MyExceptionMapper;
import diaballik.resource.TestGameResource;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestLevel_starting {
    static final Logger log = Logger.getLogger(TestGameResource.class.getSimpleName());
    Game game;
    Level_starting test = new Level_starting();
    Board b;
    @SuppressWarnings("unused")
    @RegisterExtension
    JerseyExtension jerseyExtension = new JerseyExtension(this::configureJersey);
    Application configureJersey() {
        return new ResourceConfig(GameResource.class)
                .register(MyExceptionMapper.class)
                .register(MoxyJsonFeature.class);
    }

    <T> T LogJSONAndUnmarshallValue(final Response res, final Class<T> classToRead) {
        res.bufferEntity();
        final String json = res.readEntity(String.class);
        log.log(Level.INFO, "JSON received: " + json);
        final T obj = res.readEntity(classToRead);
        res.close();
        return obj;
    }

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
    void setUp(final Client client, final URI baseUri) {
        final Response res = client
                .target(baseUri)
                .path("game/configureGameIA/Paul/NOIR/starting")
                .request()
                .post(Entity.text(""));

        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

        game = LogJSONAndUnmarshallValue(res, Game.class);
        final Response res2 = client
                .target(baseUri)
                .path("game/movePiece/5/1/5")
                .request()
                .post(Entity.text(""));
        final Response res3 = client
                .target(baseUri)
                .path("game/movePiece/5/2/5")
                .request()
                .post(Entity.text(""));
        final Response res1 = client
                .target(baseUri)
                .path("game/movePiece/5/3/5")
                .request()
                .post(Entity.text(""));
        b = game.getBoard();

    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }

    void movePieceManuallyP2(int id, int x1, int y1, int x2, int y2, Board b) {
        b.getPiecesPlayer2().get(id).setCase(b.getCase(x2,y2));
        b.getCase(x2,y2).setIsAPiece(true);
        b.getCase(x1,y1).setIsAPiece(false);
    }

    void movePieceManuallyP1(int id ,int x1, int y1, int x2, int y2, Board b) {
        b.getPiecesPlayer1().get(id).setCase(b.getCase(x2,y2));
        b.getCase(x2,y2).setIsAPiece(true);
        b.getCase(x1,y1).setIsAPiece(false);
    }

    void moveBallManuallyP2(int id1,int id2, Board b){
        b.getBallPlayer2().setPiece(b.getPiecesPlayer2().get(id2));
        b.getPiecesPlayer2().get(id2).setHaveBall(true);
        b.getPiecesPlayer2().get(id1).setHaveBall(false);
    }
    void moveBallManuallyP1(int id1,int id2, Board b){
        b.getBallPlayer1().setPiece(b.getPiecesPlayer1().get(id2));
        b.getPiecesPlayer1().get(id2).setHaveBall(true);
        b.getPiecesPlayer1().get(id1).setHaveBall(false);
    }


    @Test
    void R23_4_AI_LEVEL_STARTING_MoveRandomlyCorrectly(final Client client, final URI baseUri) {

        final Response res = client
                .target(baseUri)
                .path("game/endTurn")
                .request()
                .post(Entity.text(""));
        game = LogJSONAndUnmarshallValue(res, Game.class);
        assertEquals(2,game.getGlobalNbTurn());
    }

    @Test
    void R23_4_AI_LEVEL_STARTING_BloquerCase(){
        assertFalse(test.bloquerCase(b,b.getCase(6,1)));
        assertNotEquals(1,test.getGuardian().getId());
        assertTrue(test.bloquerCase(b,b.getCase(5,1)));
        assertEquals(1,test.getGuardian().getId());
        assertEquals(true,test.isBloquee());
        assertEquals(5,test.getCaseBloque().getX());
        assertEquals(1,test.getCaseBloque().getY());
        assertTrue(test.bloquerCase(b,b.getCase(5,5)));
        assertNotEquals(1,test.getGuardian().getId());
        assertEquals(5,test.getGuardian().getId());
        assertEquals(5,test.getCaseBloque().getX());
        assertEquals(5,test.getCaseBloque().getY());
    }

    @Test
    void R23_4_AI_LEVEL_STARTING_PassabeVirtuel(){
        assertFalse(test.passablevirtuel(6,3,b));
        assertFalse(test.passablevirtuel(3,5,b));
        assertTrue(test.passablevirtuel(5,3,b));
        assertFalse(test.passablevirtuel(4,6,b));
        assertTrue(test.passablevirtuel(3,0, b));
    }
    @Test
    void R23_4_AI_LEVEL_STARTING_BloquerpathHorizontal(){
        b.getPiecesPlayer1().get(5).setCase(b.getCase(3,3));
        assertFalse(test.bloquerpathhorizontal(b,3));
        b.getPiecesPlayer2().get(0).setCase(b.getCase(1,4));
        b.getPiecesPlayer2().get(1).setCase(b.getCase(2,4));
        MoveBall mb = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertTrue(mb.canDo());
        assertTrue(test.bloquerpathhorizontal(b,3));
        MovePiece mp = new MovePiece(test.getGuardian(),test.getCaseBloque());
        mp.doo();
        assertFalse(mb.canDo());
    }

    @Test
    void R23_4_AI_LEVEL_STARTING_BloquerPathDiagonal(){
        b.getPiecesPlayer1().get(5).setCase(b.getCase(3,6));
        assertFalse(test.bloquerpathdiagonal(b,3,6));
        b.getPiecesPlayer2().get(0).setCase(b.getCase(1,3));
        b.getPiecesPlayer2().get(1).setCase(b.getCase(2,4));
        MoveBall mb = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertTrue(mb.canDo());
        assertTrue(test.bloquerpathdiagonal(b,3,6 ));
        MovePiece mp = new MovePiece(test.getGuardian(),test.getCaseBloque());
        mp.doo();
        assertFalse(mb.canDo());
    }

    @Test
    void R23_4_AI_LEVEL_STARTING_Warning2(){
        movePieceManuallyP2(0, 6, 0, 1, 0, b);
        movePieceManuallyP2(1, 6, 1, 1, 1, b);
        movePieceManuallyP2(2, 6, 2, 1, 2, b);
        moveBallManuallyP2(3, 2,b);
        movePieceManuallyP2(3, 6, 3, 1, 3, b);
        movePieceManuallyP2(4, 6, 4, 1, 4, b);
        movePieceManuallyP2(5, 6, 5, 1, 5, b);
        movePieceManuallyP2(6, 6, 6, 1, 6, b);
        movePieceManuallyP1(6, 0, 6, 3, 6, b);
        moveBallManuallyP1(3, 6, b);
        movePieceManuallyP1(5, 3, 5, 5, 3, b);;
        MoveBall mb1 = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertTrue(test.warning2(b,b.getPiecesPlayer1().get(5),mb1));
        movePieceManuallyP2(0, 1, 0, 6, 3, b);
        assertFalse(test.warning2(b,b.getPiecesPlayer1().get(5),mb1));
        movePieceManuallyP1(6, 3, 6, 3, 3, b);
        MoveBall mb2 = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertFalse(test.warning2(b,b.getPiecesPlayer1().get(5),mb2));
        movePieceManuallyP1(6, 3, 3, 3, 2, b);
        assertFalse(test.warning2(b,b.getPiecesPlayer1().get(5),mb2));
        movePieceManuallyP2(0, 6, 3, 6, 2, b);
        assertTrue(b.getCase(6,2).getIsAPiece());
        MoveBall mb3 = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertFalse(test.warning2(b,b.getPiecesPlayer1().get(5),mb3));
    }

    @Test
    void R23_4_AI_LEVEL_STARTING_Warning3(){
        movePieceManuallyP2(0, 6, 0, 1, 0, b);
        movePieceManuallyP2(1, 6, 1, 1, 1, b);
        movePieceManuallyP2(2, 6, 2, 1, 2, b);
        moveBallManuallyP2(3, 2,b);
        movePieceManuallyP2(3, 6, 3, 1, 3, b);
        movePieceManuallyP2(4, 6, 4, 1, 4, b);
        movePieceManuallyP2(5, 6, 5, 1, 5, b);
        movePieceManuallyP2(6, 6, 6, 1, 6, b);
        movePieceManuallyP1(6, 0, 6, 3, 6, b);
        moveBallManuallyP1(3, 6, b);
        movePieceManuallyP1(5, 3, 5, 6, 3, b);;
        MoveBall mb1 = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertTrue(test.warning3(b,b.getPiecesPlayer1().get(5),mb1));
        movePieceManuallyP1(6, 3, 6, 4, 6, b);
        MoveBall mb2 = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertTrue(test.warning3(b,b.getPiecesPlayer1().get(5),mb2));
        movePieceManuallyP1(6, 4, 6, 5, 6, b);
        MoveBall mb3 = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertTrue(test.warning3(b,b.getPiecesPlayer1().get(5),mb3));
        movePieceManuallyP2(0, 1, 0, 6, 4, b);
        assertFalse(test.warning3(b,b.getPiecesPlayer1().get(5),mb3));
        movePieceManuallyP1(6, 5, 6, 4, 6, b);
        MoveBall mb4 = new MoveBall(b.getBallPlayer1().getPiece(),b.getPiecesPlayer1().get(5),b);
        assertFalse(test.warning3(b,b.getPiecesPlayer1().get(5),mb4));
    }

    @Test
    void R23_4_AI_LEVEL_STARTING_CheckPositionAdv(){
        movePieceManuallyP2(0, 6, 0, 1, 0, b);
        movePieceManuallyP2(1, 6, 1, 1, 1, b);
        movePieceManuallyP2(2, 6, 2, 1, 2, b);
        moveBallManuallyP2(3, 2, b);
        movePieceManuallyP2(3, 6, 3, 1, 3, b);
        movePieceManuallyP2(4, 6, 4, 1, 4, b);
        movePieceManuallyP2(5, 6, 5, 1, 5, b);
        movePieceManuallyP2(6, 6, 6, 1, 6, b);
        movePieceManuallyP1(6, 0, 6, 3, 6, b);
        moveBallManuallyP1(3, 6, b);
        movePieceManuallyP1(5, 3, 5, 5, 3, b);
        movePieceManuallyP1(6, 3, 6, 3, 3, b);
        movePieceManuallyP1(4, 0, 4, 6, 6, b);
        movePieceManuallyP1(3, 0, 3, 4, 0, b);
        test.checkPositionPieceAdv(b);
        assertEquals(3,test.getPiecedangereux().size());
    }

    @Test
    void R23_4_AI_LEVEL_STARTING_PlaysansEndturn(){
        movePieceManuallyP2(0, 6, 0, 4, 5, b);
        movePieceManuallyP2(2, 6, 2, 1, 2, b);
        moveBallManuallyP2( 3, 2,b);
        movePieceManuallyP2(3, 6, 3, 1, 3, b);
        movePieceManuallyP2(5, 6, 5, 1, 5, b);
        movePieceManuallyP2(6, 6, 6, 1, 6, b);
        movePieceManuallyP1(6, 0, 6, 3, 6, b);
        moveBallManuallyP1(3, 6, b);
        movePieceManuallyP1(5, 3, 5, 5, 3, b);
        movePieceManuallyP1(6, 3, 6, 3, 3, b);
        movePieceManuallyP1(4, 0, 4, 6, 6, b);
        movePieceManuallyP1(3, 0, 3, 4, 0, b);
        Case positionBall = test.getPositionBall();
        Piece guardian = test.getGuardian();
        if (!b.getBallPlayer1().getPiece().getCase().equals(positionBall)) {
            test.checkPositionPieceAdv(b);
            test.getListguardian().clear();
        }
        test.setPositionBall(b.getBallPlayer1().getPiece().getCase());
        assertEquals(3,test.getPiecedangereux().size());
        test.oneTurn(b);
        game.setNbTurn(1);
        test.getListguardian().add(new Piece(guardian.getId(), guardian.getCase(), guardian.getPlayer()));
        test.oneTurn(b);
        game.setNbTurn(2);
        test.getListguardian().add(new Piece(guardian.getId(), guardian.getCase(), guardian.getPlayer())); //est-ce qu'on a add 2 guardian different ou on a add que 1 guardian ?
        test.oneTurn(b);
        game.setNbTurn(3);
        test.checkPositionPieceAdv(b);
        assertEquals(0,test.getPiecedangereux().size());
    }


}
