importPackage(Packages.xde.lincore.mcscript)

sides = args.getNumber("s(ides)?|1", 3)
length = args.getNumber("l(ength)?|2", 10)
block = Blocks.find(args.get("b(lock)?|3", "light green"))
if (sides < 3) sides = 3
center = mc.user.position

circumference = sides * length
radius = circumference / (2 * Math.PI)
startpos = mc.user.position.north(radius)
angle = 360 / sides

turtle = new VectorTurtle(startpos, block, 0)

for (j = 0; j < 8; j++) {
	drawPolygon()
	turtle.rt(45)
}

function drawPolygon() {
	for (i = 0; i < sides; i++) {
		turtle.rt(angle).fd(length)
	}
}



