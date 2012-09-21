package xde.lincore.util.undo;

public interface IUndoHistory<T extends Undoable> {

	/**
	 * Check if an edit can be undone.
	 * @return True if there's at least one edit to undo.
	 */
	public abstract boolean canUndo();

	/**
	 * Check if an edit can be redone.
	 * @return True if there's at least one edit to redo.
	 */
	public abstract boolean canRedo();

	/**
	 * Get the number of undoable edits.
	 */
	public abstract int getUndoCount();

	/**
	 * Get the number of redoable edits.
	 */
	public abstract int getRedoCount();
	
	/**
	 * Discard all undo/redo data.
	 */
	public void clear();

	/**
	 * Gets the last edit from the stack to be undone.
	 */
	public abstract T undo();

	public abstract T redo();

	public abstract T peekUndo();

	public abstract T peekRedo();

	public abstract String getUndoDescription();

	public abstract String getRedoDescription();

	public abstract int getLimit();

	public abstract int getSize();

}