package xde.lincore.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringTools {
	private static final int STRING_NOT_FOUND = -1;
	
	public static boolean getBoolean(String string) {
		return !(string == null || string.equalsIgnoreCase("false") || 
				string.equalsIgnoreCase("no") || string.equalsIgnoreCase("off") || 
				string.equals("0") || string.isEmpty());
	}
	
	public static Integer getInteger(String string) {		
		Integer result = null;
		try {
			if (string.trim().matches("[+-]?0[xX][0-9A-Fa-f]+")) {
				String hexstring = string.replaceFirst("0[xX]", "");				
				result = Integer.valueOf(hexstring, 16);
			}
			else if (string.trim().matches("[+-]?0[bB][01]+")) {
				String binary = string.replaceFirst("0[bB]", "");				
				result = Integer.valueOf(binary, 2);
			}
			else {
				result = Integer.valueOf(string);
			}
		}
		catch (NumberFormatException e) {
			return null;
		}
		return result;
	}

	
	public static List<String> getList(String string, String delimiter) {
		return Arrays.asList(split(string, delimiter));
	}
	
	public static Map<String, String> getMap(String string, String delimiter, String assignOp) {
		HashMap<String, String> result = new HashMap<String, String>();
		String delimiter_ 	= (delimiter != null)? delimiter : ";";
		String assignOp_	= (assignOp != null)? assignOp: "=";
		String[] pairs = split(string, delimiter);
		int count = 1;
		for (String pair: pairs) {	
			String key;
			String value;			
			
			int index = pair.indexOf(assignOp_);			
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
	
	
	public static Double getNumber(String string) {
		Double result;
		try {
			result = Double.valueOf(string);
		}
		catch (NumberFormatException e) {
			Integer iresult = getInteger(string);
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
	public static String insertArgs(String string, String[] args) {		
		String allArgs = join(args);
		String result = string;
		
		for (int i = 0; i <= 9; i++) { // for all placeholders $[i+1] => $1 - $9 and i==9 => $*
			StringBuffer buffer = new StringBuffer();
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
				int index = matcher.toMatchResult().start();
				
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
	
	public static boolean isDouble(String string) {
		try {
			return Double.valueOf(string) != null;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isInteger(String string) {
		try {
			return Integer.valueOf(string) != null;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isValidList(String string, String delimiter) {
		String escapedString = string.replaceAll("\\\\" + delimiter, "");
		String value = "\\s*[^" + delimiter + "]+\\s*";
		String expr = value + "(?:" + delimiter + value + ")*" + "(?:" + delimiter + ")?";
		return string.trim().matches(expr);
	}
	
	public static boolean isValidMap(String string, String delimiter, String assignOp) {
		String escapedString = string.replaceAll("\\\\" + delimiter, "");
		String identifier 	= "[^" + delimiter + assignOp + "]+";
		String entry 		= "\\s*(?:" + identifier + "\\s*" + assignOp + "\\s*" + 
								identifier + "|" + identifier + ")\\s*";
		String map 			= entry + "(?:" + delimiter + entry + ")*" + "(?:" + delimiter + ")?";
		
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
	public static boolean isValidNumber(String testString) {
		return getNumber(testString) != null;
	}
	
	public static String join(Collection col) {
		return join(col, " ");
	}
	
	public static String join(Collection col, String delimiter) {
		if (col == null || col.isEmpty()) return "";
		Iterator iter = col.iterator();
		StringBuilder result = new StringBuilder();
		
		while (iter.hasNext()) {
			result.append(iter.next().toString());
			if (iter.hasNext()) {
				result.append(delimiter);
			}
		}
		return result.toString();
	}
	
	public static String join(String[] strings) {
		return join(strings, 0, strings.length, " ");
	}
	
	public static String join(String[] strings, int from, int to, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		for (int i = from; i < to; i++) {
			buffer.append(strings[i]);
			if (i < to - 1) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}
	
	public static String join(String[] strings, String delimiter) {
		return join(strings, 0, strings.length, delimiter);
	}
	
	
	public static String[] split(String string, String delimiter) {
		String regex = "\\s*(?<!\\\\)" + delimiter + "\\s*";
		String[] results = string.trim().split(regex);
		for (int i = 0; i < results.length; i++) {
			results[i] = results[i].replaceAll("\\\\" + delimiter, delimiter);
		}
		return results;
	}

	private StringTools() {		
	}
}
