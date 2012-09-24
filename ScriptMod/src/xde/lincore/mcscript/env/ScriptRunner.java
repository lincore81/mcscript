package xde.lincore.mcscript.env;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.mozilla.javascript.WrappedException;

import xde.lincore.mcscript.edit.IEditSession;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;
import xde.lincore.mcscript.minecraft.ScriptError;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.mod_McScript;

public class ScriptRunner implements Runnable {

	private Script script;
	private ScriptEnvironment env;
	
	public Object result;
	private ScriptArguments args;
	
	public ScriptRunner(Script script, ScriptEnvironment env) {
		this.script = script;
		this.env = env;		
	}
	
	@Override
	public void run() {
		script.setBindings(script.isScriptFile() ? env.getFileBindings() : env.getCuiBindings());
		env.scripts.registerScript(script);
		try {
			if (script.isScriptFile()) {
				script.compile();
			}
			script.eval();
			if (script.isScriptFile()) {
				env.setFileBindings(script.getBindings());
			} else {
				env.setCuiBindings(script.getBindings());
			}
		} catch (ScriptError e) {			
			env.chat.err("Error: " + e.getMessage());
		} catch (Exception e) {
			env.setLastScriptException(e);
			printException(e);
		} finally {
			env.scripts.removeScript();
		}		
	}

	private void printException(Exception e) {
		
		String msg = e.getMessage();		
		if (msg == null) {
			env.chat.err(e.toString());
		}
		else {
			String exceptionName = null;
			if (msg.contains(":")) {
				String[] msgparts = e.getMessage().split(":", 2);
				msg = msgparts[0];
				exceptionName = msgparts[1];
			}
			if (exceptionName != null) {
				env.chat.err(msg + " §f(" + exceptionName + ")");
			} else {
				env.chat.err(msg);
			}
		}
		env.setLastScriptException(e);
		e.printStackTrace();
		if (e.getCause() != null) {
			e.getCause().printStackTrace();
		}
	}
}
