package xde.lincore.util.undo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class UndoStack<T extends Undoable> {
	public static final int NO_LIMIT = 0;
	protected List<T> history;
	protected int undoPointer;
	protected int limit = 0;
	
	
	
	public UndoStack() {
		history = new ArrayList<T>();
		undoPointer = -1;
	}
	
	public UndoStack(int limit) {
		this();
		assert limit >= 0 : "The limit must not be negative!";
		this.limit = limit;
	}
	
	/**
	 * Push an edit to the top of the stack.
	 * @param edit The edit to push, must not be null.
	 */
	public void push(T edit) {
		assert edit != null;
		discardRedos();		
		history.add(edit);		
		undoPointer = top();
		checkLimit();
	}
	
	public void clear() {
		history.clear();
		undoPointer = -1;
	}
	
	public T get(int index) {
		return history.get(index);
	}


	/**
	 * Check if an edit can be undone.
	 * @return True if there's at least one edit to undo.
	 */
	public boolean canUndo() {
		return undoPointer >= 0;
	}
	
	/**
	 * Check if an edit can be redone.
	 * @return True if there's at least one edit to redo.
	 */
	public boolean canRedo() {
		return undoPointer < top();
	}
	
	/**
	 * Get the number of undoable edits.
	 */
	public int getUndoCount() {
		return undoPointer + 1;
	}
	
	/**
	 * Get the number of redoable edits.
	 */
	public int getRedoCount() {
		return top() - undoPointer;
	}	
	
	/**
	 * Gets the last edit from the stack to be undone.
	 */
	public T undo() {
		T result = peekUndo();
		undoPointer--;
		result.undo();
		return result;
	}


	public T redo() {
		T result = peekRedo();
		undoPointer++;
		result.redo();
		return result;
	}

	public T peekUndo() {
		if (canUndo()) {
			return history.get(undoPointer);
		}
		else {
			throw new IllegalStateException("Nothing to undo.");
		}
	}
	
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
	
	public String getUndoDescription() {
		T t = peekUndo();
		return t.getDescription();
	}
	
	public String getRedoDescription() {
		T t = peekRedo();
		return t.getDescription();
	}
	
	public String getDescriptionOf(int index) {
		return history.get(index).getDescription();
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
		checkLimit();
	}


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
			}
		}
	}
}
