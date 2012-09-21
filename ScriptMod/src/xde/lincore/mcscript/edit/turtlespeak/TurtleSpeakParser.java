package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import xde.lincore.mcscript.G;


public class TurtleSpeakParser {
	
	private ITurtleDialect dialect;
	private Set<TsKeyword> keywords;
	private TsBlock parseTree;
	
	public TurtleSpeakParser(ITurtleDialect dialect) {
		this.dialect = dialect;
		keywords = new HashSet<TsKeyword>();
		dialect.registerKeywords(keywords);
	}
	
	public void parse(String source) throws SyntaxError {		
		TsTokenizer tokenizer = new TsTokenizer();
		Deque<TsToken> tokens = tokenizer.run(source);		
		TsTokenAnalyzer analyzer = new TsTokenAnalyzer(tokens, keywords);		
		
		parseTree = analyzer.run();
	}
	
	public void eval() {
		if (parseTree == null) {
			throw new IllegalStateException("Not ready for evaluation. Call parse(source) first.");
		} else {
			_eval(parseTree);
		}
	}
	
	private void _eval(TsBlock block) {
		if (block.getRepeats() == -1) return;
		int i = 0;
		do {			
			for (TsStatement statement: block) {
				TsBlock result = dialect.onStatement(statement);
				if (result != null) {
					_eval(result);
				}
			}
		} while (++i < block.getRepeats());
	}
	
	
	public ITurtleDialect getDialect() {
		return dialect;
	}

	public void setDialect(ITurtleDialect dialect) {
		this.dialect = dialect;
		keywords = new HashSet<TsKeyword>();
		dialect.registerKeywords(keywords);
	}
}
