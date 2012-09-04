cubeselection = mc.types.newCuboid(mc.user.getMouseOver().down(3), 8, 6, 8)
selection = mc.types.newHollowCuboid(mc.user.getMouseOver(), 8, 6, 8)
combined = mc.types.newLogicConnector(selection, cubeselection, 1)
mc.echo(cubeselection)
mc.echo(selection)
mc.echo(combined)
mc.world.fillSelection(combined, 41, 0)
//combined = null
