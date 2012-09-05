package xde.lincore.mcscript;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import xde.lincore.util.Config;

public enum Blocks {
	Air("Air", 0),
	Stone("Stone", 1),
	Grass("Grass", 2),
	Dirt("Dirt", 3),
	Cobblestone("Cobblestone", 4),
	WoodenPlank("Wooden Plank", 5),
	Sapling("Sapling", 6),
	RedwoodSapling("Redwood Sapling", 6, 1),
	BirchSapling("Birch Sapling", 6, 2),
	Bedrock("Bedrock", 7),
	Water("Water", 8),
	StationaryWater("Stationary Water", 9),
	Lava("Lava", 10),
	StationaryLava("Stationary Lava", 11),
	Sand("Sand", 12),
	Gravel("Gravel", 13),
	GoldOre("Gold Ore", 14),
	IronOre("Iron Ore", 15),
	CoalOre("Coal Ore", 16),
	Wood("Wood", 17),
	Redwood("Redwood", 17, 1),
	Birchwood("Birchwood", 17, 2),
	Leaves("Leaves", 18),
	RedwoodLeaves("Redwood Leaves", 18, 1),
	BirchwoodLeaves("Birchwood Leaves", 18, 2),
	Sponge("Sponge", 19),
	Glass("Glass", 20),
	LapisLazuliOre("Lapis Lazuli Ore", 21),
	LapisLazuliBlock("Lapis Lazuli Block", 22),
	Dispenser("Dispenser", 23),
	Sandstone("Sandstone", 24),
	NoteBlock("Note Block", 25),
	BedBlock("Bed Block", 26),
	PoweredRail("Powered Rail", 27),
	DetectorRail("Detector Rail", 28),
	StickyPiston("Sticky Piston", 29),
	Web("Web", 30),
	DeadShrub("Dead Shrub", 31),
	TallGrass("Tall Grass", 31, 1),
	LiveShrub("Live Shrub", 31, 2),
	DeadShrub2("Dead Shrub 2", 32),
	Piston("Piston", 33),
	PistonHead("Piston Head", 34),
	Wool("Wool", 35),
	OrangeWool("Orange Wool", 35, 1),
	MagentaWool("Magenta Wool", 35, 2),
	LightBlueWool("Light Blue Wool", 35, 3),
	YellowWool("Yellow Wool", 35, 4),
	LightGreenWool("Light Green Wool", 35, 5),
	PinkWool("Pink Wool", 35, 6),
	GrayWool("Gray Wool", 35, 7),
	LightGrayWool("Light Gray Wool", 35, 8),
	CyanWool("Cyan Wool", 35, 9),
	PurpleWool("Purple Wool", 35, 10),
	BlueWool("Blue Wool", 35, 11),
	BrownWool("Brown Wool", 35, 12),
	DarkGreenWool("Dark Green Wool", 35, 13),
	RedWool("Red Wool", 35, 14),
	BlackWool("Black Wool", 35, 15),
	Dandelion("Dandelion", 37),
	Rose("Rose", 38),
	BrownMushroom("Brown Mushroom", 39),
	RedMushroom("Red Mushroom", 40),
	GoldBlock("Gold Block", 41),
	IronBlock("Iron Block", 42),
	DoubleStoneSlab("Double Stone Slab", 43),
	DoubleSandstoneSlab("Double Sandstone Slab", 43, 1),
	DoubleWoodenSlab("Double Wooden Slab", 43, 2),
	DoubleCobblestoneSlab("Double Cobblestone Slab", 43, 3),
	DoubleBrickSlab("Double Brick Slab", 43, 4),
	DoubleStoneBrickSlab("Double Stone Brick Slab", 43, 5),
	StoneSlab("Stone Slab", 44),
	SandstoneSlab("Sandstone Slab", 44, 1),
	WoodenSlab("Wooden Slab", 44, 2),
	CobblestoneSlab("Cobblestone Slab", 44, 3),
	BrickSlab("Brick Slab", 44, 4),
	StoneBrickSlab("Stone Brick Slab", 44, 5),
	Brick("Brick", 45),
	TNT("TNT", 46),
	Bookshelf("Bookshelf", 47),
	MossyCobblestone("Mossy Cobblestone", 48),
	Obsidian("Obsidian", 49),
	Torch("Torch", 50),
	Fire("Fire", 51),
	MonsterSpawner("Monster Spawner", 52),
	WoodenStairs("Wooden Stairs", 53),
	Chest("Chest", 54),
	RedstoneWire("Redstone Wire", 55),
	DiamondOre("Diamond Ore", 56),
	DiamondBlock("Diamond Block", 57),
	Workbench("Workbench", 58),
	WheatCrops("Wheat Crops", 59),
	Soil("Soil", 60),
	Furnace("Furnace", 61),
	BurningFurnace("Burning Furnace", 62),
	SignPost("Sign Post", 63),
	WoodenDoorBlock("Wooden Door Block", 64),
	Ladder("Ladder", 65),
	Rails("Rails", 66),
	CobblestoneStairs("Cobblestone Stairs", 67),
	WallSign("Wall Sign", 68),
	Lever("Lever", 69),
	StonePressurePlate("Stone Pressure Plate", 70),
	IronDoorBlock("Iron Door Block", 71),
	WoodenPressurePlate("Wooden Pressure Plate", 72),
	RedstoneOre("Redstone Ore", 73),
	GlowingRedstoneOre("Glowing Redstone Ore", 74),
	RedstoneTorch("Redstone Torch", 75),
	RedstoneTorchOn("Redstone Torch On", 76),	
	StoneButton("Stone Button", 77),
	Snow("Snow", 78),
	Ice("Ice", 79),
	SnowBlock("Snow Block", 80),
	Cactus("Cactus", 81),
	Clay("Clay", 82),
	SugarCane("Sugar Cane", 83),
	Jukebox("Jukebox", 84),
	Fence("Fence", 85),
	Pumpkin("Pumpkin", 86),
	Netherrack("Netherrack", 87),
	SoulSand("Soul Sand", 88),
	Glowstone("Glowstone", 89),
	Portal("Portal", 90),
	JackOLantern("Jack-O-Lantern", 91),
	CakeBlock("Cake Block", 92),
	RedstoneRepeater("Redstone Repeater", 93),
	RedstoneRepeaterOn("Redstone Repeater On", 94),	
	LockedChest("Locked Chest", 95),
	Trapdoor("Trapdoor", 96),
	SilverfishBlock("Silverfish Block", 97),
	SilverfishCobblestoneBlock("Silverfish Cobblestone Block", 97, 1),
	SilverfishStoneBrickBlock("Silverfish Stone Brick Block", 97, 1),	
	StoneBrick("Stone Brick", 98),
	MossyStoneBrick("Mossy Stone Brick", 98, 1),
	CrackedStoneBrick("Cracked Stone Brick", 98, 2),
	RedMushroomCap("Red Mushroom Cap", 99),
	BrownMushroomCap("Brown Mushroom Cap", 100),
	IronBars("Iron Bars", 101),
	GlassPane("Glass Pane", 102),
	MelonBlock("Melon Block", 103),
	PumpkinStem("Pumpkin Stem", 104),
	MelonStem("Melon Stem", 105),
	Vines("Vines", 106),
	FenceGate("Fence Gate", 107),
	BrickStairs("Brick Stairs", 108),
	StoneBrickStairs("Stone Brick Stairs", 109),
	Mycelium("Mycelium", 110),
	LilyPad("Lily Pad", 111),
	NetherBrick("Nether Brick", 112),
	NetherBrickFence("Nether Brick Fence", 113),
	NetherBrickStairs("Nether Brick Stairs", 114),
	NetherWart("Nether Wart", 115),
	EnchantmentTable("Enchantment Table", 116),
	BrewingStand("Brewing Stand", 117),
	Cauldron("Cauldron", 118),
	EndPortal("End Portal", 119),
	EndPortalFrame("End Portal Frame", 120),
	EndStone("End Stone", 121),
	DragonEgg("Dragon Egg", 122),
	RedstoneLamp("Redstone Lamp", 123),
	RedstoneLampOn("Redstone Lamp On", 124),	
	DoubleWoodenSlab2("Double Wooden Slab 2", 125),
	WoodenSlab2("Wooden Slab 2", 126),
	CocoaPlant("Cocoa Plant", 127),
	SandstoneStairs("Sandstone Stairs", 128),
	EmeraldOre("Emerald Ore", 129),
	EnderChest("Ender Chest", 130),
	TripwireHook("Tripwire Hook", 131),
	Tripwire("Tripwire", 132),
	EmeraldBlock("Emerald Block", 133),
	SpruceWoodStairs("Spruce Wood Stairs", 134),
	BirchWoodStairs("Birch Wood Stairs", 135),
	JungleWoodStairs("Jungle Wood Stairs", 136),
	CommandBlock("Command Block", 137),
	BeaconBlock("Beacon Block", 138),
	CobblestoneWall("Cobblestone Wall", 139),
	FlowerPot("Flower Pot", 140),
	Carrots("Carrots", 141),
	Potatoes("Potatoes", 142),
	WoodenButton("Wooden Button", 143),
	;
	
	private int id, data;
	private String name;
	
	private Blocks(String name, int id, int damage) {
		this.id = id;
		this.data = damage;
		this.name = name;
	}
	
	private Blocks(String name, int id) {
		this(name, id, 0);
	}
	
	public static Blocks find(String searchstr) {
		for (Blocks b: Blocks.values()) {
			if (b.name.equalsIgnoreCase(searchstr)) return b;
		}
		for (Blocks b: Blocks.values()) {
			if (b.name.toLowerCase().startsWith(searchstr.toLowerCase())) {
				return b;
			}
		}
		return null;
	}
	
	public static Blocks findById(int id) {
		return findById(id, 0);
	}
	
	public static Blocks findById(int id, int damage) {
		for (Blocks b: Blocks.values()) {
			if (b.id == id && b.data == damage) {
				return b;
			}
		}
		return null;
	}
	
	public int getId() {
		return id;
	}

	
	public int getData() {
		return data;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Return the minecraft Block instance with this block id. 
	 * @return The Block instance with the id of this Blocks entry or null if that Block
	 * doesn't exist. <br /><b>Don't forget:</b><code> Air (id=0)</code> is NOT a block. 
	 */
	public net.minecraft.src.Block getMcBlock() {
		Block[] list = Block.blocksList;
		if (id == 0) {
			return null;
		}
		else if (id < 0 || id >= list.length) {
			throw new RuntimeException("[Blocks.getMcBlock()] Block id is out of range: " + 
					toString() + "\nThis shouldn't happen. Please report this error.");
		}
		else {
			return list[id];
		}
	}
	
	public ItemStack getMcStack(int quantity) {
		return new ItemStack(getMcBlock(), quantity, data);
	}
	
	public String toString() {
		if (data > 0) {
			return String.format("%s (%2d:%2d)", name, id, data);
		}
		else {
			return String.format("%s (%2d)", name, id);
		}
	}
}
