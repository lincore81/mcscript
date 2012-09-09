package xde.lincore.mcscript;

import xde.lincore.mcscript.geom.Vector3d;
import xde.lincore.mcscript.geom.Voxel;

public enum CardinalDirections {
	North(Voxel.NORTH),
	East(Voxel.EAST),
	South(Voxel.SOUTH),
	West(Voxel.WEST),
	Up(Voxel.UP),
	Down(Voxel.DOWN);
	
	private Voxel move;
	
	private CardinalDirections(Voxel move) {
		this.move = move;
	}
	
	public static CardinalDirections get(String name) {
		for (CardinalDirections d: values()) {
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
