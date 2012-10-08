package xde.lincore.mcscript.spi;

import java.io.File;
import java.io.IOException;

import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.IItem;
import xde.lincore.mcscript.Items;
import xde.lincore.mcscript.env.G;
import xde.lincore.mcscript.env.Script;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.minecraft.LocalChatFacade;
import xde.lincore.mcscript.minecraft.PlayerFacade;
import xde.lincore.mcscript.minecraft.ServerFacade;
import xde.lincore.mcscript.minecraft.WorldFacade;
import xde.lincore.util.Config;
import xde.lincore.util.Text;

public class McScriptContext extends LocalChatFacade {
	
	private final ScriptEnvironment env;
	
	public final PlayerFacade 	player;
	public final ServerFacade 	server;
	//public final LocalChatFacade chat;
	
	public McScriptContext(final ScriptEnvironment env) {
		this.env = env;
		this.server = ServerFacade.getCurrentServer();
		this.player = PlayerFacade.getLocalPlayer();
		//this.chat = new LocalChatFacade();
	}
	
	public WorldFacade getWorld() {
		return server.getPlayerWorld(player);
	}
	
	
	public void abort(final String reason) {
		throw new ScriptError(reason);
	}
	
	public void exit(final Object value) {
		throw new ScriptEnd(value);
	}
	
	public void exit() {
		throw new ScriptEnd();
	}
	
	
	public IBlock block(final String blockString) {
		return Blocks.find(blockString);
	}
	
	public IItem item(final String itemString) {
		return Items.find(itemString);
	}
	
	
	public Exception getLastException() {
		return env.getLastScriptException();
	}
	
	public StackTraceElement[] getStackTrace() {
		final Exception e = env.getLastScriptException();
	
		if (e != null) {
			return e.getStackTrace();
		}
	
		return new StackTraceElement[0];
	}
	
	public void printStackTrace() {
		final StackTraceElement[] trace = getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			env.chat.err(trace[i]);
		}
	}
	
	
	
	public String loadTextFile(final String filename) throws IOException {
		final Script script = env.scripts.getScript();
		if (script == null) {
			throw new IllegalStateException("This method must be called from within a script.");
		}
		File scriptDir;
		if (script.isScriptFile()) {
			scriptDir = script.getFile().getParentFile();
		} else {
			scriptDir = env.files.getCwd();
		}
		final File file = new File(scriptDir, filename);
		final Text textfile = new Text();
		final String charset = Config.get(G.CFG_MAIN, G.PROP_ENCODING);
		textfile.readFile(file, charset);
		return textfile.toString();
	}
	
	public File getFile(final String filename) throws IOException {
		File file = new File(filename);
		if (file.isAbsolute()) return file;
		
		final Script script = env.scripts.getScript();
		
		if (script.isScriptFile()) {
			file = new File(script.getFile().getParentFile(), filename);
		}
		else {
			return new File(env.files.getCwd(), filename);
		}
		return file;
	}
	
	
	public void sleep(final long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}
