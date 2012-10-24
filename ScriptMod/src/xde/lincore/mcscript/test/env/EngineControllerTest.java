package xde.lincore.mcscript.test.env;

import javax.script.ScriptEngine;

import org.junit.Before;
import org.junit.Test;

import xde.lincore.mcscript.env.ScriptEngineController;

public class EngineControllerTest {

	ScriptEngineController engines;
	String[] tests = new String[] {
			"js", "javascript", "java", "rhino", "moz", "lua", "jlua", "luaj", "l", "py", "jython"};


	@Before
	public void setUp() {
		engines = new ScriptEngineController();
	}

	@Test
	public void testGetDefaultEngine() {
		System.out.println("\ntestGetDefaultEngine:");
		System.out.println("\t" + engines.getDefaultEngine().getFactory().getEngineName());

	}

	@Test
	public void testFindEngineName() {
		System.out.println("\ntestFindEngineName:");
		for (final String test: tests) {
			System.out.println("\t" + test + ": " + engines.findEngineName(test));
		}
	}

	@Test
	public void testGetEngine() {
		System.out.println("\ntestGetEngine:");
		for (final String test: tests) {
			ScriptEngine result = null;
			try {
				 result = engines.getEngine(test);
				 System.out.format("\t%s: %s\n", test, (result == null)? "null" : result);
			} catch (final IllegalArgumentException e) {
				System.err.println("\t" + e.getMessage());
			}

		}
	}
}
