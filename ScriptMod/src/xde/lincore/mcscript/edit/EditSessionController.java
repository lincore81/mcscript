package xde.lincore.mcscript.edit;

import java.util.ArrayDeque;

import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.minecraft.WorldFacade;
import xde.lincore.util.undo.UndoStack;

public class EditSessionController extends UndoStack<WorldEdit> {
	public static final int UNDO_LIMIT = 32;
	private final ScriptEnvironment env;
	private final ArrayDeque<WorldEdit> jobStack;

	public EditSessionController(final ScriptEnvironment env) {
		this.env = env;
		setLimit(UNDO_LIMIT);
		jobStack = new ArrayDeque<WorldEdit>();
	}


	public void checkIn(final IEditSession session) {
		if (!session.isEmpty()) {
			jobStack.push(session.getEditData());
		}
	}

	public IEditSession checkOut(final String editor, final WorldFacade world) {
		return new EditSession(editor, null, EditSession.NO_BLOCK_LIMIT, this, world);
	}

	public String getLastEditDump() {
		if (canUndo()) {
			return peekUndo().getDump();
		} else {
			return "No edits.";
		}
	}


	public String getEditDump(final int index) {
		assert index >= 0 && index <= top() : "index out of bounds.";
		return history.get(index).getDump();
	}

	public String getDump() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("EditSession (edits=%d):\n", getSize()));
		for (int i = 0; i <= top(); i++) {
			buffer.append((i == getStackPointer())? "§e" : "");
			buffer.append("#" + String.valueOf(i + 1) + ": " + get(i).getEditor() + "\n§r");
		}
		return buffer.toString();
	}

	public void dump() {
		env.chat.echo(getDump());
	}

	public void update() {
		if (jobStack.isEmpty()) {
			return;
		}
		
		while (jobStack.size() > 0) {
			if (!jobStack.peekFirst().getWorld().canEdit()) break;
			final WorldEdit edit = jobStack.pop();
			edit.flush();
			push(edit);
		}
	}
}
