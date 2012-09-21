package xde.lincore.mcscript.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import org.luaj.vm2.LuaValue;

import xde.lincore.mcscript.env.ScriptEnvironment;

public class TimeWrapper extends AbstractWrapper {
	
	protected TimeWrapper(ScriptEnvironment env, MinecraftWrapper mc) {
		super(env, mc);
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
