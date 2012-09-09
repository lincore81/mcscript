package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.geom.BoundingBox;
import xde.lincore.mcscript.geom.Voxel;
import xde.lincore.util.undo.IUndoHistory;
import xde.lincore.util.undo.Undoable;

public interface IEditSession {
	public void setBounds(BoundingBox bounds);

	public BoundingBox getBounds();

	public void setBlockLimit(int limit);

	public int getBlockLimit();

	public void setBlock(Voxel position, IBlock block);

	public void flush();

	public void clear();

	public boolean isEmpty();

	public String getDump();

	public String getEditor();
	
	public IUndoHistory getHistory();

	WorldEdit getEditData();
}
