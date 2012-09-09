package xde.lincore.mcscript;

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
import xde.lincore.mcscript.wrapper.MinecraftWrapper;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.mod_McScript;

public class ScriptRunner implements Runnable {

	private String script;
	private ScriptEngine engine;
	private MinecraftWrapper mc;
	private ScriptingEnvironment env;
	private ScriptGlobals globals;
	private String filename;
	
	public Object result;
	private ScriptArguments args;
	
	public ScriptRunner(ScriptEngine engine, String script, String filename, Map<String, String> args, 
			ScriptingEnvironment env) {
		this.script = script;
		this.env = env;
		this.engine = engine;
		this.args = new ScriptArguments(args);		
		mc = env.getMc();
	}
	
	@Override
	public void run() {	
		EntityPlayer currentUser = env.getUser();
		IEditSession session = env.getEditSessionController().checkOut();
		engine.put("mc", mc);
		engine.put("args", args);
		engine.put("globals", env.getGlobals());
		engine.put("edit", session);
		
		try {
			if (engine instanceof Compilable && filename != null) {
				Compilable cengine = (Compilable)engine;
				CompiledScript cscript = cengine.compile(script);
				mc.echo("§oScript compiled...");
				result = cscript.eval();
				
			}
			else {				
				result = engine.eval(script);				
			}
			env.getEditSessionController().checkIn(session);
		} catch (Exception e) {
			String[] msg = e.getMessage().split(":", 2);
			if (msg.length == 2) {
				mc.echo("§e" + msg[1] + " §f(" + msg[0] + ")");
			} else {
				mc.echo("§e" + e.getMessage());
			}
			env.setLastException(e);
			e.printStackTrace();
			if (e.getCause() != null) {
				e.getCause().printStackTrace();
			}
		}
	}
}
