package xde.lincore.mcscript.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.G;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.selection.ISelection;
import xde.lincore.util.StringTools;

public class WorldWrapper extends AbstractWrapper {

	protected WorldWrapper(final ScriptEnvironment env, final MinecraftWrapper mc) {
		super(env, mc);
	}
	
	public World getWorld() {
		MinecraftServer server = MinecraftServer.getServer();
		int dimension = env.getUser().dimension;
		return server.worldServerForDimension(dimension);
	}	

	public void explode(final Voxel loc, final float strength) {		
		getWorld().createExplosion((Entity) null, loc.x, loc.y, loc.z,
				strength);
	}

	public void fillSelection(final ISelection selection, final int blockId, final int blockDamage) {
		for (final Voxel v : selection.getVoxels()) {
			setBlock(v, blockId, blockDamage);
		}
	}

	public void fillSelection(final ISelection selection, final Blocks block) {
		fillSelection(selection, block.getId(), block.getMeta());
	}

	public Blocks getBlock(final Voxel v) {
		return Blocks.findById(getWorld().getBlockId(v.x, v.y, v.z),
				getWorld().getBlockMetadata(v.x, v.y, v.z));
	}

	public Voxel getHeightVoxel(final int x, final int z) {		
		final int y = getWorld().getHeightValue(x, z);
		return new Voxel(x, y, z);
	}

	public int getMaxHeight() {
		return getWorld().getHeight() - 1;
	}

	public int getBlockId(final int x, final int y, final int z) {		
		return getWorld().getBlockId(x, y, z);
	}

	public int getBlockId(final Voxel loc) {
		return getBlockId(loc.x, loc.y, loc.z);
	}

	public int getBlockMeta(final int x, final int y, final int z) {		
		return getWorld().getBlockMetadata(x, y, z);
	}

	public int getBlockMeta(final Voxel v) {
		return getBlockMeta(v.x, v.y, v.z);
	}

	public long getSeed() {		
		return getWorld().getWorldInfo().getSeed();
	}

	public boolean isValidPosition (final Voxel position) {
		return !(position.x >= 30000000 || position.x <= -30000000 ||
				 position.z >= 30000000 || position.z <= -30000000 ||
				 position.y < 0 && position.y > getMaxHeight());
	}

// TODO: fix WorldWrapper.raytrace
//	public Vector3d raytrace(final Vector3d position, final Vector3d direction, final double distance) {
//		final EntityPlayer user = env.getUser();
//		final Vector3d endVector = position.add(direction.multiply(distance));
//		final MovingObjectPosition result = user.worldObj.rayTraceBlocks_do_do(
//				position.toVec3(), endVector.toVec3(), true, true);
//		return new Vector3d(result.hitVec.xCoord, result.hitVec.yCoord,
//				result.hitVec.zCoord);
//
//		// EntityPlayer user = env.getCurrentUser();
//		// MovingObjectPosition result = user.rayTrace(distance, 1f);
//		// return new Vector(result.blockX, result.blockY, result.blockZ);
//
//		// World world = user.worldObj;
//		// Vector endVector = position.add(direction.multiply(distance));
//		// MovingObjectPosition result = world.rayTraceBlocks(position.toVec3(),
//		// endVector.toVec3());
//		// return new Vector(result.blockX, result.blockY, result.blockZ);
//	}

	public boolean setBlock(final int x, final int y, final int z, final int blockId, final int metadata) {
		return getWorld().setBlockAndMetadata(x, y, z, blockId, metadata);
	}
	
//	private World getMcWorld() {		
//		MinecraftServer server = MinecraftServer.getServer();		
//	}

//	public boolean setBlock(final int x, final int y, final int z, final String blockIdAndMeta) {
//		final String[] data = blockIdAndMeta.split("\\s*:\\s*");
//		if (data.length < 1 || data.length > 2
//				|| !StringTools.isValidNumber(data[0])
//				|| !StringTools.isValidNumber(data[1])) {
//			G.LOG.warning("Unable to parse String " + blockIdAndMeta
//					+ ".");
//			return false;
//		} else {
//			final int id = Double.valueOf(data[0]).intValue();
//			final int meta = (data.length == 2) ? Double.valueOf(data[1]).intValue()
//					: 0;
//			System.out.format("id: %d, meta: %d", id, meta);
//			return setBlock(x, y, z, id, meta);
//		}
//
//	}

//	public boolean setBlock(final Voxel v, final Blocks block) {
//		return setBlock(v.x, v.y, v.z, block.getId(), block.getMeta());
//	}

	public boolean setBlock(final Voxel v, final IBlock block) {
		assert block != null : "Argument 'block' must not be null!";
		return setBlock(v.x, v.y, v.z, block.getId(), block.getMeta());
	}

	public boolean setBlock(final Voxel v, final int id, final int metadata) {
		return setBlock(v.x, v.y, v.z, id, metadata);
	}

//	public boolean setBlockId(final int x, final int y, final int z, final int blockId) {
//		final Minecraft minecraft = ModLoader.getMinecraftInstance();
//		final EntityPlayer user = env.getUser();
//		return user.worldObj.setBlock(x, y, z, blockId);
//	}
//
//	public boolean setBlockId(final Voxel loc, final int blockId) {
//		return setBlockId(loc.x, loc.y, loc.z, blockId);
//	}
//
//	// public float getRainStrength() {
//	// EntityPlayer user = env.getCurrentUser();
//	// return user.worldObj.rai
//	// }
//
//	public boolean setBlockMeta(final int x, final int y, final int z, final int metadata) {
//		final Minecraft minecraft = ModLoader.getMinecraftInstance();
//		final EntityPlayer user = env.getUser();
//		return user.worldObj.setBlockMetadata(x, y, z, metadata);
//	}
//
//	public boolean setBlockMeta(final Voxel loc, final int metadata) {
//		return setBlockId(loc.x, loc.y, loc.z, metadata);
//	}

	public void startEdit() {
		System.out.println("startEdit called from: " + Thread.currentThread());
		getWorld().editingBlocks = true;
	}

	public void endEdit() {
		getWorld().editingBlocks = false;
	}

	public boolean canEdit() {
		return !getWorld().editingBlocks;
	}

	public void setRaining(final boolean raining) {
		getWorld().getWorldInfo().setRaining(raining);
		System.out.println("setRaining called from: " + Thread.currentThread());
	}

	public void setRainStrength(final float strength) {
		getWorld().setRainStrength(strength);
	}


	// public Vector raytrace(Vector position, Vector direction, double
	// distance) {
	// EntityPlayer user = env.getUser();
	// Vector endVector = position.add(direction.multiply(distance));
	// MovingObjectPosition result = user.worldObj.rayTraceBlocks_do_do(
	// position.toVec3(), endVector.toVec3(), true, true);
	// return new Vector(result.hitVec.xCoord, result.hitVec.yCoord,
	// result.hitVec.zCoord);
	// }

	public void sunshine() {
		setRaining(false);
	}

//	public void setBlock(final Voxel position, final BlockData block) {
//		setBlock(position, block.getId(), block.getMeta());
//	}

	public BlockData getBlockData(final Voxel position) {
		return new BlockData(getBlockId(position), getBlockMeta(position));
	}

}
