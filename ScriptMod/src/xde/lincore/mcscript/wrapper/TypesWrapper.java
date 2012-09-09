package xde.lincore.mcscript.wrapper;

import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.mcscript.geom.Voxel;
import xde.lincore.mcscript.selection.Cuboid;
import xde.lincore.mcscript.selection.HollowCuboid;
import xde.lincore.mcscript.selection.ISelection;
import xde.lincore.mcscript.selection.LogicConnector;
import xde.lincore.mcscript.selection.Subtractor;

public class TypesWrapper extends WrapperBase {

	protected TypesWrapper(ScriptingEnvironment env) {
		super(env);
	}
	
	public Cuboid newCuboid(Voxel position, int width, int height, int depth) {
		return new Cuboid(position, width, height, depth);
	}
	
	public HollowCuboid newHollowCuboid(Voxel position, int width, int height, int depth) {
		return new HollowCuboid(position, width, height, depth);
	}
	
	public LogicConnector newLogicConnector(ISelection a, ISelection b, int operator) {
		return new LogicConnector(a, b, operator);
	}
	
	public Subtractor newSubtractor(ISelection minuend, ISelection subtrahend) {
		return new Subtractor(minuend, subtrahend);
	}

}