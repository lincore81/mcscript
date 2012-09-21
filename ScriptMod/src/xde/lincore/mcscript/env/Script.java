package xde.lincore.mcscript.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.security.auth.callback.UnsupportedCallbackException;

import net.minecraft.src.EntityPlayer;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.edit.IEditSession;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;
import xde.lincore.util.Config;
import xde.lincore.util.Text;

public final class Script {
	
	public static final File NO_FILE = null;
	public static final String NO_SOURCE = null;

	private String source;
	
	private CompiledScript binary = null;

	private File file = null;
	
	private ScriptEngine engine;
	
	private Object returnValue = null;
	
	private ScriptRunner runner;
	
	private MinecraftWrapper mcWrapper;
	
	private IEditSession editSession;
		
	private ScriptArguments arguments;
	
	private ScriptGlobals globals;
	
	private ScriptEnvironment env;
	
	
	public Script(String source, File file, ScriptEngine engine, ScriptArguments arguments, 
			ScriptGlobals globals, MinecraftWrapper mcWrapper, ScriptEnvironment env) {
		this.source 	= source;
		this.file 		= file;
		this.engine 	= engine;
		this.arguments 	= arguments;
		this.mcWrapper  = mcWrapper;
		this.globals 	= globals;
		this.env		= env;
	}
	
	public boolean compile() throws ScriptException {
		if (isCompilable()) {		
			Compilable cengine = (Compilable)engine;
			binary = cengine.compile(source);
			return true;
		} else {
			return false;
		}
	}
		
	public void eval() throws ScriptException {
		EditSessionController editSessionController = env.getEditSessionController();
		editSession = editSessionController.checkOut(getEditorString(), mcWrapper.world);
		
		engine.put("mc",	  mcWrapper);
		engine.put("args", 	  arguments);
		engine.put("edit", 	  editSession);
		engine.put("globals", globals);		
		
		if (isCompiled()) {
			returnValue = binary.eval();
		} else {
			returnValue = engine.eval(source);
		}
		editSessionController.checkIn(editSession);
	}
	
	public ScriptArguments getArguments() {
		return arguments;
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

	public MinecraftWrapper getMinecraftWrapper() {
		return mcWrapper;
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

	/**
	 * Load the text file containing the script source at the given location.
	 * All common line endings are detected automatically. The charset to use is
	 * set in the configuration (config/main.cfg or /env config), the default is UTF-8.
	 *  
	 * @param 	file The location of the file to load.
	 * 
	 * @throws 	IOException Will throw {@link FileNotFoundException} if no such file exists or 
	 * 			IOException if other io-related errors happen. 
	 * 			Can also throw {@link IllegalArgumentException} if the charset name in the
	 * 			configuration is illegal ({@link IllegalCharsetNameException}) 
	 * 			or denotes an unsupported charset ({@link UnsupportedCharsetException}).
	 */
	public void load(File file) throws FileNotFoundException, IOException, IllegalArgumentException {
		Text fileContents = new Text();
		String charset = Config.get(G.CFG_MAIN, G.PROP_ENCODING);
		fileContents.readFile(file, charset);
		source = fileContents.toString();
		this.file = file;
	}
	
	public void load() throws FileNotFoundException, IOException, IllegalArgumentException {
		load(file);
	}

	public void setArguments(ScriptArguments arguments) {
		this.arguments = arguments;
	}

	public void setEngine(ScriptEngine engine) {
		this.engine = engine;
	}

//	public void setReturnValue(Object obj) {
//		this.returnValue = obj;
//	}

	public void setSource(String source) {
		this.source = source;		
	}

	protected void setScriptRunner(ScriptRunner runner) {
		this.runner = runner;
	}

//	public void setMinecraftWrapper(MinecraftWrapper mcWrapper) {
//		this.mcWrapper = mcWrapper;
//	}
//
//	public void setEditSession(IEditSession editSession) {
//		this.editSession = editSession;
//	}
	
	private String getEditorString() {
		if (isScriptFile()) {
			return getFilename();
		}
		else if (source.length() > 40) {
			return source.substring(0, 38).replace("\n|\t", " ") + "...";
		}
		else {
			return source.replace("\n|\t", " ");
		}
	}
}
