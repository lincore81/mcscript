package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.geom.BoundingBox;
import xde.lincore.mcscript.geom.Voxel;
import xde.lincore.mcscript.wrapper.MinecraftWrapper;
import xde.lincore.util.undo.IUndoHistory;
import xde.lincore.util.undo.Undoable;

public class EditSession implements IEditSession {
	private final EditSessionController controller;
	private MinecraftWrapper mc;
	private BoundingBox bounds;
	private WorldEdit worldEdit;
	private int blockLimit;
	public static final int NO_BLOCK_LIMIT = -1;
	
	protected EditSession(EditSessionController controller, MinecraftWrapper mc, BoundingBox bounds,
			int blockLimit) {
		this.controller = controller;
		this.mc = mc;
		this.bounds = bounds;
		this.blockLimit = blockLimit;
		this.worldEdit = new WorldEdit("", "EditSession", mc.world);
	}
	
	@Override
	public void setBounds(BoundingBox bounds) {
		this.bounds = bounds;

	}

	@Override
	public void setBlock(Voxel position, IBlock block) {
		if (canSetBlock(position)) {		
			IBlock oldBlock = mc.world.getBlockData(position);
			worldEdit.add(new BlockEdit(oldBlock, block, position));
		}
	}

	private boolean canSetBlock(Voxel position) {
		return (bounds == null || blockLimit == NO_BLOCK_LIMIT || 
				worldEdit.blocks.size() < blockLimit || bounds.contains(position)); 
	}

	@Override
	public void flush() {
		controller.checkIn(this);
		worldEdit.reset();
	}

	@Override
	public void clear() {
		worldEdit.clear();
	}

	@Override
	public boolean isEmpty() {
		return worldEdit.blocks.isEmpty();
	}

	@Override
	public WorldEdit getEditData() {
		return worldEdit;
	}

	@Override
	public String getDump() {
		return worldEdit.getDump();
	}

	@Override
	public String getEditor() {
		return worldEdit.editor;
	}

	@Override
	public BoundingBox getBounds() {
		return bounds;
	}

	@Override
	public void setBlockLimit(int limit) {
		this.blockLimit = limit;
	}

	@Override
	public int getBlockLimit() {
		return blockLimit;
	}

	@Override
	public IUndoHistory getHistory() {
		return controller;
	}
}
