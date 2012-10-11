package xde.lincore.mcscript.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.edit.IEditSession;
import xde.lincore.mcscript.spi.ExplicitScriptExit;
import xde.lincore.mcscript.spi.McScriptContext;
import xde.lincore.util.Config;
import xde.lincore.util.Text;

public final class Script {

	public static final File			NO_FILE		= null;
	public static final String			NO_SOURCE	= null;

	private String						source;

	private CompiledScript				binary		= null;

	private File						file		= null;

	private ScriptEngine				engine;

	private Object						returnValue	= null;

	private ScriptRunner				runner;

	private final McScriptContext		scriptContext;

	private IEditSession				editSession;

	private ScriptArguments				arguments;

	private final ScriptGlobals			globals;

	private final EditSessionController	editController;

	private Bindings					bindings;

	private long						threadId;

	public Script(final String source, final File file, final ScriptEngine engine,
			final ScriptArguments arguments, final ScriptGlobals globals,
			final McScriptContext scriptContext, final EditSessionController editController) {
		this.source = source;
		this.file = file;
		this.engine = engine;
		this.arguments = arguments;
		this.scriptContext = scriptContext;
		this.globals = globals;
		this.editController = editController;
	}

	public boolean compile() throws ScriptException {
		if (isCompilable()) {
			final Compilable cengine = (Compilable) engine;
			binary = cengine.compile(source);
			return true;
		} else {
			return false;
		}
	}

	public String dumpBindings() {
		final StringBuffer buffer = new StringBuffer();
		for (final Map.Entry<String, Object> e : bindings.entrySet()) {
			buffer.append(e.getKey() + ": " + e.getValue().toString() + "\n");
		}
		return buffer.toString();
	}

	public void eval() throws ScriptException {
		editSession = editController.checkOut(getShortDescription(), scriptContext.getWorld());
		if (bindings == null) {
			bindings = engine.getBindings(ScriptContext.GLOBAL_SCOPE);
		}
		bindings.put("mc", scriptContext);
		bindings.put("args", arguments);
		bindings.put("edit", editSession);
		bindings.put("globals", globals);
		try {
			if (isCompiled()) {
				returnValue = binary.eval(bindings);
			} else {
				returnValue = engine.eval(source, bindings);
			}
		} catch (ExplicitScriptExit e) {
			returnValue = e.getExitValue();
		}
		editController.checkIn(editSession);
	}

	public ScriptArguments getArguments() {
		return arguments;
	}

	public Bindings getBindings() {
		return bindings;
	}

	public IEditSession getEditSession() {
		return editSession;
	}

	public ScriptEngine getEngine() {
		return engine;
	}

	public File getFile() {
		return file;
	}

	public String getFilename() {
		return file.getName();
	}

	public McScriptContext getScriptContext() {
		return scriptContext;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public String getSource() {
		return source;
	}

	public boolean hasReturnValue() {
		return returnValue != null;
	}

	public boolean isCompilable() {
		return engine instanceof Compilable;
	}

	public boolean isCompiled() {
		return binary != null;
	}

	public boolean isScriptFile() {
		return file != null;
	}

	public void load() throws FileNotFoundException, IOException, IllegalArgumentException {
		load(file);
	}

	/**
	 * Load the text file containing the script source at the given location.
	 * All common line endings are detected automatically. The charset to use is
	 * set in the configuration (config/main.cfg or /env config), the default is
	 * UTF-8.
	 * 
	 * @param file
	 *            The location of the file to load.
	 * 
	 * @throws IOException
	 *             Will throw {@link FileNotFoundException} if no such file
	 *             exists or IOException if other io-related errors happen. Can
	 *             also throw {@link IllegalArgumentException} if the charset
	 *             name in the configuration is illegal (
	 *             {@link IllegalCharsetNameException}) or denotes an
	 *             unsupported charset ({@link UnsupportedCharsetException}).
	 */
	public void load(final File file) throws FileNotFoundException, IOException,
			IllegalArgumentException {
		final Text fileContents = new Text();
		final String charset = Config.get(G.CFG_MAIN, G.PROP_ENCODING);
		fileContents.readFile(file, charset);
		source = fileContents.toString();
		this.file = file;
	}

	// public void setReturnValue(Object obj) {
	// this.returnValue = obj;
	// }

	public void setArguments(final ScriptArguments arguments) {
		this.arguments = arguments;
	}

	public void setBindings(final Bindings bindings) {
		this.bindings = bindings;
	}

	// public void setMinecraftWrapper(MinecraftWrapper mcWrapper) {
	// this.mcWrapper = mcWrapper;
	// }
	//
	// public void setEditSession(IEditSession editSession) {
	// this.editSession = editSession;
	// }

	public void setEngine(final ScriptEngine engine) {
		this.engine = engine;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	protected void setScriptRunner(final ScriptRunner runner) {
		this.runner = runner;
	}

	public String getShortDescription() {
		if (isScriptFile()) {
			return getFilename();
		} else if (source.length() > 40) {
			return source.substring(0, 38).replace("\n|\t", " ") + "...";
		} else {
			return source.replace("\n|\t", " ");
		}
	}

	public void setThreadId(final long id) {
		this.threadId = id;
	}

	public long getThreadId() {
		return threadId;
	}
	
	@Override
	public String toString() {
		return getShortDescription();
	}

}
