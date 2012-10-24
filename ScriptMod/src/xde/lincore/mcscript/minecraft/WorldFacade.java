package xde.lincore.mcscript.minecraft;

import java.util.Map;

import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.WorldServer;
import xde.lincore.mcscript.BlockData;
import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.Dimensions;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.WeatherTypes;
import xde.lincore.mcscript.edit.VoxelMap;

public class WorldFacade {
	
	private WorldServer mcWorld;
	
	
	protected WorldFacade(final WorldServer mcWorld) {
		setMcWorld(mcWorld);
	}
	
	public boolean canEdit() {
		return !mcWorld.editingBlocks;
	}
	
	public boolean endEdit() {
		if (!canEdit()) {
			mcWorld.editingBlocks = false;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void explode(final Vector3d position, final float strength) {
		mcWorld.createExplosion(null, position.x, position.y, position.z, strength);
	}
	
	public void explode(final EntityFacade entity, final float strength) {
		Vector3d pos = entity.getPosition();
		mcWorld.createExplosion(entity.getMcEntity(), pos.x, pos.y, pos.z, strength);
	}
	
	public String getBiomeName(final int x, final int z) {
		return mcWorld.getBiomeGenForCoords(x, z).biomeName;
	}
	
//	public void setSeed(long seed) {
//	}
	
	//public void rawSetBlock(IBlock block, Voxel position);
	
	public IBlock getBlock(final Voxel position) {
		int id = mcWorld.getBlockId(position.x, position.y, position.z);
		int meta = mcWorld.getBlockMetadata(position.x, position.y, position.z);
		return new BlockData(id, meta);
	}
	
	public VoxelMap getBlocks(final VoxelMap voxels) {
		for (Map.Entry<Voxel, IBlock> e: voxels) {
			Voxel position = e.getKey();
			IBlock block = getBlock(position);
			e.setValue(block);
		}
		return voxels;
	}
	
	
	public Voxel getColumnTop(final int x, final int z) {
		final int y = mcWorld.getHeightValue(x, z);
		return new Voxel(x, y, z);
	}
	
	public Dimensions getDimension() {
		return Dimensions.fromValue(mcWorld.getWorldInfo().getDimension());
	}
	
	public int getMaxHeight() {
		return mcWorld.getHeight() - 1;
	}
	
	public long getSeed() {
		return mcWorld.getSeed();
	}
	
	public Voxel getSpawnPosition() {
		ChunkCoordinates coords = mcWorld.getSpawnPoint();
		return new Voxel(coords.posX, coords.posY, coords.posZ);
	}
	
	public long getTime() {
		return mcWorld.getWorldTime();
	}
	
	public WeatherTypes getWeather() {
		if (mcWorld.isThundering()) {
			return WeatherTypes.Thunderstorm;
		} else if (mcWorld.isRaining()) {
			return WeatherTypes.Rain;
		} else {
			return WeatherTypes.Sunshine;
		}
	}
	public String getWorldName() {
		return mcWorld.getWorldInfo().getWorldName();
	}
	
	public boolean isAirBlock(final Voxel position) {
		return Blocks.Air.equals(getBlock(position));
	}
	
	public boolean isLiquidBlock(final Voxel position) {
		int id = getBlock(position).getId();
		return 	id == Blocks.Water.getId() ||
				id == Blocks.StationaryWater.getId() ||
				id == Blocks.StationaryLava.getId();
	}
	
	public boolean isRaining() {
		return mcWorld.isRaining();
	}
	
	public boolean isSolidBlock(final Voxel position) {
		return mcWorld.isBlockNormalCube(position.x, position.y, position.z);
	}
	
	public boolean isThundering() {
		return mcWorld.isThundering();
	}
	
	public boolean isValidPosition(final Voxel position) {
		return !(position.x >= 30000000 || position.x <= -30000000 ||
				 position.z >= 30000000 || position.z <= -30000000 ||
				 position.y < 0 && position.y > getMaxHeight());
	}
	
	public void setBlock(final Voxel position, final IBlock block) {
		mcWorld.setBlockAndMetadata(position.x, position.y, position.z, block.getId(), block.getMeta());
	}
	
	public void setSpawnPosition(final Voxel newSpawn) {
		mcWorld.getWorldInfo().setSpawnPosition(newSpawn.x, newSpawn.y, newSpawn.z);
	}
	
	public void setTime(final long ticks) {
		mcWorld.setWorldTime(ticks);
	}
	
	public void setWeather(final WeatherTypes weather) {
		switch (weather) {
			case Sunshine:
				mcWorld.getWorldInfo().setThundering(false);
				mcWorld.getWorldInfo().setRaining(false);
				break;
			case Thunderstorm:
				mcWorld.getWorldInfo().setThundering(true);
				mcWorld.getWorldInfo().setRaining(true);
				break;
			case Rain:
				mcWorld.getWorldInfo().setThundering(false);
				mcWorld.getWorldInfo().setRaining(true);
				break;
		}
	}
	
	public void setWorldName(final String name) {
		mcWorld.getWorldInfo().setWorldName(name);
	}
	public boolean startEdit() {
		if (canEdit()) {
			mcWorld.editingBlocks = true;
			return true;
		}
		else {
			return false;
		}
	}
	
	protected WorldServer getMcWorld() {
		return mcWorld;
	}
	
	protected void setMcWorld(final WorldServer mcWorld) {
		this.mcWorld = mcWorld;
	}
	
	
}
