package xde.lincore.util.undo;

public interface Undoable {
	public String getDescription();
	public void undo();
	public void redo();
}
