package xde.lincore.mcscript.edit;

import java.util.Map;

import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;

public class FloodFillPattern implements IPattern {

	private final IBlock fillBlock;
	
	public FloodFillPattern(final IBlock fillBlock){
		this.fillBlock = fillBlock;
	}
	
	@Override
	public VoxelMap apply(final VoxelMap voxels) {
		for (Map.Entry<Voxel, IBlock> e: voxels) {
			e.setValue(fillBlock);
		}
		return voxels;
	}

}
