package diaballik.resource;

import com.github.hanleyt.JerseyExtension;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import diaballik.modele.*;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGameResource {
	static final Logger log = Logger.getLogger(TestGameResource.class.getSimpleName());
	Game game;
	Game game2;

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

	@BeforeEach
	void setUp(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGamePlayer/Paul/NOIR/Pauline")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

		game = LogJSONAndUnmarshallValue(res, Game.class);

	}

	/*@Test
	void testNewGamePvP(final Client client, final URI baseUri) {
		// final Response res = client
		// 	.target(baseUri)
		// 	.path("TODO")
		// 	.request()
		// 	.post(Entity.text(""));

		// assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

		// final Game game = LogJSONAndUnmarshallValue(res, Game.class);

		// assertNotNull(game);
		// etc.
	}

	 */

	@Test
	void R21_1_GAME_PLAYERS(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGamePlayer/Paul/NOIR/Pauline")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

		game2 = LogJSONAndUnmarshallValue(res, Game.class);

		assertNotNull(game2);

		assertEquals(game2.getPlayer1().getName(), "Paul");
		assertEquals(game2.getPlayer1().getColor(), "NOIR");
		assertEquals(game2.getPlayer2().getName(), "Pauline");
		assertEquals(game2.getPlayer2().getColor(), "BLANC");

	}

	@Test
	void R21_1_GAME_COLOUR(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGamePlayer/Paul/NOIR/Pauline")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

		game2 = LogJSONAndUnmarshallValue(res, Game.class);

		assertNotNull(game2);

		assertEquals(game2.getPlayer1().getColor(), "NOIR");
;
		assertEquals(game2.getPlayer2().getColor(), "BLANC");

	}

	@Test
	void R21_4_GAME_BALL(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGamePlayer/Paul/NOIR/Pauline")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

		game2 = LogJSONAndUnmarshallValue(res, Game.class);

		assertNotNull(game2);
        assertFalse(game2.getBoard().getPiecesPlayer1().get(0).getHaveBall());
        assertFalse(game2.getBoard().getPiecesPlayer1().get(1).getHaveBall());
        assertFalse(game2.getBoard().getPiecesPlayer1().get(2).getHaveBall());
        assertFalse(game2.getBoard().getPiecesPlayer1().get(4).getHaveBall());
        assertFalse(game2.getBoard().getPiecesPlayer1().get(5).getHaveBall());
        assertFalse(game2.getBoard().getPiecesPlayer1().get(6).getHaveBall());
		assertTrue(game2.getBoard().getPiecesPlayer1().get(3).getHaveBall());
		assertTrue(game2.getBoard().getPiecesPlayer2().get(3).getHaveBall());
		assertFalse(game2.getBoard().getPiecesPlayer2().get(0).getHaveBall());
		assertFalse(game2.getBoard().getPiecesPlayer2().get(1).getHaveBall());
		assertFalse(game2.getBoard().getPiecesPlayer2().get(2).getHaveBall());
		assertFalse(game2.getBoard().getPiecesPlayer2().get(4).getHaveBall());
		assertFalse(game2.getBoard().getPiecesPlayer2().get(5).getHaveBall());
		assertFalse(game2.getBoard().getPiecesPlayer2().get(6).getHaveBall());
	}
	@Test
	void testConfigureGameBadPlayer(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGamePlayer/Paul/RED/Pauline")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), res.getStatus());
	}

	@Test
	void R23_1_PLAYER_KINDS(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGameIA/Paul/NOIR/progressive")
				.request()
				.post(Entity.text(""));
		game2 = LogJSONAndUnmarshallValue(res, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());


		assertNotNull(game2);

		assertEquals(game2.getPlayer1().getName(), "Paul");
		assertEquals(game2.getPlayer1().getColor(), "NOIR");
		assertEquals(game2.getPlayer2().getColor(), "BLANC");
		assertEquals(IA.class, game2.getPlayer2().getClass());
	}

	@Test
	void R23_2_IA_LEVELS_PROGRESSIVE(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGameIA/Paul/NOIR/progressive")
				.request()
				.post(Entity.text(""));
		game2 = LogJSONAndUnmarshallValue(res, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());


		assertNotNull(game2);

		assertEquals(Level_progressive.class, ( (IA) game2.getPlayer2()).getLevel().getClass());
		assertEquals(IA.class, game2.getPlayer2().getClass());
	}

	@Test
	void R23_2_IA_LEVELS_NOOB(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGameIA/Paul/NOIR/noob")
				.request()
				.post(Entity.text(""));
		game2 = LogJSONAndUnmarshallValue(res, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());


		assertNotNull(game2);

		assertEquals(Level_noob.class, ( (IA) game2.getPlayer2()).getLevel().getClass());
		assertEquals(IA.class, game2.getPlayer2().getClass());
	}

	@Test
	void R23_2_IA_LEVELS_STARTING(final Client client, final URI baseUri) {
		final Response res = client
				.target(baseUri)
				.path("game/configureGameIA/Paul/NOIR/starting")
				.request()
				.post(Entity.text(""));
		game2 = LogJSONAndUnmarshallValue(res, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());


		assertNotNull(game2);

		assertEquals(Level_starting.class, ( (IA) game2.getPlayer2()).getLevel().getClass());
		assertEquals(IA.class, game2.getPlayer2().getClass());
	}

	@Test
	void R21_10_GAMEPLAY_MOVE_PIECE_RIGHT(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/0/1/0")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);

		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());

		assertNotNull(game);

		assertFalse(game.getBoard().getCase(0, 0).getIsAPiece());
		assertTrue(game.getBoard().getCase(1, 0).getIsAPiece());
		assertEquals(game.getBoard().getPiece(0).getCase().getX(), 1);
		assertEquals(game.getBoard().getPiece(0).getCase().getY(), 0);

	}

	@Test
	void R21_10_GAMEPLAY_MOVE_PIECE_UP(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/0/1/0")
				.request()
				.post(Entity.text(""));

		final Response res3 = client
				.target(baseUri)
				.path("game/movePiece/0/1/1")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res3, Game.class);

		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());

		assertNotNull(game);

		assertFalse(game.getBoard().getCase(0, 0).getIsAPiece());
		assertFalse(game.getBoard().getCase(1, 0).getIsAPiece());
		assertTrue(game.getBoard().getCase(1, 1).getIsAPiece());
		assertEquals(game.getBoard().getPiece(0).getCase().getX(), 1);
		assertEquals(game.getBoard().getPiece(0).getCase().getY(), 1);

	}

	@Test
	void R21_10_GAMEPLAY_MOVE_PIECE_DOWN(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/1/1/1")
				.request()
				.post(Entity.text(""));

		final Response res3 = client
				.target(baseUri)
				.path("game/movePiece/1/1/0")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res3, Game.class);

		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());

		assertNotNull(game);

		assertFalse(game.getBoard().getCase(0, 1).getIsAPiece());
		assertFalse(game.getBoard().getCase(1, 1).getIsAPiece());
		assertTrue(game.getBoard().getCase(1, 0).getIsAPiece());
		assertEquals(game.getBoard().getPiece(1).getCase().getX(), 1);
		assertEquals(game.getBoard().getPiece(1).getCase().getY(), 0);

	}

	@Test
	void R21_10_GAMEPLAY_MOVE_PIECE_LEFT(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/0/1/0")
				.request()
				.post(Entity.text(""));

		final Response res3 = client
				.target(baseUri)
				.path("game/movePiece/0/0/0")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res3, Game.class);

		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());

		assertNotNull(game);

		assertFalse(game.getBoard().getCase(1, 0).getIsAPiece());
		assertTrue(game.getBoard().getCase(0, 0).getIsAPiece());
		assertEquals(game.getBoard().getPiece(0).getCase().getX(), 0);
		assertEquals(game.getBoard().getPiece(0).getCase().getY(), 0);

	}

	@Test
	void R21_11_GAMEPLAY_MOVE_PIECE_WITH_BALL(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/3/1/3")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());

		assertNotNull(game);
		assertTrue(game.getBoard().getPiece(3).getHaveBall());
		assertFalse(game.getBoard().getCase(1, 3).getIsAPiece());
//
//		assertTrue(game.getBoard().getCase(1, 3).getIsAPiece());
		assertEquals(game.getBoard().getPiece(3).getCase().getX(), 0);
		assertEquals(game.getBoard().getPiece(3).getCase().getY(), 3);

	}

	@Test
	void R21_9_GAMEPLAY_MOVE_BALL(final Client client, final URI baseUri) {

		final Response res1 = client
				.target(baseUri)
				.path("game/moveBall/3/1")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res1.getStatus());

		game = LogJSONAndUnmarshallValue(res1, Game.class);
		assertFalse(game.getBoard().getPiece(1).getHaveBall());
		assertTrue(game.getBoard().getPiece(3).getHaveBall());

	}

	@Test
	void R21_6_GAMEPLAY_TURN(final Client client, final URI baseUri) {

		assertTrue(game.getPlayer1().getMyTurn());
		assertFalse(game.getPlayer2().getMyTurn());

	}


	@Test
	void testCantMovePiece(final Client client, final URI baseUri) {


		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/0/0/1")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());

		assertNotNull(game);

		assertTrue(game.getBoard().getCase(0, 0).getIsAPiece());

		assertEquals(game.getBoard().getPiece(1).getCase().getX(), 0);
		assertEquals(game.getBoard().getPiece(1).getCase().getY(), 1);

		assertEquals(game.getBoard().getPiece(0).getCase().getX(), 0);
		assertEquals(game.getBoard().getPiece(0).getCase().getY(), 0);


		final Response res3 = client
				.target(baseUri)
				.path("game/movePiece/0/3/4")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res3, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());
		assertEquals(game.getBoard().getPiece(0).getCase().getX(), 0);
		assertEquals(game.getBoard().getPiece(0).getCase().getY(), 0);

		assertFalse(game.getBoard().getCase(3, 4).getIsAPiece());

	}

	@Test
	void R21_16_UNDO_MOVE_PIECE(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/0/1/0")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());


		final Response res3 = client
				.target(baseUri)
				.path("game/undo")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res3.getStatus());

		game = LogJSONAndUnmarshallValue(res3, Game.class);

		assertNotNull(game);


		assertTrue(game.getBoard().getCase(0, 0).getIsAPiece());
		assertFalse(game.getBoard().getCase(1, 0).getIsAPiece());

	}

	@Test
	void R21_16_UNDO_MOVE_BALL(final Client client, final URI baseUri) {

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
				.path("game/moveBall/3/5")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res1.getStatus());

		game = LogJSONAndUnmarshallValue(res1, Game.class);

		assertTrue(game.getBoard().getPiece(5).getHaveBall());
		assertFalse(game.getBoard().getPiece(3).getHaveBall());

		final Response res11 = client
				.target(baseUri)
				.path("game/undo")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res11, Game.class);

		assertFalse(game.getBoard().getPiece(5).getHaveBall());
		assertTrue(game.getBoard().getPiece(3).getHaveBall());
		assertEquals(2, game.getNbTurn());
	}


	@Test
	void R21_16_REDO_MOVE_PIECE(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/0/1/0")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);
		assertEquals(Response.Status.OK.getStatusCode(), res2.getStatus());


		final Response res3 = client
				.target(baseUri)
				.path("game/undo")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res3.getStatus());

		game = LogJSONAndUnmarshallValue(res3, Game.class);

		assertNotNull(game);


		final Response res4 = client
				.target(baseUri)
				.path("game/redo")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res4.getStatus());

		game = LogJSONAndUnmarshallValue(res4, Game.class);

		assertNotNull(game);
		assertFalse(game.getBoard().getCase(0, 0).getIsAPiece());
		assertTrue(game.getBoard().getCase(1, 0).getIsAPiece());

	}

	@Test
	void R21_16_REDO_MOVE_BALL(final Client client, final URI baseUri) {

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
				.path("game/moveBall/3/5")
				.request()
				.post(Entity.text(""));

		final Response res11 = client
				.target(baseUri)
				.path("game/undo")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res11, Game.class);
		assertFalse(game.getBoard().getPiece(5).getHaveBall());
		assertTrue(game.getBoard().getPiece(3).getHaveBall());
		assertEquals(2, game.getNbTurn());

		final Response res13 = client
				.target(baseUri)
				.path("game/redo")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res13, Game.class);
		assertFalse(game.getBoard().getPiece(3).getHaveBall());
		assertTrue(game.getBoard().getPiece(5).getHaveBall());

		final Response res14 = client
				.target(baseUri)
				.path("game/undo")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res14, Game.class);
		assertFalse(game.getBoard().getPiece(5).getHaveBall());
		assertTrue(game.getBoard().getPiece(3).getHaveBall());

		final Response res = client
				.target(baseUri)
				.path("game/movePiece/4/1/4")
				.request()
				.post(Entity.text(""));

		final Response res4 = client
				.target(baseUri)
				.path("game/redo")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res4, Game.class);
		assertFalse(game.getBoard().getPiece(5).getHaveBall());
		assertTrue(game.getBoard().getPiece(3).getHaveBall());
	}

	@Test
	void testGoMenu(final Client client, final URI baseUri) {

		final Response res = client
				.target(baseUri)
				.path("game/goMenu")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

		game = LogJSONAndUnmarshallValue(res, Game.class);

		assertNotNull(game);

		assertTrue(game.getMenu());

	}



	@Test
	void R21_8_13_GAMEPLAY_ACTIONS_GAME_TURN_ORDER(final Client client, final URI baseUri) {

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
				.path("game/moveBall/3/5")
				.request()
				.post(Entity.text(""));

		final Response res = client
				.target(baseUri)
				.path("game/movePiece/4/1/4")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
		game = LogJSONAndUnmarshallValue(res, Game.class);
		assertEquals(3, game.getNbTurn());
		assertEquals(0, game.getBoard().getPiece(4).getCase().getX());
		assertEquals(4, game.getBoard().getPiece(4).getCase().getY());

		final Response res5 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));
		assertEquals(Response.Status.OK.getStatusCode(), res3.getStatus());
		game = LogJSONAndUnmarshallValue(res5, Game.class);
		assertEquals(0, game.getNbTurn());

		final Response res6 = client
				.target(baseUri)
				.path("game/movePiece/4/5/4")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());


		final Response res7 = client
				.target(baseUri)
				.path("game/movePiece/5/5/5")
				.request()
				.post(Entity.text(""));

		final Response res8 = client
				.target(baseUri)
				.path("game/movePiece/5/4/5")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res8.getStatus());
		game = LogJSONAndUnmarshallValue(res8, Game.class);
		assertEquals(game.getBoard().getPiece(5).getCase().getX(), 4);
		assertEquals(game.getBoard().getPiece(5).getCase().getY(), 5);

		assertEquals(game.getBoard().getPiece(4).getCase().getX(), 5);
		assertEquals(game.getBoard().getPiece(4).getCase().getY(), 4);
		assertEquals(3, game.getNbTurn());

		final Response res9 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res9, Game.class);
		assertEquals(0, game.getNbTurn());
	}

	@Test
	void R21_9_GAMEPLAY_MOVE_BALL_DIAG(final Client client, final URI baseUri) {


		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/5/1/5")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);
		assertTrue(game.getBoard().getPiece(3).getHaveBall());

		final Response res3 = client
				.target(baseUri)
				.path("game/movePiece/5/2/5")
				.request()
				.post(Entity.text(""));

		final Response res1 = client
				.target(baseUri)
				.path("game/moveBall/3/5")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res1.getStatus());
		game = LogJSONAndUnmarshallValue(res1, Game.class);
		assertTrue(game.getBoard().getPiece(5).getHaveBall());
		assertFalse(game.getBoard().getPiece(3).getHaveBall());

		final Response res11 = client
				.target(baseUri)
				.path("game/undo")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res11, Game.class);
		assertFalse(game.getBoard().getPiece(5).getHaveBall());
		assertTrue(game.getBoard().getPiece(3).getHaveBall());
		assertEquals(2, game.getNbTurn());

		final Response res = client
				.target(baseUri)
				.path("game/movePiece/4/1/4")
				.request()
				.post(Entity.text(""));

		final Response res5 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));

		final Response res6 = client
				.target(baseUri)
				.path("game/movePiece/4/5/4")
				.request()
				.post(Entity.text(""));

		final Response res7 = client
				.target(baseUri)
				.path("game/movePiece/5/5/5")
				.request()
				.post(Entity.text(""));

		final Response res8 = client
				.target(baseUri)
				.path("game/movePiece/5/4/5")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res8.getStatus());
		game = LogJSONAndUnmarshallValue(res8, Game.class);
		assertEquals(game.getBoard().getPiece(5).getCase().getX(), 4);
		assertEquals(game.getBoard().getPiece(5).getCase().getY(), 5);

		assertEquals(game.getBoard().getPiece(4).getCase().getX(), 5);
		assertEquals(game.getBoard().getPiece(4).getCase().getY(), 4);
		assertEquals(3, game.getNbTurn());

		final Response res9 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res9, Game.class);
		assertEquals(0, game.getNbTurn());

		final Response res10 = client
				.target(baseUri)
				.path("game/moveBall/3/5")
				.request()
				.post(Entity.text(""));


		game = LogJSONAndUnmarshallValue(res10, Game.class);
		assertFalse(game.getBoard().getPiece(5).getHaveBall());
		assertTrue(game.getBoard().getPiece(3).getHaveBall());


		final Response res12 = client
				.target(baseUri)
				.path("game/moveBall/3/4")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res12.getStatus());
		game = LogJSONAndUnmarshallValue(res12, Game.class);
		assertFalse(game.getBoard().getPiece(3).getHaveBall());
		assertTrue(game.getBoard().getPiece(4).getHaveBall());
		
	}

	@Test
	void R21_9_GAMEPLAY_MOVE_BALL_HORIZONTALLY(final Client client, final URI baseUri) {


		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/4/1/4")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);
		assertTrue(game.getBoard().getPiece(3).getHaveBall());

		final Response res3 = client
				.target(baseUri)
				.path("game/movePiece/4/2/4")
				.request()
				.post(Entity.text(""));

		final Response res4 = client
				.target(baseUri)
				.path("game/movePiece/4/2/3")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res4.getStatus());
		game = LogJSONAndUnmarshallValue(res4, Game.class);
		assertEquals(2 ,game.getBoard().getPiece(4).getCase().getX());
		assertEquals(3 ,game.getBoard().getPiece(4).getCase().getY());

		final Response res5 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));

		final Response res6 = client
				.target(baseUri)
				.path("game/movePiece/4/5/4")
				.request()
				.post(Entity.text(""));

		final Response res7 = client
				.target(baseUri)
				.path("game/movePiece/5/5/5")
				.request()
				.post(Entity.text(""));

		final Response res8 = client
				.target(baseUri)
				.path("game/movePiece/5/4/5")
				.request()
				.post(Entity.text(""));

		assertEquals(Response.Status.OK.getStatusCode(), res8.getStatus());
		game = LogJSONAndUnmarshallValue(res8, Game.class);
		assertEquals(3, game.getNbTurn());

		final Response res9 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res9, Game.class);
		assertEquals(0, game.getNbTurn());

		final Response res10 = client
				.target(baseUri)
				.path("game/moveBall/3/4")
				.request()
				.post(Entity.text(""));


		game = LogJSONAndUnmarshallValue(res10, Game.class);
		assertFalse(game.getBoard().getPiece(3).getHaveBall());
		assertTrue(game.getBoard().getPiece(4).getHaveBall());
	}

	@Test
	void R21_9_GAMEPLAY_MOVE_BALL_VERTICALLY(final Client client, final URI baseUri) {

		final Response res2 = client
				.target(baseUri)
				.path("game/movePiece/4/1/4")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res2, Game.class);
		assertTrue(game.getBoard().getPiece(3).getHaveBall());

		final Response res3 = client
				.target(baseUri)
				.path("game/movePiece/4/1/5")
				.request()
				.post(Entity.text(""));

		final Response res4 = client
				.target(baseUri)
				.path("game/moveBall/3/2")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res4, Game.class);
		assertEquals(3, game.getNbTurn());
		assertEquals(1 ,game.getBoard().getPiece(4).getCase().getX());
		assertEquals(5 ,game.getBoard().getPiece(4).getCase().getY());
		final Response res5 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res5, Game.class);
		assertEquals(0, game.getNbTurn());

		final Response res6 = client
				.target(baseUri)
				.path("game/movePiece/1/5/1")
				.request()
				.post(Entity.text(""));

		final Response res7 = client
				.target(baseUri)
				.path("game/movePiece/1/4/1")
				.request()
				.post(Entity.text(""));
		
		game = LogJSONAndUnmarshallValue(res7, Game.class);
		assertEquals(2, game.getNbTurn());

		final Response res8 = client
				.target(baseUri)
				.path("game/moveBall/3/1")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res8, Game.class);

		assertEquals(3, game.getNbTurn());
		assertEquals(4 , game.getBoard().getPiece(1).getCase().getX());
		assertEquals(1 , game.getBoard().getPiece(1).getCase().getY());
		assertTrue(game.getBoard().getPiece(1).getHaveBall());
		assertFalse(game.getBoard().getPiece(3).getHaveBall());

		final Response res9 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));
		game = LogJSONAndUnmarshallValue(res9, Game.class);
		assertEquals(0, game.getNbTurn());

		final Response res13 = client
				.target(baseUri)
				.path("game/movePiece/3/1/3")
				.request()
				.post(Entity.text(""));

		final Response res14 = client
				.target(baseUri)
				.path("game/movePiece/3/1/2")
				.request()
				.post(Entity.text(""));

		final Response res15 = client
				.target(baseUri)
				.path("game/moveBall/2/3")
				.request()
				.post(Entity.text(""));

		final Response res16 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));

		final Response res17 = client
				.target(baseUri)
				.path("game/movePiece/4/5/4")
				.request()
				.post(Entity.text(""));

		final Response res18 = client
				.target(baseUri)
				.path("game/movePiece/5/5/5")
				.request()
				.post(Entity.text(""));

		final Response res19 = client
				.target(baseUri)
				.path("game/movePiece/5/4/5")
				.request()
				.post(Entity.text(""));

		final Response res20 = client
				.target(baseUri)
				.path("game/endTurn")
				.request()
				.post(Entity.text(""));
		final Response res10 = client
				.target(baseUri)
				.path("game/moveBall/3/4")
				.request()
				.post(Entity.text(""));

		game = LogJSONAndUnmarshallValue(res10, Game.class);
		assertFalse(game.getBoard().getPiece(3).getHaveBall());
		assertTrue(game.getBoard().getPiece(4).getHaveBall());
	}
}
