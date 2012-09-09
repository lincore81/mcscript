package xde.lincore.mcscript;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public enum Items implements IStackable {
	Apple("Apple", 260),
	Arrow("Arrow", 262),
	BakedPotato("Baked Potato", 393),
	Bed("Bed", 355),
	BlazePowder("Blaze Powder", 377),
	BlazeRod("Blaze Rod", 369),
	Boat("Boat", 333),
	Bone("Bone", 352),
	BoneMeal("Bone Meal", 351, 15),
	Book("Book", 340),
	BookAndQuill("Book and Quill", 386),
	Bow("Bow", 261),
	Bowl("Bowl", 281),
	Bread("Bread", 297),
	BrewingStand("Brewing Stand", 379),
	Bucket("Bucket", 325),
	CactusGreen("Cactus Green", 351, 2),
	Cake("Cake", 354),
	Carrots("Carrots", 391),
	Cauldron("Cauldron", 380),
	ChainmailBoots("Chainmail Boots", 305),
	ChainmailChestplate("Chainmail Chestplate", 303),
	ChainmailHelmet("Chainmail Helmet", 302),
	ChainmailLeggings("Chainmail Leggings", 304),
	Charcoal("Charcoal", 263, 1),
	ClayBalls("Clay Balls", 337),
	ClayBrick("Clay Brick", 336),
	Clock("Clock", 347),
	Coal("Coal", 263),
	CocoBeans("Coco Beans", 351, 3),
	Compass("Compass", 345),
	CookedChicken("Cooked Chicken", 366),
	CookedFish("Cooked Fish", 350),
	CookedPorkchop("Cooked Porkchop", 320),
	Cookie("Cookie", 357),
	CyanDye("Cyan Dye", 351, 6),
	DandelionYellow("Dandelion Yellow", 351, 11),
	Diamond("Diamond", 264),
	DiamondAxe("Diamond Axe", 279),
	DiamondBoots("Diamond Boots", 313),
	DiamondChestplate("Diamond Chestplate", 311),
	DiamondHelmet("Diamond Helmet", 310),
	DiamondHoe("Diamond Hoe", 293),
	DiamondLeggings("Diamond Leggings", 312),
	DiamondPickaxe("Diamond Pickaxe", 278),
	DiamondShovel("Diamond Shovel", 277),
	DiamondSword("Diamond Sword", 276),
	DicCat("Cat Disc", 2257),
	Disc11("11 Disc", 2266),
	Disc13("13 Disc", 2256),
	DiscBlocks("Blocks Disc", 2258),
	DiscChirp("Chirp Disc", 2259),
	DiscFar("Far Disc", 2260),
	DiscMall("Mall Disc", 2261),
	DiscMellohi("Mellohi Disc", 2262),
	DiscStal("Stal Disc", 2263),
	DiscStrad("Strad Disc", 2264),
	DiscWard("Ward Disc", 2265),
	Egg("Egg", 344),
	Emerald("Emerald", 388),
	EnderPearl("Ender Pearl", 368),
	ExperiencePotion("Experience Potion", 384),
	EyeOfEnder("Eye of Ender", 381),
	Feather("Feather", 288),
	FermentedSpiderEye("Fermented Spider Eye", 376),
	FireCharge("Fire Charge", 385),
	FishingRod("Fishing Rod", 346),
	Flint("Flint", 318),
	FlintAndSteel("Flint and Steel", 259),
	FlowerPot("Flower Pot", 390),
	GhastTear("Ghast Tear", 370),
	GlassBottle("Glass Bottle", 374),
	GlisteringMelon("Glistering Melon", 382),
	GlowstoneDust("Glowstone Dust", 348),
	GoldAxe("Gold Axe", 286),
	GoldBoots("Gold Boots", 317),
	GoldChestplate("Gold Chestplate", 315),
	GoldenApple("Golden Apple", 322),
	GoldenCarrot("Golden Carrot", 396),
	GoldHelmet("Gold Helmet", 314),
	GoldHoe("Gold Hoe", 294),
	GoldIngot("Gold Ingot", 266),
	GoldLeggings("Gold Leggings", 316),
	GoldNugget("Gold Nugget", 371),
	GoldPickaxe("Gold Pickaxe", 285),
	GoldShovel("Gold Shovel", 284),
	GoldSword("Gold Sword", 283),
	GrayDye("Gray Dye", 351, 8),
	InkSack("Ink Sack", 351),
	IronAxe("Iron Axe", 258),
	IronBoots("Iron Boots", 309),
	IronChestplate("Iron Chestplate", 307),
	IronDoor("Iron Door", 330),
	IronHelmet("Iron Helmet", 306),
	IronHoe("Iron Hoe", 292),
	IronIngot("Iron Ingot", 265),
	IronLeggings("Iron Leggings", 308),
	IronPickaxe("Iron Pickaxe", 257),
	IronShovel("Iron Shovel", 256),
	IronSword("Iron Sword", 267),
	ItemFrame("Item Frame", 389),
	LapisLazuli("Lapis Lazuli", 351, 4),
	LavaBucket("Lava Bucket", 327),
	Leather("Leather", 334),
	LeatherBoots("Leather Boots", 301),
	LeatherChestplate("Leather Chestplate", 299),
	LeatherHelmet("Leather Helmet", 298),
	LeatherLeggings("Leather Leggings", 300),
	LightBlueDye("Light Blue Dye", 351, 12),
	LightGrayDye("Light Gray Dye", 351, 7),
	LimeDye("Lime Dye", 351, 10),
	MagentaDye("Magenta Dye", 351, 13),
	MagmaCream("Magma Cream", 378),
	Map("Map", 358),
	Map2("Map 2", 395),
	Melon("Melon", 360),
	MelonSeeds("Melon Seeds", 362),
	MilkBucket("Milk Bucket", 335),
	Minecart("Minecart", 328),
	MushroomSoup("Mushroom Soup", 282),
	NetherWartSeeds("Nether Wart Seeds", 372),
	OrangeDye("Orange Dye", 351, 14),
	Painting("Painting", 321),
	Paper("Paper", 339),
	PinkDye("Pink Dye", 351, 9),
	PoisonousPotato("Poisonous Potato", 394),
	Potato("Potato", 392),
	Potion("Potion", 373),
	PoweredMinecart("Powered Minecart", 343),
	PumpkinSeeds("Pumpkin Seeds", 361),
	PurpleDye("Purple Dye", 351, 5),
	RawBeef("Raw Beef", 363),
	RawChicken("Raw Chicken", 365),
	RawFish("Raw Fish", 349),
	RawPorkchop("Raw Porkchop", 319),
	Redstone("Redstone", 331),
	RedstoneRepeater("Redstone Repeater", 356),
	RoseRed("Rose Red", 351, 1),
	RottenFlesh("Rotten Flesh", 367),
	Saddle("Saddle", 329),
	Shears("Shears", 359),
	Sign("Sign", 323),
	Slimeball("Slimeball", 341),
	Snowball("Snowball", 332),
	SpawnBlaze("Spawn Blaze", 383, 61),
	SpawnCaveSpider("Spawn Cave Spider", 383, 59),
	SpawnChicken("Spawn Chicken", 383, 93),
	SpawnCow("Spawn Cow", 383, 92),
	SpawnCreeper("Spawn Creeper", 383, 50),
	SpawnEnderman("Spawn Enderman", 383, 58),
	SpawnGhast("Spawn Ghast", 383, 56),
	SpawnMagmaCube("Spawn Magma Cube", 383, 62),
	SpawnMooshroom("Spawn Mooshroom", 383, 96),
	SpawnOcelot("Spawn Ocelot", 383, 98),
	SpawnPig("Spawn Pig", 383, 90),
	SpawnPigman("Spawn Pigman", 383, 57),
	SpawnSheep("Spawn Sheep", 383, 91),
	SpawnSilverfish("Spawn Silverfish", 383, 60),
	SpawnSkeleton("Spawn Skeleton", 383, 51),
	SpawnSlime("Spawn Slime", 383, 55),
	SpawnSpider("Spawn Spider", 383, 52),
	SpawnSquid("Spawn Squid", 383, 94),
	SpawnVillager("Spawn Villager", 383, 120),
	SpawnWolf("Spawn Wolf", 383, 95),
	SpawnZombie("Spawn Zombie", 383, 54),
	SpiderEye("Spider Eye", 375),
	Steak("Steak", 364),
	Stick("Stick", 280),
	StoneAxe("Stone Axe", 275),
	StoneHoe("Stone Hoe", 291),
	StonePickaxe("Stone Pickaxe", 274),
	StoneShovel("Stone Shovel", 273),
	StoneSword("Stone Sword", 272),
	StorageMinecart("Storage Minecart", 342),
	String("String", 287),
	Sugar("Sugar", 353),
	Sugarcane("Sugarcane", 338),
	Sulphur("Sulphur", 289),
	WaterBucket("Water Bucket", 326),
	Wheat("Wheat", 296),
	WheatSeeds("Wheat Seeds", 295),
	WoodenAxe("Wooden Axe", 271),
	WoodenDoor("Wooden Door", 324),
	WoodenHoe("Wooden Hoe", 290),
	WoodenPickaxe("Wooden Pickaxe", 270),
	WoodenShovel("Wooden Shovel", 269),
	WoodenSword("Wooden Sword", 268),
	WrittenBook("Written Book", 387),
	;
	
	
	private int id, meta;
	private String name;
	private String regex;
	
	private Items(String name, int id) {
		this(name, id, 0);
	}
	
	private Items(String name, int id, int meta) {
		this(name, id, meta, null);
	}
	
	private Items(String name, int id, int meta, String regex) {
		this.name = name;
		this.id = id;
		this.meta = meta;
		this.regex = regex;
	}
	
	public static Items find(String name) {		
		for (Items item: Items.values()) {
			if (item.name.equalsIgnoreCase(name)) return item;
		}
		String name_ = name.toLowerCase().trim();
		for (Items item: Items.values()) {
			if (item.name.toLowerCase().startsWith(name_)) return item;
		}
		return null;
	}
	
	public static Items findById(int id, int meta) {
		for (Items item: Items.values()) {
			if (item.id == id && item.meta == meta) return item;
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public int getMeta() {
		return meta;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public ItemStack getItemStack(int quantity) {		
		return new ItemStack(getMcItem(), quantity, meta);
	}
	
	public int getMaxStackSize() {
		return getMcItem().getItemStackLimit();
	}
	
	public Item getMcItem() {		
		assert id >= 0 && id < Item.itemsList.length : "id is out of bounds!";
		return Item.itemsList[id];
	}
}
