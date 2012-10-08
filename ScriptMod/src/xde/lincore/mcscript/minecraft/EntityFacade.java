package xde.lincore.mcscript.minecraft;

import net.minecraft.src.Entity;
import xde.lincore.mcscript.Directions;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;

public class EntityFacade {
	private final Entity entity;

	protected EntityFacade(final Entity entity) {
		this.entity = entity;
	}

	public Voxel getBlockPosition() {
		return new Voxel(entity.chunkCoordX, entity.chunkCoordY, entity.chunkCoordZ);
	}

	public double getFallDistance() {
		return entity.fallDistance;
	}

	public double getHeight() {
		return entity.height;
	}

	public int getId() {
		return entity.entityId;
	}
	
	public String getName() {
		return entity.getEntityName();
	}
	
	public Directions getLookDirection() {
		return Directions.getClosest(getLookVector(), false);
	}

	public Vector3d getLookVector() {
		return MinecraftUtils.toVector3d(entity.getLookVec());
	}

	public Vector3d getMotion() {
		return new Vector3d(entity.motionX, entity.motionY, entity.motionZ);
	}

	public Vector3d getPosition() {
		return new Vector3d(entity.posX, entity.posY, entity.posZ);
	}

	public double getWidth() {
		return entity.width;
	}

	public boolean isAirborne() {
		return entity.isAirBorne;
	}

	public void remove() {
		entity.setDead();
	}

	public void setLookAngles(final double yaw, final double pitch) {
		entity.setAngles((float)yaw, (float)pitch);
	}

	public void setPosition(final Vector3d position) {
		entity.posX = position.x;
		entity.posY = position.y;
		entity.posZ = position.z;
	}
	
	protected Entity getMcEntity() {
		return entity;
	}
}
