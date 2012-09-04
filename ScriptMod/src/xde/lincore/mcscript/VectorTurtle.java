package xde.lincore.mcscript;

public class VectorTurtle {
	static void setMc(BindingsMinecraft mc) {
		VectorTurtle.mc = mc;
	}
	
	private Blocks block;
	private Vector initialPosition;
	private Vector position;
	private Voxel blockPosition;
	private int line_stiple;
	private boolean penDown;
	private static BindingsMinecraft mc;
	private Vector heading;
	
	
	public VectorTurtle(Vector position) {
		this(position, Blocks.LightGreenWool, 0);
	}
	
	public VectorTurtle(Vector position, Blocks block, int angle) {
		this.block = block;
		setPosition(position);
		penDown();		
		look(angle);
	}
	
	public VectorTurtle backward() {
		return backward(1d);
	}
	
	public VectorTurtle backward(double distance) {
		draw(distance, false);
		return this;
	}
	
	public VectorTurtle bk() {
		return backward();
	}
	
	public VectorTurtle bk(double distance) {
		return backward(distance);
	}
	
	private void draw(double distance, boolean forward) {
		if (!mc.world.canEdit()) throw new IllegalStateException("Turtle: Can't draw now, the " +
				"world is not ready for that.");
		double moved = 0d;
		double stepLength = 1d;
		mc.world.startEdit();		
			while (moved <= distance) {
				if (forward) {
					position = position.add(heading.multiply(stepLength));
				}
				else {
					position = position.sub(heading.multiply(stepLength));
				}
				Voxel newBlock = position.toVoxel();
				if (penDown && !blockPosition.equals(newBlock)) {
					mc.world.setBlock(blockPosition, block);
					blockPosition = newBlock;
				}
				if (moved < distance && moved + stepLength > distance) {
					moved = distance;
				}
				else {
					moved += stepLength;
				}
			}			
		mc.world.endEdit();
		if (!penDown) blockPosition = position.toVoxel();
	}
	
	public VectorTurtle fd() {
		return forward();
	}
	
	
	public VectorTurtle fd(double distance) {
		return forward(distance);
	}
	
	public VectorTurtle forward() {
		return forward(1d);
	}
	
	public VectorTurtle forward(double distance) {
		draw(distance, true);
		return this;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public Vector getHeading() {
		return heading;
	}
	
	public void setHeading(Vector heading) {
		this.heading = heading;
	}
	
	public boolean isPenDown() {
		return penDown;
	}
	
	private boolean isValidPosition(Vector position) {
		return !(position.x >= 30000000d || position.x <= -30000000d ||
				 position.z >= 30000000d || position.z <= -30000000d ||
				 position.y < 0d && position.y > mc.world.getMaxHeight());
	}
	
	public VectorTurtle left(int degrees) {
		heading = heading.rotateXZ(degrees);
		return this;
	}
	
	public VectorTurtle lt(int degrees) {
		return left(degrees);
	}
	
	public VectorTurtle look(int degrees) {		
		heading = Vector.fromAngleXZ(degrees);
		return this;
	}
	
	public VectorTurtle penDown() {
		penDown = true;
		return this;
	}
	
	public VectorTurtle pd() {
		return penDown();
	}

	public VectorTurtle penUp() {
		penDown = false;
		return this;
	}	
	
	public VectorTurtle pu() {
		return penUp();
	}
	
	public VectorTurtle raise(double amount) {
		Vector temp = heading;
		heading = Vector.UP;
		draw(amount, true);
		heading = temp;
		return this;
	}
	
	public VectorTurtle lower(double amount) {
		Vector temp = heading;
		heading = Vector.DOWN;
		draw(amount, true);
		heading = temp;
		return this;
	}

	public VectorTurtle right(int degrees) {
		heading = heading.rotateXZ(-degrees);
		return this;
	}
	
	public VectorTurtle rt(int degrees) {
		return right(degrees);
	}
	
	public VectorTurtle setPosition(Vector position) {
		this.position 	= position;
		initialPosition = position;
		blockPosition 	= position.toVoxel();
		return this;
	}
	
	public VectorTurtle togglePen() {
		penDown = !penDown;
		return this;
	}
	
	public VectorTurtle translate(Vector translation) {
		position.add(translation);
		return this;
	}
	
	public VectorTurtle turnAround() {
		heading = heading.invert();
		return this;
	}
	
	public void setBlock(Blocks block) {
		this.block = block;
	}
	
	public Blocks getBlock() {
		return block;
	}
	
	public double getX() {
		return position.x;
	}
	
	public double getY() {
		return position.y;
	}
	
	public double getZ() {
		return position.z;
	}
}
