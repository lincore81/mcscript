package xde.lincore.mcscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.exception.BadUserInput;
import xde.lincore.mcscript.wrapper.MinecraftWrapper;
import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommand;
import net.minecraft.src.ModLoader;
import net.minecraft.src.PlayerNotFoundException;
import net.minecraft.src.mod_McScript;

public final class ScriptingEnvironment {

	private static ScriptingEnvironment instance;
	private static EntityPlayer currentUser;
	
	public final AliasController aliases;
	public final FileController files;
	public final KeyController keys;
	
	private EditSessionController editSessionController;
	
	private ScriptEngineManager manager;
	
	private ScriptEngine currentEngine = null;
	private ScriptEngine defaultEngine;
	//private ExecutorService executor;
	private MinecraftWrapper mc;
	public final mod_McScript modInst;
	
	private ScriptGlobals globals;
	private Exception lastScriptException;
	private boolean isIngame;
	
	public static ScriptingEnvironment createInstance(mod_McScript modInst) {
		if (instance != null) {
			throw new IllegalStateException("Instance already exists!");
		}
		else {
			instance = new ScriptingEnvironment(modInst);
		}		
		return instance;
	}
	
	public static ScriptingEnvironment getInstance() {
		return instance;
	}
	
	private ScriptingEnvironment(mod_McScript modInst) {
		this.modInst 	= modInst;
		manager 		= new ScriptEngineManager();
		defaultEngine 	= manager.getEngineByName(G.DEFAULT_SCRIPT_ENGINE);
		mc 				= MinecraftWrapper.createInstance(this);
		editSessionController = new EditSessionController(mc);
		//executor = Executors.newCachedThreadPool();
		
		if (defaultEngine == null) {
			throw new RuntimeException("Could not find the default scripting engine \"" +
					G.DEFAULT_SCRIPT_ENGINE + "\".");
		}
		
		files	= new FileController(this);
		aliases = new AliasController(this);
		keys = new KeyController(this);
		globals = new ScriptGlobals();
	}
	
	
	public static MinecraftServer getServer() {
		return MinecraftServer.getServer();
	}
	
	public static EntityPlayer getUser() {
		if (currentUser == null) {
			return Minecraft.getMinecraft().thePlayer;
		}
		return currentUser;
	}
	
	public EditSessionController getEditSessionController() {
		return editSessionController;
	}
	
	
	public void doFile(String filename, String engineName, Map<String, String> args, String userName) {
		currentUser = getScriptUser(userName);
		File scriptfile = new File(G.DIR_SCRIPTS, filename);
		String script = files.readScriptFile(scriptfile);
		if (script == null) return;
		ScriptEngine engine = null;
		if (engineName != null) {
			engine = findEngine(engineName);
		}
		else {
			String extension = files.getFileExtension(filename);
			if (extension != null) {
				engine = manager.getEngineByExtension(extension);
			}
			if (engine == null) {
				mc.err("Could not determine which engine to use by file extension. Please specify the " +
						"engine you want to use.");
			}
		}		
		
		Runnable runner = new ScriptRunner(engine, script, filename, args, this);		
		runner.run();
		//executor.execute(runner);
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
		Runnable runner = new ScriptRunner(engine, script, null, null, this);
		runner.run();
		//executor.execute(runner);
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
							mc.echo(engineName + " is ambiguous. Please use a distinct identifier " +
									"for the engine you would like to use.");
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

	public ScriptEngine getCurrentEngine() {
		return (currentEngine != null)? currentEngine : defaultEngine;
	}
	
	
	
	public ScriptEngine getDefaultEngine() {
		return defaultEngine;
	}
	
	
	
	public ScriptGlobals getGlobals() {
		return globals;
	}
	
	public Exception getLastScriptException() {
		return lastScriptException;
	}	
	
	
	public MinecraftWrapper getMc() {
		return mc;
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
	
	public void setLastException(Exception lastException) {
		this.lastScriptException = lastException;
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


	public ScriptEngineManager getManager() {
		return manager;
	}


	public void onClientConnect() {
		isIngame = true;
		aliases.loadAliases();
	}


	public void onClientDisconnect() {
		isIngame = false;
	}
	
	
	public void update(float tick) {
		if (!isIngame) return;
		keys.update();
		editSessionController.update();
	}
}
