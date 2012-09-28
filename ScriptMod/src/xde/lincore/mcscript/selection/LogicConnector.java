package xde.lincore.mcscript.selection;

import java.util.ArrayList;
import java.util.HashSet;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.Voxel;



public class LogicConnector extends SelectionBase {

	public static final int OP_OR 	= 0;
	public static final int OP_AND 	= 1;
	public static final int OP_XOR	= 2;
	public static final int OP_NAND	= 3;



	private final ISelection a, b;
	private int operator;
	private ArrayList<Voxel> voxels;

	public LogicConnector(final ISelection a, final ISelection b, final int operator) {
		this.a = a;
		this.b = b;
		if (operator >= OP_OR && operator <= OP_NAND) {
			this.operator = operator;
		}
		else {
			G.LOG.warning("LogicConnector: invalid operator, defaulting to OP_OR.");
			this.operator = OP_OR;
		}
		setVoxels();
		volume = voxels.size();
		bounds = a.getBounds().add(b.getBounds());
	}

	private void setVoxels() {
		voxels = new ArrayList<Voxel>();
		final ArrayList<Voxel> va = a.getVoxels();
		final ArrayList<Voxel> vb = b.getVoxels();
		final HashSet<Voxel> all = new HashSet<Voxel>(va.size() + vb.size(), 1f);
		all.addAll(va);
		all.addAll(vb);
		int i = 0;
		for (final Voxel v: all) {
			if (connect(va.contains(v), vb.contains(v))) {
				voxels.add(v);
				System.out.println(String.valueOf(i++) + ": " + v);
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

	private boolean connect(final boolean ca, final boolean cb) {
		switch (operator) {
			case OP_OR:
				return (ca || cb);
			case OP_AND:
				return (ca && cb);
			case OP_XOR:
				return (ca && !cb || !ca && cb);
			case OP_NAND:
				return !(ca && cb);
			default:
				throw new RuntimeException("Impossible state: operator is invalid!");
		}
	}

}
