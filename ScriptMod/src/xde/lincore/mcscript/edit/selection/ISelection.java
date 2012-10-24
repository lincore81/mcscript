package xde.lincore.mcscript.edit.selection;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.edit.VoxelMap;

public interface ISelection {

	public int getWidth();
	public int getHeight();
	public int getDepth();

	public BoundingBox getBounds();

	public boolean contains(Voxel v);
	
	public void setCenter(Voxel center);

	public int getVolume();
	public VoxelMap getVoxels();
}
