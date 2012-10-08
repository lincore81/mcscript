package xde.lincore.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import xde.lincore.mcscript.env.G;



public final class Config {
	private static Map<String, Properties> properties;
	private Config() {}

	static {
		properties = new HashMap<String, Properties>();
	}

	public static void clearMap(final String name) {
		properties.get(name).clear();
	}

	public static void clearMap(final String name, final Properties defaults) {
		properties.get(name).clear();
		properties.get(name).putAll(defaults);
	}

	public static boolean contains(final String map, final String key) {
		return properties.get(map).containsKey(key);
	}

	public static void createMap(final String name) {
		properties.put(name, new Properties());
	}

	public static void createMap(final String name, final Properties defaults) {
		final Properties p = new Properties();
		p.putAll(defaults);
		properties.put(name, p);
	}

	public static void removeMap(final String name) {
		properties.remove(name);
	}

	public static Properties getMap(final String name) {
		return properties.get(name);
	}

	public static SortedMap getSortedMap(final String name) {
		final SortedMap result = new TreeMap();
		result.putAll(properties.get(name));
		return result;
	}

	public static String get(final String propertiesName, final String key) {
		return properties.get(propertiesName).getProperty(key);
	}

	public static boolean remove(final String map, final String key) {
		return properties.get(map).remove(key) != null;
	}

	public static void rename(final String mapName, final String newName) {
		final Properties p = properties.remove(mapName);
		properties.put(newName, p);
	}

	public static void set(final String table, final String key, final String value) {
		properties.get(table).setProperty(key, value);
	}

	public static void setMultiple(final String table, final Map<String, String> entries) {
		properties.get(table).putAll(entries);
	}

	public static boolean load(final String name) {
		return load(generateFileName(name), name);
	}

	public static boolean load(final String fileName, final String name) {
		Properties result = null;
		if (properties.containsKey(name)) {
			result = properties.get(name);
		}
		else {
			result = new Properties();
		}
		G.DIR_CONFIG.mkdirs();
		final File file = new File(G.DIR_CONFIG, fileName);
		BufferedReader reader = null;
		try {
			if (file.exists() || file.createNewFile()) {
				reader = new BufferedReader(new FileReader(file));
				result.load(reader);
			}
		} catch (final IOException e) {
			G.LOG.severe("Could not open/create config file: " + file.getAbsolutePath());
			e.printStackTrace();
			return false;
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace(); // shit hit the fan?
					return false;
				}
			}
		}
		properties.put(name, result);
		return true;
	}

	public static boolean save(final String name) {
		return save(generateFileName(name), name);
	}

	public static boolean save(final String fileName, final String table) {
		BufferedWriter writer = null;
		G.DIR_CONFIG.mkdirs();
		final File file = new File(G.DIR_CONFIG, fileName);
		try {
			writer = new BufferedWriter(new FileWriter(file));
			properties.get(table).store(writer, table);
		}
		catch (final IOException e) {
			G.LOG.severe("Could not save config file " + file.getAbsolutePath());
			e.printStackTrace();
			return false;
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	public static String generateFileName(final String mapName) {
		return mapName + G.EXT_CONFIG;
	}

	public static boolean autosave(final String name) {
		return autosave(generateFileName(name), name);
	}



	public static boolean autosave(final String fileName, final String table) {
		if (StringTools.getBoolean(properties.get(table).getProperty(G.PROP_AUTOSAVE, "on"))) {
			return save(fileName, table);
		}
		else {
			return true; // only return false if autosave is enabled and errors occured.
		}
	}
}