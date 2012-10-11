package xde.lincore.mcscript.ui;

public enum Keywords {
	Alias		("a(lias)?|als"),
	Cat			("cat|type"),
	ChangeDir	("cd|chdir"),
	Config		("c(onfig)?|cfg"),
	Current		("current"),
	Cwd			("cwd"),
	Default		("def(ault)?"),
	Editor		("ed(it(or)?)?"),
	Engine		("engine"),
	Engines		("engines"),
	Files		("files|f"),
	Find		("find"),
	Get			("get"),
	Help		("h(elp)?|\\?"),
	Info		("i(nfo)?"),
	Key			("k(ey(s)?)?"),
	Kill		("kill"),
	List		("list|ls"),
	Manager		("manager|mgr|fm"),
	Reload		("reload"),
	Remove		("remove|rm"),
	Reset		("reset"),
	Save		("save"),
	Set			("set"),
	Scripts		("scripts|sc"),
	NoMatch		(""),
	;
	private String regex;
	private Keywords(final String regex) {
		this.regex = "(?i)" + regex;
	}

	public static Keywords findMatch(final String token) {
		for (final Keywords k: Keywords.values()) {
			if (token.matches(k.regex)) {
				return k;
			}
		}
		return NoMatch;
	}

	public boolean matches(final String token) {
		return token.matches(regex);
	}

	public static String getPathString(final Keywords... keywords) {
		final StringBuilder result = new StringBuilder();
		int i = 0;
		for (final Keywords k: keywords) {
			result.append(k.toString().toLowerCase());
			if (++i < keywords.length) {
				result.append(" ");
			}
		}
		return result.toString();
	}
}
