package xde.lincore.mcscript.env;

import java.util.Collection;

public class ChatIoController extends AbstractController {

	public ChatIoController(final ScriptEnvironment env) {
		super(env);
	}

	public void err(final Object obj) {
		echo("§b" + obj.toString());
	}

	public void echo(final Object obj) {
		if (obj == null) {
			env.getUser().sendChatToPlayer("null");
		}
		else if (obj instanceof String) {
			String msg = (String)obj;
			msg = msg.replaceAll("\t", "  ").replaceAll("\r", "").
					replaceAll("%([0-9A-Fa-f])%", "\u00A7" + "$1");
			System.out.println(msg);

			if (msg.toString().indexOf('\n') == -1) {
				env.getUser().sendChatToPlayer(msg);
			}
			else {
				final String[] lines = msg.split("\n");
				for (final String line: lines) {
					env.getUser().sendChatToPlayer(line);
				}
			}
		}
		else if (obj instanceof Collection) {
			final Collection col = (Collection)obj;
			for (final Object e: col) {
				env.getUser().sendChatToPlayer(obj.toString());
			}
		}
		else if (obj instanceof Object[]) {
			final Object[] array = (Object[])obj;
			for (final Object e: array) {
				env.getUser().sendChatToPlayer(obj.toString());
			}
		}
		else {
			env.getUser().sendChatToPlayer(obj.toString());
		}
	}

	public void format(final String format, final Object... args) {
		echo(String.format(format, args));
	}
}
