package xde.lincore.mcscript.minecraft;

import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.G;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.selection.ISelection;
import xde.lincore.util.StringTools;
import net.minecraft.client.Minecraft;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class WorldWrapper extends AbstractWrapper {

	protected WorldWrapper(ScriptEnvironment env, MinecraftWrapper mc) {
		super(env, mc);
	}

	public void explode(Voxel loc, float strength) {
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		EntityPlayer user = env.getUser();
		user.worldObj.createExplosion((Entity) null, loc.x, loc.y, loc.z,
				strength);
	}

	public void fillSelection(ISelection selection, int blockId, int blockDamage) {
		for (Voxel v : selection.getVoxels()) {
			setBlock(v, blockId, blockDamage);
		}
	}

	public void fillSelection(ISelection selection, Blocks block) {
		fillSelection(selection, block.getId(), block.getMeta());
	}

	public Blocks getBlock(Voxel v) {
		World world = env.getUser().worldObj;
		return Blocks.findById(world.getBlockId(v.x, v.y, v.z),
				world.getBlockMetadata(v.x, v.y, v.z));
	}

	public Voxel getHeightVoxel(int x, int z) {
		World world = env.getUser().worldObj;
		int y = world.getHeightValue(x, z);
		return new Voxel(x, y, z);
	}

	public int getMaxHeight() {
		return env.getUser().worldObj.getHeight() - 1;
	}

	public int getBlockId(int x, int y, int z) {
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		EntityPlayer user = env.getUser();
		return user.worldObj.getBlockId(x, y, z);
	}

	public int getBlockId(Voxel loc) {
		return getBlockId(loc.x, loc.y, loc.z);
	}

	public int getBlockMeta(int x, int y, int z) {
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		EntityPlayer user = env.getUser();
		return user.worldObj.getBlockMetadata(x, y, z);
	}
	
	public int getBlockMeta(Voxel v) {
		return getBlockMeta(v.x, v.y, v.z);
	}

	public long getSeed() {
		EntityPlayer user = env.getUser();
		return user.worldObj.getWorldInfo().getSeed();
	}
	
	private boolean isValidPosition(Voxel position) {
		return !(position.x >= 30000000 || position.x <= -30000000 ||
				 position.z >= 30000000 || position.z <= -30000000 ||
				 position.y < 0 && position.y > getMaxHeight());
	}

	public Vector3d raytrace(Vector3d position, Vector3d direction, double distance) {
		EntityPlayer user = env.getUser();
		Vector3d endVector = position.add(direction.multiply(distance));
		MovingObjectPosition result = user.worldObj.rayTraceBlocks_do_do(
				position.toVec3(), endVector.toVec3(), true, true);
		return new Vector3d(result.hitVec.xCoord, result.hitVec.yCoord,
				result.hitVec.zCoord);

		// EntityPlayer user = env.getCurrentUser();
		// MovingObjectPosition result = user.rayTrace(distance, 1f);
		// return new Vector(result.blockX, result.blockY, result.blockZ);

		// World world = user.worldObj;
		// Vector endVector = position.add(direction.multiply(distance));
		// MovingObjectPosition result = world.rayTraceBlocks(position.toVec3(),
		// endVector.toVec3());
		// return new Vector(result.blockX, result.blockY, result.blockZ);
	}

	public boolean setBlock(int x, int y, int z, int blockId, int metadata) {
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		EntityPlayer user = env.getUser();
		return user.worldObj.setBlockAndMetadata(x, y, z, blockId, metadata);
	}

	public boolean setBlock(int x, int y, int z, String blockIdAndMeta) {
		String[] data = blockIdAndMeta.split("\\s*:\\s*");
		if (data.length < 1 || data.length > 2
				|| !StringTools.isValidNumber(data[0])
				|| !StringTools.isValidNumber(data[1])) {
			G.LOG.warning("Unable to parse String " + blockIdAndMeta
					+ ".");
			return false;
		} else {
			int id = Double.valueOf(data[0]).intValue();
			int meta = (data.length == 2) ? Double.valueOf(data[1]).intValue()
					: 0;
			System.out.format("id: %d, meta: %d", id, meta);
			return setBlock(x, y, z, id, meta);
		}

	}

	public boolean setBlock(Voxel v, Blocks block) {
		return setBlock(v.x, v.y, v.z, block.getId(), block.getMeta());
	}
	
	public boolean setBlock(Voxel v, IBlock block) {
		return setBlock(v.x, v.y, v.z, block.getId(), block.getMeta());
	}

	public boolean setBlock(Voxel v, int id, int metadata) {
		return setBlock(v.x, v.y, v.z, id, metadata);
	}

	public boolean setBlockId(int x, int y, int z, int blockId) {
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		EntityPlayer user = env.getUser();
		return user.worldObj.setBlock(x, y, z, blockId);
	}

	public boolean setBlockId(Voxel loc, int blockId) {
		return setBlockId(loc.x, loc.y, loc.z, blockId);
	}

	// public float getRainStrength() {
	// EntityPlayer user = env.getCurrentUser();
	// return user.worldObj.rai
	// }

	public boolean setBlockMeta(int x, int y, int z, int metadata) {
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		EntityPlayer user = env.getUser();
		return user.worldObj.setBlockMetadata(x, y, z, metadata);
	}

	public boolean setBlockMeta(Voxel loc, int metadata) {
		return setBlockId(loc.x, loc.y, loc.z, metadata);
	}
	
	public void startEdit() {
		env.getUser().worldObj.editingBlocks = true;
	}
	
	public void endEdit() {
		env.getUser().worldObj.editingBlocks = false;
	}
	
	public boolean canEdit() {
		return !env.getUser().worldObj.editingBlocks;
	}

	public void setRaining(boolean raining) {
		EntityPlayer user = env.getUser();
		user.worldObj.getWorldInfo().setRaining(raining);
	}

	public void setRainStrength(float strength) {
		EntityPlayer user = env.getUser();
		user.worldObj.setRainStrength(strength);
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

	public void setBlock(Voxel position, BlockData block) {
		setBlock(position, block.getId(), block.getMeta());
	}
	
	public BlockData getBlockData(Voxel position) {
		return new BlockData(getBlockId(position), getBlockMeta(position));
	}

}
