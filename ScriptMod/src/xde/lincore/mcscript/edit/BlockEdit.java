package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.BindingsWorld;
import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.Voxel;
import xde.lincore.util.undo.Undoable;


final class BlockEdit {
	
	public final BlockData oldBlock, newBlock;
	public final Voxel position;	
	
	public BlockEdit(BlockData oldBlock, BlockData newBlock, Voxel position) {
		this.oldBlock = oldBlock;
		this.newBlock = newBlock;
		this.position = position;		
	}
	
	@Override
	public String toString() {
		return String.format("Position=%s, Block: %s -> %s", position, oldBlock, newBlock);
	}
}
