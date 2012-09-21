package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Iterator;

public enum TsArgumentTypes {
	Float("<real number>"),
	Integer("<integer>");	

	public final String name;

	private TsArgumentTypes(final String name) {
		this.name = name;
	}

	public static String concat(final Iterable<TsArgumentTypes> args) {
		final StringBuilder result = new StringBuilder();
		final Iterator<TsArgumentTypes> iter = args.iterator();
		while (iter.hasNext()) {
			result.append(iter.next().name).append(' ');
		}
		return result.toString().trim();
	}
	
	public boolean matches(TsToken token) {
		switch (this) {
			case Float:
				return token.type == TsTokenType.Float || token.type == TsTokenType.Integer;
			case Integer:
				return token.type == TsTokenType.Integer;
			default:
				return false;
		}
	}
}