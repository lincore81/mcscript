package xde.lincore.mcscript.minecraft;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.KeyBinding;
import xde.lincore.mcscript.ui.Keys;


public class InputFacade {
	public static final String[] GAME_KEYS;
	private final Map<KeyBinding, String> bindings;
	private final IActionListener listener;
	
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
	
	public InputFacade(final IActionListener listener) {
		bindings = new HashMap<KeyBinding, String>();
		this.listener = listener;
		if (listener == null) throw new IllegalArgumentException();
	}
	
	
	public void update() {
		for (final Map.Entry<KeyBinding, String> b: bindings.entrySet()) {
			if (b.getKey().isPressed()) {
				listener.onAction(b.getValue());
			}
		}
	}
	
	
	public boolean isGameKey(final Keys key) {
		return isGameKey(getKeyBinding(key));
	}

	protected boolean isGameKey(final KeyBinding binding) {
		if (binding == null) {
			return false;
		}
		for (final String desc: GAME_KEYS) {
			if (desc.equals(binding.keyDescription)) {
				return true;
			}
		}
		final int keycode = binding.keyCode;
		if (keycode >= Keys.Key1.keycode && keycode <= Keys.Key9.keycode) {
			return true;
		}
		return false;
	}
	
	public void setKey(final Keys key, final String action) {
		if (key == null || action == null || action.isEmpty()) {
			throw new IllegalArgumentException();
		} else {
			if (isBound(key)) {
				final KeyBinding b = unbind(key);
				bindings.remove(b);
			}
			final KeyBinding binding = bind(key, action);
			bindings.put(binding, action);
		}
	}

	public void removeKey(final Keys key) {
		if (key != null) {
			final KeyBinding b = unbind(key);
			bindings.remove(b);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public String getAction(final Keys key) {
		if (isBound(key)) {
			return bindings.get(getKeyBinding(key));
		} else {
			return null;
		}
	}
	
	private KeyBinding bind(final Keys key, final String description) {
		return new KeyBinding(description, key.keycode);
	}

	private KeyBinding unbind(final Keys key) {
		final KeyBinding binding = getKeyBinding(key);
		if (binding == null) {
			return null;
		} else {
			KeyBinding.keybindArray.remove(binding);
			return (KeyBinding)(KeyBinding.hash.removeObject(key.keycode));
		}
	}

	private boolean isBound(final Keys key) {
		return getKeyBinding(key) != null;
	}

	private KeyBinding getKeyBinding(final Keys key) {
		return (KeyBinding)(KeyBinding.hash.lookup(key.keycode));
	}
}
