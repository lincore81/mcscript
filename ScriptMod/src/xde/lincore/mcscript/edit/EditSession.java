package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.env.ScriptRunner;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;
import xde.lincore.mcscript.minecraft.WorldWrapper;
import xde.lincore.util.undo.IUndoHistory;
import xde.lincore.util.undo.Undoable;

public class EditSession implements IEditSession {
	private final EditSessionController controller;	
	private BoundingBox bounds;
	private WorldEdit worldEdit;
	private WorldWrapper world;
	private int blockLimit;	
	public static final int NO_BLOCK_LIMIT = -1;
	
	protected EditSession(String editor, BoundingBox bounds,
			int blockLimit, EditSessionController controller, WorldWrapper world) {
		
		this.world = world;
		this.controller = controller;
		this.bounds = bounds;
		this.blockLimit = blockLimit;
		this.worldEdit = new WorldEdit(editor, world);
	}
	
	@Override
	public void setBounds(BoundingBox bounds) {
		this.bounds = bounds;
	}

	@Override
	public void setBlock(Voxel position, IBlock block) {
		if (canSetBlock(position)) {		
			IBlock oldBlock = world.getBlockData(position);
			worldEdit.add(new BlockEdit(oldBlock, block, position));
		}
	}

	private boolean canSetBlock(Voxel position) {
		return (bounds == null || blockLimit == NO_BLOCK_LIMIT || 
				worldEdit.getBlocks().size() < blockLimit || bounds.contains(position)); 
	}

	@Override
	public void flush() {
		controller.checkIn(this);
		worldEdit = new WorldEdit(worldEdit.getEditor(), world);
	}
	
	@Override
	public void flush(String description) {
		worldEdit.setDescription(description);
		flush();
	}

	@Override
	public void clear() {
		worldEdit.clear();
	}

	@Override
	public boolean isEmpty() {
		return worldEdit.getBlocks().isEmpty();
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
		return worldEdit.getEditor();
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

	@Override
	public void setDescription(String description) {
		worldEdit.setDescription(description);
		
	}

	@Override
	public String getDescription() {
		return worldEdit.getDescription();
	}
}
