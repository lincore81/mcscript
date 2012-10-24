package xde.lincore.mcscript.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import xde.lincore.util.Config;

public final class ScriptEngineController {
	public static final String DEFAULT_ENGINE = null;

	private ScriptEngine defaultEngine;
	private ScriptEngine currentEngine;
	private HashMap<String, ScriptEngine> enginePool;
	private ScriptEngineManager manager;

	public ScriptEngineController() {
		reset();
	}

	public void reset() {
		manager = new ScriptEngineManager();
		String defaultEngineProperty = Config.get(G.CFG_MAIN, G.PROP_DEFAULT_ENGINE);
		assert defaultEngineProperty != null : "IAmStupidException: defaultEngineProperty is null!";
		defaultEngine = manager.getEngineByName(defaultEngineProperty);
		if (defaultEngine == null) {
			throw new RuntimeException("Could not find the default scripting engine \"" +
					defaultEngineProperty + "\".");
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

	public void setCurrentEngine(final String identifier) {
		currentEngine = getEngine(identifier);
	}

	public ScriptEngine getEngine(final String identifier) {
		if (identifier == null) {
			return currentEngine;
		}
		final String engineName = findEngineName(identifier);
		if (engineName == null) {
			throw new IllegalArgumentException(
					identifier + " does not denote an available script engine.");
		} else {
			if (enginePool.containsKey(engineName)) {
				return enginePool.get(engineName);
			} else{
				final ScriptEngine result = manager.getEngineByName(engineName);
				enginePool.put(engineName, result);
				return result;
			}
		}
	}

	public ScriptEngineManager getManager() {
		return manager;
	}

	public String findEngineName(final String identifier){
		final String _identifier = identifier.toLowerCase();
		String result = null;
		for (final ScriptEngineFactory f: manager.getEngineFactories()) {
			final ArrayList<String> names = new ArrayList<String>();
			names.add(f.getEngineName());
			names.addAll(f.getNames());
			for (final String name: names) {
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
		final StringBuffer buffer = new StringBuffer();
		for (final Map.Entry<String, ScriptEngine> e: enginePool.entrySet()) {
			buffer.append(e.getKey() + ": " + e.getValue().toString() + "\n");
		}
		return buffer.toString();
	}

}
