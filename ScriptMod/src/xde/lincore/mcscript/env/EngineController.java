package xde.lincore.mcscript.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import xde.lincore.mcscript.G;

public final class EngineController {
	public static final String DEFAULT_ENGINE = null;
	
	private ScriptEngine defaultEngine;		
	private ScriptEngine currentEngine;		
	private HashMap<String, ScriptEngine> enginePool;
	private ScriptEngineManager manager;
	
	public EngineController() {
		reset();
	}
	
	public void reset() {
		manager = new ScriptEngineManager();
		defaultEngine = manager.getEngineByName(G.DEFAULT_SCRIPT_ENGINE);
		if (defaultEngine == null) {
			throw new RuntimeException("Could not find the default scripting engine \"" +
					G.DEFAULT_SCRIPT_ENGINE + "\".");
		}
		currentEngine = defaultEngine;
		enginePool = new HashMap<String, ScriptEngine>();
		enginePool.put(defaultEngine.getFactory().getEngineName(), defaultEngine);
	}
	
	public ScriptEngine getDefaultEngine() {
		return defaultEngine;
	}
	
	public ScriptEngine getCurrentEngine() {
		return currentEngine;
	}	
	
	public void setCurrentEngine(String identifier) {
		currentEngine = getEngine(identifier);
	}
	
	public ScriptEngine getEngine(String identifier) {
		if (identifier == null) {
			return currentEngine;
		}
		String engineName = findEngineName(identifier);
		if (engineName == null) {		
			throw new IllegalArgumentException(
					identifier + " does not denote an available script engine.");
		} else {
			if (enginePool.containsKey(engineName)) {
				return enginePool.get(engineName);
			} else{
				ScriptEngine result = manager.getEngineByName(engineName);
				enginePool.put(engineName, result);			
				return result;
			}
		}
	}
	
	public ScriptEngineManager getManager() {
		return manager;
	}

	public String findEngineName(String identifier){
		String _identifier = identifier.toLowerCase();
		String result = null;		
		for (ScriptEngineFactory f: manager.getEngineFactories()) {
			ArrayList<String> names = new ArrayList<String>();
			names.add(f.getEngineName());
			names.addAll(f.getNames());			
			for (String name: names) {
				if (name.toLowerCase().startsWith(_identifier)) {
					if (result == null) {
						result = f.getNames().get(0);
						break; // check other engine names for ambiguouty (sp?)
					}
					else {
						throw new IllegalArgumentException(
								identifier + " is ambiguous. Please use a distinct identifier " +
								"for the engine you would like to use.");
					}
				}
			}
		}		
		return result;
	}
	
	public String dumpPool() {
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, ScriptEngine> e: enginePool.entrySet()) {
			buffer.append(e.getKey() + ": " + e.getValue().toString() + "\n");
		}
		return buffer.toString();
	}
	
}
