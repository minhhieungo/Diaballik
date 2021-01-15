package diaballik.modele;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class MoveBall extends Action {

	private Piece oldpiece;

	private Piece newpiece;

	private boolean freepieceline;

	private Board board;

	private int x1;
	private int x2;
	private int y1;
	private int y2;

	public MoveBall(final Piece p1, final Piece p2, final Board b) {
		oldpiece = p1;
		newpiece = p2;
		board = b;
		freepieceline = true;
		x1 = oldpiece.getCase().getX();
		x2 = newpiece.getCase().getX();
		y1 = oldpiece.getCase().getY();
		y2 = newpiece.getCase().getY();
	}
	/**
	 *  
	 */
	@Override
	public void doo() {
		oldpiece.setHaveBall(false);
		if (board.getPlayer1().getMyTurn()) {
			board.getBallPlayer1().setPiece(newpiece);
		} else {
			board.getBallPlayer2().setPiece(newpiece);
		}
		newpiece.setHaveBall(true);
	}
	/**
	 *  
	 */
	@Override
	public void undo() {
		final Piece olP = newpiece;
		final Piece newP = oldpiece;
		final MoveBall re = new MoveBall(olP, newP, board);
		re.doo();
	}



	/**
	 *  
	 */
	@Override
	public void redo() {
		this.doo();

	}

	public boolean line() {
		/* check if two piece is on a line*/
		return Objects.equals(abs(x1 - x2), abs(y1 - y2)) || Objects.equals(x1, x2) || Objects.equals(y1, y2);
	}


	public void moveHorizontal() { /*move to the right*/
		if(x1 < x2) {
			IntStream.range(1, x2 - x1).forEach(i -> {
				if(board.getCase(x1 + i, y1).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}

		/*move to the left*/
		if(x1 > x2) {
			IntStream.range(1, x1 - x2).forEach(i -> {
				if(board.getCase(x1 - i, y1).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}
	}

	public void moveVertical() {
		/*move to the top*/
		if(y1 < y2) {
			IntStream.range(1, y2 - y1).forEach(i -> {
				if(board.getCase(x1, y1 + i).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}

		/*move to the bottom*/
		if(y1 > y2) {
			IntStream.range(1, y1 - y2).forEach(i -> {
				if(board.getCase(x1, y1 - i).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}
	}

	public void moveUpDiagonal() {
		/*move to the first quadrant */
		if (x1 < x2) {
			IntStream.range(1, y2 - y1).forEach(i -> {
				if(board.getCase(x1 + i, y1 + i).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}

		/*move to the second quadrant */
		if (x1 > x2) {
			IntStream.range(1, y2 - y1).forEach(i -> {
				if(board.getCase(x1 + i, y1 + i).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}

	}

	public void moveDownDiagonal() {
		/*move to the third quadrant */
		if (x1 > x2) {
			IntStream.range(1, y1 - y2).forEach(i -> {
				if(board.getCase(x1 - i, y1 - i).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}

		/*move to the fourth quadrant */
		if (x1 < x2) {
			IntStream.range(1, y1 - y2).forEach(i -> {
				if(board.getCase(x1 + i, y1 - i).getIsAPiece()) {
					freepieceline = false;
				}
			});
		}
	}

	public boolean piecefreeline() {
		/* check if the line between two piece is free */
		/*in the case we have two piece in a same row */
		if(y1 == y2) {
			moveHorizontal();
		}
		/*in the case we have two piece in a same column */
		if(x1 == x2) {
			moveVertical();
		}

		if (y1 < y2) {
			moveUpDiagonal();
		}

		if (y1 > y2) {
			moveDownDiagonal();
		}
		return freepieceline;

	}
	/**
	 *  
	 */
	@Override
	public boolean canDo() {

		/*we check if our pieces belong to same players*/
		/*we check if our pieces are different*/
		if (!(oldpiece.getPlayer().getColor().equals(newpiece.getPlayer().getColor())) || (oldpiece.getId() == newpiece.getId()) || !oldpiece.getHaveBall()) {
			return false;
		}
		/*we check if our pieces are on the same line and the line is a free-piece line*/
		if(line() && piecefreeline()) {
			return true;
		}
		return false;
	}

}
