package diaballik.modele;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Board {

	public static final int width = 7;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static final int height = 7;

	private List<Piece> piecesPlayer1;
	private List<Piece> piecesPlayer2;
	private Player player1;
	private Player player2;
	private List<Case> board;
	private Ball ballPlayer1;
	private Ball ballPlayer2;

	public Board(final Player play1, final Player play2) {

		player1 = play1;
		player2 = play2;
		player1.setMyTurn(true);
		piecesPlayer1 = new ArrayList<>();
		piecesPlayer2 = new ArrayList<>();
		board = new ArrayList<>();



		IntStream.range(0, height).forEach(i -> {
			IntStream.range(0, width).forEach(j -> {
				final Case c = new Case(i, j);
				board.add(c);
				if (i == 0) {
					c.setIsAPiece(true);
					final Piece p = new Piece(j, c, player1);

					if (j == (width / 2)) {
						ballPlayer1 = new Ball(p);
						p.setHaveBall(true);
					}
					piecesPlayer1.add(p);
				}
				if (i == (height - 1)) {
					c.setIsAPiece(true);
					final Piece p = new Piece(j, c, player2);

					if (j == (width / 2)) {
						ballPlayer2 = new Ball(p);
						p.setHaveBall(true);
					}
					piecesPlayer2.add(p);
				}


			});


		});
	}

	public Board() {
	}


	public List<Piece> getPiecesPlayer1() {
		return piecesPlayer1;
	}

	public List<Piece> getPiecesPlayer2() {
		return piecesPlayer2;
	}

	/**
	 *
	 */

	public Case getCase(final int posX, final int posY) {
	    if ((0 <= posX) && (0 <= posY) && (posY <= height - 1) && (posX <= width - 1)) {
            return board.get(posX * width + posY);
        }
	    return new Case(posX, posY);
	}

	/**
	 *
	 */
	public Piece getPiece(final int idPiece) {

		if (player1.getMyTurn()) {
			final Stream<Piece> sp = piecesPlayer1.stream();
			final Piece p = (Piece) sp.filter(x -> x.getId() == idPiece).findFirst().get();
			return p;
		} else {
			final Stream<Piece> sp = piecesPlayer2.stream();
			final Piece p = (Piece) sp.filter(x -> x.getId() == idPiece).findFirst().get();
			return p;
		}
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public Ball getBallPlayer1() {
		return ballPlayer1;
	}

	public Ball getBallPlayer2() {
		return ballPlayer2;
	}

}
