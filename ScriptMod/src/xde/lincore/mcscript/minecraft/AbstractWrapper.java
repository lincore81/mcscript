package xde.lincore.mcscript.minecraft;

import xde.lincore.mcscript.env.ScriptEnvironment;

public abstract class AbstractWrapper {
	ScriptEnvironment env;
	MinecraftWrapper mc;

	protected AbstractWrapper(final ScriptEnvironment env, final MinecraftWrapper mc) {
		this.env = env;
		this.mc = mc;
	}
}
