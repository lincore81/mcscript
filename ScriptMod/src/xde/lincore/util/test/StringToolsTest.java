package xde.lincore.util.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import xde.lincore.util.StringTools;

public class StringToolsTest {


	static HashMap<String, Boolean> mapTests; // test string, expected result
	static HashMap<String, Boolean> listTests; // dito
	static HashMap<String, Boolean> numberTests; // dito
	static HashMap<String, Boolean> integerTests; // dito


	@BeforeClass
	public static void setup() {
		mapTests = new HashMap<String, Boolean>();
		mapTests.put("x=42.391;y=68.000;z=65.700", true);
		mapTests.put("		config.autosave = on; ", true);
		mapTests.put("		config.auto;save = on; ", true);
		mapTests.put("	luftballons = 99	", true);
		mapTests.put("12.4; Yesterday; yes", true);
		mapTests.put("answer==42", false);
		mapTests.put("=x;z=a", false);
		mapTests.put("this=;love=hate;", false);
		mapTests.put("name=Mario; initials=M; silblings=1; colors=blue, red; ", true);
		mapTests.put("firstname=Augustus; lastname=Caesar; street=Plaza di Whatever; streetnumber=771; " +
					"city=Roma; state  =Lazio; phone=123456789; " +
					"email = caesar@imperator.gov; job   =    Pontifex Maximus ;   ", true);

		listTests = new HashMap<String, Boolean>();
		listTests.put("12.4; Yesterday; (12, 40, 89);", true);
		listTests.put("12.4, Yesterday, (12, 40, 89)", true);
		listTests.put(";", false);
		listTests.put("x;;p;n;", false);
		listTests.put("; 1979 ; 1988", false);
		listTests.put("\"this\\; is\"; a test; 78; true", true);

		numberTests = new HashMap<String, Boolean>();
		numberTests.put("68.3f", true);
		numberTests.put("42", true);
		numberTests.put(".1", true);
		numberTests.put("1.", true);
		numberTests.put("+55", true);
		numberTests.put("55+", false);
		numberTests.put("0x10cp1022", true);
		numberTests.put("-78e-06", true);
		numberTests.put("1_000_000", false);
		numberTests.put("72,4", false);
		numberTests.put("16d", true);

		integerTests = new HashMap<String, Boolean>();
		integerTests.put("70", true);
		integerTests.put("0", true);
		integerTests.put("+1867632", true);
		integerTests.put("-522e12", true);
		integerTests.put("0x10C", true);
		integerTests.put("0.7", false);
		integerTests.put(".7", false);
		integerTests.put("7.", false);
		integerTests.put("68.3f", false);
		integerTests.put("42", false);
		integerTests.put(".1", false);
		integerTests.put("1.", false);
		integerTests.put("+55", true);
		integerTests.put("55+", false);
		integerTests.put("0x10c", true);
		integerTests.put("-78e-06", true);
		integerTests.put("1_000_000", false);
		integerTests.put("72,4", false);
		integerTests.put("16d", false);
		integerTests.put("Hello", false);
		integerTests.put("abcdef0x", false);
		integerTests.put("0b100101010", true);
		integerTests.put("0b100201010", true);
	}

	@Test
	public void testGetBoolean() {
		assertTrue(StringTools.getBoolean("yes"));
		assertFalse(StringTools.getBoolean("no"));
		assertFalse(StringTools.getBoolean(null));
		assertTrue(StringTools.getBoolean("TRUE!"));
		assertFalse(StringTools.getBoolean("FALSE"));
		assertFalse(StringTools.getBoolean(""));
		assertTrue(StringTools.getBoolean("����h%$%/�($)"));
		assertFalse(StringTools.getBoolean("oFf"));
		assertFalse(StringTools.getBoolean("false"));
	}

	@Test
	public void testGetNumber() {
		System.out.println("Testing getNumber:");
		int i = 1;
		final Map<String, Boolean> tests = new HashMap<String, Boolean>(numberTests);
		tests.putAll(integerTests);
		for (final String testString: tests.keySet()) {
			System.out.format("\tcase #%d: %s", i, testString);
			final Double num = StringTools.getNumber(testString);
			System.out.format(" (valid? %b)%n", num != null);
			i++;

			if (num == null) {
				System.out.println("\t\tnull");
			}
			else {
				System.out.println("\t\t" + num.toString());
			}
		}
		System.out.println();
	}

	@Test
	public void testGetList() {
		System.out.println("\nTesting getList:");
		int i = 1;
		for (final String testString: listTests.keySet()) {
			System.out.format("\tcase #%d: %s (valid? %b)%n", i, testString,
					StringTools.isValidList(testString, ";"));
			i++;
			final List<String> list = StringTools.getList(testString, ";");
			if (list == null) {
				System.out.println("\t\tnull");
			}
			else if (list.isEmpty()) {
				System.out.println("\t\tempty");
			}
			else {
				for (final String listElement: list) {
					System.out.println("\t\t-\"" + listElement + "\"");
				}
			}
			System.out.println();
		}
	}

	@Test
	public void testGetMap() {
		System.out.println("Testing getMap:");
		int i = 1;
		for (final String testString: mapTests.keySet()) {
			System.out.format("\tcase #%d: %s (valid? %b)%n", i, testString,
					StringTools.isValidMap(testString, ";", "="));
			i++;
			Map<String, String> map;
			try {
				map = StringTools.getMap(testString, ";", "=");
			}
			catch (final IllegalArgumentException e) {
				System.out.println("\t\t" + e.getMessage() + "\n");
				continue;
			}
			if (map == null) {
				System.out.println("\t\tnull");
			}
			else if (map.isEmpty()) {
				System.out.println("\t\tempty");
			}
			else {
				for (final Map.Entry<String, String> entry: map.entrySet()) {
					System.out.println("\t\t\"" + entry.getKey() + "\" = \"" + entry.getValue() + "\"");
				}
			}
			System.out.println();
		}
	}

	@Test
	public void testIsValidList() {
		int i = 1;
		System.out.println("Testing isValidList:");
		for (final Map.Entry<String, Boolean> e : listTests.entrySet()) {
			final boolean result = StringTools.isValidList(e.getKey(), ";");
			System.out.format("\tcase #%d: %b (%b expected) for \"%s\"%n", i, result, e.getValue(), e.getKey());
			assertTrue(result == e.getValue());
			i++;
		}
	}

	@Test
	public void testIsValidMap() {
		int i = 1;
		System.out.println("Testing isValidMap:");
		for (final Map.Entry<String, Boolean> e : mapTests.entrySet()) {
			final boolean result = StringTools.isValidMap(e.getKey(), ";", "=");
			System.out.format("\tcase #%d: %b (%b expected) for \"%s\"%n", i, result, e.getValue(), e.getKey());
			assertTrue(result == e.getValue());
			i++;
		}
	}

}
