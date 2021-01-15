package diaballik.modele;


import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Level_noob extends Level {


    public Level_noob() {
    }

    @Override
    public void play(final Board b, final Game game) {
        oneTurn(b);
        game.setNbTurn(1);
        oneTurn(b);
        game.setNbTurn(2);
        oneTurn(b);
        game.setNbTurn(3);
        game.endTurn();

    }
    
    public void oneTurn(final Board b) {
        final Random r = new Random();
        final int rand1; // permet de choisir MovePiece ou MoveBall
        final int rand2; // permet de choisir si je suis un MovePiece la piece que je vais bouger
        //checker si la balle peut etre bouger ou non pour savoir quel mouvement on fait
        if (canMoveballAI(b)) {
            rand1 = r.nextInt(2);
        } else {
            rand1 = 0;
        }
        if(rand1 == 0) {
            rand2 = r.nextInt(7);

            final Piece p1 = b.getPiece(rand2);


            final MovePiece mp = movePieceRandomly(p1, b);


            if(!mp.canDo()) {
                this.oneTurn(b);
                return;
            } else {
                mp.doo();
                return;
            }

        } else {
            final Piece p2 = b.getBallPlayer2().getPiece();
            final MoveBall mb = moveBallRandomly(p2, b);
            if(!mb.canDo()) {
                this.oneTurn(b);
                return;
            } else {
                mb.doo();
                return;
            }
        }
    }
}
