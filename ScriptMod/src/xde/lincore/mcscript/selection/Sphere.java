package xde.lincore.mcscript.selection;

import java.util.ArrayList;

import xde.lincore.mcscript.Voxel;


public class Sphere extends SelectionBase {

	protected Voxel center;
	protected double radius;

	public Sphere(final Voxel center, final double radius) {
		this.center = center;
		this.radius = radius;


	}

	@Override
	public boolean contains(final Voxel v) {
		return center.distanceTo(v) <= radius;
	}

	@Override
	public ArrayList<Voxel> getVoxels() {
		final double r2 = radius * radius;
		return null;
	}

}
