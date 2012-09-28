package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Set;

public interface ITurtleDialect {

	public void registerKeywords(final Set<TsKeyword> keywords);
	public TsBlock onStatement(TsStatement command);
}
