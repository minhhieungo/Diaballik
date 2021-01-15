package diaballik.modele;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Level_starting extends Level {
    public Deque<Piece> getPiecedangereux() {
        return piecedangereux;
    }

    public Case getPositionBall() {
        return positionBall;
    }

    public void setPositionBall(final Case positionBall) {
        this.positionBall = positionBall;
    }

    public Piece getGuardian() {
        return guardian;
    }

    public List<Piece> getListguardian() {
        return listguardian;
    }

    public Case getCaseBloque() {
        return caseBloque;
    }

    public boolean isBloquee() {
        return bloquee;
    }

    private Deque<Piece> piecedangereux;
    // le position du Ball de Player, je utilise ce variable pour detecter si il y a le changerment de position du Ball entre les tours
    private Case positionBall;
    // indique le piece qu'on va utiliser pour defendre contre le piecedangereux dans 1 move
    private Piece guardian;
    // le list des guardian qu'on a utilise dans ce tourne
    private List<Piece> listguardian;
    // le Case qu'on va bloque utilise notre guardian
    private Case caseBloque;
    // variable pour dire si on a reussir de bloquer ou pas
    private boolean bloquee;
    
    public Level_starting() {
        piecedangereux = new ArrayDeque<>();
        listguardian = new ArrayList<>();
        positionBall = new Case(0, 3);
        positionBall.setIsAPiece(true);
        caseBloque = new Case(8, 8);
        guardian = new Piece(100, caseBloque, new Human());;
        bloquee = false;
    }

    //fonction pour predire si on peut fait un moveBall // regarde si peut aller en x,y
    public boolean passablevirtuel(final int x, final int y, final Board b) {
        if (!b.getCase(x, y).getIsAPiece()) {
            final Case c = new Case(x, y);
            final Piece p = new Piece(100, c, b.getPlayer1());
            final MoveBall mb = new MoveBall(b.getBallPlayer1().getPiece(), p, b);
            return mb.canDo();
        }
        return false;
    }

    // test si le piece dangereux est a colonnes 5 , et (soit le piece est dans le meme lige
    // avec le ball, soit on peut avoir un pass virtuel dans les cases (6,y), (6,y+1), 6(y-1)
    public boolean warning2(final Board b, final Piece i, final MoveBall mb) {
        if ((i.getCase().getX() == 5) && mb.canDo() && b.getCase(6, i.getCase().getY()).getIsAPiece() &&
                (positionBall.getY() == i.getCase().getY())) {
            return false;
        }
        return ((i.getCase().getX() == 5) &&
                (mb.canDo()
                || passablevirtuel(6, i.getCase().getY(), b)
                || passablevirtuel(6, i.getCase().getY() + 1, b)
                || passablevirtuel(6, i.getCase().getY() - 1, b)));
    }
    // test si le piece dangereux est a colonnes 6 , et (soit le piece est dans le meme lige
    // avec le ball, soit on peut avoir un pass virtuel dans les cases (6,y), (6,y+1), 6(y-1)
    // (6,y+2) , (6,y-2)
    public boolean warning3(final Board b, final Piece i, final MoveBall mb) {
        return ((i.getCase().getX() == 6) && (mb.canDo()
                || passablevirtuel(6, i.getCase().getY() + 1, b)
                || passablevirtuel(6, i.getCase().getY() - 1, b)
                || (passablevirtuel(6, i.getCase().getY() + 2, b) &&
                !b.getCase(6, i.getCase().getY() + 1).getIsAPiece())
                || (passablevirtuel(6, i.getCase().getY() - 2, b) &&
                !b.getCase(6, i.getCase().getY() - 1).getIsAPiece())));
    }


    //fontionc pour warn AI si il y a un danger
    //zone dangereux est 3 cologne 4,5,6
    public void checkPositionPieceAdv(final Board b) {
        final Stream<Piece> sp = b.getPiecesPlayer1().stream();
        sp.forEach(i -> {
            final MoveBall mb = new MoveBall(b.getBallPlayer1().getPiece(), i, b);
            // test si le piece dangereux est a colonnes 4,et il n'y a pas un piece danse le case (5,y), et (soit le piece est dans le meme lige
            // avec le ball, soit on peut avoir un pass virtuel dans le case (6,y)
            if ((i.getCase().getX() == 4) && !b.getCase(5, i.getCase().getY()).getIsAPiece() &&
                    (mb.canDo() || passablevirtuel(6, i.getCase().getY(), b))) {
                piecedangereux.addLast(i);
            } else if (warning2(b, i, mb)) {
                piecedangereux.addLast(i);
            } else if (warning3(b, i, mb)) {
                piecedangereux.addLast(i);
            }
        });
    }

    //3 action pour respond

    //action fondamental, check nos piece et cherche un piece avec qui on peut utiliser pour
    // bloquer un Case c. Si il y a, on sauvegarde le piece dans guardian et le case dans caseBloque
    public boolean bloquerCase(final Board b, final Case c) {
        final Stream<Piece> sp2 = b.getPiecesPlayer2().stream();
        sp2.forEach(i -> {
            final MovePiece mp = new MovePiece(i, c);
                if (mp.canDo()) {
                    guardian = i;
                    bloquee = true;
                    caseBloque = c;
                }
            });
        return bloquee;
    }


    // action base sur bloquerCase, on appeler le bloquerCase sur toute des Case de un ligne
    public boolean bloquerpathhorizontal(final Board b, final int x) {
        final long nbCaseBloc = IntStream.range(positionBall.getX() + 1, x).filter(j -> bloquerCase(b, b.getCase(j, positionBall.getY())))
                .count();
        if(nbCaseBloc > 0) {
            bloquee = true;
        } else {
            bloquee = false;
        }
        return bloquee;
    }

    // action base sur bloquerCase, on appeller le bloquerCase sur tous des Case de un diagonal
    public boolean bloquerpathdiagonal(final Board b, final int x, final int y) {
        if (y < positionBall.getY()) {
            final long nbCaseBloc = IntStream.range(1, x - positionBall.getX()).filter(j ->
                bloquerCase(b, b.getCase(positionBall.getX() + j, positionBall.getY() - j)))
                .count();
            if(nbCaseBloc > 0) {
                bloquee = true;
            } else {
                bloquee = false;
            }

        } else if (y > positionBall.getY()) {
            final long nbCaseBloc = IntStream.range(1, x - positionBall.getX())
                    .filter(j -> bloquerCase(b, b.getCase(positionBall.getX() + j, positionBall.getY() + j)))
                    .count();
            if(nbCaseBloc > 0) {
                bloquee = true;
            } else {
                bloquee = false;
            }
        }
        return bloquee;
    }

    // respond s'il y a au moin un piece dangereux dans le cologne 4
    public void respond1(final Board b, final Piece pdangereux) {
        //si le piece dangereux est sur le meme ligne avec le ball
        if (pdangereux.getCase().getY() == positionBall.getY()) {
            // si on peut bloquer le path entre le piece dangereux et le ball
            // ou si on peut bloquer le case (5,y) ou (6,y)
            if (bloquerpathhorizontal(b, 4)
                    || bloquerCase(b, b.getCase(5, pdangereux.getCase().getY()))
                    || bloquerCase(b, b.getCase(6, pdangereux.getCase().getY()))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return;
            }
        } else { //si on peut bloquer le path entre le ball et le case (6,y)
                if (bloquerpathdiagonal(b, 6, pdangereux.getCase().getY()) || bloquerCase(b, b.getCase(6, pdangereux.getCase().getY()))) {
                    final MovePiece mp = new MovePiece(guardian, caseBloque);
                    mp.doo();
                    bloquee = false;
                    return;
                }
        }

        final Random r = new Random();
        final int rand2;
        rand2 = r.nextInt(7);
        final Piece p1 = b.getPiece(rand2);
        final MovePiece mp = movePieceRandomly(p1, b);
        if (!mp.canDo()) {
            this.oneTurn(b);
        } else {
            mp.doo();
        }
    }

    // un cas dans le method respond2
    public boolean bloqueCando2(final Board b, final Piece pdangereux) {
        //si le piece dangereux est sur le meme ligne avec le ball
        if (pdangereux.getCase().getY() == positionBall.getY()) {
            // si on peut bloquer le path entre le piece dangereux et le ball
            // ou si on peut bloquer le case (6,y)
            if (bloquerpathhorizontal(b, 5) || bloquerCase(b, b.getCase(6, pdangereux.getCase().getY()))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        } else if (positionBall.getY() < pdangereux.getCase().getY()) { //si le ball est à cote droite de piece damgereux
            // si on peut bloquer le path entre le piece dangereux et le ball
            // ou si on peut bloquer le case (6,y-1)
            if (bloquerpathdiagonal(b, 5, pdangereux.getCase().getY())
                    || bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() - 1))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        } else if (positionBall.getY() > pdangereux.getCase().getY()) { //si le ball est à cote gauche de piece damgereux
            // si on peut bloquer le path entre le piece dangereux et le ball
            // ou si on peut bloquer le case (6,y+1)
            if (bloquerpathdiagonal(b, 5, pdangereux.getCase().getY())
                    || bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() + 1))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        }
        return false;
    }

    // respond s'il y a au moin un piece dangereux dans le cologne 5
    public void respond2(final Board b, final Piece pdangereux) {
        final MoveBall mb = new MoveBall(b.getBallPlayer1().getPiece(), pdangereux, b);
        // si on peut passer le ball a piece dangereux
        if (mb.canDo()) {
            //appeler le fonction bloqueCando2
            if (bloqueCando2(b, pdangereux)) {
                return;
            }

        } else if (passablevirtuel(6, pdangereux.getCase().getY(), b)) { //si on peut passer virtuelement le ball au cas (6,y)
            //check si on peut bloquer ce case
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY()))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return;
            }
        } else if (passablevirtuel(6, pdangereux.getCase().getY() + 1, b)) {
            //si on peut passer virtuelement le ball au cas (6,y+1)
            //check si on peut bloquer ce case
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() + 1))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return;
            }
        } else if (passablevirtuel(6, pdangereux.getCase().getY() - 1, b)) {
            //si on peut passer virtuelement le ball au cas (6,y-1)
            //check si on peut bloquer ce case
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() - 1))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return;
            }
        }
        final Random r = new Random();
        final int rand2;
        rand2 = r.nextInt(7);
        final Piece p1 = b.getPiece(rand2);
        final MovePiece mp = movePieceRandomly(p1, b);
        if (!mp.canDo()) {
            this.oneTurn(b);
        } else {
            mp.doo();
        }
    }

    // un cas dans le method respond2
    public boolean bloqueCando3(final Board b, final Piece pdangereux) {
        //si le piece dangereux est sur le meme ligne avec le ball
        if (pdangereux.getCase().getY() == positionBall.getY()) {
            // si on peut bloquer le path entre le piece dangereux et le ball
            if (bloquerpathhorizontal(b, 6)) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        } else if (positionBall.getY() < pdangereux.getCase().getY()) {
            // si le ball est à cote gauche de piece damgereux
            // check si on peut bloquer le path entre le piece dangereux et le ball
            if (bloquerpathdiagonal(b, 6, pdangereux.getCase().getY())) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        } else if (positionBall.getY() > pdangereux.getCase().getY()) {

            // si le ball est à cote gauche de piece damgereux
            // check si on peut bloquer le path entre le piece dangereux et le ball
            if (bloquerpathdiagonal(b, 6, pdangereux.getCase().getY())) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        }
        return false;
    }

    //method check si adv peut passer virtuellement au cas (6,y+2) ou (6,y-2)
    //si oui on doit bloquer ces case
    public boolean passablevirtuel2(final Board b, final Piece pdangereux) {
        if (passablevirtuel(6, pdangereux.getCase().getY() + 2, b)
                && !b.getCase(6, pdangereux.getCase().getY() + 1).getIsAPiece()) {
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() + 2))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        } else if (passablevirtuel(6, pdangereux.getCase().getY() - 2, b)) {
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() - 2))
                    && !b.getCase(6, pdangereux.getCase().getY() - 1).getIsAPiece()) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        }
        return false;
    }

    //method check si adv peut passer virtuellement au cas (6,y+1) ou (6,y-1) ou (6,y)
    //si oui on doit bloquer ces case
    public boolean passablevirtuel1(final Board b, final Piece pdangereux) {
        if (passablevirtuel(6, pdangereux.getCase().getY(), b)) {
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY()))) {
                // ici j'essaie de mettre une piece a l'IA la ou mon adversaire se trouve, PB!
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        } else if (passablevirtuel(6, pdangereux.getCase().getY() + 1, b)) {
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() + 1))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        } else if (passablevirtuel(6, pdangereux.getCase().getY() - 1, b)) {
            if (bloquerCase(b, b.getCase(6, pdangereux.getCase().getY() - 1))) {
                final MovePiece mp = new MovePiece(guardian, caseBloque);
                mp.doo();
                bloquee = false;
                return true;
            }
        }
        return false;
    }

    // respond s'il y a au moin un piece dangereux dans le cologne 6
    public void respond3(final Board b, final Piece pdangereux) {
        final MoveBall mb = new MoveBall(b.getBallPlayer1().getPiece(), pdangereux, b);
        // si on peut passer le ball a piece dangereux
        if (mb.canDo()) {
            //appeler le fonction bloqueCando3
            if (bloqueCando3(b, pdangereux)) {
                return;
            }
        } else if (passablevirtuel1(b, pdangereux) || passablevirtuel2(b, pdangereux)) {
            return;
        }
        final Random r = new Random();
        final int rand2;
        rand2 = r.nextInt(7);
        final Piece p1 = b.getPiece(rand2);
        final MovePiece mp = movePieceRandomly(p1, b);
        if (!mp.canDo()) {
            this.oneTurn(b);
        } else {
            mp.doo();
        }
    }


    //2 action respond : Bloquer le path et Bloquer le case
    //3 niveau de warning ( donc des different respond)
    //si il y a pas le danger , AI move piece randomly
    //detect le piece qui est le plus haut ( et aussi des piece dans les 3 dernieres ligne)
    // toujour detecter le position du ball
    //
    @Override
    public void play(final Board b, final Game game) {

        //check si le position du ball adv est change ou pas
        if (!b.getBallPlayer1().getPiece().getCase().equals(positionBall)) { // pourquoi on check que qd on a la balle de mon adversaire qui bouge???
            // si oui on doit check position des piece adv
            listguardian.clear();
            checkPositionPieceAdv(b);
        }
        //update le position du ball
        positionBall = b.getBallPlayer1().getPiece().getCase();
        oneTurn(b);
        game.setNbTurn(1);
        // apres chaque tour on ajoute le piece qu'on a utilise pour bloquer dans le list guardian
        // donc on ne le touche plus ce tour
        listguardian.add(new Piece(guardian.getId(), guardian.getCase(), guardian.getPlayer()));
        oneTurn(b);
        game.setNbTurn(2);
        listguardian.add(new Piece(guardian.getId(), guardian.getCase(), guardian.getPlayer())); //est-ce qu'on a add 2 guardian different ou on a add que 1 guardian ?
        oneTurn(b);
        game.setNbTurn(3);
        guardian = null;
        game.endTurn();

    }


    public void oneTurn(final Board b) {
        if (piecedangereux.size() != 0) {
            final Piece p = piecedangereux.removeLast();
            switch (p.getCase().getX()) {
                case 5:

                    respond2(b, p);
                    break;
                case 6:
                    respond3(b, p);
                    break;
                default:

                    respond1(b, p);
                    break;
            }
        } else {
            final Random r = new Random();
            final int rand1; // permet de choisir MovePiece ou MoveBall
            final int rand2;
            if (canMoveballAI(b)) {
                rand1 = r.nextInt(2);
            } else {
                rand1 = 0;
            }
            if (rand1 == 0) {
                rand2 = r.nextInt(7);
                final Piece p1 = b.getPiece(rand2);
                if (listguardian.contains(p1)) {
                    this.oneTurn(b);
                    return;
                }
                final MovePiece mp = movePieceRandomly(p1, b);
                if (mp.canDo()) {
                    mp.doo();
                    return;
                } else {
                    this.oneTurn(b);
                    return;
                }

            } else {
                final Piece p2 = b.getBallPlayer2().getPiece();
                final MoveBall mb = moveBallRandomly(p2, b);
                if (mb.canDo()) {
                    mb.doo();
                    return;
                } else {
                    this.oneTurn(b);
                    return;
                }
            }
        }
    }
}
