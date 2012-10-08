package xde.lincore.mcscript.env;

import java.util.Collection;

import xde.lincore.mcscript.minecraft.LocalChatFacade;

public class ChatIoController extends AbstractController {

	private LocalChatFacade chat;
	
	public ChatIoController(final ScriptEnvironment env, LocalChatFacade chat) {
		super(env);
		this.chat = chat;
	}

	public void err(final Object obj) {
		chat.err(obj);
	}

	public void echo(final Object obj) {
		chat.echo(obj);		
	}

	public void format(final String format, final Object... args) {
		chat.format(format, args);
	}
}
