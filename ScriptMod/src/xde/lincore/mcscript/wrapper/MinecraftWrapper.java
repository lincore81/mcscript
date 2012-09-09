package xde.lincore.mcscript.wrapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.Items;
import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.edit.Turtle;
import xde.lincore.mcscript.edit.VectorTurtle;
import xde.lincore.mcscript.geom.Vector3d;
import xde.lincore.mcscript.geom.Voxel;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Vec3;
import net.minecraft.src.mod_McScript;

public class MinecraftWrapper extends WrapperBase {
	
	public final TimeWrapper time;
	public final UserWrapper user;
	public final WorldWrapper world;
	public final TypesWrapper types;	
	
	private static MinecraftWrapper instance;

	public static MinecraftWrapper createInstance(ScriptingEnvironment env) {
		if (instance != null) {
			throw new IllegalStateException("Instance already exists!");
		}
		else {
			instance = new MinecraftWrapper(env);
		}		
		return instance;
	}
	
	public static MinecraftWrapper getInstance() {
		return instance;
	}
	
	protected static void destroy() {
		instance = null;
	}
	
	private MinecraftWrapper(ScriptingEnvironment env) {
		super(env);
		user = new UserWrapper(env);
		time = new TimeWrapper(env);
		world = new WorldWrapper(env);
		types = new TypesWrapper(env);
	}
	
//	public void echo(Object... obj) {
//		for (Object e: obj) {
//			echo(e);
//		}
//	}
	
	public Exception getLastException() {
		return env.getLastScriptException();
	}
	
	public StackTraceElement[] getStackTrace() {
		Exception e = env.getLastScriptException();
		
		if (e != null) {
			return e.getStackTrace();
		}
		
		return new StackTraceElement[0];
	}
	
	public void printStackTrace() {
		StackTraceElement[] trace = getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			err(trace[i]);
		}
	}
	
	public Blocks block(String name) {
		return Blocks.find(name);
	}
	
	public Blocks block(int id) {
		return Blocks.findById(id);
	}
	
	public Blocks block(int id, int meta) {
		return Blocks.findById(id, meta);
	}
	
	public Items item(String name) {
		return Items.find(name);
	}
	
	public Items item(int id) {
		return Items.findById(id, 0);
	}
	
	public Items item(int id, int meta) {
		return Items.findById(id, meta);
	}
	
	public void err(Object obj) {
		echo("§b" + obj.toString());
	}
	
	public void echo(Object obj) {
		if (obj == null) {
			env.getUser().sendChatToPlayer("null");
		}
		else if (obj instanceof String) {
			String msg = (String)obj;
			msg = msg.replaceAll("\t", "  ").replaceAll("\r", "").
					replaceAll("%([0-9A-Fa-f])%", "\u00A7" + "$1");
			System.out.println(msg);
			
			if (msg.toString().indexOf('\n') == -1) {
				env.getUser().sendChatToPlayer((String)msg);
			}
			else {
				String[] lines = msg.split("\n");
				for (String line: lines) {
					env.getUser().sendChatToPlayer(line);
				}
			}
		}
		else if (obj instanceof Collection) {
			Collection col = (Collection)obj;
			for (Object e: col) {
				echo(e);
			}
		}	
		else if (obj instanceof Object[]) {
			Object[] array = (Object[])obj;
			for (Object e: array) {
				echo(e);
			}
		}
		else {
			env.getUser().sendChatToPlayer(obj.toString());
		}
	}
	
	public void format(String format, Object... args) {
		echo(format.format(format, args));
	}
	

	public void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
