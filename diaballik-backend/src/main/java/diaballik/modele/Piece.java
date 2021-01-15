package diaballik.modele;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Piece {

	private boolean haveABall;
	private Player player;
	private int id;
	private Case cas;

	/**
	 *  
	 */
	public Piece(final int id, final Case c, final Player p) {
		player = p;
		this.id = id;
		cas = c;
		haveABall = false;
		cas.setIsAPiece(true);
	}

	public Piece() {
	}

	public Case getCase() {
		return cas;
	}

	/**
	 *  
	 */

	public void setCase(final Case c) {
		cas = c;
	}

	/**
	 *  
	 */
	public void setHaveBall(final boolean b) {
		haveABall = b;
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 *
	 */
	public int getId() {
		return id;
	}

	public boolean getHaveBall() {
		return haveABall;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Piece piece = (Piece) o;
		return haveABall == piece.haveABall &&
				id == piece.id &&
				player.equals(piece.player) &&
				cas.equals(piece.cas);
	}

	@Override
	public int hashCode() {
		return Objects.hash(haveABall, player, id, cas);
	}

}
