mcscript 0.041 for Minecraft 1.3.2
Date: 09/28/2012


ABOUT:
mcscript is a mod that adds scripting support to Minecraft. Any JSR-223 compliant
scripting engine can be used by putting it's jar-file into the ./minecraft/bin directory.

If you use Oracle's HotSpot JVM (unless you know what that means you almost certainly do) there is
already built-in support for JavaScript/ECMAScript 1.6. If you are using a different JVM you might
want to check the documentation.

Languages/Engines tested so far:
- JavaScript (ECMAScript) 1.6 / Mozilla Rhino
- Python / jython 2.5.3
- Lua 5.1 / luaj 2.0.2


DOWNLOAD:
No releases are planned for the immediate future.
However, you can download the source and build it yourself.

BUILD:
To build the source with MCP, put xde/* in MCP/src.
Alternatively, given that you use eclipse, you can use the eclipse project provided,
which will put the binaries in MCP/eclipse/Client/bin. To make this work, put mcscript
on Client's build path. mcscript's build path should contain the projects Client and
Server. (If you know a better way to set up multiple projects with ModLoader still finding mod_ classes, please tell me!)
If you get two circular dependency errors "A cycle was detected in the build path of ..."
open the preferences window and goto Java/Compiler/Building, then change "Circular dependencies"
from error to warning.

Before you can use the mod, a small modification must be applied to
net.minecraft.src.IntegratedServer:
- Go to the beginning of public void tick() (line 112)
- Insert: xde.lincore.mcscript.env.ScriptEnvironment.getInstance().onTick();
That's it.


DOCUMENTATION:
There is none at the moment, sorry.


FEEDBACK:
Yes, please!


BUG REPORTS & FEATURE REQUESTS:
https://github.com/lincore81/mcscript/issues


CONTACT:
lincore81~gmail~com


LEGAL STUFF:
Don't forget to read the LICENSE.
If you think the license terms are inadequate, please share your concerns.
Minecraft is copyright (c) Mojang AB (www.mojang.com)
