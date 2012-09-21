package xde.lincore.test.mcscript.edit.turtle.parser;

import static org.junit.Assert.*;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import xde.lincore.mcscript.edit.turtlespeak.SyntaxError;
import xde.lincore.mcscript.edit.turtlespeak.TsToken;
import xde.lincore.mcscript.edit.turtlespeak.TsTokenizer;

public class TsTokenizerTest {

	private static final int	HashMap	= 0;
	HashMap<String, Boolean> testCases;
	
	
	@Before
	public void setUp() {
		testCases = new HashMap<String, Boolean>();
		//			  PROGRAM									SYNTAX ERROR?
		testCases.put("rep 20 (rep 4 (fd 17.5 rt 90) goup)", 	false);
		testCases.put("fd rt fd lt bk rt rep 2 (rt)", 			false);
		testCases.put("()(())()()()()()())(()((())", 			false);
		testCases.put("to octagon (rep 8(fd 20 rt 45))", 		false);
		testCases.put("rep 20 :(rep 4 (fd 17.5 rt 90) goup)", 	true);		
		testCases.put("äpfel und birnen", 						true);
		testCases.put("78a", 									true);		
		testCases.put("hôtel", 									true);		
		testCases.put("fd?", 									true);
	}

	@Test
	public void testRun() {
		int i = 1;
		for (Map.Entry<String, Boolean> e: testCases.entrySet()) {
			TsTokenizer tokenizer = new TsTokenizer();
			Deque<TsToken> tokens = null;
			String program = e.getKey();
			boolean shouldThrow = e.getValue();
			System.out.format("Case #%d/%d: \"%s\" (syntax error? %b)\n", i++, testCases.size(), program, shouldThrow);
			try {
				tokens = tokenizer.run(program);
			} catch(SyntaxError se) {
				System.out.println("\t" + se);
				if (!shouldThrow) {
					System.out.println("\tFAILED: program is correct, but tokenizer threw.");
					fail();
				}
				else {
					System.out.println("\tPASSED");
				}
				continue;
			}
			while (!tokens.isEmpty()) {
				System.out.println("\t" + tokens.pollLast());
			}
			if (shouldThrow) {
				System.out.println("\tFAILED: program is incorrect, but tokenizer didn't throw.");
				fail();
			} else {
				System.out.println("\tPASSED");
			}
		}
	}

}
