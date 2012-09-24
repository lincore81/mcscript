package xde.lincore.test.mcscript.env;

import static org.junit.Assert.*;

import javax.script.ScriptEngine;

import org.junit.Before;
import org.junit.Test;

import xde.lincore.mcscript.env.EngineController;

public class EngineControllerTest {
	
	EngineController engines;
	String[] tests = new String[] {
			"js", "javascript", "java", "rhino", "moz", "lua", "jlua", "luaj", "l", "py", "jython"};
	

	@Before
	public void setUp() {
		engines = new EngineController();
	}

	@Test
	public void testGetDefaultEngine() {
		System.out.println("\ntestGetDefaultEngine:");
		System.out.println("\t" + engines.getDefaultEngine().getFactory().getEngineName());
		
	}
	
	@Test
	public void testFindEngineName() {		
		System.out.println("\ntestFindEngineName:");
		for (String test: tests) {
			System.out.println("\t" + test + ": " + engines.findEngineName(test));
		}
	}

	@Test
	public void testGetEngine() {
		System.out.println("\ntestGetEngine:");
		for (String test: tests) {
			ScriptEngine result = null;
			try {
				 result = engines.getEngine(test);
				 System.out.format("\t%s: %s\n", test, (result == null)? "null" : result);
			} catch (IllegalArgumentException e) {
				System.err.println("\t" + e.getMessage());
			}
			
		}
	}
}
