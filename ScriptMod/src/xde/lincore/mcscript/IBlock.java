package xde.lincore.mcscript;

public interface IBlock {
	public int getId();
	public int getMeta();
	public String getName();
	public boolean hasMeta();
	public boolean hasNbtData();
}
