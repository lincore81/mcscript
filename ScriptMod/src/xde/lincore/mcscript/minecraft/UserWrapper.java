package xde.lincore.mcscript.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.env.ScriptEnvironment;

public class UserWrapper extends AbstractWrapper {

	public final InventoryWrapper inv;

	protected UserWrapper(final ScriptEnvironment env, final MinecraftWrapper mc) {
		super(env, mc);
		inv = new InventoryWrapper(env, mc);
	}

	public String getName() {
		return env.getUser().getEntityName();
	}

	public double getX() {
		return env.getUser().posX;
	}

	public double getY() {
		return env.getUser().posY;
	}

	public double getZ() {
		return env.getUser().posZ;
	}

	public Vector3d getPosition() {
		final EntityPlayer user = env.getUser();
		return new Vector3d(user.posX, user.posY, user.posZ);
	}

	public Vector3d getLookVector() {
		final EntityPlayer user = env.getUser();
		return Vector3d.fromVec3(user.getLookVec());
	}

	public void teleport(final Vector3d position) {
		final EntityPlayer user = env.getUser();
		user.setPositionAndUpdate(position.x, position.y, position.z);
	}

	public void noclip(final boolean state) {
		env.getUser().noClip = state;
	}

	public boolean hasNoclip() {
		return env.getUser().noClip;
	}

	public Vector3d raytrace(final double distance) {
		final MovingObjectPosition hit = env.getUser().rayTrace(distance, 1f);
//		mod_Script.LOG.info(String.format("raytrace for user %s, hit: %d %d %d, type: %s",
//				env.getUser().username, hit.blockX, hit.blockY, hit.blockZ,  hit.typeOfHit.toString()));
		return new Vector3d(hit.blockX, hit.blockY, hit.blockZ);
	}

	public Voxel getMouseOver() {
		final Minecraft minecraft = ModLoader.getMinecraftInstance();
		if (minecraft.objectMouseOver == null) {
			return null;
		}
		return new Voxel(
				minecraft.objectMouseOver.blockX,
				minecraft.objectMouseOver.blockY,
				minecraft.objectMouseOver.blockZ);
	}

}
