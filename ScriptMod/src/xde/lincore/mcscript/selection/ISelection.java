package xde.lincore.mcscript.selection;

import java.util.ArrayList;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Voxel;

public interface ISelection {

	public int getWidth();
	public int getHeight();
	public int getDepth();

	public BoundingBox getBounds();

	public boolean contains(Voxel v);

	public int getVolume();
	public ArrayList<Voxel> getVoxels();
}
