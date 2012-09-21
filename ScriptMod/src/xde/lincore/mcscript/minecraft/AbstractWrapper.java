package xde.lincore.mcscript.minecraft;

import xde.lincore.mcscript.env.ScriptEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public abstract class AbstractWrapper {
	ScriptEnvironment env;
	MinecraftWrapper mc;
	
	protected AbstractWrapper(ScriptEnvironment env, MinecraftWrapper mc) {
		this.env = env;
		this.mc = mc;
	}
}
