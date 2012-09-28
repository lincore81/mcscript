package xde.lincore.mcscript.ui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.KeyBinding;

import org.lwjgl.input.Keyboard;

import xde.lincore.util.StringTools;

public enum Keys {
	Key0(Keyboard.KEY_0, "0"),
	Key1(Keyboard.KEY_1, "1"),
	Key2(Keyboard.KEY_2, "2"),
	Key3(Keyboard.KEY_3, "3"),
	Key4(Keyboard.KEY_4, "4"),
	Key5(Keyboard.KEY_5, "5"),
	Key6(Keyboard.KEY_6, "6"),
	Key7(Keyboard.KEY_7, "7"),
	Key8(Keyboard.KEY_8, "8"),
	Key9(Keyboard.KEY_9, "9"),
	KeyA(Keyboard.KEY_A, "A"),
	KeyADD(Keyboard.KEY_ADD, "Plus (numpad)", "num(pad )?(plus|add|\\+)"), /* - on main keyboard */
	KeyAPOSTROPHE(Keyboard.KEY_APOSTROPHE, "Apostrophe", "'"),
	KeyAX(Keyboard.KEY_AX, "AX"), /* backspace */
	KeyB(Keyboard.KEY_B, "B"),
	KeyBACK(Keyboard.KEY_BACK, "Backspace", "back"),
	KeyBACKSLASH(Keyboard.KEY_BACKSLASH, "Backslash", "\\\\"),
	KeyC(Keyboard.KEY_C, "C"),
	KeyCAPITAL(Keyboard.KEY_CAPITAL, "Capital", "caps( lock)?"),
	KeyCIRCUMFLEX(Keyboard.KEY_CIRCUMFLEX, "Circumflex", "^"),
	KeyCOMMA(Keyboard.KEY_COMMA, "Command", ","),
	KeyCONVERT(Keyboard.KEY_CONVERT, "Convert"),
	KeyD(Keyboard.KEY_D, "D"),
	KeyDELETE(Keyboard.KEY_DELETE, "Delete", "del"),
	KeyDOWN(Keyboard.KEY_DOWN, "Down"),
	KeyE(Keyboard.KEY_E, "E"),
	KeyEND(Keyboard.KEY_END, "End"),
	KeyEQUALS(Keyboard.KEY_EQUALS, "Equals", "eq|="),
	KeyEsc(Keyboard.KEY_ESCAPE, "Escape", "esc(ape)?"),
	KeyF(Keyboard.KEY_F, "F"),
	KeyF1(Keyboard.KEY_F1, "F1"),
	KeyF10(Keyboard.KEY_F10, "F10"),
	KeyF11(Keyboard.KEY_F11, "F11"),
	KeyF12(Keyboard.KEY_F12, "F12"),
	KeyF13(Keyboard.KEY_F13, "F13"),
	KeyF14(Keyboard.KEY_F14, "F14"),
	KeyF15(Keyboard.KEY_F15, "F15"),
	KeyF2(Keyboard.KEY_F2, "F2"),
	KeyF3(Keyboard.KEY_F3, "F3"),
	KeyF4(Keyboard.KEY_F4, "F4"),
	KeyF5(Keyboard.KEY_F5, "F5"), /* accent grave */
	KeyF6(Keyboard.KEY_F6, "F6"),
	KeyF7(Keyboard.KEY_F7, "F7"),
	KeyF8(Keyboard.KEY_F8, "F8"),
	KeyF9(Keyboard.KEY_F9, "F9"),
	KeyG(Keyboard.KEY_G, "G"),
	KeyGRAVE(Keyboard.KEY_GRAVE, "Grave", "´"),
	KeyH(Keyboard.KEY_H, "H"),
	KeyHOME(Keyboard.KEY_HOME, "Home"),
	KeyI(Keyboard.KEY_I, "I"),
	KeyINSERT(Keyboard.KEY_INSERT, "Insert", "ins"),
	KeyJ(Keyboard.KEY_J, "J"), /* . on main keyboard */
	KeyK(Keyboard.KEY_K, "K"), /* / on main keyboard */
	KeyKANA(Keyboard.KEY_KANA, "Kana"),
	KeyKANJI(Keyboard.KEY_KANJI, "Kanji"), /* * on numeric keypad */
	KeyL(Keyboard.KEY_L, "L"), /* left Alt */
	KeyLBRACKET(Keyboard.KEY_LBRACKET, "Left bracket", "\\(|l(eft )?bracket"),
	KeyLCONTROL(Keyboard.KEY_LCONTROL, "Left control", "l(eft )?(ctrl|control)"),
	KeyLEFT(Keyboard.KEY_LEFT, "Left"),
	KeyLMENU(Keyboard.KEY_LMENU, "Left alt", "l(eft )?alt"),
	KeyLMETA(Keyboard.KEY_LMETA, "Left Meta", "l(eft )?(meta|win(dows)?|apple)"),
	KeyLSHIFT(Keyboard.KEY_LSHIFT, "Left shift", "l(eft )?shift"),
	KeyM(Keyboard.KEY_M, "M"),
	KeyMinus(Keyboard.KEY_MINUS, "Minus", "min|-"),
	KeyMULTIPLY(Keyboard.KEY_MULTIPLY, "Multiply", "\\*|mul(ti)?"),
	KeyN(Keyboard.KEY_N, "N"),
	KeyNEXT(Keyboard.KEY_NEXT, "Page down", "pg( )?(dow)?n|next"),
	KeyNOCONVERT(Keyboard.KEY_NOCONVERT, "No convert"),
	KeyNUMLOCK(Keyboard.KEY_NUMLOCK, "Numlock"),
	KeyNUMPAD0(Keyboard.KEY_NUMPAD0, "0 (numpad)", "num(pad)?\\s?0"), /* Scroll Lock */
	KeyNUMPAD1(Keyboard.KEY_NUMPAD1, "1 (numpad)", "num(pad)?\\s?1"),
	KeyNUMPAD2(Keyboard.KEY_NUMPAD2, "2 (numpad)", "num(pad)?\\s?2"),
	KeyNUMPAD3(Keyboard.KEY_NUMPAD3, "3 (numpad)", "num(pad)?\\s?3"),
	KeyNUMPAD4(Keyboard.KEY_NUMPAD4, "4 (numpad)", "num(pad)?\\s?4"),
	KeyNUMPAD5(Keyboard.KEY_NUMPAD5, "5 (numpad)", "num(pad)?\\s?5"),
	KeyNUMPAD6(Keyboard.KEY_NUMPAD6, "6 (numpad)", "num(pad)?\\s?6"),
	KeyNUMPAD7(Keyboard.KEY_NUMPAD7, "7 (numpad)", "num(pad)?\\s?7"),
	KeyNUMPAD8(Keyboard.KEY_NUMPAD8, "8 (numpad)", "num(pad)?\\s?8"), /* + on numeric keypad */
	KeyNUMPAD9(Keyboard.KEY_NUMPAD9, "9 (numpad)", "num(pad)?\\s?9"),
	KeyNumpadDECIMAL(Keyboard.KEY_DECIMAL, "Comma (numpad)", "num(pad)?\\s?(,|dec(imal)?)"),
	KeyNumpadDIVIDE(Keyboard.KEY_DIVIDE, "Divide (numpad", "num(pad)?\\s(/|div(ide)?)"),
	KeyNUMPADENTER(Keyboard.KEY_NUMPADENTER, "Enter (numpad)", "num(pad)?\\s?(ent(er)?|ret(urn)?)"),
	KeyNUMPADEQUALS(Keyboard.KEY_NUMPADEQUALS, "Equals (numpad)", "num(pad)?\\s?(=|eq(uals)?)"),
	KeySUBTRACT(Keyboard.KEY_SUBTRACT, "Subtract (numpad)",
			"sub(tract)?|num(pad)?\\s(-|min(us)?|sub(tract)?)"), /* Home on arrow keypad */
	KeyO(Keyboard.KEY_O, "O"),
	KeyP(Keyboard.KEY_P, "P"),
	KeyPAUSE(Keyboard.KEY_PAUSE, "Pause"),
	KeyPERIOD(Keyboard.KEY_PERIOD, "Period", "\\."),
	KeyPRIOR(Keyboard.KEY_PRIOR, "Page up", "pg( )?up|prior"),
	KeyQ(Keyboard.KEY_Q, "Q"), /* (Japanese keyboard)            */
	KeyR(Keyboard.KEY_R, "R"), /* (Japanese keyboard)            */
	KeyRBRACKET(Keyboard.KEY_RBRACKET, "Right bracket", "\\)|r(ight )?bracket"), /* (Japanese keyboard)            */
	KeyRCONTROL(Keyboard.KEY_RCONTROL, "Right control", "r(ight )?(control|ctrl)"), /* (Japanese keyboard)            */
	KeyRETURN(Keyboard.KEY_RETURN, "Enter", "ent|ret(urn)?"),
	KeyRIGHT(Keyboard.KEY_RIGHT, "Right"), /* (Japanese keyboard)            */
	KeyRMENU(Keyboard.KEY_RMENU, "Right alt", "r(ight )?alt"), /* (Japanese keyboard)            */
	KeyRMETA(Keyboard.KEY_RMETA, "Right Meta", "r(ight )?(meta|win(dows)?|apple)"), /*                     (Japan AX) */
	KeyRSHIFT(Keyboard.KEY_RSHIFT, "Right shift", "r(ight )?shift"),
	KeyS(Keyboard.KEY_S, "S"),
	KeySCROLL(Keyboard.KEY_SCROLL, "Scroll"),
	KeySEMICOLON(Keyboard.KEY_SEMICOLON, "Semicolon", ";"),
	KeySLASH(Keyboard.KEY_SLASH, "Slash", "/"), /* right Alt */
	KeySPACE(Keyboard.KEY_SPACE, "Space"), /* Pause */

	KeySYSRQ(Keyboard.KEY_SYSRQ, "SYSRQ"), /* UpArrow on arrow keypad */
	KeyT(Keyboard.KEY_T, "T"), /* PgUp on arrow keypad */
	KeyTAB(Keyboard.KEY_TAB, "Tab", "tabulator"), /* LeftArrow on arrow keypad */
	KeyU(Keyboard.KEY_U, "U"), /* RightArrow on arrow keypad */
	KeyUP(Keyboard.KEY_UP, "Up"), /* End on arrow keypad */
	KeyV(Keyboard.KEY_V, "V"), /* DownArrow on arrow keypad */
	KeyW(Keyboard.KEY_W, "W"), /* PgDn on arrow keypad */
	KeyX(Keyboard.KEY_X, "X"), /* Insert on arrow keypad */
	KeyY(Keyboard.KEY_Y, "Y"), /* Delete on arrow keypad */
	KeyYEN(Keyboard.KEY_YEN, "Yen", "¥"),
	KeyZ(Keyboard.KEY_Z, "Z"),
	;

	private int keycode;
	private String name;
	private String regex;

	private Keys(final int keycode, final String name) {
		this(keycode, name, null);
	}

	private Keys(final int keycode, final String name, final String regex) {
		this.keycode = keycode;
		this.name = name;
		if (regex != null) {
			this.regex = "(?i)" + regex;
		}
	}

	public static List<Keys> apropos(final String str) {
		final List<Keys> result = new ArrayList<Keys>();
		final String lname = str.toLowerCase();
		for (final Keys k: Keys.values()) {
			boolean match = k.name.toLowerCase().contains(lname);
			if (!match && k.regex != null) {
				match = str.matches(k.regex);
			}
			if (match) {
				result.add(k);
			}
		}
		return result;
	}

	public static Keys find(final String name) {
		if (name.matches("#[0-9A-Fa-f]+")) {
			final String codeStr = name.substring(1);
			System.out.println(codeStr);
			final Integer keycode = StringTools.getInteger(codeStr);
			return (keycode != null)? findByKeycode(keycode) : null;
		}
		for (final Keys k: Keys.values()) {
			boolean match = k.name.equalsIgnoreCase(name);
			if (!match && k.regex != null) {
				match = name.matches(k.regex);
			}
			if (match) {
				return k;
			}
		}
		return null;
	}

	public static Keys findByKeycode(final int keycode) {
		for (final Keys k: Keys.values()) {
			if (k.keycode == keycode) {
				return k;
			}
		}
		return null;
	}

	public static String getDump() {
		final StringBuilder result = new StringBuilder();
		for (final Keys k: Keys.values()) {
			result.append(k.toString() + "\n");
		}
		return result.toString();
	}

	public KeyBinding bind(final String description) {
		return new KeyBinding(description, keycode);
	}

	public KeyBinding unbind() {
		final KeyBinding b = getKeyBinding();
		if (b == null) {
			return null;
		} else {
			KeyBinding.keybindArray.remove(b);
			return (KeyBinding)(KeyBinding.hash.removeObject(keycode));
		}
	}

	public boolean isBound() {
		return getKeyBinding() != null;
	}

	public KeyBinding getKeyBinding() {
		return (KeyBinding)(KeyBinding.hash.lookup(keycode));
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "0x" + Integer.toHexString(keycode).toUpperCase() + ": " + name;
	}

	public int getKeycode() {
		return keycode;
	}
}
