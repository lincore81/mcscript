package xde.lincore.util.undo;

public interface Undoable {
	public String getEditor();
	public void setDescription(String description);
	public String getDescription();
	public void undo();
	public void redo();
}
