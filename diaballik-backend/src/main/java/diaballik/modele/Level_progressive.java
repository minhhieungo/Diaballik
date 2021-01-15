package diaballik.modele;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Level_progressive extends Level {

    private List<Level> level;

    public Level_progressive() {

        level = new ArrayList();
        level.add(new Level_noob());
        level.add(new Level_starting());
    }


    @Override
    public void play(final Board b, final Game game) {

        if(game.getGlobalNbTurn() > 8) {
            level.get(1).play(b, game);
        } else {
            level.get(0).play(b, game);
        }
    }

}
