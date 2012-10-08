package xde.lincore.mcscript;


public enum Directions {
	North	(Voxel.NORTH, 	2),
	East	(Voxel.EAST, 	3),
	South	(Voxel.SOUTH, 	0),
	West	(Voxel.WEST, 	1),
	Up		(Voxel.UP, 	 -255),
	Down	(Voxel.DOWN, -256);

	private Voxel voxel;
	private int mcValue;
	
	private Directions(final Voxel move) {
		this(move, -1);
	}

	private Directions(final Voxel move, final int mcValue) {
		this.voxel = move;
		this.mcValue = mcValue;
	}

	public static Directions get(final int value) {
		for (final Directions d: values()) {
			if (d.mcValue == value) {
				return d;
			}
		}
		return null;
	}
	
	public static Directions get(final String name) {
		for (final Directions d: values()) {
			if (d.name().equalsIgnoreCase(name)) {
				return d;
			}
		}
		return null;
	}
	
	public static Directions getClosest(final Vector3d from, final Vector3d to, final boolean horizontalOnly) {
		return getClosest(to.sub(from), horizontalOnly);
	}
	
	public static Directions getClosest(final Vector3d vector, final boolean horizontalOnly) {
		assert vector != null : "vector argument is null.";
		if (vector == Vector3d.ZERO) return North;
		Double shortestDistance 	= null;
		Directions closestDirection = null;
		
		for (Directions direction: values()) {
			if (horizontalOnly && !direction.isHorizontal()) continue;
			
			Vector3d directionVector = new Vector3d(direction.voxel);
			double distance = directionVector.sub(vector.normalise()).length();
			
			if (shortestDistance == null || distance < shortestDistance) {
				closestDirection = direction;
				shortestDistance = distance;
			}
		}
		return closestDirection;
	}

	public Vector3d toVector() {
		return new Vector3d(voxel);
	}
	
	public Voxel toVoxel() {
		return voxel;
	}

	public Directions invert() {
		switch (this) {
			case North:
				return South;
			case East:
				return West;
			case South:
				return North;
			case West:
				return East;
			case Up:
				return Down;
			case Down:
				return Up;
			default:
				return null;
		}
	}

	public boolean isHorizontal() {
		return mcValue >= 0;
	}

	public Directions turnLeft() {
		switch (this) {
			case North:
				return West;
			case East:
				return North;
			case South:
				return East;
			case West:
				return South;
			default:
				return null;
		}
	}

	public Directions turnRight() {
		switch (this) {
			case North:
				return East;
			case East:
				return South;
			case South:
				return West;
			case West:
				return North;
			default:
				return null;
		}
	}
}
