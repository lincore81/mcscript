package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;


public final class BlockEdit {
	private IBlock oldBlock, newBlock;
	private Voxel position;

	public BlockEdit(final IBlock oldBlock, final IBlock newBlock, final Voxel position) {
		this.oldBlock = oldBlock;
		this.newBlock = newBlock;
		this.position = position;
	}
	
	public BlockEdit(final Voxel position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return String.format("Position=%s, Block: %s -> %s",
				(position != null) ? position : "null",
				(oldBlock != null) ? oldBlock : "null",
				(newBlock != null) ? newBlock : "null");
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

	public IBlock getOldBlock() {
		return oldBlock;
	}

	public IBlock getNewBlock() {
		return newBlock;
	}

	public Voxel getPosition() {
		return position;
	}

	public void setOldBlock(final IBlock oldBlock) {
		this.oldBlock = oldBlock;
	}

	public void setNewBlock(final IBlock newBlock) {
		this.newBlock = newBlock;
	}

	public void setPosition(final Voxel position) {
		this.position = position;
	}

	public boolean validate() {
		return !(position == null || oldBlock == null || newBlock == null);
	}
}
