package xde.lincore.mcscript.env;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import xde.lincore.util.StringTools;

public class ScriptArguments {

	private Iterator<String> keyIter, valueIter;
	private Map<String, String> args;

	protected ScriptArguments(final Map<String, String> args) {
		if (args != null) {
			this.args = args;
		}
		else {
			this.args = new HashMap<String, String>(0);
		}
	}


	public int count() {
		return args.size();
	}

	public boolean contains(final String key) {
		return args.containsKey(key);
	}

	public String get(final String key) {
		return findMatch(key);
	}

	public String get(final String key, final String defaultValue) {
		final String value = findMatch(key);
		if (value == null) {
			return defaultValue;
		}
		else {
			return value;
		}
	}

	public double getNumber(final String key) {
		final String value = findMatch(key);
		if (value != null) {
			return StringTools.getNumber(value);
		}
		else {
			return Double.NaN;
		}
	}

	public double getNumber(final String key, final Double defaultValue) {
		final String value = findMatch(key);
		if (value != null) {
			final Double result = StringTools.getNumber(value);
			if (result != null) {
				return result;
			}
		}
		return defaultValue;
	}

	public Boolean has(final String bool) {
		final String value = findMatch(bool);
		if (value != null) {
			return StringTools.getBoolean(value);
		}
		else {
			return false;
		}
	}

	public String next() {
		if (keyIter == null || !keyIter.hasNext()) {
			keyIter = args.keySet().iterator();
		}
		return keyIter.next();
	}

	public String nextValue() {
		return get(next());
	}

	public String first() {
		keyIter = args.keySet().iterator();
		return keyIter.next();
	}

	public boolean hasNext() {
		if (keyIter == null) {
			keyIter = args.keySet().iterator();
		}
		return keyIter.hasNext();
	}

	private String findMatch(final String re) {
		for (final Entry<String, String> e: args.entrySet()) {
			if (e.getKey().matches(re)) {
				return e.getValue();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("Arguments:\n");
		for (final Map.Entry<String, String> e: args.entrySet()) {
			buffer.append("\t" + e.getKey() + ": " + e.getValue() + "\n");
		}
		return buffer.toString();
	}

}
