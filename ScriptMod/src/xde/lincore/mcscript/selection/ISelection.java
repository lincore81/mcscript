package xde.lincore.mcscript.selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Voxel;

import net.minecraft.src.AxisAlignedBB;

public interface ISelection {
	
	public int getWidth();
	public int getHeight();
	public int getDepth();
	
	public BoundingBox getBounds();
	
	public boolean contains(Voxel v);
	
	public int getVolume();
	public ArrayList<Voxel> getVoxels();
}
