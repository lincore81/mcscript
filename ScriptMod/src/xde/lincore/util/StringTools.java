package xde.lincore.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringTools {
	private static final int STRING_NOT_FOUND = -1;

	public static boolean getBoolean(final String string) {
		return !(string == null || string.equalsIgnoreCase("false") ||
				string.equalsIgnoreCase("no") || string.equalsIgnoreCase("off") ||
				string.equals("0") || string.isEmpty());
	}

	public static Integer getInteger(final String string) {
		Integer result = null;
		try {
			if (string.trim().matches("[+-]?(0[xX])?[0-9A-Fa-f]+")) {
				final String hexstring = string.replaceFirst("0[xX]", "");
				result = Integer.valueOf(hexstring, 16);
			}
			else if (string.trim().matches("[+-]?0[bB][01]+")) {
				final String binary = string.replaceFirst("0[bB]", "");
				result = Integer.valueOf(binary, 2);
			}
			else {
				result = Integer.valueOf(string);
			}
		}
		catch (final NumberFormatException e) {
			return null;
		}
		return result;
	}


	public static List<String> getList(final String string, final String delimiter) {
		return Arrays.asList(split(string, delimiter));
	}

	public static Map<String, String> getMap(final String string, final String delimiter, final String assignOp) {
		final HashMap<String, String> result = new HashMap<String, String>();
		final String delimiter_ 	= (delimiter != null)? delimiter : ";";
		final String assignOp_	= (assignOp != null)? assignOp: "=";
		final String[] pairs = split(string, delimiter);
		int count = 1;
		for (final String pair: pairs) {
			String key;
			String value;

			final int index = pair.indexOf(assignOp_);
			if (index == STRING_NOT_FOUND) {
				key = String.valueOf(count++);
				value = pair.trim();
			}
			else {
				key = pair.substring(0, index).trim();
				value = pair.substring(index + 1).trim();
			}
			if (key.isEmpty() || value.isEmpty()) {
				throw new IllegalArgumentException("Bad format: " + string);
			}
			result.put(key, value);
		}
		return result;
	}


	public static Double getNumber(final String string) {
		Double result;
		try {
			result = Double.valueOf(string);
		}
		catch (final NumberFormatException e) {
			final Integer iresult = getInteger(string);
			result = (iresult != null)? iresult.doubleValue() : null;
		}
		return result;
	}

	/**
	 * Insert arguments [n-1] => [0]..[8] for placeholders $n => $1 - $9 and
	 * all arguments for $*. Use "\$x" to avoid replacement.
	 * @param string The string to insert the arguments into.
	 * @param args The arguments to replace the placeholders with.
	 * @return The given string with arguments inserted or the same string
	 * 		   if nothing was changed.
	 */
	public static String insertArgs(final String string, final String[] args) {
		final String allArgs = join(args);
		String result = string;

		for (int i = 0; i <= 9; i++) { // for all placeholders $[i+1] => $1 - $9 and i==9 => $*
			final StringBuffer buffer = new StringBuffer();
			Pattern pattern;
			Matcher matcher;
			String replacement;

			if (i < 9) { // check for $1 to %9
				pattern = Pattern.compile("\\$" + String.valueOf(i + 1));
				matcher = pattern.matcher(result);
				replacement = (i < args.length)? args[i] : ""; // if not enough args given replace with ""
			}
			else { // check for $* in last loop
				pattern = Pattern.compile("\\$\\*");
				matcher = pattern.matcher(result);
				replacement = allArgs;
			}
			// find all occurences of "$x" and replace with argument if not preceeded by backslash('\'):
			while (matcher.find()) {
				final int index = matcher.toMatchResult().start();

				// don't insert arg for "\$x", but do insert for "\\$x" (escaped backslash)
				if (index > 0 && string.charAt(index - 1) == '\\') {
					if (!(index > 1 && string.charAt(index - 2) == '\\')) {
						continue;
					}
				}
				matcher.appendReplacement(buffer, replacement);
			}
			matcher.appendTail(buffer);
			result = buffer.toString();
		}
		return result;
	}

	public static boolean isDouble(final String string) {
		try {
			return Double.valueOf(string) != null;
		}
		catch (final NumberFormatException e) {
			return false;
		}
	}

	public static boolean isInteger(final String string) {
		try {
			return Integer.valueOf(string) != null;
		}
		catch (final NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidList(final String string, final String delimiter) {
		final String escapedString = string.replaceAll("\\\\" + delimiter, "");
		final String value = "\\s*[^" + delimiter + "]+\\s*";
		final String expr = value + "(?:" + delimiter + value + ")*" + "(?:" + delimiter + ")?";
		return string.trim().matches(expr);
	}

	public static boolean isValidMap(final String string, final String delimiter, final String assignOp) {
		final String escapedString = string.replaceAll("\\\\" + delimiter, "");
		final String identifier 	= "[^" + delimiter + assignOp + "]+";
		final String entry 		= "\\s*(?:" + identifier + "\\s*" + assignOp + "\\s*" +
								identifier + "|" + identifier + ")\\s*";
		final String map 			= entry + "(?:" + delimiter + entry + ")*" + "(?:" + delimiter + ")?";

		return escapedString.trim().matches(map);
	}

	/**
	 * Test whether the given String is a valid number.
	 * Only use this method when you don't want to get the converted number afterwards,
	 * in which case it is cheaper and still safe to call getNumber directly.
	 * @param testString The String to test
	 * @return true if the String could be successfully converted into a Double or an Integer,
	 * 			otherwise false.
	 */
	public static boolean isValidNumber(final String testString) {
		return getNumber(testString) != null;
	}

	public static String join(final Collection col) {
		return join(col, " ");
	}

	public static String join(final Collection col, final String delimiter) {
		if (col == null || col.isEmpty()) {
			return "";
		}
		final Iterator iter = col.iterator();
		final StringBuilder result = new StringBuilder();

		while (iter.hasNext()) {
			result.append(iter.next().toString());
			if (iter.hasNext()) {
				result.append(delimiter);
			}
		}
		return result.toString();
	}

	public static String join(final String[] strings) {
		return join(strings, 0, strings.length, " ");
	}

	public static String join(final String[] strings, final int from, final int to, final String delimiter) {
		final StringBuffer buffer = new StringBuffer();
		for (int i = from; i < to; i++) {
			buffer.append(strings[i]);
			if (i < to - 1) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	public static String join(final String[] strings, final String delimiter) {
		return join(strings, 0, strings.length, delimiter);
	}


	public static String[] split(final String string, final String delimiter) {
		final String regex = "\\s*(?<!\\\\)" + delimiter + "\\s*";
		final String[] results = string.trim().split(regex);
		for (int i = 0; i < results.length; i++) {
			results[i] = results[i].replaceAll("\\\\" + delimiter, delimiter);
		}
		return results;
	}


	/**
	 * Repeat a String {@code repeat} times to form a new String.
	 * @param 	string 	The String to repeat or null.
	 * @param 	times 	How often to repeat {@code string}. Must not be negative.
	 * @return	A new String consisting of the original {@code string} repeated,
	 * 			an empty String ({@code ""}) if {@code times} equals {@code 0} or {@code "null"} if
	 * 			{@code string} is {@code null}.
	 * @throws 	NegativeArraySizeException (unchecked) thrown if {@code times} is negative.
	 */
	public static String repeat(final String string, final int times) throws NegativeArraySizeException {
		switch (times) {
			case 0:
				return "";
			case 1:
				return string;
			default:
				return new String(new char[times]).replace("\0", string);
		}
	}



	private StringTools() {}
}
