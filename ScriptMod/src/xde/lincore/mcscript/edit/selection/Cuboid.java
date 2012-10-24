package xde.lincore.mcscript.edit.selection;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.edit.VoxelMap;
import xde.lincore.mcscript.math.RoundingMethod;


public class Cuboid extends SelectionBase {

	public Cuboid(final Voxel center, final double radius) {
		super(center, radius);
	}
	
	public Cuboid(final Voxel pos1, final Voxel pos2) {
		this(pos1, pos2.sub(pos1).x + 1, pos2.sub(pos1).y + 1, pos2.sub(pos1).z + 1);
	}

	public Cuboid(final Voxel position, final int width, final int height, final int depth) {
		bounds = new BoundingBox(
				position.x, position.x + width  - 1,
				position.y, position.y + height - 1,
				position.z, position.z + depth  - 1);
		volume = Math.abs(width * height * depth);
	}

	@Override
	public int getWidth() {
		return bounds.getWidth();
	}

	@Override
	public int getHeight() {
		return bounds.getHeight();
	}

	@Override
	public int getDepth() {
		return bounds.getDepth();
	}

	@Override
	public BoundingBox getBounds() {
		return bounds;
	}

	@Override
	public boolean contains(final Voxel v) {
		return bounds.contains(v);
	}




	@Override
	public VoxelMap getVoxels() {
		VoxelMap result = new VoxelMap();
		//Voxel[] result = new Voxel[(int)volume];

		for (int x = bounds.getMinX(); x <= bounds.getMaxX(); x++) {
			for (int y = bounds.getMinY(); y <= bounds.getMaxY(); y++) {
				for (int z = bounds.getMinZ(); z <= bounds.getMaxZ(); z++) {
					result.put(new Voxel(x, y, z));
				}
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


