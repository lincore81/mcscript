package xde.lincore.mcscript;

import java.util.Iterator;


public final class BoundingBox implements Iterable<Voxel> {
	private int
		minX, maxX,
		minY, maxY,
		minZ, maxZ;

	public BoundingBox(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
		minX = (x1 < x2)? x1 : x2;
		maxX = (x2 > x1)? x2 : x1;
		minY = (y1 < y2)? y1 : y2;
		maxY = (y2 > y1)? y2 : y1;
		minZ = (z1 < z2)? z1 : z2;
		maxZ = (z2 > z1)? z2 : z1;
	}

	public BoundingBox(final Voxel pos1, final Voxel pos2) {
		this(pos1.x, pos2.x, pos1.y, pos2.y, pos1.z, pos2.z);
	}
	
	public BoundingBox(final BoundingBox other) {
		this.minX = other.minX;
		this.minY = other.minY;
		this.minZ = other.minZ;
		this.maxX = other.maxX;
		this.maxY = other.maxY;
		this.maxZ = other.maxZ;
	}
	
	private BoundingBox() {}

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

	public boolean contains(final Voxel v) {
		return !(v == null ||
				v.x < minX || v.x > maxX ||
				v.y < minY || v.y > maxY ||
				v.z < minZ || v.z > maxZ);
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
	
	public BoundingBox contract(final Directions direction, final int amount) {
		Voxel contraction = direction.toVoxel().multiply(amount);
		return resize(contraction, false);
	}
	
	public BoundingBox expand(final Directions direction, final int amount) {
		Voxel expansion = direction.toVoxel().multiply(amount);
		return resize(expansion, true);
	}
	
	public int getDepth() {
		return maxZ - minZ + 1;
	}
	
	public int getHeight() {
		return maxY - minY + 1;
	}
	
	public Voxel getMax() {
		return new Voxel(maxX, maxY, maxZ);
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public int getMaxY() {
		return maxY;
	}
	
	public int getMaxZ() {
		return maxZ;
	}

	public Voxel getMin() {
		return new Voxel(minX, minY, minZ);
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public Voxel getPos1() {
		return new Voxel(minX, minY, minZ);
	}

	public Voxel getPos2() {
		return new Voxel(maxX, maxY, maxZ);
	}

	public int getWidth() {
		return maxX - minX + 1;
	}
	
	public int getVolume() {
		return getWidth() * getHeight() * getDepth();
	}

	public BoundingBox inset(final int amount) {
		return new BoundingBox(
				minX + amount, maxX - amount,
				minY + amount, maxY - amount,
				minZ + amount, maxZ - amount);
	}
	
	@Override
	public Iterator<Voxel> iterator() {
		return new BoxIterator(this);
	}

	public BoundingBox outset(final int amount) {
		return new BoundingBox(
				minX - amount, maxX + amount,
				minY - amount, maxY + amount,
				minZ - amount, maxZ + amount);
	}

	public BoundingBox resize(final Voxel vox, final boolean expand) {
		Voxel vox_ = expand ? vox : vox.invert();
		Voxel pos1 = getPos1();
		Voxel pos2 = getPos2();
		
		if (vox_.x > 0) {
			pos2 = pos2.add(vox_.x, 0, 0);
		} else {
			pos1 = pos1.add(vox_.x, 0, 0);
		}
		
		if (vox_.y > 0) {
			pos2 = pos2.add(0, vox_.y, 0);
		} else {
			pos1 = pos1.add(0, vox_.y, 0);
		}
		
		if (vox_.z > 0) {
			pos2 = pos2.add(0, 0, vox_.z);
		} else {
			pos1 = pos1.add(0, 0, vox_.z);
		}
		
		return new BoundingBox(pos1, pos2);
	}

	@Override
	public String toString() {
		return String.format("x=%d (%d..%d), y=%d (%d..%d), z=%d (%d..%d)",
				getWidth(), minX, maxX, getHeight(), minY, maxY, getDepth(), minZ, maxZ);
	}

	public boolean touches(final Voxel v) {
		return 	(((v.x == minX || v.x == maxX) && containsY(v.y) && containsZ(v.z)) ||
				 ((v.y == minY || v.y == maxY) && containsX(v.x) && containsZ(v.z)) ||
				 ((v.z == minZ || v.z == maxZ) && containsX(v.x) && containsY(v.y)));
	}

	public BoundingBox translate(final Voxel translation) {
		BoundingBox result = new BoundingBox(this);
		
		result.minX += translation.x;
		result.maxX += translation.x;
		
		result.minY += translation.y;
		result.maxY += translation.y;
		
		result.minZ += translation.z;
		result.maxZ += translation.z;
		
		return result;
	}
}

class BoxIterator implements Iterator<Voxel> {

	BoundingBox box;
	boolean hasNext;
	int x, y, z;
	
	public BoxIterator(final BoundingBox box) {
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