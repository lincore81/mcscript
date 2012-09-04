importPackage(Packages.xde.lincore.mcscript)

length = args.getNumber("w(idth)?|1", 16)
block = mc.getBlock(args.get("b(lock)?|2", "Sandstone"))
height = Math.ceil(length / 2)

center = mc.user.position.toVoxel()
startpos = center.sub(Math.ceil(length / 2), 0, Math.ceil(length / 2))

//~ pos1 = mc.user.getPosition().sub(length / 2 + 1, 0, length / 2 + 1).toVoxel()
//~ pos2 = pos1.add(length - 1, 0, length - 1)
turtle = new Turtle(startpos, block, Directions.East)


for (y = 0; y < height; y++) {
	drawSquare(length)
	nextStep()
	length -= 2
}

mc.echo("Done!")


function drawSquare(length) {
	if (length > 1) {
		turtle
			.fd(length - 1).rt()
			.fd(length - 1).rt()
			.fd(length - 1).rt()
			.fd(length - 1).rt()
	}
	else {
		turtle.fd().pu().bk().pd()
	}
}

function nextStep() {
	turtle.penUp().raise(1).rt().fd().lt().fd().penDown()
}
