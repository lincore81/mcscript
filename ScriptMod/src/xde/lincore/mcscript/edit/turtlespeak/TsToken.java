package xde.lincore.mcscript.edit.turtlespeak;

public class TsToken {
	public final String string;
	public final TsTokenType type;

	public TsToken (final String str, final TsTokenType type) {
		this.string = str;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type.name + ": \"" + string + "\"";
	}
}