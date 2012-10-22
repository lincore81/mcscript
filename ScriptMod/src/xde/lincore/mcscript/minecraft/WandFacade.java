package xde.lincore.mcscript.minecraft;

import net.minecraft.src.ModLoader;
import xde.lincore.mcscript.ICollectable;
import xde.lincore.mcscript.env.G;

public class WandFacade {
	private static final String[] colors = new String[] {
		"White",
		"Red",
		"Orange",
		"Yellow",
		"Green",
		"Cyan",
		"Blue",
		"Magenta",
		"Purple"
	};
	
	private static final ItemWand[] items 			= new ItemWand[colors.length];
	private static final int[] 		imageIds 		= new int[colors.length];
	private static final int 		wandSubtypeCount= colors.length;
	private static final String 	wandNameMask 	= "%s wand";
	private static final String 	filenameMask 	= "/item/wands/magic-wand-%s.png";
	private static final String 	terrainPng 		= "/gui/items.png";
		
	private static int uid;
	//private static int itemWandId;
	private static boolean isSetup;
	
	private final IWandUseListener listener;
	private static ItemWand wandInstance;
	
	
	public WandFacade(final IWandUseListener listener, final int itemWandId) throws NullPointerException {
		if (listener == null) {
			throw new NullPointerException("listener can not be null");
		}
		if (!isSetup) setup(itemWandId, this);
		this.listener = listener;
	}
	
	public int bindWand(final PlayerFacade player) throws IllegalStateException {
		if (!isPlayerHoldingWand(player)) {
			throw new IllegalStateException("Player must hold wand to bind.");
		}
		int uid = nextUid();
		ICollectable item = player.getItemHeld();
		int trueMeta = getLower4Bits(item.getMeta());
		int totalMeta = uid << 4 + trueMeta;
		CollectableData data = new CollectableData(item.getId(), totalMeta);
		player.holdItem(data, 1);
		return uid;
	}
	
	public boolean isPlayerHoldingWand(final PlayerFacade player) {
		ICollectable heldItem = player.getItemHeld();
		return (heldItem != null && heldItem.getId() == wandInstance.shiftedIndex);
	}

	public int getSubtypeCount() {
		return wandSubtypeCount;
	}

	public void giveWand(final PlayerFacade player, final String color, final boolean replaceItemInSlot) {
		int slot = player.getFirstFreeSlotIndex();
		if (replaceItemInSlot || slot == -1) {
			slot = player.getHotbarIndex();
		}
		
		int colorIndex = getColorIndex(color);
		player.giveItem(wandInstance.shiftedIndex, colorIndex, 1);
	}
	
	public boolean hasColor(final String color) {
		return getColorIndex(color) > -1;
	}
	
	
	public int unbindWand(final PlayerFacade player) throws IllegalStateException {
		if (!isPlayerHoldingWand(player)) {
			throw new IllegalStateException("Player must hold wand to unbind.");
		}
		CollectableData data = new CollectableData(player.getItemHeld());
		int uid = getUpper28Bits(data.getMeta());
		data.setMeta(getLower4Bits(data.getMeta()));
		player.holdItem(data, 1);
		return uid;
	}
	
	
	protected String getItemName(final int meta) {
		int trueMeta = getLower4Bits(meta);
		if (meta >= wandSubtypeCount) {
			throw new IllegalArgumentException(String.format(
					"trueMeta of this wand is outside of bounds (%d >= %d)\n",
					trueMeta, wandSubtypeCount));
		}
		return String.format(wandNameMask, colors[trueMeta]);
	}
	
	protected void onWandUse(final int meta) {
		int wandId = getUpper28Bits(meta);
		listener.onWandUse(wandId);
	}
	
	protected int getIconIndex(final int meta) {
		int trueMeta = getLower4Bits(meta);
		assert trueMeta >= 0 && trueMeta < wandSubtypeCount;
		return imageIds[trueMeta];
	}
	
	private static void addOverride(final int index) {
		String filename = String.format(filenameMask, colors[index]).toLowerCase();
		try {
			imageIds[index] = ModLoader.addOverride(terrainPng, filename);
		}
		catch (RuntimeException e) {
			G.LOG.throwing(WandFacade.class.getSimpleName(), "setup()", e);
			G.LOG.warning("An error occured while loading item icon '" + filename + "'." );
			throw e; // yes we crash
		}
	}

	private static int nextUid() {
		return uid++;
	}

	private static void setup(final int itemWandId, final WandFacade instance) {
		for (int i = 0; i < colors.length; i++) {
			addOverride(i);
		}
		wandInstance = new ItemWand(itemWandId, instance);
		wandInstance.setIconIndex(imageIds[0]);
		isSetup = true;
	}

	private int getColorIndex(final String color) {
		String color_ = color.toLowerCase();
		int i = 0;
		for (String c: colors) {
			if (c.toLowerCase().startsWith(color_)) {
				return i;
			}
			i++;
		}
		return -1;
	}


	private int getLower4Bits(final int meta) {
		return meta & 0xf;
	}


	private int getUpper28Bits(final int meta) {
		return meta - getLower4Bits(meta);
	}
	
}
