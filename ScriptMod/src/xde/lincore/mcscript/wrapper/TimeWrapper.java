package xde.lincore.mcscript.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import org.luaj.vm2.LuaValue;

import xde.lincore.mcscript.ScriptingEnvironment;

public class TimeWrapper extends WrapperBase {
	
	protected TimeWrapper(ScriptingEnvironment env) {
		super(env);
	}

	public void set(long time) {
		//System.out.println("setTime...");
		for (int i = 0; i < MinecraftServer.getServer().theWorldServer.length; ++i)
        {            
			MinecraftServer.getServer().theWorldServer[i].setTime(time);            
        }
	}
	
	public long get() {
		return MinecraftServer.getServer().theWorldServer[0].getWorldTime();
	}
}
