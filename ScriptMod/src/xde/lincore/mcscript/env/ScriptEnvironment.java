package xde.lincore.mcscript.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.minecraft.Commands;
import xde.lincore.mcscript.minecraft.ILocalChatWriter;
import xde.lincore.mcscript.minecraft.LocalChatFacade;
import xde.lincore.mcscript.minecraft.PlayerFacade;
import xde.lincore.mcscript.minecraft.ServerFacade;
import xde.lincore.mcscript.spi.McScriptContext;
import xde.lincore.mcscript.ui.McChatLogHandler;
import xde.lincore.util.Config;

public final class ScriptEnvironment {

	private static ScriptEnvironment instance;
	private McScriptContext scriptContext;

	private Bindings cuiBindings;
	private Bindings fileBindings;

	public final AliasController aliases;
	public final FileController files;
	public final KeyController keys;
	public final ILocalChatWriter chat;
	public final IScriptController scripts;
	public final EngineController engines;
	public final Commands commands;
	public final WandController wands;

	private final EditSessionController editSessionController;

	private ExecutorService executor;

	private final ScriptGlobals globals;
	private Throwable lastScriptException;
	private boolean isIngame;
	private final boolean multithreading;
	private PlayerFacade player;

	public static ScriptEnvironment createInstance() {
		if (instance != null) {
			throw new IllegalStateException("Instance already exists!");
		}
		else {
			instance = new ScriptEnvironment();
		}
		return instance;
	}

	public static ScriptEnvironment getInstance() {
		return instance;
	}

	private ScriptEnvironment() {
		if (G.DEBUG) G.LOG.setLevel(Level.ALL);
		G.LOG.addHandler(new McChatLogHandler(this));
		G.DIR_MOD.mkdirs();
		G.DIR_SCRIPTS.mkdir();
		G.DIR_CACHE.mkdir();
		Config.createMap(G.CFG_MAIN, G.defaultProperties);
		Config.load(G.CFG_MAIN);
		multithreading = Config.getBoolean(G.CFG_MAIN, G.PROP_MULTI_THREADING);
		if (multithreading) {
			executor = Executors.newCachedThreadPool();
		}
		
		commands = new Commands(this);
		editSessionController = new EditSessionController(this);
		scripts = new ThreadedScriptController(this);
		chat 	= new LocalChatFacade();
		files	= new FileController(this);
		aliases = new AliasController(this);
		keys 	= new KeyController(this);
		wands 	= new WandController(this);
		globals = new ScriptGlobals();
		engines = new EngineController();
		 		
		aliases.loadAliases();
	}


	

	public Bindings getCuiBindings() {
		return cuiBindings;
	}

	public Bindings getFileBindings() {
		return fileBindings;
	}

	public void setCuiBindings(final Bindings bindings) {
		cuiBindings = bindings;
	}

	public void setFileBindings(final Bindings bindings) {
		fileBindings = bindings;
	}

	public EditSessionController getEditSessionController() {
		return editSessionController;
	}
	
	public McScriptContext getScriptContext() {
		return scriptContext;
	}

	public void runScriptFile(final String fileName, final String engineName, final Map<String, String> args)
			throws FileNotFoundException, IOException {
		final ScriptEngine engine = engines.getEngine(engineName);
		final File file = files.resolvePath(fileName);
		files.assertIsValidFile(file);
		final Script script = new Script(Script.NO_SOURCE, file, engine,
				new ScriptArguments(args), globals, scriptContext, editSessionController);
		script.load();
		startScriptRunner(script);
	}

	public void runScript(final String source, final String engineName, final Map<String, String> args) {
		final ScriptEngine engine = engines.getEngine(engineName);
		final Script script = new Script(source, Script.NO_FILE, engine, new ScriptArguments(args),
				globals, scriptContext, editSessionController);
		startScriptRunner(script);
	}

	private void startScriptRunner(final Script script) {
		final ScriptRunner runner = new ScriptRunner(script, this);
		if (multithreading) {
			executor.execute(runner);
		} else {
			runner.run();
		}
	}

	public boolean isMultithreaded() {
		return multithreading;
	}

	public ScriptGlobals getGlobals() {
		return globals;
	}

	public Throwable getLastScriptException() {
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

	public void setLastScriptException(final Throwable lastException) {
		lastScriptException = lastException;
	}

	public boolean stopThread(final int id) {
		final ThreadGroup grp = Thread.currentThread().getThreadGroup();
		final int threadCount = grp.activeCount();
		final Thread[] threads = new Thread[threadCount];
		grp.enumerate(threads);
		for (int i = 0; i < threadCount; i++) {
			if (threads[i].getId() == id) {
				threads[i].stop();
				return true;
			}
		}
		return false;
	}
	
	public PlayerFacade getPlayer() {
		return player;
	}

	public boolean isConnected() {
		return isIngame;
	}

	public void onConnect() {
		isIngame = true;
		scriptContext = new McScriptContext(this);
		commands.registerCommands(ServerFacade.getCurrentServer());
		aliases.registerAliases();
		player = PlayerFacade.getLocalPlayer();
	}


	public void onDisconnect() {
		isIngame = false;
		commands.unregisterCommands();
		player = null;
		scriptContext = null;
	}


	public void onTick() {
		if (!isIngame) {
			return;
		}
		keys.update();
		editSessionController.update();
	}
}
