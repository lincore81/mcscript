package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;


final class BlockEdit {
	public final IBlock oldBlock, newBlock;
	public final Voxel position;

	public BlockEdit(final IBlock oldBlock, final IBlock newBlock, final Voxel position) {
		if (newBlock == null) {
			throw new NullPointerException("new block to set must not be null!"); // possible script error
		}
		else if (position == null) {
			throw new NullPointerException("position to set the block at must not be null!"); // possible script error
		}
		// if oldBlock is null that's my fault, let it crash hard :~)
		
		this.oldBlock = oldBlock;
		this.newBlock = newBlock;
		this.position = position;
	}

	@Override
	public String toString() {
		return String.format("Position=%s, Block: %s -> %s", position, oldBlock, newBlock);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof BlockEdit) {
			return equalsPosition((BlockEdit)obj);
		} else {
			return false;
		}
	}

	public boolean equalsPosition(final BlockEdit other) {
		return (position.equalsVoxel(other.position));
	}
}
