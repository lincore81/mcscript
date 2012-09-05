package xde.lincore.mcscript.edit;

import java.util.ArrayList;

import xde.lincore.mcscript.BindingsWorld;
import xde.lincore.util.undo.Undoable;


class WorldEdit implements Undoable {

	ArrayList<BlockEdit> blocks;
	String description;
	BindingsWorld world;
	
	public WorldEdit(String description, BindingsWorld world) {
		this.description = description;
		blocks = new ArrayList<BlockEdit>();
		this.world = world;
	}
	
	public void add(BlockEdit edit) {
		blocks.add(edit);
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
}
