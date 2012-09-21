package xde.lincore.mcscript.env;

import java.util.Collection;

import net.minecraft.src.EntityPlayer;

public class ChatIoController extends AbstractController {
	
	public ChatIoController(ScriptEnvironment env) {
		super(env);
	}

	public void err(Object obj) {
		echo("§b" + obj.toString());
	}

	public void echo(Object obj) {
		if (obj == null) {
			env.getUser().sendChatToPlayer("null");
		}
		else if (obj instanceof String) {
			String msg = (String)obj;
			msg = msg.replaceAll("\t", "  ").replaceAll("\r", "").
					replaceAll("%([0-9A-Fa-f])%", "\u00A7" + "$1");
			System.out.println(msg);
			
			if (msg.toString().indexOf('\n') == -1) {
				env.getUser().sendChatToPlayer((String)msg);
			}
			else {
				String[] lines = msg.split("\n");
				for (String line: lines) {
					env.getUser().sendChatToPlayer(line);
				}
			}
		}
		else if (obj instanceof Collection) {
			Collection col = (Collection)obj;
			for (Object e: col) {
				env.getUser().sendChatToPlayer(obj.toString());
			}
		}	
		else if (obj instanceof Object[]) {
			Object[] array = (Object[])obj;
			for (Object e: array) {
				env.getUser().sendChatToPlayer(obj.toString());
			}
		}
		else {
			env.getUser().sendChatToPlayer(obj.toString());
		}
	}

	public void format(String format, Object... args) {
		echo(format.format(format, args));
	}
}
