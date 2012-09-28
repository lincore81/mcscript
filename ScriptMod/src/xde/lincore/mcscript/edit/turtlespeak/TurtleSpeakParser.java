package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;


public class TurtleSpeakParser {

	private ITurtleDialect dialect;
	private Set<TsKeyword> keywords;
	private TsBlock parseTree;

	public TurtleSpeakParser(final ITurtleDialect dialect) {
		this.dialect = dialect;
		keywords = new HashSet<TsKeyword>();
		dialect.registerKeywords(keywords);
	}

	public void parse(final String source) throws SyntaxError {
		final TsTokenizer tokenizer = new TsTokenizer();
		final Deque<TsToken> tokens = tokenizer.run(source);
		final TsTokenAnalyzer analyzer = new TsTokenAnalyzer(tokens, keywords);

		parseTree = analyzer.run();
	}

	public void eval() {
		if (parseTree == null) {
			throw new IllegalStateException("Not ready for evaluation. Call parse(source) first.");
		} else {
			_eval(parseTree);
		}
	}

	private void _eval(final TsBlock block) {
		if (block.getRepeats() == -1) {
			return;
		}
		int i = 0;
		do {
			for (final TsStatement statement: block) {
				final TsBlock result = dialect.onStatement(statement);
				if (result != null) {
					_eval(result);
				}
			}
		} while (++i < block.getRepeats());
	}


	public ITurtleDialect getDialect() {
		return dialect;
	}

	public void setDialect(final ITurtleDialect dialect) {
		this.dialect = dialect;
		keywords = new HashSet<TsKeyword>();
		dialect.registerKeywords(keywords);
	}
}
