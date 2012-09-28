package xde.lincore.mcscript;


public enum CardinalDirections {
	North(Voxel.NORTH),
	East(Voxel.EAST),
	South(Voxel.SOUTH),
	West(Voxel.WEST),
	Up(Voxel.UP),
	Down(Voxel.DOWN);

	private Voxel move;

	private CardinalDirections(final Voxel move) {
		this.move = move;
	}

	public static CardinalDirections get(final String name) {
		for (final CardinalDirections d: values()) {
			if (d.name().equalsIgnoreCase(name)) {
				return d;
			}
		}
		return null;
	}

	public Voxel getVoxel() {
		return move;
	}

	public Vector3d getVector() {
		return new Vector3d(move);
	}

	public CardinalDirections turnLeft() {
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

	public CardinalDirections turnRight() {
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

	public CardinalDirections turnAround() {
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
		return !(this == Up && this == Down);
	}
}
