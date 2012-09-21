package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.python.google.common.base.CaseFormat;

public class TsKeyword {
	public final int id;
	public final String name;
	public final Pattern pattern;
	public final boolean expectsBlock;
	public final List<TsArgumentTypes> expectedArgs;
	
	public TsKeyword(final int id, final String name, final String regex, 
			final boolean expectsBlock, final TsArgumentTypes... expectedArgs ) {
		this.id 			= id;
		this.name 			= name;
		this.expectsBlock 	= expectsBlock;
		this.expectedArgs 	= Collections.unmodifiableList(Arrays.asList(expectedArgs)) ;
		pattern 			= Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	public boolean checkArg(final int index, final TsArgumentTypes type) {
		if (index >= 0 && index < expectedArgs.size()) {
			return expectedArgs.get(index) == type;
		} else {
			return false;
		}
	}

	public TsArgumentTypes getExpectedArgumentType(final int index) {
		if (index >= 0 && index < expectedArgs.size()) {
			return expectedArgs.get(index);
		}
		else {
			return null;
		}
	}
	
	public boolean matches(TsToken token) {
		return pattern.matcher(token.string).matches();
	}

	public String getSyntaxString() {
		return name + " " + TsArgumentTypes.concat(expectedArgs) + (expectsBlock ? " (...)" : "");
	}
	
	@Override
	public String toString() {
		return name;
	}
}