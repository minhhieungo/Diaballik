package diaballik.resource;
import diaballik.modele.Game;
import diaballik.modele.Level_noob;
import diaballik.modele.Level_progressive;
import diaballik.modele.Level_starting;
import io.swagger.annotations.Api;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

@Singleton
@Path("game")
@Api(value = "game")
public class GameResource {

	private Game newGame;

	public GameResource() {
		newGame = new Game();
	}
	//c'est le front-end qui veut changer quelque chose dans le back-end
	@POST
	@Path("configureGamePlayer/{name1}/{colour1}/{name2}")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postConfigureGameP(@PathParam("name1") final String name1, @PathParam("colour1") final String colour1, @PathParam("name2") final String name2) {


		try { //tentative de transaction
			newGame = new Game();
			newGame.configureGameWithPlayer(name1, colour1, name2);

			return Response.status(Response.Status.OK).entity(newGame).build();

		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage()).build());
		}
	}

	@POST
	@Path("configureGameIA/{name1}/{colour1}/{level}")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postConfigureGameIA(@PathParam("name1") final String name1, @PathParam("colour1") final String colour1, @PathParam("level") final String level) {


		try { //tentative de transaction
			newGame = new Game();
			if("progressive".equals(level)) {
				newGame.configureGameWithIA(name1, colour1, new Level_progressive());
				return Response.status(Response.Status.OK).entity(newGame).build();
			}
			if("noob".equals(level)) {
				newGame.configureGameWithIA(name1, colour1, new Level_noob());
				return Response.status(Response.Status.OK).entity(newGame).build();
			}
			if("starting".equals(level)) {
				newGame.configureGameWithIA(name1, colour1, new Level_starting());
				return Response.status(Response.Status.OK).entity(newGame).build();
			} else {
				throw new Exception("The level is incorrect");
			}

		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage()).build());
		}
	}

	@POST
	@Path("movePiece/{pieceId}/{posX}/{posY}")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postMovePiece(@PathParam("pieceId") final int pieceId, @PathParam("posX") final int posX, @PathParam("posY") final int posY) {

		try { //tentative de transaction
			newGame.updatePiece(pieceId, posX, posY);
			return Response.status(Response.Status.OK).entity(newGame).build();
		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		}
	}

	@POST
	@Path("moveBall/{pieceId1}/{pieceId2}")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postMoveBall(@PathParam("pieceId1") final int pieceId1, @PathParam("pieceId2") final int pieceId2) {

		try { //tentative de transaction
			newGame.updateBall(pieceId1, pieceId2);
			return Response.status(Response.Status.OK).entity(newGame).build();
		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		}
	}

	@POST
	@Path("redo")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postRedo() {

		try { //tentative de transaction
			newGame.redo();
			return Response.status(Response.Status.OK).entity(newGame).build();
		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		}
	}

	@POST
	@Path("undo")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postUndo() {

		try { //tentative de transaction
			newGame.undo();
			return Response.status(Response.Status.OK).entity(newGame).build();
		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		}
	}

	@POST
	@Path("endTurn")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postEndTurn() {

		try { //tentative de transaction
			newGame.endTurn();
			return Response.status(Response.Status.OK).entity(newGame).build();
		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		}
	}

	@POST
	@Path("goMenu")
	@Produces(MediaType.APPLICATION_JSON) // permet de certifier que l'on produit un objetde type XML elle est pas obligé par défaut ça doit être qqchsoe (JSON ?!)
	public Response postGoMenu() {

		try { //tentative de transaction
			newGame.goMenu();
			return Response.status(Response.Status.OK).entity(newGame).build();
		} catch (IllegalStateException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "Cannot persist").build());
		} catch (final NullPointerException ex) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST, "The name is not correct").build());
		}
	}

}
