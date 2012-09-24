package xde.lincore.mcscript.env;

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

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.python.google.common.io.Files;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;
import xde.lincore.mcscript.ui.BadUserInputException;
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

public final class ScriptEnvironment {

	private static ScriptEnvironment instance;
	private EntityPlayer currentUser;
	private MinecraftWrapper mcWrapper;
	
	private Bindings cuiBindings;
	private Bindings fileBindings;
	
	public final AliasController aliases;
	public final FileController files;
	public final KeyController keys;
	public final ChatIoController chat;
	public final IScriptController scripts;
	public final EngineController engines;
	
	private EditSessionController editSessionController;
	
	//private ExecutorService executor;	
	public final mod_McScript modInst;
	
	private ScriptGlobals globals;
	private Exception lastScriptException;
	private boolean isIngame;
	
	public static ScriptEnvironment createInstance(mod_McScript modInst) {
		if (instance != null) {
			throw new IllegalStateException("Instance already exists!");
		}
		else {
			instance = new ScriptEnvironment(modInst);
		}		
		return instance;
	}
	
	public static ScriptEnvironment getInstance() {
		return instance;
	}
	
	private ScriptEnvironment(mod_McScript modInst) {
		this.modInst 	= modInst;		
		editSessionController = new EditSessionController(this);
		
		//executor = Executors.newCachedThreadPool();
				
		scripts = new ThreadedScriptController(this);		
		chat 	= new ChatIoController(this);
		files	= new FileController(this);
		aliases = new AliasController(this);
		keys 	= new KeyController(this);
		globals = new ScriptGlobals();		
		engines = new EngineController();
	}
	
	
	public MinecraftServer getServer() {
		return MinecraftServer.getServer();
	}
	
	public EntityPlayer getUser() {
		return currentUser;
	}
	
	public Bindings getCuiBindings() {
		return cuiBindings;
	}
	
	public Bindings getFileBindings() {
		return fileBindings;
	}
	
	public void setCuiBindings(Bindings bindings) {
		cuiBindings = bindings;
	}
	
	public void setFileBindings(Bindings bindings) {
		fileBindings = bindings;
	}
	
	public EditSessionController getEditSessionController() {
		return editSessionController;
	}
	
	
	
	public void runScriptFile(String fileName, String engineName, Map<String, String> args) 
			throws FileNotFoundException, IOException {
		ScriptEngine engine = engines.getEngine(engineName);
		
		File file = files.resolvePath(fileName);
		files.assertIsValidFile(file);
		
		Script script = new Script(Script.NO_SOURCE, file, engine, new ScriptArguments(args), 
				globals, mcWrapper, editSessionController);
		script.load();
		ScriptRunner runner = new ScriptRunner(script, this);
		runner.run();		
		//executor.execute(runner);
	}
	
	public void runScript(String source, String engineName, Map<String, String> args) {		 
		ScriptEngine engine = engines.getEngine(engineName);		
		Script script = new Script(source, Script.NO_FILE, engine, new ScriptArguments(args), 
				globals, mcWrapper, editSessionController);
		ScriptRunner runner = new ScriptRunner(script, this);		
		runner.run();		
		//executor.execute(runner);
	}
	

	
	
	
	public ScriptGlobals getGlobals() {
		return globals;
	}
	
	public Exception getLastScriptException() {
		return lastScriptException;
	}	
	

//	public EntityPlayer getScriptUser(String userName) {
//	    {
//	        EntityPlayerMP result = MinecraftServer.getServer().getConfigurationManager().
//	        		getPlayerForUsername(userName);
//	        if (result != null) {
//	            return result;
//	        }
//	        else {
//	        	throw new PlayerNotFoundException();
//	        }
//	    }
//	}
	
	public void setLastScriptException(Exception lastException) {
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



	public void onClientConnect() {
		isIngame = true;
		aliases.loadAliases();
		currentUser = Minecraft.getMinecraft().thePlayer;
		mcWrapper = new MinecraftWrapper(this);
	}


	public void onClientDisconnect() {
		isIngame = false;
		currentUser = null;
		mcWrapper = null;
	}
	
	
	public void update(float tick) {
		if (!isIngame) return;
		keys.update();
		editSessionController.update();
	}
}
