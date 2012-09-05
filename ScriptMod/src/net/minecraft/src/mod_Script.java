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

import xde.lincore.mcscript.BindingsMinecraft;
import xde.lincore.mcscript.BindingsTime;
import xde.lincore.mcscript.CommandAlias;
import xde.lincore.mcscript.CommandRunScript;
import xde.lincore.mcscript.CommandScriptEnv;
import xde.lincore.mcscript.G;
import xde.lincore.mcscript.McChatLogHandler;
import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public final class mod_Script extends BaseMod {

	
	private ScriptingEnvironment env;

	@Override
	public String getVersion() {
		return G.VERSION + " (" + G.BUILD_DATE + ")";
	}

	@Override
	public String getName() {
		return G.MOD_NAME;
	}

	@Override
	public void load() {
		G.LOG.setLevel(Level.ALL);
		env = new ScriptingEnvironment(this);
		G.LOG.addHandler(new McChatLogHandler(env));
				
		CommandRunScript runCommand = new CommandRunScript(env); 
		CommandAlias.runCommand = runCommand;
		ModLoader.addCommand(runCommand);
		ModLoader.addCommand(new CommandScriptEnv(this, env));
		
		ModLoader.registerKey(this, new KeyBinding(G.KEYBIND_TOGGLE_CONSOLE, Keyboard.KEY_F12), false);
		setupFiles();
	}

	
	private void setupFiles() {
		G.MOD_DIR.mkdirs();
		G.LOG.fine("ScriptMod directory: " + G.MOD_DIR.getAbsolutePath());
		Config.createMap(G.CFG_MAIN, getDefaultProperties());
		Config.load(G.CFG_MAIN);
	}
	
	public Properties getDefaultProperties() {
		Properties result = new Properties(); 
		result.setProperty(G.PROP_AUTOSAVE, "yes");
		result.setProperty(G.PROP_ENCODING, Text.DEFAULT_CHARSET.name());
		String cwd = null;
		try {
			cwd = new File(G.MOD_DIR, "scripts/").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			cwd = new File(G.MOD_DIR, "scripts/").getAbsolutePath();
		}
		result.setProperty(G.PROP_CWD, cwd);
		return result;
	}
	

	@Override
	public boolean onTickInGame(float var1, Minecraft var2) {
		return super.onTickInGame(var1, var2);
	}

	@Override
	public void clientConnect(NetClientHandler var1) {
		env.onClientConnect();
		System.out.println("Client connect!");
	}

	@Override
	public void clientDisconnect(NetClientHandler var1) {
		System.out.println("Client disconnect!");
	}

	@Override
	public void keyboardEvent(KeyBinding bind) {
		if (bind.keyDescription.equals(G.KEYBIND_TOGGLE_CONSOLE)) {
			System.out.println("Toggle console!");
		}
	}
}
