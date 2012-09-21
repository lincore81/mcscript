package xde.lincore.mcscript.env;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;
import xde.lincore.mcscript.ui.CommandAlias;
import xde.lincore.mcscript.ui.CommandRunScript;
import xde.lincore.util.Config;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommand;
import net.minecraft.src.ModLoader;

public class AliasController extends AbstractController {	
	
	public AliasController(ScriptEnvironment env) {
		super(env);
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
		return Config.get(G.CFG_ALIAS, name);
	}
	
	public Map getAliases() {
		return Config.getMap(G.CFG_ALIAS);
	}
	
	public Map getSortedMap() {
		return Config.getSortedMap(G.CFG_ALIAS);
	}
	
	public void loadAliases() {
		Config.load(G.CFG_ALIAS);
		for (Map.Entry alias: Config.getMap(G.CFG_ALIAS).entrySet()) {
			registerAlias((String)(alias.getKey()), (String)(alias.getValue()));
		}
	}
	
	private boolean registerAlias(String name, String script) {
		if (commandExists(name) && !Config.contains(G.CFG_ALIAS, name)) { // built-in command?
			env.chat.err("There already is a command with the name \"" +
					name + "\". Please pick a different one.");
			return false;
		}
		CommandHandler handler;
		if (MinecraftServer.getServer().getCommandManager() instanceof CommandHandler) {
			handler = (CommandHandler)(MinecraftServer.getServer().getCommandManager());
		}
		else {
			env.chat.err("An unexpected error has occured, I can't set the alias, sorry.");
			G.LOG.warning("Command manager is not an instance of CommandHandler, dunno what to do!");			
			return false;
		}
		handler.registerCommand(new CommandAlias(env, name));
		return true;
	}
	
	public void reloadAliases() {
		String tmpName = "aliasTmp";
		Config.load(Config.generateFileName(G.CFG_ALIAS), "aliasTmp");		
		for (Map.Entry alias: Config.getMap("aliasTmp").entrySet()) {
			setAlias((String)(alias.getKey()), (String)(alias.getValue()));
		}
		Config.removeMap("aliasTmp");
	}
	
	public boolean removeAlias(String name) {
		if (commandExists(name)) {
			if (Config.contains(G.CFG_ALIAS, name)) {
				CommandHandler handler;
				if (MinecraftServer.getServer().getCommandManager() instanceof CommandHandler) {
					handler = (CommandHandler)(MinecraftServer.getServer().getCommandManager());
				}
				else {
					env.chat.err("An unexpected error has occured, I can't remove the alias, sorry.");
					G.LOG.warning("Command manager is not an instance of CommandHandler, dunno what to do!");			
					return false;
				}

				Map commandMap;
			    Set commandSet;
				try {
					commandMap = (Map)ModLoader.getPrivateValue(CommandHandler.class, handler, "commandMap");
					commandSet = (Set)ModLoader.getPrivateValue(CommandHandler.class, handler, "commandSet");
				} catch (IllegalArgumentException e) {					
					G.LOG.severe(e.getMessage());
					e.printStackTrace();
					return false;
				} catch (SecurityException e) {
					G.LOG.severe(e.getMessage());
					e.printStackTrace();
					return false;
				} catch (NoSuchFieldException e) {
					G.LOG.severe(e.getMessage());
					e.printStackTrace();
					return false;
				}
				ICommand cmd = (ICommand)commandMap.get(name);
				if (cmd != null) {
					commandMap.remove(name);
					commandSet.remove(cmd);
					Config.remove(G.CFG_ALIAS, name);
					Config.save(G.CFG_ALIAS);
					env.chat.echo("Ok.");
					return true;
				}
				
			}
		}
		env.chat.err("The alias \"" + name + "\" doesn't exist.");	
		return false;
	}
	
	public void setAlias(String name, String script) {		
		if (registerAlias(name, script)) {
			Config.set(G.CFG_ALIAS, name, script);
			Config.save(G.CFG_ALIAS);
		}
	}
	
	public void setMultipleAliases(Map<String, String> aliases) {
		for (Entry<String, String> alias: aliases.entrySet()) {
			setAlias(alias.getKey(), alias.getValue());
		}
	}
}