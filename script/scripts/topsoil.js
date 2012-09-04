voxel = mc.user.getMouseOver()
if (voxel == null) {
	voxel = mc.user.getPosition().toVoxel()
}

block 	= mc.getBlock(args.get("(b(lock)?|3)", "grass"))
xRange 	= args.getNumber("x(range)?|1", 0)
zRange 	= args.getNumber("z(range)?|2", 0)

fromX 	= voxel.x - xRange;
toX 	= voxel.x + xRange;
fromZ	= voxel.z - zRange;
toZ		= voxel.z + zRange;


for (x = fromX; x <= toX; x++) {
	for (z = fromZ; z <= toZ; z++) {
		top = mc.world.getHeightVoxel(x, z)
		mc.world.setBlock(top, block)
	}
}
