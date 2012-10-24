package xde.lincore.mcscript.env;

import java.io.Serializable;
import java.util.HashMap;

public final class ScriptGlobals implements Serializable {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -6422223408376680006L;
	private final HashMap<String, Object> vars;

	protected ScriptGlobals() {
		vars = new HashMap<String, Object>();
	}

	public void set(final String key, final Object value) {
		vars.put(key, value);
	}

	public Object get(final String key) {
		return vars.get(key);
	}
	
	public Object getObject(final String key, final Class objType) {
		Object result = vars.get(key);
		if (result.getClass().getName().equals(objType.getName())) {
			return result;
		}
		else {
			return null;
		}
	}

	public Object get(final String key, final Object defaultValue) {
		final Object value = vars.get(key);
		return (value != null)? value : defaultValue;
	}

	public double getNumber(final String key, final double defaultValue) {
		final Object value = vars.get(key);
		if (value != null && value instanceof Number) {
			try {
				return (Double)value;
			}
			catch (final ClassCastException e) {
				e.printStackTrace();
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public boolean contains(final String key) {
		return vars.containsKey(key);
	}
	
	public boolean contains(final String key, final Class objType) {
		return vars.containsKey(key) && vars.get(key).getClass().getName().equals(objType);
	}

	public boolean equals(final String key, final Object obj) {
		final Object value = vars.get(key);
		if (value == null) {
			return obj == null;
		}
		else {
			return value.equals(obj);
		}
	}
}
