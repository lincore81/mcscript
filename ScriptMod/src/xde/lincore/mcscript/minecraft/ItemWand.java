package xde.lincore.mcscript.minecraft;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemWand extends Item {
	
	private final WandFacade facade;
		
	protected ItemWand(final int id, final WandFacade facade) {
		super(id);
		this.facade = facade;
		this.maxStackSize = 1;
		this.setMaxDamage(0);
		this.hasSubtypes = true;
		this.bFull3D = true;
	}

	@Override
	public int getIconFromDamage(final int meta) {
		return facade.getIconIndex(meta);
	}

	@Override
	public float getStrVsBlock(final ItemStack par1ItemStack, final Block par2Block) {
		return 30f;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world,
			final EntityPlayer player) {
		facade.onWandUse(stack.getItemDamage());
		return stack;
	}

	@Override
	public int getDamageVsEntity(final Entity par1Entity) {
		return 30;
	}

	@Override
	public String getItemNameIS(final ItemStack stack) {
		return facade.getItemName(stack.getItemDamage());
	}

	@Override
	public void addInformation(final ItemStack par1ItemStack, final List par2List) {
		// TODO Implement ItemWand.addInformation
		super.addInformation(par1ItemStack, par2List);
	}

	@Override
	public String getItemDisplayName(final ItemStack stack) {
		return getItemNameIS(stack);
	}

	@Override
	public void getSubItems(final int id, final CreativeTabs tab, final List stackList) {
		for (int meta = 0; meta < facade.getSubtypeCount(); meta++) {
			stackList.add(new ItemStack(id, 1, meta));
		}
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return CreativeTabs.tabTools;
	}
}
