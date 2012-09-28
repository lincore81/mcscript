package xde.lincore.mcscript.env;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.KeyBinding;
import xde.lincore.mcscript.G;
import xde.lincore.mcscript.ui.Keys;
import xde.lincore.util.Config;

public class KeyController extends AbstractController {

	private final Map<KeyBinding, String> bindings;
	public static final String[] GAME_KEYS;

	static {
		GAME_KEYS = new String[] {
				"key.attack",
				"key.use",
				"key.forward",
				"key.left",
				"key.back",
				"key.right",
				"key.jump",
				"key.sneak",
				"key.drop",
				"key.inventory",
				"key.chat",
				"key.playerlist",
				"key.pickItem",
				"key.command"
		};
	}

	public KeyController(final ScriptEnvironment env) {
		super(env);
		bindings = new HashMap<KeyBinding, String>();
		loadConfig();
	}

	public void loadConfig() {
		Config.load(G.CFG_KEYS);
		for (final Map.Entry e: Config.getMap(G.CFG_KEYS).entrySet()) {
			boolean success = true;
			final Keys key = Keys.find((String)(e.getKey()));
			final KeyBinding binding = key.getKeyBinding();
			if (binding == null && !isGameKey(binding)) {
				success = setKey((String)(e.getKey()), (String)(e.getValue()));
			} else {
				bindings.put(binding, (String)(e.getValue()));
			}
			if (!success) {
				G.LOG.warning("Unable to bind key " + key + " to action " + e.getValue());
			}
		}
	}

	public void saveConfig() {
		Config.save(G.CFG_KEYS);
	}

	public boolean isGameKey(final String keyStr) {
		final Keys key = Keys.find(keyStr);
		if (key == null || !key.isBound()) {
			return false;
		}
		return isGameKey(key.getKeyBinding());
	}

	public boolean isGameKey(final KeyBinding binding) {
		if (binding == null) {
			return false;
		}
		for (final String desc: GAME_KEYS) {
			if (desc.equals(binding.keyDescription)) {
				return true;
			}
		}
		final int keycode = binding.keyCode;
		if (keycode >= Keys.Key1.getKeycode() && keycode <= Keys.Key9.getKeycode()) {
			return true;
		}
		return false;
	}

	public boolean setKey(final String keyStr, final String action) {
		final Keys key = Keys.find(keyStr);
		if (key == null || action == null || action.isEmpty()) {
			return false;
		} else {
			if (key.isBound()) {
				final KeyBinding b = key.unbind();
				bindings.remove(b);
			}
			final KeyBinding binding = key.bind(action);
			bindings.put(binding, action);
			Config.set(G.CFG_KEYS, key.getName(), action);
			return true;
		}
	}

	public boolean removeKey(final String keyStr) {
		if (keyStr == null || keyStr.isEmpty()) {
			return false;
		}
		final Keys key = Keys.find(keyStr);
		if (key != null) {
			final KeyBinding b = key.unbind();
			bindings.remove(b);
			Config.remove(G.CFG_KEYS, key.getName());
			return true;
		} else {
			return false;
		}
	}

	public void update() {
		for (final Map.Entry<KeyBinding, String> b: bindings.entrySet()) {
			if (b.getKey().isPressed()) {
				onAction(b.getValue());
			}
		}
	}

	public void onAction(final String action) {
		final ICommandSender sender = env.getUser();
		if (action.startsWith("/")) {
			CommandHandler handler;
			if (MinecraftServer.getServer().getCommandManager() instanceof CommandHandler) {
				handler = (CommandHandler)(MinecraftServer.getServer().getCommandManager());
			}
			else {
				env.chat.err("An unexpected error has occured, I can't run the command, sorry.");
				G.LOG.warning("Command manager is not an instance of CommandHandler, dunno what to do!");
				return;
			}
			handler.executeCommand(sender, action);
		}
		else {
			final String[] newargs = action.split(" ");
			System.out.println(action);
			env.modInst.getRunCommand().processCommand(sender, newargs);
		}
	}

	public String getAction(final Keys key) {
		if (key.isBound()) {
			return bindings.get(key.getKeyBinding());
		} else {
			return null;
		}
	}
}
