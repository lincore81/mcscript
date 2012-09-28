package xde.lincore.mcscript.minecraft;

import net.minecraft.server.MinecraftServer;
import xde.lincore.mcscript.env.ScriptEnvironment;

public class TimeWrapper extends AbstractWrapper {

	protected TimeWrapper(final ScriptEnvironment env, final MinecraftWrapper mc) {
		super(env, mc);
	}

	public void set(final long time) {
		//System.out.println("setTime...");
		for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i)
        {
			MinecraftServer.getServer().worldServers[i].setTime(time);
        }
	}

	public long get() {
		return MinecraftServer.getServer().worldServers[0].getWorldTime();
	}
}
