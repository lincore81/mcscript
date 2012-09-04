package xde.lincore.mcscript.selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import xde.lincore.mcscript.Voxel;

import net.minecraft.src.mod_Script;


public class LogicConnector extends SelectionBase {

	public static final int OP_OR 	= 0;
	public static final int OP_AND 	= 1;
	public static final int OP_XOR	= 2;
	public static final int OP_NAND	= 3;
	
	
	
	private ISelection a, b;
	private int operator;
	private ArrayList<Voxel> voxels;
	
	public LogicConnector(ISelection a, ISelection b, int operator) {
		this.a = a;
		this.b = b;
		if (operator >= OP_OR && operator <= OP_NAND) {
			this.operator = operator;
		}
		else {
			mod_Script.LOG.warning("LogicConnector: invalid operator, defaulting to OP_OR.");
			this.operator = OP_OR;
		}
		setVoxels();
		volume = voxels.size();
		bounds = a.getBounds().add(b.getBounds());
	}
	
	private void setVoxels() {
		voxels = new ArrayList<Voxel>();
		ArrayList<Voxel> va = a.getVoxels();
		ArrayList<Voxel> vb = b.getVoxels();
		HashSet<Voxel> all = new HashSet<Voxel>(va.size() + vb.size(), 1f);
		all.addAll(va);
		all.addAll(vb);
		int i = 0;
		for (Voxel v: all) {
			if (connect(va.contains(v), vb.contains(v))) {
				voxels.add(v);
				System.out.println(String.valueOf(i++) + ": " + v);
			}
		}
	}

	@Override
	public boolean contains(Voxel v) {
		return voxels.contains(v);
	}

	@Override
	public ArrayList<Voxel> getVoxels() {
		return voxels;
	}
	
	private boolean connect(boolean ca, boolean cb) {
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
