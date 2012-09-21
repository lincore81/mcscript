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
		env.scripts.registerScript(script);
		try {
			if (script.isScriptFile()) {
				script.compile();
			}
			script.eval();
		} catch (Exception e) {
			env.setLastScriptException(e);
			printException(e);
		} finally {
			env.scripts.removeScript();
		}
	}

	private void printException(Exception e) {
		String[] msg = e.getMessage().split(":", 2);
		if (msg.length == 2) {
			env.chat.echo("§e" + msg[1] + " §f(" + msg[0] + ")");
		} else {
			env.chat.echo("§e" + e.getMessage());
		}
		env.setLastScriptException(e);
		e.printStackTrace();
		if (e.getCause() != null) {
			e.getCause().printStackTrace();
		}
	}
}
