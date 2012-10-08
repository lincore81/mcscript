package net.minecraft.src;

import xde.lincore.mcscript.env.G;
import xde.lincore.mcscript.env.ScriptEnvironment;

public final class mod_McScript extends BaseMod {

	private ScriptEnvironment env;

	@Override
	public String getVersion() {
		return G.MOD_VERSION + " (" + G.MOD_BUILD_DATE + ")";
	}

	@Override
	public String getName() {
		return G.MOD_NAME;
	}

	@Override
	public void load() {
		env = ScriptEnvironment.createInstance();
	}

	@Override
	public void clientConnect(final NetClientHandler netHandler) {
		env.onConnect();
	}

	@Override
	public void clientDisconnect(final NetClientHandler netHandler) {
		env.onDisconnect();
	}
}
