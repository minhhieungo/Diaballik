package diaballik.modele;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Game {

	private boolean win;


	private boolean menu;

	public void setNbTurn(final int nbTurn) {
		this.nbTurn = nbTurn;
	}

	private int nbTurn;

	public void setGlobalNbTurn(final int globalNbTurn) {
		this.globalNbTurn = globalNbTurn;
	}

	private int globalNbTurn;

	private Board board;

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	private Player player1;

	private Player player2;

	@XmlTransient
	private Deque<Undoable> undoableActions;
	@XmlTransient
	private Deque<Undoable> redoableActions;

	public boolean getWin() {
		return win;
	}

	public int getNbTurn() {
		return nbTurn;
	}

	public Game() {
		win = false;
		globalNbTurn = 0;
		menu = false;
		player1 = null;
		player2 = null;
		nbTurn = 0;
		undoableActions = new ArrayDeque<>();
		redoableActions = new ArrayDeque<>();
	}
	/**
	 *  
	 */
	public void configureGameWithIA(final String namePlayer1, final String colorPlayer1, final Level level) throws Exception {
		win = false;
		player1 = new Human(namePlayer1, colorPlayer1);
		player1.setMyTurn(true);
		switch(colorPlayer1) {
			case "BLANC":
					player2 = new IA(level, "IA_diaballik", "NOIR");
					board = new Board(player1, player2);
					break;
			case "NOIR":
					player2 = new IA(level, "IA_diaballik", "BLANC");
					board = new Board(player1, player2);
					break;
			default: throw new Exception("Couleur invalide");

		}

	}
	public boolean getMenu() {
		return menu;
	}

	public Board getBoard() {
		return board;
	}

	/**
	 *  
	 */
	public void configureGameWithPlayer(final String namePlayer1, final String colorPlayer1, final String namePlayer2) throws Exception {
		win = false;
		player1 = new Human(namePlayer1, colorPlayer1);
		player1.setMyTurn(true);
		switch(colorPlayer1) {
			case "BLANC":
				player2 = new Human(namePlayer2, "NOIR");
				board = new Board(player1, player2);
				break;
			case "NOIR":
				player2 = new Human(namePlayer2, "BLANC");
				board = new Board(player1, player2);
				break;
			default: throw new Exception("Couleur invalide");
		}
	}
	/**
	 *  
	 */
	public void addActionUndo(final Action action) { //est qu'on surprimer ces deux fonction?
		undoableActions.addFirst(action);
	}

	/**
	 *  
	 */
	public void addActionRedo(final Action action) {
		redoableActions.addFirst(action);
	}

	/**
	 *  
	 */
	public void updatePiece(final int idPiece, final int posX, final int posY) {
		if(nbTurn < 3) {
			final Case newCase = board.getCase(posX, posY);
			final Piece piece = board.getPiece(idPiece);
			final MovePiece mp = new MovePiece(piece, newCase);

			if (mp.canDo()) {
				mp.doo();
				nbTurn++;
				undoableActions.addFirst(mp);
				redoableActions.clear();
			}

		}
	}

	public void updateBall(final int idPiece1, final int idPiece2) {

		if(nbTurn < 3) {
			final Piece pAnc = board.getPiece(idPiece1);
			final Piece pNew = board.getPiece(idPiece2);
			final MoveBall a = new MoveBall(pAnc, pNew, board);

			if (a.canDo()) {
				a.doo();
				nbTurn++;
				undoableActions.addFirst(a);
				redoableActions.clear(); //exception pas besoin de gérer car gérer dans le front
			}

			this.checkWin();

			if (win) {
				System.out.println("GAGNE OU PERDU");
				// Le front end vérifiera l'argument win et en fonction de ca affichage différent

			}
		}
	}

	public void checkWin() {
		Case c = board.getBallPlayer1().getPiece().getCase();
		final List<Case> list1 = IntStream
				.range(0, 7)
				.mapToObj(j -> board.getCase(6, j)).collect(Collectors.toList());

		if(list1.contains(c)) {
			win = true;
		}

		c = board.getBallPlayer2().getPiece().getCase();
		final List<Case> list2 = IntStream
				.range(0, 7)
				.mapToObj(j -> board.getCase(0, j)).collect(Collectors.toList());

		if(list2.contains(c)) {
			win = true;
		}

	}

	/**
	 *  
	 */
	public void undo() {
		if(undoableActions.size() > 0) {
			final Undoable a = undoableActions.removeFirst(); //addFirst et removeFirst pour ajouter en tete et prendre en tete
			((Action) a).undo();
			redoableActions.addFirst(a);
			nbTurn--;
		}
	}

	/**
	 *  
	 */
	public void redo() {
		if(redoableActions.size() > 0) {
			final Undoable a = redoableActions.removeFirst();
			((Action) a).redo();
			undoableActions.addFirst(a);
			nbTurn++;
		}
	}

	/**
	 *  
	 */
	public void setWin(final boolean bool) {
		win = bool;
	}

	/**
	 *  
	 */
	public void endTurn() {
		if (nbTurn == 3) {
			globalNbTurn = globalNbTurn + 1;
			undoableActions.clear();
			redoableActions.clear();
			nbTurn = 0;
			if (player1.getMyTurn()) {
				player1.setMyTurn(false);
				player2.setMyTurn(true);
				player2.play(board, this);

			} else {
				player1.setMyTurn(true);
				player2.setMyTurn(false);
				player1.play(board, this);
			}
		}
	}

	public int getGlobalNbTurn() {
		return globalNbTurn;
	}

	public void goMenu() {
		menu = true;
	}

}
