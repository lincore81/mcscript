package xde.lincore.mcscript.edit;

import java.util.ArrayDeque;

import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.env.ScriptRunner;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;
import xde.lincore.mcscript.minecraft.WorldWrapper;
import xde.lincore.util.undo.UndoStack;

public class EditSessionController extends UndoStack<WorldEdit> {
	public static final int UNDO_LIMIT = 32;
	private final ScriptEnvironment env;	
	private ArrayDeque<WorldEdit> jobStack;
		
	public EditSessionController(ScriptEnvironment env) {
		this.env = env;		
		setLimit(UNDO_LIMIT);
		jobStack = new ArrayDeque<WorldEdit>();
	}
	

	public void checkIn(IEditSession session) {
		if (!session.isEmpty()) {
			jobStack.push(session.getEditData());
		}
	}
	
	public IEditSession checkOut(String editor, WorldWrapper world) {
		return new EditSession(editor, null, EditSession.NO_BLOCK_LIMIT, this, world);
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
			buffer.append(get(i).getEditor() + ": " + getDescriptionOf(i) + "\n");			
		}
		return buffer.toString();
	}
	
	public void dump() {
		env.chat.echo(getDump());
	}

	public void update() {
		if (jobStack.isEmpty()) return;
		
		env.getUser().worldObj.editingBlocks = true;
		try {
			while (jobStack.size() > 0) {			
				WorldEdit edit = jobStack.pop();
				edit.flush();
				push(edit);
			}
		} finally {
			env.getUser().worldObj.editingBlocks = false;
		}
	}
}
