package diaballik.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Ball {

	/**
	 *  
	 */
	private Piece piece;

	public Ball(final Piece p) {
		piece = p;
	}

	public Ball() {
	}

	public Piece getPiece() {
		return piece;
	}

	/**
	 *  
	 */
	public void setPiece(final Piece p) {
		piece = p;
	}

}
