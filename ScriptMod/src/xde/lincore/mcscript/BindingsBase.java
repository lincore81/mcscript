package xde.lincore.mcscript;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public abstract class BindingsBase {
	ScriptingEnvironment env;
	
	protected BindingsBase(ScriptingEnvironment env) {
		this.env = env;
	}
}
