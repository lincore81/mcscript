package xde.lincore.mcscript;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import xde.lincore.util.Config;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommand;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_Script;

public class AliasController {
	private BindingsMinecraft mc;
	private ScriptingEnvironment env;
	
	public AliasController(BindingsMinecraft mc, ScriptingEnvironment env) {
		this.mc = mc;
		this.env = env;
	}
	
	private boolean commandExists(String name) {
		Map<String, ICommand> commands = MinecraftServer.getServer().getCommandManager().getCommands();
		for (Map.Entry<String, ICommand> entry: commands.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public String getAlias(String name) {
		return Config.get(Config.CFG_ALIAS, name);
	}
	
	public Map getAliases() {
		return Config.getMap(Config.CFG_ALIAS);
	}
	
	public Map getSortedMap() {
		return Config.getSortedMap(Config.CFG_ALIAS);
	}
	
	public void loadAliases() {
		Config.load(Config.CFG_ALIAS);
		for (Map.Entry alias: Config.getMap(Config.CFG_ALIAS).entrySet()) {
			registerAlias((String)(alias.getKey()), (String)(alias.getValue()));
		}
	}
	
	private boolean registerAlias(String name, String script) {
		if (commandExists(name) && !Config.contains(Config.CFG_ALIAS, name)) { // built-in command?
			mc.echo("§cThere already is a command with the name \"" +
					name + "\". Please pick a different one.");
			return false;
		}
		CommandHandler handler;
		if (MinecraftServer.getServer().getCommandManager() instanceof CommandHandler) {
			handler = (CommandHandler)(MinecraftServer.getServer().getCommandManager());
		}
		else {
			mc.echo("§cAn unexpected error has occured, I can't set the alias, sorry.");
			mod_Script.LOG.warning("Command manager is not an instance of CommandHandler, dunno what to do!");			
			return false;
		}
		handler.registerCommand(new CommandAlias(env, name));
		return true;
	}
	
	public void reloadAliases() {
		String tmpName = "aliasTmp";
		Config.load(Config.generateFileName(Config.CFG_ALIAS), "aliasTmp");		
		for (Map.Entry alias: Config.getMap("aliasTmp").entrySet()) {
			setAlias((String)(alias.getKey()), (String)(alias.getValue()));
		}
		Config.removeMap("aliasTmp");
	}
	
	public boolean removeAlias(String name) {
		if (commandExists(name)) {
			if (Config.contains(Config.CFG_ALIAS, name)) {
				CommandHandler handler;
				if (MinecraftServer.getServer().getCommandManager() instanceof CommandHandler) {
					handler = (CommandHandler)(MinecraftServer.getServer().getCommandManager());
				}
				else {
					mc.echo("§cAn unexpected error has occured, I can't remove the alias, sorry.");
					mod_Script.LOG.warning("Command manager is not an instance of CommandHandler, dunno what to do!");			
					return false;
				}

				Map commandMap;
			    Set commandSet;
				try {
					commandMap = (Map)ModLoader.getPrivateValue(CommandHandler.class, handler, "commandMap");
					commandSet = (Set)ModLoader.getPrivateValue(CommandHandler.class, handler, "commandSet");
				} catch (IllegalArgumentException e) {					
					mod_Script.LOG.severe(e.getMessage());
					e.printStackTrace();
					return false;
				} catch (SecurityException e) {
					mod_Script.LOG.severe(e.getMessage());
					e.printStackTrace();
					return false;
				} catch (NoSuchFieldException e) {
					mod_Script.LOG.severe(e.getMessage());
					e.printStackTrace();
					return false;
				}
				ICommand cmd = (ICommand)commandMap.get(name);
				if (cmd != null) {
					commandMap.remove(name);
					commandSet.remove(cmd);
					Config.remove(Config.CFG_ALIAS, name);
					Config.save(Config.CFG_ALIAS);
					mc.echo("Ok.");
					return true;
				}
				
			}
		}
		mc.echo("The alias \"" + name + "\" doesn't exist.");	
		return false;
	}
	
	public void setAlias(String name, String script) {		
		if (registerAlias(name, script)) {
			Config.set(Config.CFG_ALIAS, name, script);
			Config.save(Config.CFG_ALIAS);
		}
	}
	
	public void setMultipleAliases(Map<String, String> aliases) {
		for (Entry<String, String> alias: aliases.entrySet()) {
			setAlias(alias.getKey(), alias.getValue());
		}
	}
}
