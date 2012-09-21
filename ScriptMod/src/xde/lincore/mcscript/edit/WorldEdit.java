package xde.lincore.mcscript.edit;

import java.util.ArrayList;
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
	private String editor;
	private WorldWrapper world;
	
	public WorldEdit(String editor, WorldWrapper world) {
		this.editor = editor;		
		blocks = new HashSet<BlockEdit>();
		this.world = world;
	}
	
	protected WorldEdit(WorldEdit other) {		
		this(other.editor, other.world);
	}
	
	public Set<BlockEdit> getBlocks() {
		return blocks;
	}
	
	public void add(BlockEdit edit) {
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
		for (BlockEdit edit: blocks) {
			world.setBlock(edit.position, edit.oldBlock);
		}
	}

	public void flush() {
		for (BlockEdit edit: blocks) {
			world.setBlock(edit.position, edit.newBlock);
		}
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
		StringBuffer buffer = new StringBuffer(blocks.size() * 50 + 30);
		buffer.append(toString() + "\n");
		for (BlockEdit edit: blocks) {
			buffer.append("  " + edit.toString() + "\n");
		}
		return buffer.toString();
	}
	
	@Override
	public String toString() {
		String description_ = (description != null)? description : super.toString();		
		return description_ + ", edited by " + editor + " (" + blocks.size() + " blocks)";
	}
	
	public WorldEdit mergeWith(WorldEdit other) {
		for (BlockEdit edit: other.blocks) {
			add(edit);
		}
		return this;
	}

	@Override
	public String getEditor() {
		return editor;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}


}
