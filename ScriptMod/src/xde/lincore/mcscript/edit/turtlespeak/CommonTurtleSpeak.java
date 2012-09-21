package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Set;

import xde.lincore.util.UniqueId;

public abstract class CommonTurtleSpeak implements ITurtleDialect {

	public static final int REPEAT = -0xFF;

	@Override
	public TsBlock onStatement(final TsStatement statement) {
		switch (statement.keyword.id) {
			case REPEAT:
				statement.getBlock().setRepeats(statement.arguments.get(0).intValue());
				return statement.getBlock();
		}
		return null;
	}

	@Override
	public void registerKeywords(final Set<TsKeyword> keywords) {
		keywords.add(new TsKeyword(REPEAT, "repeat", "rep(eat)?", true, TsArgumentTypes.Integer));
	}

}
