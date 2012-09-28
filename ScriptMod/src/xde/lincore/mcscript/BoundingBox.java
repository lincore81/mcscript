package xde.lincore.mcscript;

import java.util.Iterator;


public final class BoundingBox implements Iterable<Voxel> {
	private int minX, maxX, minY, maxY, minZ, maxZ;

	private BoundingBox() {}

	public BoundingBox(final Voxel pos1, final Voxel pos2) {
		this(pos1.x, pos2.x, pos1.y, pos2.y, pos1.z, pos2.z);
	}

	public BoundingBox(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
		minX = (x1 < x2)? x1 : x2;
		maxX = (x2 > x1)? x2 : x1;
		minY = (y1 < y2)? y1 : y2;
		maxY = (y2 > y1)? y2 : y1;
		minZ = (z1 < z2)? z1 : z2;
		maxZ = (z2 > z1)? z2 : z1;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public Voxel getMin() {
		return new Voxel(minX, minY, minZ);
	}

	public Voxel getMax() {
		return new Voxel(maxX, maxY, maxZ);
	}

	public int getWidth() {
		return maxX - minX + 1;
	}

	public int getHeight() {
		return maxY - minY + 1;
	}

	public int getDepth() {
		return maxZ - minZ + 1;
	}

	public boolean containsX(final int x) {
		return minX <= x && x <= maxX;
	}

	public boolean containsY(final int y) {
		return minY <= y && y <= maxY;
	}

	public boolean containsZ(final int z) {
		return minZ <= z && z <= maxZ;
	}

	public boolean contains(final Voxel v) {
		return !(v == null ||
				v.x < minX || v.x > maxX ||
				v.y < minY || v.y > maxY ||
				v.z < minZ || v.z > maxZ);
	}

	public boolean touches(final Voxel v) {
		return 	(((v.x == minX || v.x == maxX) && containsY(v.y) && containsZ(v.z)) ||
				 ((v.y == minY || v.y == maxY) && containsX(v.x) && containsZ(v.z)) ||
				 ((v.z == minZ || v.z == maxZ) && containsX(v.x) && containsY(v.y)));
	}

	@Override
	public String toString() {
		return String.format("X: (%d..%d), Y: (%d..%d), Z: (%d..%d)", minX, maxX, minY, maxY, minZ, maxZ);
	}

	public BoundingBox add(final BoundingBox other) {
		final BoundingBox result = new BoundingBox();
		result.minX = Math.min(minX, other.minX);
		result.maxX = Math.max(maxX, other.maxX);
		result.minY = Math.min(minY, other.minY);
		result.maxY = Math.max(maxY, other.maxY);
		result.minZ = Math.min(minZ, other.minZ);
		result.maxZ = Math.max(maxZ, other.maxZ);
		return result;
	}

	@Override
	public Iterator<Voxel> iterator() {
		return new BoxIterator(this);
	}
}

class BoxIterator implements Iterator<Voxel> {

	BoundingBox box;	
	boolean hasNext;
	int x, y, z;
	
	public BoxIterator(BoundingBox box) {
		this.box = box;
		x = box.getMinX();
		y = box.getMinY();
		z = box.getMinZ();
		hasNext = true;
	}
	
	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public Voxel next() {
		if (!hasNext) throw new IllegalStateException("There is no next value.");
		Voxel result = new Voxel(x, y, z);
		y++;
		if (y > box.getMaxY()) {
			y = box.getMinY();
			x++;
			if (x > box.getMaxX()) {
				x = box.getMinX();
				z++;
				if (z > box.getMaxZ()) {
					hasNext = false;
				}
			}
		}
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();		
	}
	
}