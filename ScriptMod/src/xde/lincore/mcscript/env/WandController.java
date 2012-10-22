package xde.lincore.mcscript.env;

import java.util.HashMap;
import java.util.Map;

import xde.lincore.mcscript.minecraft.IWandUseListener;
import xde.lincore.mcscript.minecraft.WandFacade;

public class WandController extends AbstractController implements IWandUseListener {
	
	private final WandFacade facade;
	private final Map<Integer, String> bindings;
	
	public WandController(final ScriptEnvironment env) {
		super(env);
		facade = new WandFacade(this, 2048);
		bindings = new HashMap<Integer, String>();
	}

	@Override
	public void onWandUse(final int wandId) {
		if (bindings.containsKey(wandId)) {
			String action = bindings.get(wandId);
			if (action.startsWith("/")) {
				env.commands.doCommand(env.getPlayer(), action);
			}
			else {
				env.commands.doRunCommand(env.getPlayer(), action);
			}
		}
	}
	
	public Map dumpBindings() {
		return bindings;
	}
	
	public void bind(final String action) {
		int id = facade.bindWand(env.getPlayer());
		bindings.put(id, action);
	}
	
	public void unbind() {
		int id = facade.unbindWand(env.getPlayer());
		bindings.remove(id);
	}
	
	public boolean hasColor(final String color) {
		return facade.hasColor(color);
	}
	
	public void give(final String color) {
		facade.giveWand(env.getPlayer(), color, false);
	}
}
