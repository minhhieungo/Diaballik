package diaballik.modele;
public class MovePiece extends Action {

	private Case newCase; //nouvelle case à laquelle je veux accéder
	private Piece piece;
	private Case oldCase;

	public MovePiece(final Piece p, final Case c) {
		piece = p;
		oldCase = p.getCase();
		newCase = c;
	}
	/**
	 *
	 */
	@Override
	public void doo() {
			piece.getCase().setIsAPiece(false);
			piece.setCase(newCase);
			piece.getCase().setIsAPiece(true);
	}

	/**
	 *
	 */
	@Override
	public void undo() {
		final MovePiece mp = new MovePiece(piece, oldCase);
		mp.doo();

	}


	/**
	 *
	 */
	@Override
	public void redo() {
		this.doo();

	}

	/**
	 *
	 */
	@Override
	public boolean canDo() {
		if (piece.getHaveBall() || (newCase.getIsAPiece()) || !newCase.caseInTheBoard()) {
			return false;
		}
		if(canMoveRight() || canMoveLeft() || canMoveUp() || canMoveDown()) {
			return true;
		}
		return false;
	}

	//ici je cheque si ma case est voisine a ma piece
	public boolean canMoveRight() {
		return (oldCase.getX() + 1 == newCase.getX()) && (oldCase.getY() == newCase.getY());
	}

	public boolean canMoveLeft() {
		return (oldCase.getX() - 1 == newCase.getX()) && (oldCase.getY() == newCase.getY());
	}

	public boolean canMoveUp() {
		return (oldCase.getX()  == newCase.getX()) && (oldCase.getY() + 1 == newCase.getY());
	}

	public boolean canMoveDown() {
		return (oldCase.getX() == newCase.getX()) && (oldCase.getY() - 1 == newCase.getY());
	}

	public Case getNewCase() {
		return newCase;
	}

	public Piece getPiece() {
		return piece;
	}
}
