package xde.lincore.mcscript;

public enum Directions {
	North(Voxel.NORTH),
	East(Voxel.EAST),
	South(Voxel.SOUTH),
	West(Voxel.WEST),
	Up(Voxel.UP),
	Down(Voxel.DOWN);
	
	private Voxel move;
	
	private Directions(Voxel move) {
		this.move = move;
	}
	
	public static Directions get(String name) {
		for (Directions d: values()) {
			if (d.name().equalsIgnoreCase(name)) {
				return d;
			}
		}
		return null;
	}
	
	public Voxel getVoxel() {
		return move;
	}
	
	public Vector getVector() {
		return new Vector(move);
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
	
	public Directions turnAround() {
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
