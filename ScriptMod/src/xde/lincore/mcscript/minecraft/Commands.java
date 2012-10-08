package xde.lincore.mcscript.minecraft;


import java.util.Map;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommand;
import net.minecraft.src.ModLoader;
import xde.lincore.mcscript.env.G;
import xde.lincore.mcscript.env.ScriptEnvironment;

public final class Commands {
	private final CommandRunScript runCommand;
	private final CommandScriptEnv envCommand;
	private final ScriptEnvironment env;
	private ServerFacade server;
	
	public Commands(final ScriptEnvironment env){
		this.env = env;
		runCommand = new CommandRunScript(env, this);
		envCommand = new CommandScriptEnv(env, this);
	}
	
	public boolean commandExists(final String name) {
		if (server == null) throw new IllegalStateException("Server is null. Not connected?");
		final Map<String, ICommand> commands = server.getMcServer().getCommandManager().getCommands();
		for (final Map.Entry<String, ICommand> entry: commands.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public void registerCommands(final ServerFacade server) {
		this.server = server;
		CommandHandler handler = getCommandHandler();
		handler.registerCommand(runCommand);
		handler.registerCommand(envCommand);
	}
	
	public void unregisterCommands() {
		server = null;
	}
	
	public void registerAlias(final String name) {
		getCommandHandler().registerCommand(new CommandAlias(name, env, this));
	}
	
	public boolean removeAlias(final String name) {
		CommandHandler handler = getCommandHandler();
		Map commandMap;
	    Set commandSet;
		try {
			//TODO: Getting private fields "commandMap" & "commandSet" doesn't work on reobf class!
			commandMap = (Map)ModLoader.getPrivateValue(CommandHandler.class, handler, "commandMap");
			commandSet = (Set)ModLoader.getPrivateValue(CommandHandler.class, handler, "commandSet");
		} catch (final IllegalArgumentException e) {
			G.LOG.severe(e.toString());
			throw(new RuntimeException(e));
		} catch (final SecurityException e) {
			G.LOG.severe(e.toString());
			throw(new RuntimeException(e));
		} catch (final NoSuchFieldException e) {
			G.LOG.severe(e.toString());
			throw(new RuntimeException(e));
		}
		final ICommand cmd = (ICommand)commandMap.get(name);
		if (cmd != null) {
			commandMap.remove(name);
			commandSet.remove(cmd);
			return true;
		}
		else return false;
	}

	public void doCommand(final PlayerFacade player, final String commandString) {
		getCommandHandler().executeCommand(player.getLocalMcEntityPlayer(), commandString);
	}
	
	public void doRunCommand(final PlayerFacade player, final String argString) {
		runCommand.processCommand(player.getLocalMcEntityPlayer(), new String[]{argString});
	}
	
	
	protected CommandHandler getCommandHandler() {
		if (server == null) throw new IllegalStateException("Server is null. Not connected?");
		MinecraftServer mcServer = server.getMcServer();
		assert mcServer.getCommandManager() instanceof CommandHandler :
				"The command manager is not an instance of CommandHandler!";
		return (CommandHandler)mcServer.getCommandManager();
	}

	protected CommandRunScript getRunCommand() {
		return runCommand;
	}

	protected CommandScriptEnv getEnvCommand() {
		return envCommand;
	}
}
