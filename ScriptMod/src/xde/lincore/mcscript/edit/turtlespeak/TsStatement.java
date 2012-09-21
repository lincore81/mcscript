package xde.lincore.mcscript.edit.turtlespeak;

import java.util.ArrayList;
import java.util.List;

import xde.lincore.util.StringTools;

public class TsStatement {
	public final TsKeyword keyword;
	public final List<Float> arguments;
	private TsBlock block;

	public TsStatement(final TsKeyword keyword) {
		this.keyword = keyword;
		arguments = new ArrayList<Float>();
		block = null;
	}
	
	public boolean isComplete() {
		if (keyword == null) return false;
		return true;
	}

	public TsBlock getBlock() {
		return block;
	}

	public void setBlock(TsBlock block) {
		this.block = block;
	}
	
	public boolean hasBlock() {
		return block != null;
	}
	
	@Override
	public String toString() {
		return String.format("Keyword=%s; Arguments=\"%s\"; hasBlock? %b", keyword.name, 
				StringTools.join(arguments), hasBlock());
	}

	public String dump(int indent) {
		String tabs = StringTools.repeat("    ", indent);
		if (hasBlock())
			return String.format("%s%s %s %s%s\n", tabs, keyword.name, 
					StringTools.join(arguments), block.dump(++indent), tabs + ")");
		else {
			return tabs + keyword.name + " " + StringTools.join(arguments) + "\n";
		}
	}
}