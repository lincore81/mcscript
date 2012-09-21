package xde.lincore.test.mcscript.edit.turtle.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import xde.lincore.mcscript.edit.turtlespeak.ITurtleDialect;
import xde.lincore.mcscript.edit.turtlespeak.SyntaxError;
import xde.lincore.mcscript.edit.turtlespeak.TurtleSpeakParser;
import xde.lincore.test.mcscript.edit.turtle.TurtleTestDialect;

public class TurtleSpeakParserTest {

	private ITurtleDialect dialect;
	private TurtleSpeakParser parser;
	
	private static final String program = "rep 5 (rep 4 (fd 20 rt) goup 1) (reset)";
	
	@Before
	public void setUp() {
		dialect = new TurtleTestDialect();
		parser = new TurtleSpeakParser(dialect);
	}

	@Test
	public void test() {
		try {
			System.out.println("Parsing...");
			parser.parse(program);
			System.out.println("Evaluating...");
			parser.eval();
		} catch (SyntaxError e) {
			System.err.println(e.getMessage());			
		}
	}

}
