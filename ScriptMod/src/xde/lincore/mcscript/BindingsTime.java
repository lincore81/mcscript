package xde.lincore.mcscript;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import org.luaj.vm2.LuaValue;

public class BindingsTime extends BindingsBase {
	
	protected BindingsTime(ScriptingEnvironment env) {
		super(env);
		// TODO Auto-generated constructor stub
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
