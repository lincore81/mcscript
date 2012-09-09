package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.CardinalDirections;
import xde.lincore.mcscript.geom.Voxel;
import xde.lincore.mcscript.wrapper.MinecraftWrapper;

public class Turtle {
	private Blocks block;
	private Voxel initialPosition;	
	private Voxel position;
	private int line_stiple;
	private boolean penDown;
	private static MinecraftWrapper mc;
	private CardinalDirections direction;
	private CardinalDirections horizDirection;
	
	
	public Turtle(Voxel position) {
		this(position, Blocks.Stone, CardinalDirections.North);
	}
	
	public Turtle(Voxel position, Blocks block, CardinalDirections direction) {
		mc = MinecraftWrapper.getInstance();
		this.block = block;
		setPosition(position);
		this.direction = direction;
		horizDirection = (direction.isHorizontal())? direction : CardinalDirections.North;
		penDown();
	}
	
	public Turtle backward() {
		return backward(1);
	}
	
	public Turtle backward(int times) {
		draw(times, false);
		return this;
	}
	
	public Turtle bk() {
		return backward();
	}
	
	public Turtle bk(int times) {
		return backward(times);
	}
	
	public Turtle down() {
		look(CardinalDirections.Down);
		return this;
	}
	
	private void draw(int times, boolean forward) {
		if (times == 0) return;
		if (!mc.world.canEdit()) throw new IllegalStateException("Turtle: Can't draw now, the " +
				"world is not ready for me, yet.");
		mc.world.startEdit();
		for (int i = 0; i < times; i++) {
			if (!isValidPosition(position)) continue;
			
			if (penDown) { 
				mc.world.setBlock(position, block);
			}
			if (forward) {
				position = position.add(direction.getVoxel());
			}
			else {
				position = position.sub(direction.getVoxel());
			}
		}
		mc.world.endEdit();
	}
	
	public Turtle fd() {
		return forward();
	}
	
	
	public Turtle fd(int times) {
		return forward(times);
	}
	
	public Turtle forward() {
		return forward(1);
	}
	
	public Turtle forward(int times) {
		draw(times, true);
		return this;
	}
	
	public Voxel getPosition() {
		return position;
	}
	
	public CardinalDirections getDirection() {
		return direction;
	}
	
	public boolean isPenDown() {
		return penDown;
	}
	
	private boolean isValidPosition(Voxel position) {
		return !(position.x >= 30000000 || position.x <= -30000000 ||
				 position.z >= 30000000 || position.z <= -30000000 ||
				 position.y < 0 && position.y > mc.world.getMaxHeight());
	}
	
	public Turtle left() {
		look(direction.turnLeft());
		return this;
	}
	
	public Turtle lt() {
		return left();
	}
	
	public Turtle look(String direction) {
		CardinalDirections dir = CardinalDirections.get(direction);
		if (dir != null) {
			look(dir);
		}
		return this;
	}

	public Turtle look(CardinalDirections direction) {
		if (direction == null) {
			throw new RuntimeException("Turtle: I can't look this way.");
		}
		this.direction = direction;
		
//		if (direction.isHorizontal()) {
//			horizDirection = direction;
//		}
//		
//		if (this.direction.isHorizontal()) {
//			this.direction = direction;
//		}
		
		return this;
	}
	
	public Turtle lookForward() {
		this.direction = horizDirection;
		return this;
	}
	
	public Turtle penDown() {
		penDown = true;
		return this;
	}
	
	public Turtle pd() {
		return penDown();
	}

	public Turtle penUp() {
		penDown = false;
		return this;
	}	
	
	public Turtle pu() {
		return penUp();
	}
	
	public Turtle raise() {
		CardinalDirections tmp = direction;
		up().forward();
		look(tmp);
		return this;
	}
	
	public Turtle raise(int height) {
		CardinalDirections tmp = direction;
		up().forward(height);
		look(tmp);
		return this;
	}
	
	public Turtle lower() {
		CardinalDirections tmp = direction;
		down().forward();
		look(tmp);
		return this;
	}

	public Turtle right() {		
		look(direction.turnRight());
		return this;
	}
	
	public Turtle rt() {
		return right();
	}
	
	public Turtle setPosition(Voxel position) {
		this.position = position;		
		initialPosition = position;
		return this;
	}
	
	public Turtle togglePen() {
		penDown = !penDown;
		return this;
	}
	
	public Turtle translate(Voxel translation) {
		position.add(translation);
		return this;
	}
	
	public Turtle turnAround() {
		look(direction.turnAround());
		return this;
	}
	
	public Turtle up() {
		look(CardinalDirections.Up);
		return this;
	}
	
	public void setBlock(Blocks block) {
		this.block = block;
	}
	
	public Blocks getBlock() {
		return block;
	}
	
	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}
	
	public int getZ() {
		return position.z;
	}
	
	public void parse(String commands) {
		
	}
}

