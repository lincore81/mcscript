package xde.lincore.mcscript;

public enum Keyword {
	Alias("alias|als"),
	Cat("cat|type"),
	Config("config|cfg"),
	Current("current"),
	Default("default"),
	Engine("engine"),
	Engines("engines"),
	Files("files|fls"),
	Get("get"),
	Help("h(elp)?|\\?"),
	Info("(i)?(nfo)?"),	
	Kill("kill"),
	List("list|ls"),
	Manager("manager|mgr|fm"),
	Reload("reload"),
	Remove("remove|rm"),
	Reset("reset"),
	Save("save"),
	Set("set"),
	Threads("threads"),
	NoMatch("")
	;
	private String regex;
	private Keyword(String regex) {
		this.regex = "(?i)" + regex;
	}
	
	public static Keyword findMatch(String token) {
		for (Keyword k: Keyword.values()) {			
			if (token.matches(k.regex)) {
				return k;
			}
		}
		return NoMatch;
	}
	
	public boolean matches(String token) {
		return token.matches(regex);
	}
}
