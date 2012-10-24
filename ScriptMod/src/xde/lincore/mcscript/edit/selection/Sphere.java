package xde.lincore.mcscript.edit.selection;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.edit.VoxelMap;
import xde.lincore.mcscript.math.RoundingMethod;


public class Sphere extends SelectionBase {
	
	public Sphere(final Voxel center, final double radius) {
		super(center, radius);
	}

	@Override
	public boolean contains(final Voxel v) {
		return center.distanceTo(v) <= radius;
	}

	@Override
	public VoxelMap getVoxels() {
		VoxelMap result = new VoxelMap();
		for (Voxel v: bounds) {
			if (v.distanceTo(center) <= radius) {
				result.put(v);
			}
		}
		return result;
	}

	@Override
	public void setCenter(final Voxel center) {
		this.center = center;
		Vector3d pos1 = center.toVector3d().sub(radius, radius, radius);
		Vector3d pos2 = center.toVector3d().add(radius, radius, radius);
		bounds = new BoundingBox(
				new Voxel(pos1, RoundingMethod.Round),
				new Voxel(pos2, RoundingMethod.Round));
	}

}
