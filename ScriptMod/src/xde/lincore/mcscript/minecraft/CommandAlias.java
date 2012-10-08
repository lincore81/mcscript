package xde.lincore.mcscript.minecraft;

import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.util.StringTools;

public class CommandAlias extends CommandBase {

	private final ScriptEnvironment env;
	private final String name;
	private final Commands commands;

	public CommandAlias(final String name, final ScriptEnvironment env, final Commands commands) {
		this.env = env;
		this.name = name;
		this.commands = commands;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] args) {
		final String command = StringTools.insertArgs(env.aliases.getAlias(name), args);
		if (command.startsWith("/")) {
			commands.getCommandHandler().executeCommand(sender, command);
		}
		else {
			final String[] newargs = command.split(" ");
			commands.getRunCommand().processCommand(sender, newargs);
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender par1iCommandSender) {
		return true;
	}
}
