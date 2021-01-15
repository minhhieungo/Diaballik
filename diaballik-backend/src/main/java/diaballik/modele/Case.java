package diaballik.modele;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Case {

	private int positionX;

	private int positionY;

	private boolean isAPiece;
	/**
	 *  
	 */
	public Case(final int x, final int y) {
		positionX = x;
		positionY = y;
		isAPiece = false;
	}

	public Case() {
	}

	public int getX() {
		return positionX;
	}

	/**
	 *  
	 */
	public int getY() {
		return positionY;
	}

	/**
	 *  
	 */
	public void setIsAPiece(final boolean b) {
		isAPiece = b;
	}

	public boolean caseInTheBoard() {
		return 0 <= positionX && 0 <= positionY && 6 >= positionX && 6 >= positionY;
	}

	public boolean getIsAPiece() {
		return isAPiece;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Case aCase = (Case) o;
		return positionX == aCase.positionX &&
				positionY == aCase.positionY &&
				isAPiece == aCase.isAPiece;
	}

	@Override
	public int hashCode() {
		return Objects.hash(positionX, positionY, isAPiece);
	}
}
