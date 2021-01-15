package diaballik.modele;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class IA extends Player {

    Level level;

    public IA(final Level l, final String name, final String color) {
        super(name, color);
        level = l;
    }

    public IA() {
        super();
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public void play(final Board b, final Game game) {
       level.play(b, game);
    }

}
