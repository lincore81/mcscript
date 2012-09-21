package xde.lincore.mcscript;


public final class BoundingBox {
	private int minX, maxX, minY, maxY, minZ, maxZ;

	private BoundingBox() {}
	
	public BoundingBox(Voxel pos1, Voxel pos2) {
		this(pos1.x, pos2.x, pos1.y, pos2.y, pos1.z, pos2.z);
	}
	
	public BoundingBox(int x1, int x2, int y1, int y2, int z1, int z2) {
		this.minX = (x1 < x2)? x1 : x2;
		this.maxX = (x2 > x1)? x2 : x1;
		this.minY = (y1 < y2)? y1 : y2;
		this.maxY = (y2 > y1)? y2 : y1;
		this.minZ = (z1 < z2)? z1 : z2;
		this.maxZ = (z2 > z1)? z2 : z1;
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
	
	public boolean containsX(int x) {
		return minX <= x && x <= maxX;
	}
	
	public boolean containsY(int y) {
		return minY <= y && y <= maxY;
	}
	
	public boolean containsZ(int z) {
		return minZ <= z && z <= maxZ;
	}
	
	public boolean contains(Voxel v) {
		return !(v == null || 
				v.x < minX || v.x > maxX ||
				v.y < minY || v.y > maxY ||
				v.z < minZ || v.z > maxZ);
	}
	
	public boolean touches(Voxel v) {		
		return 	(((v.x == minX || v.x == maxX) && containsY(v.y) && containsZ(v.z)) ||
				 ((v.y == minY || v.y == maxY) && containsX(v.x) && containsZ(v.z)) ||
				 ((v.z == minZ || v.z == maxZ) && containsX(v.x) && containsY(v.y)));
	}
	
	@Override
	public String toString() {
		return String.format("x=%d..%d, y=%d..%d, z=%d..%d", minX, maxX, minY, maxY, minZ, maxZ); 
	}

	public BoundingBox add(BoundingBox other) {
		BoundingBox result = new BoundingBox();
		result.minX = Math.min(this.minX, other.minX);
		result.maxX = Math.max(this.maxX, other.maxX);
		result.minY = Math.min(this.minY, other.minY);
		result.maxY = Math.max(this.maxY, other.maxY);
		result.minZ = Math.min(this.minZ, other.minZ);
		result.maxZ = Math.max(this.maxZ, other.maxZ);
		return result;
	}
}
