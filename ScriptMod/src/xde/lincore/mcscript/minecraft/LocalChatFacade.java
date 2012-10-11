package xde.lincore.mcscript.minecraft;

import java.util.Collection;

public class LocalChatFacade implements ILocalChatWriter {
	
	public LocalChatFacade(){}
	
	public void err(final Object obj) {
		echo("§b" + obj.toString());
	}

	public void echo(final Object obj) {
		if (obj == null) {
			PlayerFacade.getLocalMcEntityPlayer().sendChatToPlayer("null");
		}
		else if (obj instanceof String) {
			String msg = (String)obj;
			msg = msg.replaceAll("\t", "  ").replaceAll("\r", "").
					replaceAll("%([0-9A-Fa-f])%", "\u00A7" + "$1");
			System.out.println(msg);

			if (msg.toString().indexOf('\n') == -1) {
				PlayerFacade.getLocalMcEntityPlayer().sendChatToPlayer(msg);
			}
			else {
				final String[] lines = msg.split("\n");
				for (final String line: lines) {
					PlayerFacade.getLocalMcEntityPlayer().sendChatToPlayer(line);
				}
			}
		}
		else if (obj instanceof Collection) {
			final Collection col = (Collection)obj;
			for (final Object e: col) {
				PlayerFacade.getLocalMcEntityPlayer().sendChatToPlayer(obj.toString());
			}
		}
		else if (obj instanceof Object[]) {
			final Object[] array = (Object[])obj;
			for (final Object e: array) {
				PlayerFacade.getLocalMcEntityPlayer().sendChatToPlayer(obj.toString());
			}
		}
		else {
			PlayerFacade.getLocalMcEntityPlayer().sendChatToPlayer(obj.toString());
		}
	}

	public void format(final String format, final Object... args) {
		echo(String.format(format, args));
	}
}
