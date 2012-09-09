package xde.lincore.mcscript.edit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import xde.lincore.mcscript.wrapper.WorldWrapper;
import xde.lincore.util.undo.Undoable;


class WorldEdit implements Undoable {

	Set<BlockEdit> blocks;
	String description;
	String editor;
	WorldWrapper world;
	
	public WorldEdit(String description, String editor, WorldWrapper world) {
		this.editor = editor;
		this.description = description;
		blocks = new HashSet<BlockEdit>();
		this.world = world;
	}
	
	protected WorldEdit(WorldEdit other) {		
		this.editor 		= other.editor;
		this.description 	= other.description;
		this.world 			= other.world;
		this.blocks 		= other.blocks;
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
		return description + " (" + blocks.size() + " blocks)";
	}
	
	public WorldEdit mergeWith(WorldEdit other) {
		for (BlockEdit edit: other.blocks) {
			add(edit);
		}
		return this;
	}


}
