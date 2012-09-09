package xde.lincore.mcscript.edit;

import java.util.ArrayDeque;

import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.geom.Voxel;
import xde.lincore.mcscript.wrapper.MinecraftWrapper;
import xde.lincore.util.undo.UndoStack;

public class EditSessionController extends UndoStack<WorldEdit> {
	public static final int UNDO_LIMIT = 32;
	private MinecraftWrapper mc;	
	ArrayDeque<WorldEdit> jobStack;
	WorldEdit currentEdit = null;
		
	public EditSessionController(MinecraftWrapper mc) {
		this.mc = mc;
		setLimit(UNDO_LIMIT);
		jobStack = new ArrayDeque<WorldEdit>();
	}
	

	public void checkIn(IEditSession session) {
		if (!session.isEmpty()) {
			jobStack.push(session.getEditData());
		}
	}
	
	public IEditSession checkOut() {
		return new EditSession(this, mc, null, EditSession.NO_BLOCK_LIMIT);
	}
	
	public String getLastEditDump() {
		if (canUndo()) {
			return peekUndo().getDump();
		}
		else return "No edits.";
	}
	
	
	public String getEditDump(int index) {
		assert index >= 0 && index <= top() : "index out of bounds.";
		return history.get(index).getDump();
	}
	
	public String getDump() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("EditSession (edits=%d):\n", getSize()));
		for (int i = 0; i <= top(); i++) {			
			buffer.append((i == getStackPointer())? "  >" : "   ");
			buffer.append(get(i).editor + ": " + getDescriptionOf(i) + "\n");			
		}
		return buffer.toString();
	}
	
	public void dump() {
		mc.echo(getDump());
	}

	public void update() {
		if (jobStack.isEmpty()) return;
		
		mc.world.startEdit();
		try {
			while (jobStack.size() > 0) {			
				WorldEdit edit = jobStack.pop();
				edit.flush();
				push(edit);
			}
		} finally {
			mc.world.endEdit();
		}
	}
}
