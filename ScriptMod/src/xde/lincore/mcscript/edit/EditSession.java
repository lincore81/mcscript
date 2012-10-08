package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.minecraft.WorldFacade;
import xde.lincore.util.undo.IUndoHistory;

public class EditSession implements IEditSession {
	private final EditSessionController controller;
	private BoundingBox bounds;
	private WorldEdit worldEdit;
	private final WorldFacade world;
	private int blockLimit;
	public static final int NO_BLOCK_LIMIT = -1;

	protected EditSession(final String editor, final BoundingBox bounds,
			final int blockLimit, final EditSessionController controller, final WorldFacade world) {

		this.world = world;
		this.controller = controller;
		this.bounds = bounds;
		this.blockLimit = blockLimit;
		worldEdit = new WorldEdit(editor, world);
	}

	@Override
	public void setBounds(final BoundingBox bounds) {
		this.bounds = bounds;
	}

	@Override
	public void setBlock(final Voxel position, final IBlock block) {
		if (canSetBlock(position)) {
			final IBlock oldBlock = world.getBlock(position);
			worldEdit.add(new BlockEdit(oldBlock, block, position));
		}
	}

	private boolean canSetBlock(final Voxel position) {
		return (bounds == null || blockLimit == NO_BLOCK_LIMIT ||
				worldEdit.getBlocks().size() < blockLimit || bounds.contains(position));
	}

	@Override
	public void flush() {
		controller.checkIn(this);
		worldEdit = new WorldEdit(worldEdit.getEditor(), world);
	}

	@Override
	public void flush(final String description) {
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
	public void setBlockLimit(final int limit) {
		blockLimit = limit;
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
	public void setDescription(final String description) {
		worldEdit.setDescription(description);

	}

	@Override
	public String getDescription() {
		return worldEdit.getDescription();
	}
}
