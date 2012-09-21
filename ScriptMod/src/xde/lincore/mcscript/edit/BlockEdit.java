package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.minecraft.WorldWrapper;
import xde.lincore.util.undo.Undoable;


final class BlockEdit {
	
	public final IBlock oldBlock, newBlock;
	public final Voxel position;	
	
	public BlockEdit(IBlock oldBlock, IBlock newBlock, Voxel position) {
		this.oldBlock = oldBlock;
		this.newBlock = newBlock;
		this.position = position;		
	}
	
	@Override
	public String toString() {
		return String.format("Position=%s, Block: %s -> %s", position, oldBlock, newBlock);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof BlockEdit) {
			return equalsPosition((BlockEdit)obj);
		}
		else return false;
	}
	
	public boolean equalsPosition(BlockEdit other) {
		return (this.position.equalsVoxel(other.position));
	}
}
