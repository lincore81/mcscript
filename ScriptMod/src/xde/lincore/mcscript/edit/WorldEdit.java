package xde.lincore.mcscript.edit;

import java.util.HashSet;
import java.util.Set;

import xde.lincore.mcscript.minecraft.WorldWrapper;
import xde.lincore.util.undo.Undoable;

/**
 *
 *
 *
 */
class WorldEdit implements Undoable {

	private Set<BlockEdit> blocks;
	private String description;
	private final String editor;
	private final WorldWrapper world;

	public WorldEdit(final String editor, final WorldWrapper world) {
		this.editor = editor;
		blocks = new HashSet<BlockEdit>();
		this.world = world;
	}

	protected WorldEdit(final WorldEdit other) {
		this(other.editor, other.world);
	}

	public Set<BlockEdit> getBlocks() {
		return blocks;
	}

	public void add(final BlockEdit edit) {
		if (!blocks.add(edit)) {
			blocks.remove(edit);
			blocks.add(edit);
		}
	}

	@Override
	public String getDescription() {
		return toString();
	}

	@Override
	public void undo() {
		for (final BlockEdit edit: blocks) {
			world.setBlock(edit.position, edit.oldBlock);
		}
	}

	public void flush() {
		world.startEdit();
		for (final BlockEdit edit: blocks) {
			world.setBlock(edit.position, edit.newBlock);
		}
		world.endEdit();
	}

	@Override
	public void redo() {
		flush();
	}

	public void clear() {
		blocks.clear();
	}

	public void reset() {
		blocks = new HashSet<BlockEdit>();
	}

	public String getDump() {
		final StringBuffer buffer = new StringBuffer(blocks.size() * 50 + 30);
		buffer.append(toString() + "\n");
		for (final BlockEdit edit: blocks) {
			buffer.append("  " + edit.toString() + "\n");
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		//String description_ = (description != null)? description : super.toString();
		return "edited by " + editor + " (" + blocks.size() + " blocks)";
	}

	public WorldEdit mergeWith(final WorldEdit other) {
		for (final BlockEdit edit: other.blocks) {
			add(edit);
		}
		return this;
	}

	@Override
	public String getEditor() {
		return editor;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public WorldWrapper getWorld() {
		return world;
	}


}
