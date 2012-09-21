package xde.lincore.mcscript.selection;

import java.util.ArrayList;
import java.util.List;

import xde.lincore.mcscript.Voxel;


public class Sphere extends SelectionBase {

	protected Voxel center;
	protected double radius;
	
	public Sphere(Voxel center, double radius) {
		this.center = center;
		this.radius = radius;
		
		
	}
	
	@Override
	public boolean contains(Voxel v) {
		return center.distanceTo(v) <= radius;
	}

	@Override
	public ArrayList<Voxel> getVoxels() {
		double r2 = radius * radius;
		return null;
	}

}
