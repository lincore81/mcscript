package xde.lincore.mcscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.python.google.common.io.Files;

import xde.lincore.util.Config;
import xde.lincore.util.Text;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommand;
import net.minecraft.src.ModLoader;
import net.minecraft.src.PlayerNotFoundException;
import net.minecraft.src.mod_Script;

public final class ScriptingEnvironment {

	public static final String DEFAULT_SCRIPT_ENGINE = "js";
	public static final String FILE_ALIASES_CFG = "alias.cfg";
	public static final File SCRIPTS_DIR = new File(mod_Script.MOD_DIR, "scripts/");
	public static final File CACHE_DIR = new File(mod_Script.MOD_DIR, "cache/");
	
	private ScriptEngineManager manager;
	private static EntityPlayer currentUser;
	private ScriptEngine currentEngine = null;
	private ScriptEngine defaultEngine;
	//private ExecutorService executor;
	private BindingsMinecraft mc;
	private mod_Script modInst;
	
	public AliasController aliases;
	private ScriptGlobals globals;
	
	public ScriptingEnvironment(mod_Script modInst) {
		this.modInst = modInst;
		manager = new ScriptEngineManager();
		defaultEngine = manager.getEngineByName(DEFAULT_SCRIPT_ENGINE);
		mc = new BindingsMinecraft(this);
		//executor = Executors.newCachedThreadPool();
		
		if (defaultEngine == null) {
			throw new IllegalArgumentException("Could not find the default scripting engine \"" +
					DEFAULT_SCRIPT_ENGINE + "\".");
		}
		
		aliases = new AliasController(mc, this);
		globals = new ScriptGlobals();
	}
	
	
	public BindingsMinecraft getMc() {
		return mc;
	}
	
	
	public static EntityPlayer getUser() {
		if (currentUser == null) {
			return Minecraft.getMinecraft().thePlayer;
		}
		return currentUser;
	}


	
	
	public ScriptEngine getCurrentEngine() {
		return (currentEngine != null)? currentEngine : defaultEngine;
	}
	
	public ScriptEngine getDefaultEngine() {
		return defaultEngine;
	}

	public EntityPlayer getScriptUser(String userName) {
	    {
	        EntityPlayerMP result = MinecraftServer.getServer().getConfigurationManager().
	        		getPlayerForUsername(userName);
	        if (result != null) {
	            return result;
	        }
	        else {
	        	throw new PlayerNotFoundException();
	        }
	    }
	}
	
	public void eval(String script, String engineName, String userName) {
		currentUser = getScriptUser(userName);
		ScriptEngine engine;
		if (engineName != null) {
			engine = findEngine(engineName);
		}
		else {
			engine = getCurrentEngine();
		}
		if (engine == null) return;
		Runnable runner = new ScriptRunner(engine, script, null, this);
		runner.run();
		//executor.execute(runner);
	}
	
	public void doFile(String filename, String engineName, Map<String, String> args, String userName) {
		currentUser = getScriptUser(userName);
		File scriptfile = new File(SCRIPTS_DIR, filename);
		String script = readScriptFile(scriptfile);
		if (script == null) return;
		ScriptEngine engine = null;
		if (engineName != null) {
			engine = findEngine(engineName);
		}
		else {
			String extension = getFileExtension(filename);
			if (extension != null) {
				engine = manager.getEngineByExtension(extension);
			}
			if (engine == null) {
				mc.echo("§6Could not determine which engine to use by file extension. Please specify the " +
						"engine you want to use.");
			}
		}		
		
		Runnable runner = new ScriptRunner(engine, script, args, this);		
		runner.run();
		//executor.execute(runner);
	}
	
	
	
	public String readScriptFile(File scriptfile) {
		Text result = new Text();
		String charset = Config.get(Config.CFG_MAIN, Globals.PROP_ENCODING);
		try {
			result.readFile(scriptfile, charset);
		} catch (FileNotFoundException e) {
			mc.echo("§6File not found: " + scriptfile.toString());
			return null;
		} catch (IOException e) {
			mc.echo("§6An error occured while trying to access the file " + scriptfile.toString());
			mc.echo(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			Config.remove(Config.CFG_MAIN, Globals.PROP_ENCODING);
			throw new BadUserInput("§6Invalid or unsupported file encoding: \"" + 
					charset + "\". " + Globals.PROP_ENCODING + 
					" has been reset to " + Text.DEFAULT_CHARSET.name());
		}
		return result.toString();
	}


	public boolean stopThread(int id) {
		ThreadGroup grp = Thread.currentThread().getThreadGroup();
		int threadCount = grp.activeCount();
		Thread[] threads = new Thread[threadCount];
		grp.enumerate(threads);		
		for (int i = 0; i < threadCount; i++) {			
			if (threads[i].getId() == id) {
				threads[i].stop();
				return true;
			}
		}
		return false;
	}
	
	public ScriptEngine findEngine(String engineName) {
		ScriptEngine result = manager.getEngineByName(engineName);
		if (result != null) {
			return result;
		}
		else {
			for (ScriptEngineFactory f: manager.getEngineFactories()) {			
				ArrayList<String> names = new ArrayList<String>();
				names.add(f.getEngineName());
				names.addAll(f.getNames());
				for (String n: names) {
					if (n.toLowerCase().startsWith(engineName.toLowerCase())) {
						if (result == null) {
							result = f.getScriptEngine();
							break; // check other engine names for ambiguouty
						}
						else {
							mc.echo(engineName + " is ambiguous. Please use a distinct identifier for the " +
									"engine you would like to use.");
							return null;
						}
					}
				}
			}
			if (result == null) {
				mc.echo(engineName + " does not denote an available script engine. Type /env get engines " +
						"for a list of available engines.");
			}
			return result;
		}
	}
	
	protected ScriptEngineManager getManager() {
		return manager;
	}
	
	public String getFileExtension(String fileName) {
		if (fileName.length() == 0 || fileName.endsWith(".")) return null; // there's no extension to get
		
		for (int i = fileName.length() - 1; i >= 0; i--) { // look for a '.' and return everything after it.
			if (fileName.charAt(i) == '.') {
				return fileName.substring(i + 1);
			}
		}
		
		return null; // no extension found
	}
	
	public void onClientConnect() {
		aliases.loadAliases();
	}


	public ScriptGlobals getGlobals() {
		return globals;
	}
}
