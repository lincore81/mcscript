package xde.lincore.mcscript;

import net.minecraft.src.ItemStack;

public interface IStackable {
	public ItemStack getItemStack(int quantity);
}
