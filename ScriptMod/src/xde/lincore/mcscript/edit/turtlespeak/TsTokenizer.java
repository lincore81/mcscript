package xde.lincore.mcscript.edit.turtlespeak;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class TsTokenizer {

	public TsTokenizer() {}

	public Deque<TsToken> run(final String source) throws SyntaxError {
		final String source_ = source.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ");
		final Scanner scanner = new Scanner(source_);
		final Deque<TsToken> tokens = new ArrayDeque<TsToken>();
		while (scanner.hasNext()) {
			final TsTokenType type = findType(scanner);
			final String tokenString = scanner.next(type.regex);
			tokens.push(new TsToken(tokenString, type));
		}
		return tokens;
	}

	private TsTokenType findType(final Scanner scanner) throws SyntaxError {
		for (final TsTokenType t: TsTokenType.values()) {
			if (scanner.hasNext(t.regex)) {
				return t;
			}
		}
		throw new SyntaxError("Syntax error on token \"" + scanner.next() +
				"\", delete this token.");
	}
}
