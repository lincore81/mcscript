package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.edit.selection.ISelection;
import xde.lincore.mcscript.env.ScriptEnvironment;

public class Brush {
	private ISelection selection;
	private IPattern pattern;
	
	public Brush() {}
	
	public Brush(final ISelection selection, final IPattern pattern) {
		this.selection = selection;
		this.pattern = pattern;
	}

	public ISelection getSelection() {
		return selection;
	}

	public IPattern getPattern() {
		return pattern;
	}

	public void setSelection(final ISelection selection) {
		this.selection = selection;
	}

	public void setPattern(final IPattern pattern) {
		this.pattern = pattern;
	};
	
	public void paint(final Voxel center) {
		if (selection == null) throw new IllegalStateException("Brush has no selection.");
		if (pattern == null) throw new IllegalStateException("Brush has no pattern.");
		selection.setCenter(center);
		VoxelMap voxels = pattern.apply(selection.getVoxels());
		
		final IEditSession editSession = ScriptEnvironment.getInstance().scripts.getScript().getEditSession();
		editSession.setBlocks(voxels);
	}
}
