package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.Voxel;

public interface ITurtle {

	public abstract ITurtle backward(int times);	

	public abstract ITurtle forward(int times);

	public abstract Voxel getBlockPosition();

	public abstract int getX();

	public abstract int getY();

	public abstract int getZ();

	public abstract boolean isPenDown();

	public abstract ITurtle left(int amount);

	public abstract ITurtle goDown(int blocks);

	public abstract void parse(String commands);

	public abstract ITurtle penDown();

	public abstract ITurtle penUp();

	public abstract ITurtle goUp(int height);

	public abstract void reset();

	public abstract ITurtle right(int amount);

	public abstract ITurtle setBlockPosition(Voxel position);

	public abstract ITurtle togglePen();

	public abstract ITurtle turnAround();	

}