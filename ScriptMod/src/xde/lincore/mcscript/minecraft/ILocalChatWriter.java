package xde.lincore.mcscript.minecraft;

public interface ILocalChatWriter {
	public void echo(final Object obj);
	
	public void err(final Object obj);
	
	public void format(final String format, final Object... args);
}