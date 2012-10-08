package xde.lincore.mcscript.minecraft;

import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import xde.lincore.mcscript.Dimensions;

public final class ServerFacade {
	private final MinecraftServer server;
	private final HashMap<Dimensions, WorldFacade> worlds;
	
	protected ServerFacade(final MinecraftServer server) {
		this.server = server;
		worlds = new HashMap<Dimensions, WorldFacade>();
	}
	
	public static ServerFacade getCurrentServer() {
		return new ServerFacade(MinecraftServer.getServer());
	}
	
	public WorldFacade getWorld(final Dimensions dimension) {
		if (worlds.containsKey(dimension)) {
			return worlds.get(dimension);
		} else {
			WorldFacade world = new WorldFacade(server.worldServerForDimension(dimension.getValue()));
			worlds.put(dimension, world);
			return world;
		}
	}
	
	public String getWorldDirectoryString() {
		return server.worldServers[0].getSaveHandler().getSaveDirectoryName();
	}
	
	public WorldFacade getPlayerWorld(final PlayerFacade player) {
		return getWorld(player.getDimension());
	}

	protected MinecraftServer getMcServer() {
		return server;
	}
}
