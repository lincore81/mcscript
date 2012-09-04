package xde.lincore.util.parse;

public interface IParameter {
	public abstract boolean matches(String str);
	public abstract boolean rawMatches(String str);
}