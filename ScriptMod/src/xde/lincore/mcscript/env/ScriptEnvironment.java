package xde.lincore.mcscript.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_McScript;
import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;

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

	private final EditSessionController editSessionController;

	//private ExecutorService executor;
	public final mod_McScript modInst;

	private final ScriptGlobals globals;
	private Exception lastScriptException;
	private boolean isIngame;

	public static ScriptEnvironment createInstance(final mod_McScript modInst) {
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

	private ScriptEnvironment(final mod_McScript modInst) {
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

	public void setCuiBindings(final Bindings bindings) {
		cuiBindings = bindings;
	}

	public void setFileBindings(final Bindings bindings) {
		fileBindings = bindings;
	}

	public EditSessionController getEditSessionController() {
		return editSessionController;
	}



	public void runScriptFile(final String fileName, final String engineName, final Map<String, String> args)
			throws FileNotFoundException, IOException {
		final ScriptEngine engine = engines.getEngine(engineName);

		final File file = files.resolvePath(fileName);
		files.assertIsValidFile(file);

		final Script script = new Script(Script.NO_SOURCE, file, engine, new ScriptArguments(args),
				globals, mcWrapper, editSessionController);
		script.load();
		final ScriptRunner runner = new ScriptRunner(script, this);
		runner.run();
		//executor.execute(runner);
	}

	public void runScript(final String source, final String engineName, final Map<String, String> args) {
		final ScriptEngine engine = engines.getEngine(engineName);
		final Script script = new Script(source, Script.NO_FILE, engine, new ScriptArguments(args),
				globals, mcWrapper, editSessionController);
		final ScriptRunner runner = new ScriptRunner(script, this);
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

	public void setLastScriptException(final Exception lastException) {
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


	public void onTick() {
		if (!isIngame) {
			return;
		}
		keys.update();
		editSessionController.update();
	}
}
