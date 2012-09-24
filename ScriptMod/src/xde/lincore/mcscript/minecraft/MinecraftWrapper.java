package xde.lincore.mcscript.minecraft;

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
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.edit.EditSessionController;
import xde.lincore.mcscript.edit.IEditSession;
import xde.lincore.mcscript.edit.Turtle;
import xde.lincore.mcscript.edit.VectorTurtle;
import xde.lincore.mcscript.env.ScriptRunner;
import xde.lincore.mcscript.env.ScriptEnvironment;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Vec3;
import net.minecraft.src.mod_McScript;

public class MinecraftWrapper {
	public final ScriptEnvironment env;
	public final TimeWrapper time;
	public final UserWrapper user;
	public final WorldWrapper world;
	
	public MinecraftWrapper(ScriptEnvironment env) {
		this.env = env;
		user 	= new UserWrapper(env, this);
		time 	= new TimeWrapper(env, this);
		world 	= new WorldWrapper(env, this);		
	}
	
	public void abort(String reason) {
		throw new ScriptError(reason);
	}
	
	public void echo(Object obj) {
		env.chat.echo(obj);
	}
	
	public void err(Object obj) {
		env.chat.err(obj);
	}
	
	public void format(String format, Object... args) {
		env.chat.format(format, args);
	}
	
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
			env.chat.err(trace[i]);
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
	
	
	

	public void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
