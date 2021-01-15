package diaballik.modele;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso({Level_progressive.class, Level_starting.class, Level_noob.class})
public abstract class Level {

    public MoveBall moveBallRandomly(final Piece oldpiece, final Board b) {
        final Random r = new Random();

        final int rand2;
        rand2 = r.nextInt(7);
        final Piece newpiece = b.getPiece(rand2);
        final MoveBall mb = new MoveBall(oldpiece, newpiece, b);

        return mb;
    }


    public MovePiece movePieceRandomly(final Piece p, final Board b) {
        final Random r = new Random();
        final Case oldcase = p.getCase();
        final Case newcase;
        final int r1 = r.nextInt(4);
        switch (r1) {
            case 1:
                if(oldcase.getY() < 6) {
                    newcase = b.getCase(oldcase.getX(), oldcase.getY() + 1);
                }else {
                    newcase = b.getCase(oldcase.getX(), oldcase.getY());
                }
                break;
            case 2:
                if(oldcase.getX() > 0) {
                    newcase = b.getCase(oldcase.getX() - 1, oldcase.getY());
                }else {
                    newcase = b.getCase(oldcase.getX(), oldcase.getY());
                }
                break;
            case 3:
                if(oldcase.getY() < 0) {
                    newcase = b.getCase(oldcase.getX(), oldcase.getY() - 1);
                } else {
                    newcase = b.getCase(oldcase.getX(), oldcase.getY());
                }
                break;
            default:
                if(oldcase.getX() < 6) {
                    newcase = b.getCase(oldcase.getX() + 1, oldcase.getY());
                } else {
                    newcase = b.getCase(oldcase.getX(), oldcase.getY());
                }
                break;
        };
        final MovePiece mp = new MovePiece(p, newcase);
        return mp;
    }

    public boolean canMoveballAI(final Board b) {
        final AtomicReference<Boolean> bool = new AtomicReference<>(false);
        final Piece p = b.getBallPlayer2().getPiece();
        IntStream.range(0, 7).forEach(i -> {
            final Piece newpiece = b.getPiece(i);
            final MoveBall mb = new MoveBall(p, newpiece, b);
            if (mb.canDo()) {
                bool.set(true);
            }
        });
        return bool.get();
    }

    public abstract void play(Board b, Game game);
}
