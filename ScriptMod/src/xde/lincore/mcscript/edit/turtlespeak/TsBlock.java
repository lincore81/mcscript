package xde.lincore.mcscript.edit.turtlespeak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xde.lincore.util.StringTools;

import net.minecraft.src.StringUtils;

public class TsBlock implements Iterable<TsStatement> {	
	private final List<TsStatement> content;
	private final TsStatement context;
	private final TsBlock parent;
	private int repeats;

	public TsBlock(TsBlock parent, TsStatement context) {
		this.content 	= new ArrayList<TsStatement>();
		this.parent 	= parent;
		this.context 	= context;
		repeats = 0;
	}
	
	public void add(TsStatement statement) {
		content.add(statement);
	}
	
	public TsStatement getContext() {
		return context;
	}
	
	public TsBlock getParent() {
		return parent;
	}
	
//	public List<TsStatement> getContent() {
//		return content;
//	}
	
	@Override
	public String toString() {
		return String.format("context=%s, size=%d, parent=%s", context, content.size(), 
				(parent != null)? parent : "is root block");
	}
	
	public String dump(int indent) {
		StringBuilder result = new StringBuilder();
		String tabs = StringTools.repeat("    ", indent);
		if (parent != null) result.append("(\n");
		for (TsStatement s: content) {
			result.append(s.dump(indent));
		}		
		return result.toString();
	}

	@Override
	public Iterator<TsStatement> iterator() {
		return content.iterator();
	}

	public int getRepeats() {
		return repeats;
	}
	
	public void setRepeats(int repeats) {
		this.repeats = repeats;
	}
}