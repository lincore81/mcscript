package xde.lincore.util.undo;

import java.util.ArrayList;
import java.util.List;

public class UndoStack<T extends Undoable> implements IUndoHistory<T> {
	public static final int NO_LIMIT = 0;
	protected List<T> history;
	protected int undoPointer;
	protected int limit = 0;



	public UndoStack() {
		history = new ArrayList<T>();
		undoPointer = -1;
	}

	public UndoStack(final int limit) {
		this();
		assert limit >= 0 : "The limit must not be negative!";
		this.limit = limit;
	}

	/**
	 * Push an Undoable to the top of the stack.
	 * @param edit The Undoable to push, must not be null.
	 */
	public void push(final T edit) {
		assert edit != null;
		discardRedos();
		history.add(edit);
		undoPointer = top();
		checkLimit();
	}


	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#clear()
	 */
	@Override
	public void clear() {
		history.clear();
		undoPointer = -1;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return undoPointer >= 0;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#canRedo()
	 */
	@Override
	public boolean canRedo() {
		return undoPointer < top();
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#getUndoCount()
	 */
	@Override
	public int getUndoCount() {
		return undoPointer + 1;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#getRedoCount()
	 */
	@Override
	public int getRedoCount() {
		return top() - undoPointer;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#undo()
	 */
	@Override
	public T undo() {
		final T result = peekUndo();
		undoPointer--;
		result.undo();
		return result;
	}


	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#redo()
	 */
	@Override
	public T redo() {
		final T result = peekRedo();
		undoPointer++;
		result.redo();
		return result;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#peekUndo()
	 */
	@Override
	public T peekUndo() {
		if (canUndo()) {
			return history.get(undoPointer);
		}
		else {
			throw new IllegalStateException("Nothing to undo.");
		}
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#peekRedo()
	 */
	@Override
	public T peekRedo() {
		if (canRedo()) {
			return history.get(undoPointer + 1);
		}
		else {
			throw new IllegalStateException("Nothing to redo.");
		}
	}


	public void discardRedos() {
		while (undoPointer < top()) {
			history.remove(top());
		}
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#getUndoDescription()
	 */
	@Override
	public String getUndoDescription() {
		final T t = peekUndo();
		return t.getDescription();
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#getRedoDescription()
	 */
	@Override
	public String getRedoDescription() {
		final T t = peekRedo();
		return t.getDescription();
	}

	public String getDescriptionOf(final int index) {
		return history.get(index).getDescription();
	}

	public T get(final int index) {
		return history.get(index);
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#getLimit()
	 */
	@Override
	public int getLimit() {
		return limit;
	}

	public void setLimit(final int limit) {
		this.limit = limit;
		checkLimit();
	}

	/* (non-Javadoc)
	 * @see xde.lincore.util.undo.IUndoHistory#getSize()
	 */
	@Override
	public int getSize() {
		return history.size();
	}


	public int getStackPointer() {
		return undoPointer;
	}

	/**
	 * Get the highest valid stack index.
	 */
	public int top() {
		return history.size() - 1;
	}

	private void checkLimit() {
		if (limit > NO_LIMIT) {
			while (history.size() > limit) {
				history.remove(0);
				undoPointer--;
			}
		}
	}
}
