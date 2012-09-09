package xde.lincore.mcscript.wrapper;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.Items;
import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.mcscript.IStackable;

public class InventoryWrapper extends WrapperBase {

	protected InventoryWrapper(ScriptingEnvironment env) {
		super(env);
	}
	
	public void giveItem(Items item) {
		giveItem(item, item.getMaxStackSize());
	}
	
	public void giveItem(Items item, int amount) {
		int amount_ = (amount > 0)? amount : 1; 
		env.getUser().inventory.addItemStackToInventory(item.getItemStack(amount));
	}
	
	public void holdItem(IStackable item, int quantity) {
		int slot = getHotbarIndex();
		env.getUser().inventory.setInventorySlotContents(slot, item.getItemStack(quantity));
	}
	
	public void setSlotContent(int slot, IStackable item, int quantity) {
		env.getUser().inventory.setInventorySlotContents(slot, item.getItemStack(quantity));
	}
	
	public int getHotbarIndex() {
		return env.getUser().inventory.currentItem;
	}
	
	

}
