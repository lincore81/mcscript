package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;
import xde.lincore.util.undo.IUndoHistory;

public interface IEditSession {
	public void setBounds(BoundingBox bounds);

	public BoundingBox getBounds();

	public void setBlockLimit(int limit);

	public int getBlockLimit();

	public void setBlock(Voxel position, IBlock block);
	
	public void setBlocks(VoxelMap voxels);

	public void flush();

	public void flush(String description);

	public void clear();

	public boolean isEmpty();

	public String getDump();

	public String getEditor();

	public IUndoHistory getHistory();

	public void setDescription(String description);

	public String getDescription();

	WorldEdit getEditData();


	
}
