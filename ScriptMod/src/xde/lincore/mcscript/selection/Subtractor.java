package xde.lincore.mcscript.selection;

import java.util.ArrayList;

import xde.lincore.mcscript.Voxel;


public class Subtractor extends SelectionBase {

	private final ISelection minuend, subtrahend;
	private ArrayList<Voxel> voxels;

	public Subtractor(final ISelection minuend, final ISelection subtrahend) {
		this.minuend = minuend;
		this.subtrahend = subtrahend;
		bounds = minuend.getBounds();
		setVoxels();
		volume = voxels.size();
	}

	private void setVoxels() {
		voxels = new ArrayList<Voxel>();
		for (final Voxel v: minuend.getVoxels()) {
			if (!subtrahend.contains(v)) {
				voxels.add(v);
			}
		}
	}

	@Override
	public boolean contains(final Voxel v) {
		return voxels.contains(v);
	}

	@Override
	public ArrayList<Voxel> getVoxels() {
		return voxels;
	}

}
