package xde.lincore.mcscript.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import xde.lincore.mcscript.Dimensions;
import xde.lincore.mcscript.ICollectable;
import xde.lincore.mcscript.IItem;
import xde.lincore.mcscript.TargetTypes;
import xde.lincore.mcscript.Voxel;

public final class PlayerFacade extends EntityFacade {

	private final EntityPlayer player;
	
	protected PlayerFacade(final EntityPlayer player) {
		super(player);
		this.player = player;
	}
	
	@Override
	public String getName() {
		return player.getEntityName();
	}
	
	public Dimensions getDimension() {
		return Dimensions.fromValue(player.dimension);
	}
	
	private static MovingObjectPosition getMouseOver() {
		return Minecraft.getMinecraft().objectMouseOver;
	}
	
	public static TargetTypes hasTarget() {
		return MinecraftUtils.toTargetType(getMouseOver());
	}
	
	public static PlayerFacade getLocalPlayer() {
		return new PlayerFacade(Minecraft.getMinecraft().thePlayer);
	}
	
	protected static EntityPlayer getLocalMcEntityPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public void sendChatMessage(final String message) {
		player.sendChatToPlayer(message);
	}
	
	
	
	public static Voxel getTargetBlock() {
		MovingObjectPosition mop = getMouseOver();
		if (mop == null || mop.typeOfHit !=
				EnumMovingObjectType.TILE) {
			return null;
		} else {
			return new Voxel(mop.blockX, mop.blockY, mop.blockZ);
		}
	}
	
	public static EntityFacade getTargetEntity() {
		final Minecraft minecraft = ModLoader.getMinecraftInstance();
		if (minecraft.objectMouseOver == null || minecraft.objectMouseOver.typeOfHit !=
				EnumMovingObjectType.ENTITY) {
			return null;
		} else {
			return new EntityFacade(minecraft.objectMouseOver.entityHit);
		}
	}
	
	public void giveItem(final int id, final int meta, final int amount) {
		ItemStack stack = new ItemStack(id, amount, meta);
		player.inventory.addItemStackToInventory(stack);
	}
	
	public void giveItem(final IItem item) {
		giveItem(item, MinecraftUtils.getMaxStackSize(item));
	}

	public void giveItem(final IItem item, final int amount) {
		final int amount_ = (amount > 0)? amount : 1;
		ItemStack stack = MinecraftUtils.toItemStack(item, amount);
		player.inventory.addItemStackToInventory(stack);
	}

	public void holdItem(final ICollectable item, final int quantity) {
		final int slot = getHotbarIndex();
		player.inventory.setInventorySlotContents(slot, MinecraftUtils.toItemStack(item, quantity));
	}

	public void setSlotContent(final int slot, final ICollectable item, final int quantity) {
		player.inventory.setInventorySlotContents(slot,  MinecraftUtils.toItemStack(item, quantity));
	}

	public int getHotbarIndex() {
		return player.inventory.currentItem;
	}
	
	public int getFirstFreeSlotIndex() {
		return player.inventory.getFirstEmptyStack();
	}
	
	public ICollectable getItemHeld() {
		ItemStack stack = player.inventory.getStackInSlot(getHotbarIndex());
		return MinecraftUtils.toCollectable(stack);
	}
}
