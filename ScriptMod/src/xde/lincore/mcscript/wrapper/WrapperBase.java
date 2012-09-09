package xde.lincore.mcscript.wrapper;

import xde.lincore.mcscript.ScriptingEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public abstract class WrapperBase {
	ScriptingEnvironment env;
	
	protected WrapperBase(ScriptingEnvironment env) {
		this.env = env;
	}
}
