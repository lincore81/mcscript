package xde.lincore.mcscript.env;

import java.io.Serializable;
import java.util.HashMap;

import xde.lincore.util.StringTools;

public final class ScriptGlobals implements Serializable {

	private HashMap<String, Object> vars;
	
	protected ScriptGlobals() {
		vars = new HashMap<String, Object>();
	}	

	public void set(String key, Object value) {
		vars.put(key, value);
	}
	
	public Object get(String key) {
		return vars.get(key);
	}
	
	public Object get(String key, Object def) {
		Object value = vars.get(key);
		return (value != null)? value : def;
	}
	
	public double getNumber(String key, double defaultValue) {
		Object value = vars.get(key);
		if (value != null) {
			try {
				return (Double)value;
			}
			catch (ClassCastException e) {
				e.printStackTrace();
				return defaultValue;
			}
		}
		return defaultValue;
	}
	
	public boolean contains(String key) {
		return vars.containsKey(key);
	}
	
	public boolean equals(String key, Object obj) {
		Object value = vars.get(key);
		if (value == null) {
			return obj == null;
		}
		else {
			return value.equals(obj);
		}
	}
}
