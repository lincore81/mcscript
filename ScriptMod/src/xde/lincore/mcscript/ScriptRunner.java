package xde.lincore.mcscript;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.mozilla.javascript.WrappedException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.mod_Script;

public class ScriptRunner implements Runnable {

	private String script;
	private ScriptEngine engine;
	private BindingsMinecraft mc;
	private ScriptingEnvironment env;
	private ScriptGlobals globals;
	
	public Object result;
	private ScriptArguments args;
	
	public ScriptRunner(ScriptEngine engine, String script, Map<String, String> args, 
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
		engine.put("mc", mc);		
		engine.put("args", args);
		engine.put("globals", env.getGlobals());
		
		
		if (engine instanceof Compilable && script.length() > 30) {
			Compilable cengine = (Compilable)engine;
			try {
				CompiledScript cscript = cengine.compile(script);
				mc.echo("§oScript compiled...");
				cscript.eval();
//			} catch (WrappedException e) {
			} catch (Exception e) {
				mc.echo("§6" + e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			try {
				result = engine.eval(script);
			} catch (Exception e) {
				mc.echo("§6" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
