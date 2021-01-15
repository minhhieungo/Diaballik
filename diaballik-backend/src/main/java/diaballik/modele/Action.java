package diaballik.modele;

public abstract class Action implements Undoable {

	/**
	 *  
	 */
	public abstract void doo();

	/**
	 *  
	 */
	public abstract boolean canDo();

	/**
	 *  
	 */
	@Override
	public abstract void undo();
	/**
	 *  
	 */
	@Override
	public abstract void redo();

}
