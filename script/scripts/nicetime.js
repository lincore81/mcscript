// Print and set the time in a nice HH:MM format.


function getTime() {
	time = mc.time.get() + 6000
	if (time >= 24000) time -= 24000
	hours = Math.floor((time) / 1000)
	minutes = Math.round((time % 1000) / 1000 * 60)	
	return ((hours < 10) ? "0" + hours : hours) + ":" + ((minutes < 10) ? "0" + minutes : minutes)
}


function printTime() {
	mc.echo("It is now " + getTime() + ".")
}



function setTime(time) {
	timeStrings = {
		"morning"	:     0,
		"dawn"		: -1000,
		"day"		:	200,
		"noon"		:  6000,
		"afternoon"	:  8000,
		"teatime"	: 10000,
		"dusk"		: 12500,
		"night"		: 16000,
		"mignight"	: 18000		
	}

	for (timeOfDay in timeStrings) {
		if (time.equalsIgnoreCase(timeOfDay)) {
			mc.time.set(timeStrings[timeOfDay])
			printTime()
			return;
		}
	}

	if (time.matches("[0-9]{1,2}:[0-9]{1,2}")) { // HH:MM 24h format
		pair 	= time.split(":")
		hours 	= pair[0]		
		minutes = pair[1]
		sixtyDividedByMinutes = ((minutes > 0)? (60 / minutes) : 0)
		newtime = hours * 1000 + (sixtyDividedByMinutes * 1000 - 6000)
		
		mc.time.set(newtime)
	}
	else { // treat as integer, similar to built-in "/time" command
		mc.time.set(parseInt(time))
	}
	
}

if (args.hasNext()) {
	setTime(args.nextValue())
}
else {
	mc.echo(getTime())
}



