width = 16
height = 16
blackBlock = "35:15"
whiteBlock = "35:0"

pos = mc.user.getPosition().up(-1)
white = true
for (x = 0; x < width; x++) {
	for (z = 0; z < height; z++) {
		mc.world.setBlock(pos.x + x, pos.y, pos.z + z, (white)? whiteBlock : blackBlock)
		white = !white
	}
	white = !white
}

