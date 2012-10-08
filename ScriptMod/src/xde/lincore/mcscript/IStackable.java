package xde.lincore.mcscript;

public interface IStackable extends ICollectable {
	public int getAmount();
	public void setAmount(int amount);
	public void addAmount(int amount);
	public boolean canMerge(IStackable other);
	public IStackable merge(IStackable other);
}
