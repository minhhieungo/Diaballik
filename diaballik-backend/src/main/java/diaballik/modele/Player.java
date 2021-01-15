package diaballik.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Human.class, IA.class})
public abstract class Player {

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Player player = (Player) o;
		return myTurn == player.myTurn &&
				name.equals(player.name) &&
				color.equals(player.color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, color, myTurn);
	}

	public Player() {
	}

	public String getName() {
		return name;
	}

	private String name;

	private String color;

	private boolean myTurn;

	public Player(final String n, final String c) {
		name = n;
		color = c;
		myTurn = false;
	}

	/**
	 *  
	 */
	public void setMyTurn(final boolean b) {
		myTurn = b;
	}

	/**
	 *  
	 */
	public String getColor() {
		return color;
	}

	/**
	 *
	 */
	public boolean getMyTurn() {
		return myTurn;
	}


	public abstract void play(Board board, Game game);
}
