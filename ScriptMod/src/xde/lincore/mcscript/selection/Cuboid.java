package xde.lincore.mcscript.selection;

import java.util.ArrayList;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Voxel;


public class Cuboid extends SelectionBase {

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
	public ArrayList<Voxel> getVoxels() {
		final ArrayList<Voxel> result = new ArrayList<Voxel>(volume);
		//Voxel[] result = new Voxel[(int)volume];

		for (int x = bounds.getMinX(); x <= bounds.getMaxX(); x++) {
			for (int y = bounds.getMinY(); y <= bounds.getMaxY(); y++) {
				for (int z = bounds.getMinZ(); z <= bounds.getMaxZ(); z++) {
					result.add(new Voxel(x, y, z));
				}
			}
		}
		return result;
	}
}


