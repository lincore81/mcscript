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

import xde.lincore.mcscript.G;



public final class Config {
	private static Map<String, Properties> properties;	
	private Config() {}
	
	static {
		properties = new HashMap<String, Properties>();
	}
	
	public static void clearMap(String name) {
		properties.get(name).clear();
	}
	
	public static void clearMap(String name, Properties defaults) {
		properties.get(name).clear();
		properties.get(name).putAll(defaults);
	}
	
	public static boolean contains(String map, String key) {
		return properties.get(map).containsKey(key);
	}
	
	public static void createMap(String name) {
		properties.put(name, new Properties());
	}
	
	public static void createMap(String name, Properties defaults) {
		Properties p = new Properties();
		p.putAll(defaults);
		properties.put(name, p);
	}
	
	public static void removeMap(String name) {
		properties.remove(name);
	}
	
	public static Properties getMap(String name) {
		return properties.get(name);
	}
	
	public static SortedMap getSortedMap(String name) {
		SortedMap result = new TreeMap();
		result.putAll(properties.get(name));
		return result;
	}
	
	public static String get(String propertiesName, String key) {
		return properties.get(propertiesName).getProperty(key);
	}
	
	public static boolean remove(String map, String key) {
		return properties.get(map).remove(key) != null;
	}
	
	public static void rename(String mapName, String newName) {
		Properties p = properties.remove(mapName);
		properties.put(newName, p);
	}
	
	public static void set(String table, String key, String value) {
		properties.get(table).setProperty(key, value);		
	}
	
	public static void setMultiple(String table, Map<String, String> entries) {
		properties.get(table).putAll(entries);
	}
	
	public static boolean load(String name) {
		return load(generateFileName(name), name);
	}
	
	public static boolean load(String fileName, String name) {
		Properties result = null;
		if (properties.containsKey(name)) {
			result = properties.get(name);
		}
		else {
			result = new Properties();
		}
		G.DIR_CONFIG.mkdirs();
		File file = new File(G.DIR_CONFIG, fileName);
		BufferedReader reader = null;
		try {
			if (file.exists() || file.createNewFile()) {
				reader = new BufferedReader(new FileReader(file));
				result.load(reader);
			}			
		} catch (IOException e) {
			G.LOG.severe("Could not open/create config file: " + file.getAbsolutePath());
			e.printStackTrace();
			return false;
		}
		finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace(); // shit hit the fan?
					return false;
				}
		}
		properties.put(name, result);
		return true;
	}
	
	public static boolean save(String name) {
		return save(generateFileName(name), name);
	}
	
	public static boolean save(String fileName, String table) {
		BufferedWriter writer = null;
		G.DIR_CONFIG.mkdirs();
		File file = new File(G.DIR_CONFIG, fileName);
		try {			
			writer = new BufferedWriter(new FileWriter(file));			
			properties.get(table).store(writer, table);
		} 
		catch (IOException e) {
			G.LOG.severe("Could not save config file " + file.getAbsolutePath());
			e.printStackTrace();
			return false;
		}
		finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
		}
		return true;
	}
	
	public static String generateFileName(String mapName) {
		return mapName + G.EXT_CONFIG;
	}
	
	public static boolean autosave(String name) {
		return autosave(generateFileName(name), name);
	}
	

	
	public static boolean autosave(String fileName, String table) {
		if (StringTools.getBoolean(properties.get(table).getProperty(G.PROP_AUTOSAVE, "on"))) {
			return save(fileName, table);
		}
		else {
			return true; // only return false if autosave is enabled and errors occured.
		}
	}
}