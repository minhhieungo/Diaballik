package diaballik.modele;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Human extends Player {

    public Human(final String name, final String color) {

        super(name, color);
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    public Human() {
        super();
    }

    @Override
    public void play(final Board board, final Game game) {
        
    }
}
