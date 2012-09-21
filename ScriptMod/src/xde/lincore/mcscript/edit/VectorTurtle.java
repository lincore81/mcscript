package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;

public class VectorTurtle {
	
	private Blocks block;
	private Vector3d initialPosition;
	private Vector3d position;
	private Voxel blockPosition;
	private int line_stiple;
	private boolean penDown;	
	private Vector3d heading;
	private IEditSession edit;
	
	
	public VectorTurtle(Vector3d position, IEditSession edit) {
		this(position, Blocks.EmeraldBlock, 0, edit);
	}
	
	public VectorTurtle(Vector3d position, Blocks block, int angle, IEditSession edit) {		
		this.block = block;
		this.edit = edit;
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
		 
		double moved = 0d;
		double stepLength = 1d;
			while (moved <= distance) {
				if (forward) {
					position = position.add(heading.multiply(stepLength));
				}
				else {
					position = position.sub(heading.multiply(stepLength));
				}
				Voxel newBlock = position.toVoxel();
				if (penDown && !blockPosition.equals(newBlock)) {
					edit.setBlock(blockPosition, block);
					blockPosition = newBlock;
				}
				if (moved < distance && moved + stepLength > distance) {
					moved = distance;
				}
				else {
					moved += stepLength;
				}
			}
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
	
	public Vector3d getPosition() {
		return position;
	}
	
	public Vector3d getHeading() {
		return heading;
	}
	
	public void snap() {
		this.position = blockPosition.toVector().add(0.5d, 0.5d, 0.5d);
	}
	
	public void setHeading(Vector3d heading) {
		this.heading = heading;
	}
	
	public boolean isPenDown() {
		return penDown;
	}
	
	public VectorTurtle left(int degrees) {
		heading = heading.rotateXZ(degrees);
		return this;
	}
	
	public VectorTurtle lt(int degrees) {
		return left(degrees);
	}
	
	public VectorTurtle look(int degrees) {		
		heading = Vector3d.fromAngleXZ(degrees);
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
		Vector3d temp = heading;
		heading = Vector3d.UP;
		draw(amount, true);
		heading = temp;
		return this;
	}
	
	public VectorTurtle lower(double amount) {
		Vector3d temp = heading;
		heading = Vector3d.DOWN;
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
	
	public VectorTurtle setPosition(Vector3d position) {
		this.position 	= position;
		initialPosition = position;
		blockPosition 	= position.toVoxel();
		return this;
	}
	
	public VectorTurtle togglePen() {
		penDown = !penDown;
		return this;
	}
	
	public VectorTurtle translate(Vector3d translation) {
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
