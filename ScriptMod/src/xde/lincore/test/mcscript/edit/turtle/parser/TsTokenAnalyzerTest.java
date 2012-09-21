package xde.lincore.test.mcscript.edit.turtle.parser;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.python.antlr.adapter.KeywordAdapter;

import xde.lincore.mcscript.edit.turtlespeak.SyntaxError;
import xde.lincore.mcscript.edit.turtlespeak.TsArgumentTypes;
import xde.lincore.mcscript.edit.turtlespeak.TsBlock;
import xde.lincore.mcscript.edit.turtlespeak.TsKeyword;
import xde.lincore.mcscript.edit.turtlespeak.TsToken;
import xde.lincore.mcscript.edit.turtlespeak.TsTokenAnalyzer;
import xde.lincore.mcscript.edit.turtlespeak.TsTokenizer;

public class TsTokenAnalyzerTest {

	TsTokenizer 		tokenizer;
	
	TsTokenAnalyzer 	analyzer;
	
	TsBlock 			parseTree;
	
	Deque<TsToken> 		tokens;
	
	HashSet<TsKeyword> 	keywords;
	
	
	static final String SOURCE = "rep 20 (rep 4 (fd 17.5 rt 90) goup 1) reset";

	
	
	@Before
	public void setUp() throws Exception {
		keywords = new HashSet<TsKeyword>();		
		registerKeywords(
			new TsKeyword(00, "forward", 	"fd|forward", 		false, 	TsArgumentTypes.Float),
			new TsKeyword(01, "backward", 	"bk|back(ward)?", 	false, 	TsArgumentTypes.Float),
			new TsKeyword(02, "goup", 		"goup", 			false, 	TsArgumentTypes.Float),
			new TsKeyword(03, "godown", 	"god(ow)?n", 		false, 	TsArgumentTypes.Float),
			
			new TsKeyword(06, "left", 		"lt|left", 			false, 	TsArgumentTypes.Integer),
			new TsKeyword(07, "right", 		"rt|right", 		false, 	TsArgumentTypes.Integer),
			
			new TsKeyword(20, "repeat", 	"rep(eat)?", 		true, 	TsArgumentTypes.Integer),
			new TsKeyword(21, "reset", 		"reset", 			false)
		);
		
		tokenizer = new TsTokenizer();
		tokens = tokenizer.run(SOURCE);
		
		// dump all tokens:
		Deque<TsToken> tokensCopy = new ArrayDeque<TsToken>(tokens);
		while (!tokensCopy.isEmpty()) {
			System.out.println(tokensCopy.pollLast());
		}
		System.out.println("\n" + SOURCE + "\n");
	}

	@Test
	public void testRun() {
		analyzer = new TsTokenAnalyzer(tokens, keywords);
		
		try {
			parseTree = analyzer.run();
			System.out.println(parseTree.dump(0));
		} catch (SyntaxError e) {
			System.err.println(e.getMessage());
			fail();
		}	
	}

	private void registerKeywords(TsKeyword... keywords) {
		for (TsKeyword k: keywords)
			this.keywords.add(k);
	}
}

