package diaballik.model;

import com.github.hanleyt.JerseyExtension;
import diaballik.modele.Game;
import diaballik.modele.IA;
import diaballik.modele.Level_progressive;
import diaballik.resource.GameResource;
import diaballik.resource.MyExceptionMapper;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.net.URI;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLevel_progressive {


    static final Logger log = Logger.getLogger(TestLevel_progressive.class.getSimpleName());
    Game game;
    Game game2;


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
                .path("game/configureGameIA/Paul/NOIR/progressive")
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
                .path("game/moveBall/3/5")
                .request()
                .post(Entity.text(""));

        game = LogJSONAndUnmarshallValue(res1, Game.class);

    }


    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void R23_5_AI_LEVEL_PROGRESSIVE(final Client client, final URI baseUri) {

        final Response res = client
                .target(baseUri)
                .path("game/endTurn")
                .request()
                .post(Entity.text(""));




        game = LogJSONAndUnmarshallValue(res, Game.class);
        game.setGlobalNbTurn(9);
        game.setNbTurn(3);
        game.endTurn();
        assertEquals(Level_progressive.class, ((IA) game.getPlayer2()).getLevel().getClass());

    }

}
