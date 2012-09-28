package xde.lincore.mcscript.selection;

import java.util.ArrayList;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Voxel;


public class HollowCuboid extends SelectionBase {

	protected int border = 1;
	protected int counter;


	public HollowCuboid(final Voxel position, final int width, final int height, final int depth) {
		bounds = new BoundingBox(
				position.x, position.x + width  - 1,
				position.y, position.y + height - 1,
				position.z, position.z + depth  - 1);

		// volume == surface area = 2(ab + ac + bc):
		final int ab = width * depth;
		final int ac = width * (height - 2);
		final int bc = (height - 2) * (depth - 2);
		System.out.format("ab=%d, ac=%d, bc=%d%n", ab, ac, bc);
		volume = 2 * (ab + ac + bc);
	}


	@Override
	public boolean contains(final Voxel v) {
		return bounds.touches(v);
	}


	@Override
	public ArrayList<Voxel> getVoxels() {
		final ArrayList<Voxel> result = new ArrayList<Voxel>(volume);

		final int i = 0;
		int x, y, z;

		// bottom and top
		y = 0;
		do {
			for (x = 0; x < getWidth(); x++) {
				for (z = 0; z < getDepth(); z++) {
//					System.out.format("x:%d, y:%d, z:%d, i:%d\n", x, y, z, i);
					result.add(new Voxel(
							x + bounds.getMinX(),
							y + bounds.getMinY(),
							z + bounds.getMinZ()));
				}
			}
			y += getHeight() - 1;
		} while (y < getHeight());

		System.out.println("bottom and top done: " + String.valueOf(i));

		// north and south
		z = 0;
		do {
				for (x = 0; x < getWidth(); x++) {
					for (y = 1; y < getHeight() - 1; y++) {
						result.add(new Voxel(
								x + bounds.getMinX(),
								y + bounds.getMinY(),
								z + bounds.getMinZ()));
					}
				}
				z += getDepth() - 1;
		} while (z < getDepth());

		System.out.println("north and south done: " + String.valueOf(i));

		// east and west
		x = 0;
		do {
			for (z = 1; z < getDepth() - 1; z++) {
				for (y = 1; y < getHeight() - 1; y++) {
					result.add(new Voxel(
							x + bounds.getMinX(),
							y + bounds.getMinY(),
							z + bounds.getMinZ()));
				}
			}
			x += getWidth() - 1;
		} while (x < getWidth());

		System.out.println("east and west done: " + String.valueOf(i));

		return result;
	}
}
