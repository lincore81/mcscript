package xde.lincore.mcscript.minecraft;

import java.io.File;
import java.io.IOException;

import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.G;
import xde.lincore.mcscript.Items;
import xde.lincore.mcscript.env.Script;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.util.Config;
import xde.lincore.util.Text;

public class MinecraftWrapper {
	public final ScriptEnvironment env;
	public final TimeWrapper time;
	public final UserWrapper user;
	public final WorldWrapper world;

	public MinecraftWrapper(final ScriptEnvironment env) {
		this.env = env;
		user 	= new UserWrapper(env, this);
		time 	= new TimeWrapper(env, this);
		world 	= new WorldWrapper(env, this);
	}

	public void abort(final String reason) {
		throw new ScriptError(reason);
	}

	public void echo(final Object obj) {
		env.chat.echo(obj);
	}

	public void err(final Object obj) {
		env.chat.err(obj);
	}

	public void format(final String format, final Object... args) {
		env.chat.format(format, args);
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

	public Blocks block(final String name) {
		return Blocks.find(name);
	}

	public Blocks block(final int id) {
		return Blocks.findById(id);
	}

	public Blocks block(final int id, final int meta) {
		return Blocks.findById(id, meta);
	}

	public Items item(final String name) {
		return Items.find(name);
	}

	public Items item(final int id) {
		return Items.findById(id, 0);
	}

	public Items item(final int id, final int meta) {
		return Items.findById(id, meta);
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
