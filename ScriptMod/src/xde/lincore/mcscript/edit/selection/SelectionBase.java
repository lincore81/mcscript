package xde.lincore.mcscript.edit.selection;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.math.RoundingMethod;


public abstract class SelectionBase implements ISelection{

	protected BoundingBox bounds;
	protected int volume;
	protected Voxel center;
	protected double radius;

	
	public SelectionBase(final Voxel center, final double radius) {
		this.center = center;
		this.radius = radius;
		Vector3d pos1 = center.toVector3d().sub(radius, radius, radius);
		Vector3d pos2 = center.toVector3d().add(radius, radius, radius);
		bounds = new BoundingBox(
				new Voxel(pos1, RoundingMethod.Round),
				new Voxel(pos2, RoundingMethod.Round));
	}
	
	protected SelectionBase() {}
	
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