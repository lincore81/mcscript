package xde.lincore.mcscript.selection;

import java.util.Iterator;

import xde.lincore.mcscript.geom.BoundingBox;
import xde.lincore.mcscript.geom.Voxel;


public abstract class SelectionBase implements ISelection{

	protected BoundingBox bounds;
	protected int volume;

	@Override
	public int getWidth() {
		return bounds.getWidth();
	}

	@Override
	public int getHeight() {
		return bounds.getHeight();
	}

	@Override
	public int getDepth() {
		return bounds.getDepth();
	}
	
	@Override
	public int getVolume() {
		return volume;
	}

	@Override
	public BoundingBox getBounds() {
		return bounds;
	}
	
	@Override
	public String toString() {
		return String.format("(%s) - %s\n" +
				"    volume=%d, width=%d, height=%d, depth=%d", this.getClass().getSimpleName(), 
				bounds, volume,	getWidth(), getHeight(), getDepth());
	}

}