package xde.lincore.mcscript.edit.turtlespeak;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TsTokenizer {
	
	public TsTokenizer() {}
	
	public Deque<TsToken> run(final String source) throws SyntaxError {
		String source_ = source.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ");
		Scanner scanner = new Scanner(source_);		
		Deque<TsToken> tokens = new ArrayDeque<TsToken>();
		while (scanner.hasNext()) {			
			TsTokenType type = findType(scanner);
			String tokenString = scanner.next(type.regex);
			tokens.push(new TsToken(tokenString, type));
		}
		return tokens;
	}
	
	private TsTokenType findType(Scanner scanner) throws SyntaxError {
		for (TsTokenType t: TsTokenType.values()) {
			if (scanner.hasNext(t.regex)) {
				return t;				
			}
		}
		throw new SyntaxError("Syntax error on token \"" + scanner.next() + 
				"\", delete this token.");
	}
}
