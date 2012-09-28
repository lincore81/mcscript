package xde.lincore.mcscript.edit.turtlespeak;

import java.util.regex.Pattern;

enum TsTokenType {
	Identifier("(?i)[A-Z_]\\w*", "identifier"),
	Integer("[+-]?\\d+", "integer"),
	Float("[+-]?\\d+(?:\\.\\d+)", "real number"),
	BlockStart("\\(", "left parenthesis"),
	BlockEnd("\\)", "right parenthesis");

	public final Pattern regex;
	public final String name;

	private TsTokenType(final String regex, final String name) {
		this.regex = Pattern.compile(regex);
		this.name = name;
	}


}