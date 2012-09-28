package xde.lincore.mcscript.edit.turtlespeak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xde.lincore.util.StringTools;

public class TsBlock implements Iterable<TsStatement> {
	private final List<TsStatement> content;
	private final TsStatement context;
	private final TsBlock parent;
	private int repeats;

	public TsBlock(final TsBlock parent, final TsStatement context) {
		content 	= new ArrayList<TsStatement>();
		this.parent 	= parent;
		this.context 	= context;
		repeats = 0;
	}

	public void add(final TsStatement statement) {
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

	public String dump(final int indent) {
		final StringBuilder result = new StringBuilder();
		final String tabs = StringTools.repeat("    ", indent);
		if (parent != null) {
			result.append("(\n");
		}
		for (final TsStatement s: content) {
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

	public void setRepeats(final int repeats) {
		this.repeats = repeats;
	}
}