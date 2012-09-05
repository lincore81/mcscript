package xde.lincore.mcscript.edit;

import java.util.ArrayDeque;

import xde.lincore.mcscript.BindingsMinecraft;
import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.Voxel;
import xde.lincore.util.undo.UndoStack;

public class EditSession {	
	public static final int UNDO_LIMIT = 256;
	private BindingsMinecraft mc;
	private boolean referredEditing;
	private boolean isEditing;
	UndoStack<WorldEdit> history;
	WorldEdit currentEdit = null;
		
	public EditSession(BindingsMinecraft mc, boolean referredEditing) {
		this.mc = mc;
		this.referredEditing = referredEditing;
		history = new UndoStack<WorldEdit>(UNDO_LIMIT);
	}
	
	public void begin(String description) {
		if (isEditing) {
			throw new IllegalStateException("Already editing: \"" +
					currentEdit.getDescription() + "\"");
		}
		else {
			currentEdit = new WorldEdit(description, mc.world);
			isEditing = true;
			mc.world.startEdit();
		}
	}
	
	public void finish() {
		if (!isEditing) {
			throw new IllegalStateException("No edit to finish.");
		}
		else {
			if (referredEditing) {
				currentEdit.flush();
			}
			history.push(currentEdit);
			currentEdit = null;
			mc.world.endEdit();
		}
	}
	
	public boolean isEditing() {
		return isEditing;
	}
	
	public void setBlock(Voxel position, Blocks newBlock) {
		setBlock(position, new BlockData(newBlock.getId(), newBlock.getData()));
	}
	
	public void setBlock(Voxel position, BlockData newBlock) {
		if (!isEditing) {
			throw new IllegalStateException("Can only place blocks while editing.");
		}
		BlockData oldBlock = mc.world.getBlockData(position);
		currentEdit.add(new BlockEdit(oldBlock, newBlock, position));
		if (!referredEditing) {
			mc.world.setBlock(position, newBlock);
		}
	}
	
	public boolean canUndo() {
		return history.canUndo();
	}
	
	public boolean canRedo() {
		return history.canRedo();
	}	
	
	public void undo() {
		history.undo();
	}
	
	public void redo() {
		history.redo();
	}
	
	public String getLastEditDump() {
		if (history.canUndo()) {
			return history.peekUndo().getDump();
		}
		else return "No edits.";
	}
	
	public int getEditCount() {
		return history.getSize();
	}
	
	public String getEditDump(int index) {
		assert index >= 0 && index <= history.top() : "index out of bounds.";
		return history.get(index).getDump();
	}
	
	public String getDump() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("EditSession (editing=%b, edits=%d, referred=%b):\n",
				isEditing, history.getSize(), referredEditing));
		for (int i = 0; i <= history.top(); i++) {			
			buffer.append((i == history.getStackPointer())? "  >" : "   ");
			buffer.append(history.getDescriptionOf(i) + "\n");			
		}
		return buffer.toString();
	}
}
