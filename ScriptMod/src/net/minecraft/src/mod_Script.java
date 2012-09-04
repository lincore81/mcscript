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
import java.util.logging.Logger;

import xde.lincore.mcscript.BindingsMinecraft;
import xde.lincore.mcscript.BindingsTime;
import xde.lincore.mcscript.CommandAlias;
import xde.lincore.mcscript.CommandRunScript;
import xde.lincore.mcscript.CommandScriptEnv;
import xde.lincore.mcscript.Globals;
import xde.lincore.mcscript.McChatLogHandler;
import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public final class mod_Script extends BaseMod {

	
	private ScriptingEnvironment env;	

	public static final File MOD_DIR = new File(Minecraft.getMinecraftDir(), "mods/script/");
	public static final Logger LOG = Logger.getLogger("ScriptMod");
	
	@Override
	public String getVersion() {
		return "0.1a 2012-08-23";
	}

	@Override
	public String getName() {
		return "ScriptMod";
	}

	@Override
	public void load() {
		LOG.setLevel(Level.ALL);
		env = new ScriptingEnvironment(this);
		LOG.addHandler(new McChatLogHandler(env));
				
		CommandRunScript runCommand = new CommandRunScript(env); 
		CommandAlias.runCommand = runCommand;
		ModLoader.addCommand(runCommand);
		ModLoader.addCommand(new CommandScriptEnv(this, env));
		
		setupFiles();
	}

	
	private void setupFiles() {
		MOD_DIR.mkdirs();
		LOG.fine("ScriptMod directory: " + MOD_DIR.getAbsolutePath());
		Config.createMap(Config.CFG_MAIN, getDefaultProperties());
		Config.load(Config.CFG_MAIN);
	}
	
	public Properties getDefaultProperties() {
		Properties result = new Properties(); 
		result.setProperty(Globals.PROP_AUTOSAVE, "yes");
		result.setProperty(Globals.PROP_ENCODING, Text.DEFAULT_CHARSET.name());
		String cwd = null;
		try {
			cwd = new File(MOD_DIR, "scripts/").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			cwd = new File(MOD_DIR, "scripts/").getAbsolutePath();
		}
		result.setProperty(Globals.PROP_CWD, cwd);
		return result;
	}
	

	@Override
	public boolean onTickInGame(float var1, Minecraft var2) {
		// TODO Auto-generated method stub
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
}
