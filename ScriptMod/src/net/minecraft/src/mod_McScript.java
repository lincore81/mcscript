package net.minecraft.src;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;

import org.lwjgl.input.Keyboard;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.ui.CommandRunScript;
import xde.lincore.mcscript.ui.CommandScriptEnv;
import xde.lincore.mcscript.ui.McChatLogHandler;
import xde.lincore.util.Config;
import xde.lincore.util.Text;

public final class mod_McScript extends BaseMod {

	private CommandRunScript runCommand;
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
		G.LOG.setLevel(Level.ALL);
		env = ScriptEnvironment.createInstance(this);
		G.LOG.addHandler(new McChatLogHandler(env));
		setupHooks();
		setupFiles();
	}

	public CommandRunScript getRunCommand() {
		return runCommand;
	}


	private void setupHooks() {
		runCommand = new CommandRunScript(env);
		ModLoader.addCommand(runCommand);
		ModLoader.addCommand(new CommandScriptEnv(this, env));
		ModLoader.setInGameHook(this, true, true);
		ModLoader.registerKey(this, new KeyBinding(G.BIND_TOGGLE_CONSOLE, Keyboard.KEY_F12), false);
	}

	private void setupFiles() {
		G.DIR_MOD.mkdirs();
		G.DIR_SCRIPTS.mkdir();
		G.DIR_CACHE.mkdir();
		Config.createMap(G.CFG_MAIN, setupDefaultProperties());
		Config.load(G.CFG_MAIN);
	}

	public Properties setupDefaultProperties() {
		final Properties result = new Properties();
		result.setProperty(G.PROP_AUTOSAVE, "yes");
		result.setProperty(G.PROP_ENCODING, Text.DEFAULT_CHARSET.name());
		result.setProperty(G.PROP_TOOL_FILEMGR, "auto");
		result.setProperty(G.PROP_TOOL_EDITOR, "auto");
		result.setProperty(G.PROP_PATH, ".");
		String cwd = null;
		try {
			cwd = G.DIR_SCRIPTS.getCanonicalPath();
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		result.setProperty(G.PROP_CWD, cwd);
		return result;
	}


//	@Override
//	public boolean onTickInGame(final float tick, final Minecraft mcInstance) {
//		env.update(tick);
//		System.out.println(Thread.currentThread());
//		return false;
//	}

	@Override
	public void clientConnect(final NetClientHandler var1) {
		env.onClientConnect();
	}

	@Override
	public void clientDisconnect(final NetClientHandler var1) {
		env.onClientDisconnect();
	}

	@Override
	public void keyboardEvent(final KeyBinding bind) {
	}
}
