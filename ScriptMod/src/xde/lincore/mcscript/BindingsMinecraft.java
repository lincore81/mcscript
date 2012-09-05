package xde.lincore.mcscript;

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

import xde.lincore.mcscript.edit.EditSession;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Vec3;
import net.minecraft.src.mod_Script;

public class BindingsMinecraft extends BindingsBase {
	
	public final BindingsTime time;
	public final BindingsUser user;
	public final BindingsWorld world;
	public final BindingsTypes types;
	public final EditSession edit;
	
	protected BindingsMinecraft(ScriptingEnvironment env) {
		super(env);
		user = new BindingsUser(env);
		time = new BindingsTime(env);
		world = new BindingsWorld(env);
		types = new BindingsTypes(env);
		edit = new EditSession(this, true);
		Turtle.setMc(this);
		VectorTurtle.setMc(this);
	}
	
//	public void echo(Object... obj) {
//		for (Object e: obj) {
//			echo(e);
//		}
//	}
	
	public Exception getLastException() {
		return env.getLastException();
	}
	
	public StackTraceElement[] getStackTrace() {
		Exception e = env.getLastException();
		
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
	
	public Blocks getBlock(String name) {
		return Blocks.find(name);
	}
	
	public Blocks getBlock(int id) {
		return Blocks.findById(id);
	}
	
	public Blocks getBlock(int id, int damage) {
		return Blocks.findById(id, damage);
	}
	
	public void err(Object obj) {
		echo("§c" + obj.toString());
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
	
	
	public Vector newVector(double x, double y, double z) {
		return new Vector(x, y, z);
	}
	
	public Voxel newVoxel(int x, int y, int z) {
		return new Voxel(x, y, z);
	}
	
	public Vector zeroVector() {
		return Vector.ZERO;
	}
	
	public void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
