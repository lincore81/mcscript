package xde.lincore.mcscript.minecraft;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.ICollectable;
import xde.lincore.mcscript.IItem;
import xde.lincore.mcscript.TargetTypes;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;

/**
 * Among other utility methods that don't belong to a class of this package,
 * this class provides various static type conversion methods between Minecraft's
 * original data types and mcscript's equivalents that are not part of this package
 * and should thus not access minecraft code.
 * 
 */
public final class MinecraftUtils {
	private MinecraftUtils() {}

	protected static Vec3 toVec3(final Vector3d v) {
		return Vec3.createVectorHelper(v.x, v.y, v.z);
	}

	protected static Vector3d toVector3d(final Vec3 v) {
		return new Vector3d(v.xCoord, v.yCoord, v.zCoord);
	}

	protected static Voxel toVoxel(final ChunkCoordinates coords) {
		return new Voxel(coords.posX, coords.posY, coords.posZ);
	}

	protected static ChunkCoordinates toChunkCoordinates(final Voxel voxel) {
		return new ChunkCoordinates(voxel.x, voxel.y, voxel.z);
	}
	
	protected static Block toMcBlock(final IBlock block) {
		int id = block.getId();
		final Block[] list = Block.blocksList;
		if (id == 0) {
			return null;
		}
		else if (id < 0 || id >= list.length) {
			throw new IllegalArgumentException("Bad block id: " + String.valueOf(id));
		}
		else {
			return list[id];
		}
	}
	
	protected static Item toMcItem(final IItem item) {
		if (item.getId() >= 0 && item.getId() < Item.itemsList.length) {
			return Item.itemsList[item.getId()];
		}
		else {
			throw new IllegalArgumentException("Bad item id: " + String.valueOf(item.getId()));
		}
	}
	
	protected static ICollectable toCollectable(final ItemStack stack) {
		if (stack == null) return null;
		return new CollectableData(stack.itemID, stack.getItemDamage());
	}
	
	protected static ItemStack toItemStack(final ICollectable item, final int size) {
		if (item instanceof IBlock) {
			Block mcBlock = toMcBlock((IBlock)item);
			return new ItemStack(mcBlock, size, item.getMeta());
		}
		else if (item instanceof IItem) {
			Item mcItem = toMcItem((IItem)item);
			return new ItemStack(mcItem, size, item.getMeta());
		}
		else {
			return new ItemStack(item.getId(), size, item.getMeta());
		}
	}
	
	protected static int getMaxStackSize(final IItem item) {
		return toMcItem(item).getItemStackLimit();
	}
	
	protected static TargetTypes toTargetType(final MovingObjectPosition mop) {
		if (mop == null) return null;
		
		switch(mop.typeOfHit) {
			case TILE: 		return TargetTypes.Block;
			case ENTITY:	return TargetTypes.Entity;
			default:
				throw new UnsupportedOperationException("Unknown EnumMovingObjectType element.");
		}
	}
	
	public static File getMinecraftDirectory() {
		return Minecraft.getMinecraftDir();
	}
}
