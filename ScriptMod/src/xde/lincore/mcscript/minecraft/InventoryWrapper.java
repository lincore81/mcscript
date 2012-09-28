package xde.lincore.mcscript.minecraft;

import xde.lincore.mcscript.IStackable;
import xde.lincore.mcscript.Items;
import xde.lincore.mcscript.env.ScriptEnvironment;

public class InventoryWrapper extends AbstractWrapper {

	protected InventoryWrapper(final ScriptEnvironment env, final MinecraftWrapper mc) {
			super(env, mc);
	}

	public void giveItem(final Items item) {
		giveItem(item, item.getMaxStackSize());
	}

	public void giveItem(final Items item, final int amount) {
		final int amount_ = (amount > 0)? amount : 1;
		env.getUser().inventory.addItemStackToInventory(item.getItemStack(amount));
	}

	public void holdItem(final IStackable item, final int quantity) {
		final int slot = getHotbarIndex();
		env.getUser().inventory.setInventorySlotContents(slot, item.getItemStack(quantity));
	}

	public void setSlotContent(final int slot, final IStackable item, final int quantity) {
		env.getUser().inventory.setInventorySlotContents(slot, item.getItemStack(quantity));
	}

	public int getHotbarIndex() {
		return env.getUser().inventory.currentItem;
	}



}
