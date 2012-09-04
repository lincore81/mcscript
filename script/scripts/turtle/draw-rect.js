importPackage(Packages.xde.lincore.mcscript)

xlen 	= args.getNumber("x|w(idth)?|1", 8) - 1
zlen	= args.getNumber("z|l(ength)?|2", 8) - 1
block 	= mc.getBlock(args.get("b(lock)?|3", "stone"))

startPos = mc.user.mouseOver
if (startPos == null) startPos = mc.user.position.toVoxel()



turtle = new Turtle(startPos, block, Directions.East)



turtle.fd(xlen).rt().fd(zlen).rt().fd(xlen).rt().fd(zlen)

