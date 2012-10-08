package xde.lincore.mcscript.env;

import java.util.Map;

import xde.lincore.mcscript.minecraft.IActionListener;
import xde.lincore.mcscript.minecraft.InputFacade;
import xde.lincore.mcscript.ui.Keys;
import xde.lincore.util.Config;

public class KeyController extends AbstractController implements IActionListener {

	InputFacade input;
	
	public KeyController(final ScriptEnvironment env) {
		super(env);
		input = new InputFacade(this);
		loadConfig();
	}

	public void loadConfig() {
		Config.load(G.CFG_KEYS);
		for (final Map.Entry e: Config.getMap(G.CFG_KEYS).entrySet()) {
			boolean success = true;
			final Keys key = Keys.find((String)(e.getKey()));
			if (input.isGameKey(key)) {
				G.LOG.warning("Config contains game key: " + key.toString());
				continue;
			}
			try {
				input.setKey(key, (String)e.getValue());
			}
			catch (IllegalArgumentException ex) {
				G.LOG.warning("Bad property in config: " + e.getKey().toString() + " = " + e.getValue().toString());
				continue;
			}
		}
	}

	public void saveConfig() {
		Config.save(G.CFG_KEYS);
	}

	public boolean setKey(final String keyString, final String action) {
		Keys key = Keys.find(keyString);
		if (key == null) return false;
		input.setKey(key, action);
		Config.set(G.CFG_KEYS, key.name, action);
		return true;
	}
	
	public boolean isGameKey(final String keyString) {
		Keys key = Keys.find(keyString);
		return input.isGameKey(key);
	}
	
	public boolean removeKey(final String keyString) {
		Keys key = Keys.find(keyString);
		if (key == null) return false;
		input.removeKey(key);
		Config.remove(G.CFG_KEYS, key.name);
		return true;
	}

	
	public String getAction(final Keys key) {
		return input.getAction(key);
	}



	public void update() {
		input.update();
	}

	@Override
	public void onAction(final String action) {
		if (action.startsWith("/")) {
			env.commands.doCommand(env.getPlayer(), action);
		}
		else {
			env.commands.doRunCommand(env.getPlayer(), action);
		}
	}

	
}
