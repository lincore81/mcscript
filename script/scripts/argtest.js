while (args.hasNext()) {
	name = args.next()
	value = args.get(name)
	mc.echo(name + ": " + value)	
}
