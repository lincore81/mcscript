package xde.lincore.mcscript.env;

import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

public class ScriptBindingsController extends AbstractController {
	
	private Bindings globalBindings;
	
	public ScriptBindingsController(final ScriptEnvironment env) {
		super(env);
	}
	
	public synchronized Bindings getBindings(final Script script) {
		if (globalBindings == null) {
			globalBindings = script.getEngine().getBindings(ScriptContext.GLOBAL_SCOPE);
		}
		if (script.isScriptFile()) {
			return cloneGlobalBindings(script);
		} else {
			return globalBindings;
		}
	}
	
	public synchronized void setBindings(final Script script, final Bindings bindings) {
		if (!script.isScriptFile()) {
			globalBindings = bindings;
		}
	}
	
	public void reset() {
		globalBindings = null;
	}
	
	private Bindings cloneGlobalBindings(final Script script) {
		if (globalBindings == null) {
			throw new IllegalStateException("globalBindings is null.");
		}
		ScriptEngine engine = script.getEngine();
		Bindings result = engine.createBindings();
		result.putAll(globalBindings);
		return result;
	}
	
	public synchronized Object export(final String name, final Object value) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Can't export a variable with no name.");
		}
		if (globalBindings == null) {
			throw new IllegalStateException("globalBindings is null.");
		}
		return globalBindings.put(name, value);
	}
	
	public synchronized String dumpGlobalBindings() {
		if (globalBindings == null) {
			throw new IllegalStateException("globalBindings is null.");
		}
		StringBuilder result = new StringBuilder();
		int i = 0;
		String mask = "%d:  %s = %s (%s)\n";
		for (Map.Entry<String, Object> e: globalBindings.entrySet()) {
			String name  = e.getKey();
			Object value = (e.getValue() != null)? e.getValue() != null : "null";
			String type  = value.getClass().getSimpleName();
			result.append(String.format(mask, ++i, name, value, type));
		}
		return result.toString();
	}
}
