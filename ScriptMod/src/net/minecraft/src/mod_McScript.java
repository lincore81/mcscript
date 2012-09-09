package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.lwjgl.input.Keyboard;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.mcscript.ui.CommandAlias;
import xde.lincore.mcscript.ui.CommandRunScript;
import xde.lincore.mcscript.ui.CommandScriptEnv;
import xde.lincore.mcscript.ui.McChatLogHandler;
import xde.lincore.mcscript.wrapper.MinecraftWrapper;
import xde.lincore.mcscript.wrapper.TimeWrapper;
import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public final class mod_McScript extends BaseMod {

	private CommandRunScript runCommand;	
	private ScriptingEnvironment env;

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
		env = ScriptingEnvironment.createInstance(this);		
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
		Properties result = new Properties(); 
		result.setProperty(G.PROP_AUTOSAVE, "yes");
		result.setProperty(G.PROP_ENCODING, Text.DEFAULT_CHARSET.name());
		result.setProperty(G.PROP_TOOL_FILEMGR, "auto");
		result.setProperty(G.PROP_TOOL_EDITOR, "auto");
		result.setProperty(G.PROP_PATH, ".");
		String cwd = null;
		try {
			cwd = G.DIR_SCRIPTS.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
		result.setProperty(G.PROP_CWD, cwd);
		return result;
	}
	

	@Override
	public boolean onTickInGame(float tick, Minecraft mcInstance) {
		env.update(tick);
		return true;
	}

	@Override
	public void clientConnect(NetClientHandler var1) {
		env.onClientConnect();		
	}

	@Override
	public void clientDisconnect(NetClientHandler var1) {
		env.onClientDisconnect();
	}

	@Override
	public void keyboardEvent(KeyBinding bind) {		
	}
}
