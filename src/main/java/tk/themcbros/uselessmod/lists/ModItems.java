package tk.themcbros.uselessmod.lists;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tk.themcbros.uselessmod.UselessMod;
import tk.themcbros.uselessmod.items.*;
import tk.themcbros.uselessmod.machine.MachineTier;
import tk.themcbros.uselessmod.machine.Upgrade;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@ObjectHolder(UselessMod.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UselessMod.MOD_ID)
public class ModItems {
	
	private static final ItemGroup GROUP = ModItemGroups.USELESS_ITEM_GROUP;
	private static final List<Item> ITEMS = Lists.newArrayList();

	// Block Items
	// Wood
	public static final BlockItem USELESS_LOG = registerBlockItem("useless_log", ModBlocks.USELESS_LOG);
	public static final BlockItem STRIPPED_USELESS_LOG = registerBlockItem("stripped_useless_log", ModBlocks.STRIPPED_USELESS_LOG);
	public static final BlockItem USELESS_WOOD = registerBlockItem("useless_wood", ModBlocks.USELESS_WOOD);
	public static final BlockItem STRIPPED_USELESS_WOOD = registerBlockItem("stripped_useless_wood", ModBlocks.STRIPPED_USELESS_WOOD);
	public static final BlockItem USELESS_PLANKS = registerBlockItem("useless_planks", ModBlocks.USELESS_PLANKS);
	public static final BlockItem USELESS_SLAB = registerBlockItem("useless_slab", ModBlocks.USELESS_SLAB);
	public static final BlockItem USELESS_STAIRS = registerBlockItem("useless_stairs", ModBlocks.USELESS_STAIRS);
	public static final BlockItem USELESS_FENCE = registerBlockItem("useless_fence", ModBlocks.USELESS_FENCE);
	public static final BlockItem USELESS_SIGN = registerItem("useless_sign", new SignItem(new Item.Properties().group(GROUP), ModBlocks.USELESS_SIGN, ModBlocks.USELESS_WALL_SIGN));
	public static final BlockItem USELESS_PRESSURE_PLATE = registerBlockItem("useless_pressure_plate", ModBlocks.USELESS_PRESSURE_PLATE);
	public static final BlockItem USELESS_TRAPDOOR = registerBlockItem("useless_trapdoor", ModBlocks.USELESS_TRAPDOOR);
	public static final BlockItem USELESS_FENCE_GATE = registerBlockItem("useless_fence_gate", ModBlocks.USELESS_FENCE_GATE);
	public static final BlockItem USELESS_BUTTON = registerBlockItem("useless_button", ModBlocks.USELESS_BUTTON);
	public static final BlockItem WOODEN_USELESS_DOOR = registerBlockItem("wooden_useless_door", ModBlocks.WOODEN_USELESS_DOOR);

	// Natural
	public static final BlockItem USELESS_SAPLING = registerBlockItem("useless_sapling", ModBlocks.USELESS_SAPLING);
	public static final BlockItem USELESS_LEAVES = registerBlockItem("useless_leaves", ModBlocks.USELESS_LEAVES);
	public static final BlockItem USELESS_GRASS_BLOCK = registerBlockItem("useless_grass_block", ModBlocks.USELESS_GRASS_BLOCK);
	public static final BlockItem USELESS_DIRT = registerBlockItem("useless_dirt", ModBlocks.USELESS_DIRT);
	public static final BlockItem USELESS_GRASS = registerBlockItem("useless_grass", ModBlocks.USELESS_GRASS);
	public static final BlockItem USELESS_FERN = registerBlockItem("useless_fern", ModBlocks.USELESS_FERN);
	public static final BlockItem LARGE_USELESS_FERN = registerItem("large_useless_fern", new TallBlockItem(ModBlocks.LARGE_USELESS_FERN, new Item.Properties().group(GROUP)));
	public static final BlockItem TALL_USELESS_GRASS = registerItem("tall_useless_grass", new TallBlockItem(ModBlocks.TALL_USELESS_GRASS, new Item.Properties().group(GROUP)));
	public static final BlockItem RED_ROSE = registerBlockItem("red_rose", ModBlocks.RED_ROSE);
	public static final BlockItem BLUE_ROSE = registerBlockItem("blue_rose", ModBlocks.BLUE_ROSE);

	// Metal
	public static final BlockItem USELESS_BLOCK = registerBlockItem("useless_block", ModBlocks.USELESS_BLOCK);
	public static final BlockItem SUPER_USELESS_BLOCK = registerBlockItem("super_useless_block", ModBlocks.SUPER_USELESS_BLOCK);
	public static final BlockItem USELESS_BARS = registerBlockItem("useless_bars", ModBlocks.USELESS_BARS);
	public static final BlockItem SUPER_USELESS_BARS = registerBlockItem("super_useless_bars", ModBlocks.SUPER_USELESS_BARS);
	public static final BlockItem USELESS_DOOR = registerBlockItem("useless_door", ModBlocks.USELESS_DOOR);
	public static final BlockItem SUPER_USELESS_DOOR = registerBlockItem("super_useless_door", ModBlocks.SUPER_USELESS_DOOR);
	public static final BlockItem USELESS_ORE = registerBlockItem("useless_ore", ModBlocks.USELESS_ORE);
	public static final BlockItem USELESS_ORE_NETHER = registerBlockItem("useless_ore_nether", ModBlocks.USELESS_ORE_NETHER);
	public static final BlockItem USELESS_ORE_END = registerBlockItem("useless_ore_end", ModBlocks.USELESS_ORE_END);
	public static final BlockItem SUPER_USELESS_ORE = registerBlockItem("super_useless_ore", ModBlocks.SUPER_USELESS_ORE);
	public static final BlockItem SUPER_USELESS_ORE_NETHER = registerBlockItem("super_useless_ore_nether", ModBlocks.SUPER_USELESS_ORE_NETHER);
	public static final BlockItem SUPER_USELESS_ORE_END = registerBlockItem("super_useless_ore_end", ModBlocks.SUPER_USELESS_ORE_END);
	public static final BlockItem CHEESE_MAKER = registerBlockItem("cheese_maker", ModBlocks.CHEESE_MAKER);

	// Machines / Cables / Closet
	public static final BlockItem CRUSHER = registerBlockItem("crusher", ModBlocks.CRUSHER);
	public static final BlockItem ELECTRIC_CRUSHER = registerBlockItem("electric_crusher", ModBlocks.ELECTRIC_CRUSHER);
	public static final BlockItem ELECTRIC_FURNACE = registerBlockItem("electric_furnace", ModBlocks.ELECTRIC_FURNACE);
	public static final BlockItem COMPRESSOR = registerBlockItem("compressor", ModBlocks.COMPRESSOR);
	public static final BlockItem MAGMA_CRUCIBLE = registerBlockItem("magma_crucible", ModBlocks.MAGMA_CRUCIBLE);
	public static final BlockItem CHARGER = registerBlockItem("charger", ModBlocks.CHARGER);
	public static final BlockItem GLOWSTONE_GENERATOR = registerBlockItem("glowstone_generator", ModBlocks.GLOWSTONE_GENERATOR);
	public static final BlockItem LAVA_GENERATOR = registerBlockItem("lava_generator", ModBlocks.LAVA_GENERATOR);
	public static final BlockItem COFFEE_MACHINE = registerBlockItem("coffee_machine", ModBlocks.COFFEE_MACHINE);
	public static final BlockItem CREATIVE_POWER_BLOCK = registerItem("creative_power_block", new CreativeEnergyBlockItem(ModBlocks.CREATIVE_POWER_BLOCK, new Item.Properties().group(GROUP)));
	public static final BlockItem POWER_CONTROL_BLOCK = registerBlockItem("power_control_block", ModBlocks.POWER_CONTROL_BLOCK);
	public static final BlockItem ENERGY_CABLE = registerBlockItem("energy_cable", ModBlocks.ENERGY_CABLE);
	public static final BlockItem FLUID_PIPE = registerBlockItem("fluid_pipe", ModBlocks.FLUID_PIPE);
	public static final BlockItem FLUID_TANK = registerBlockItem("fluid_tank", ModBlocks.FLUID_TANK);
	public static final BlockItem CLOSET = registerBlockItem("closet", ModBlocks.CLOSET, ModItemGroups.CLOSET_GROUP);

	// Misc
	public static final BlockItem WHITE_LAMP = registerItem("white_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.WHITE));
	public static final BlockItem ORANGE_LAMP = registerItem("orange_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.ORANGE));
	public static final BlockItem MAGENTA_LAMP = registerItem("magenta_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.MAGENTA));
	public static final BlockItem LIGHT_BLUE_LAMP = registerItem("light_blue_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.LIGHT_BLUE));
	public static final BlockItem YELLOW_LAMP = registerItem("yellow_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.YELLOW));
	public static final BlockItem LIME_LAMP = registerItem("lime_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.LIME));
	public static final BlockItem PINK_LAMP = registerItem("pink_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.PINK));
	public static final BlockItem GRAY_LAMP = registerItem("gray_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.GRAY));
	public static final BlockItem LIGHT_GRAY_LAMP = registerItem("light_gray_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.LIGHT_GRAY));
	public static final BlockItem CYAN_LAMP = registerItem("cyan_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.CYAN));
	public static final BlockItem PURPLE_LAMP = registerItem("purple_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.PURPLE));
	public static final BlockItem BLUE_LAMP = registerItem("blue_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.BLUE));
	public static final BlockItem BROWN_LAMP = registerItem("brown_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.BROWN));
	public static final BlockItem GREEN_LAMP = registerItem("green_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.GREEN));
	public static final BlockItem RED_LAMP = registerItem("red_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.RED));
	public static final BlockItem BLACK_LAMP = registerItem("black_lamp", new LampBlockItem(new Item.Properties().group(GROUP), DyeColor.BLACK));
	public static final BlockItem CANVAS = registerBlockItem("canvas", ModBlocks.CANVAS);
	public static final BlockItem PAINT_BUCKET = registerBlockItem("paint_bucket", ModBlocks.PAINT_BUCKET);
	public static final BlockItem LIGHT_SWITCH = registerItem("light_switch", new LightSwitchBlockItem(ModBlocks.LIGHT_SWITCH, new Item.Properties().group(GROUP)));
	public static final BlockItem LIGHT_SWITCH_BLOCK = registerItem("light_switch_block", new LightSwitchBlockItem(ModBlocks.LIGHT_SWITCH_BLOCK, new Item.Properties().group(GROUP)));


	// Items
	// Creative / Metal
	public static final Item USELESS_ITEM = registerItem("useless_item", new UselessItem(new Item.Properties().group(GROUP)));
	public static final Item USELESS_INGOT = registerItem("useless_ingot");
	public static final Item SUPER_USELESS_INGOT = registerItem("super_useless_ingot");
	public static final Item USELESS_NUGGET = registerItem("useless_nugget");
	public static final Item SUPER_USELESS_NUGGET = registerItem("super_useless_nugget");
	public static final Item MACHINEFRAME = registerItem("machineframe");
	public static final Item BLANK_UPGRADE = registerItem("blank_upgrade", new UpgradeItem(new Item.Properties().group(GROUP), Upgrade.NULL));
	public static final Item SPEED_UPGRADE = registerItem("speed_upgrade", new UpgradeItem(new Item.Properties().group(GROUP), Upgrade.SPEED));
	public static final Item USELESS_TIER_UPGRADE = registerItem("useless_tier_upgrade", new TierUpgradeItem(new Item.Properties().group(GROUP), Upgrade.TIER_USELESS, MachineTier.USELESS));
	public static final Item SUPER_USELESS_TIER_UPGRADE = registerItem("super_useless_tier_upgrade", new TierUpgradeItem(new Item.Properties().group(GROUP), Upgrade.TIER_SUPER_USELESS, MachineTier.SUPER_USELESS));

	// Natural / Food
	public static final Item USELESS_WHEAT_SEEDS = registerItem("useless_wheat_seeds", new BlockNamedItem(ModBlocks.USELESS_WHEAT, new Item.Properties().group(GROUP)));
	public static final Item USELESS_WHEAT = registerItem("useless_wheat");
	public static final Item COFFEE_SEEDS = registerItem("coffee_seeds", new BlockNamedItem(ModBlocks.COFFEE_SEEDS, new Item.Properties().group(GROUP)));
	public static final Item COFFEE_BEANS = registerItem("coffee_beans");
	public static final Item USELESS_SOUP = registerItem("useless_soup", new Item(new Item.Properties().food(FoodList.USELESS_SOUP).maxStackSize(1).group(GROUP)));
	public static final Item USELESS_BREAD = registerItem("useless_bread", new Item(new Item.Properties().food(FoodList.USELESS_BREAD).group(GROUP)));
	public static final Item USELESS_FLOUR = registerItem("useless_flour");
	public static final Item SUGARED_MILK = registerItem("sugared_milk", new Item(new Item.Properties().maxStackSize(1).group(GROUP)));
	public static final Item CUP = registerItem("cup");
	public static final Item COFFEE_CUP = registerItem("coffee_cup", new CoffeeCupItem(new Item.Properties().maxStackSize(1).group(GROUP)));

	// Dusts / Plates
	public static final Item USELESS_STICK = registerItem("useless_stick");
	public static final Item IRON_DUST = registerItem("iron_dust");
	public static final Item USELESS_DUST = registerItem("useless_dust");
	public static final Item SUPER_USELESS_DUST = registerItem("super_useless_dust");
	public static final Item COAL_DUST = registerItem("coal_dust");
	public static final Item GOLD_DUST = registerItem("gold_dust");
	public static final Item EMERALD_DUST = registerItem("emerald_dust");
	public static final Item DIAMOND_DUST = registerItem("diamond_dust");
	public static final Item IRON_PLATE = registerItem("iron_plate");
	public static final Item GOLD_PLATE = registerItem("gold_plate");
	public static final Item USELESS_PLATE = registerItem("useless_plate");
	public static final Item SUPER_USELESS_PLATE = registerItem("super_useless_plate");

	// Tools
	public static final SwordItem USELESS_SWORD = registerItem("useless_sword", new SwordItem(ToolMaterialList.useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final AxeItem USELESS_AXE = registerItem("useless_axe", new AxeItem(ToolMaterialList.useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final PickaxeItem USELESS_PICKAXE = registerItem("useless_pickaxe", new PickaxeItem(ToolMaterialList.useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final ShovelItem USELESS_SHOVEL = registerItem("useless_shovel", new ShovelItem(ToolMaterialList.useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final HoeItem USELESS_HOE = registerItem("useless_hoe", new HoeItem(ToolMaterialList.useless, 1f, new Item.Properties().group(GROUP)));
	public static final SwordItem SUPER_USELESS_SWORD = registerItem("super_useless_sword", new SwordItem(ToolMaterialList.super_useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final AxeItem SUPER_USELESS_AXE = registerItem("super_useless_axe", new AxeItem(ToolMaterialList.super_useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final PickaxeItem SUPER_USELESS_PICKAXE = registerItem("super_useless_pickaxe", new PickaxeItem(ToolMaterialList.super_useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final ShovelItem SUPER_USELESS_SHOVEL = registerItem("super_useless_shovel", new ShovelItem(ToolMaterialList.super_useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final HoeItem SUPER_USELESS_HOE = registerItem("super_useless_hoe", new HoeItem(ToolMaterialList.super_useless, 1f, new Item.Properties().group(GROUP)));

	// TODO: Make all energy tools
	public static final SwordItem ELECTRIC_USELESS_SWORD = registerItem("electric_useless_sword", new EnergySwordItem(ToolMaterialList.useless, 0, 1f, new Item.Properties().group(GROUP)));
	public static final PickaxeItem ELECTRIC_USELESS_PICKAXE = registerItem("electric_useless_pickaxe", new EnergyPickaxeItem(ToolMaterialList.useless, -2, 1f, new Item.Properties().group(GROUP)));

	// Armor
	public static final ArmorItem USELESS_HELMET = registerItem("useless_helmet", new ArmorItem(ArmorMaterialList.USELESS, EquipmentSlotType.HEAD, new Item.Properties().group(GROUP)));
	public static final ArmorItem USELESS_CHESTPLATE = registerItem("useless_chestplate", new ArmorItem(ArmorMaterialList.USELESS, EquipmentSlotType.CHEST, new Item.Properties().group(GROUP)));
	public static final ArmorItem USELESS_LEGGINGS = registerItem("useless_leggings", new ArmorItem(ArmorMaterialList.USELESS, EquipmentSlotType.LEGS, new Item.Properties().group(GROUP)));
	public static final ArmorItem USELESS_BOOTS = registerItem("useless_boots", new ArmorItem(ArmorMaterialList.USELESS, EquipmentSlotType.FEET, new Item.Properties().group(GROUP)));
	public static final ArmorItem SUPER_USELESS_HELMET = registerItem("super_useless_helmet", new ArmorItem(ArmorMaterialList.SUPER_USELESS, EquipmentSlotType.HEAD, new Item.Properties().group(GROUP)));
	public static final ArmorItem SUPER_USELESS_CHESTPLATE = registerItem("super_useless_chestplate", new ArmorItem(ArmorMaterialList.SUPER_USELESS, EquipmentSlotType.CHEST, new Item.Properties().group(GROUP)));
	public static final ArmorItem SUPER_USELESS_LEGGINS = registerItem("super_useless_leggings", new ArmorItem(ArmorMaterialList.SUPER_USELESS, EquipmentSlotType.LEGS, new Item.Properties().group(GROUP)));
	public static final ArmorItem SUPER_USELESS_BOOTS = registerItem("super_useless_boots", new ArmorItem(ArmorMaterialList.SUPER_USELESS, EquipmentSlotType.FEET, new Item.Properties().group(GROUP)));

	// Misc Tools
	public static final HammerItem HAMMER = registerItem("hammer", new HammerItem(new Item.Properties().group(GROUP)));
	public static final PaintBrushItem PAINT_BRUSH = registerItem("paint_brush", new PaintBrushItem(new Item.Properties().maxStackSize(1).group(GROUP)));

	// Misc
	public static final SpawnEggItem USELESS_ENTITY_SPAWN_EGG = registerItem("useless_cow_spawn_egg", new SpawnEggItem(ModEntityTypes.USELESS_COW, 0x2b8a4a, 0x195c19, new Item.Properties().group(GROUP)));
	public static final GrenadeItem GRENADE = registerItem("grenade", new GrenadeItem(new Item.Properties().group(GROUP).maxStackSize(16)));
	public static final BucketItem USELESS_WATER_BUCKET = registerItem("useless_water_bucket", new BucketItem(() -> ModFluids.USELESS_WATER, new Item.Properties().group(ModItemGroups.USELESS_ITEM_GROUP).maxStackSize(1).containerItem(Items.BUCKET)));
	public static final CanisterItem CANISTER = registerItem("canister", new CanisterItem(new Item.Properties().group(GROUP)));

	@SubscribeEvent
	public static void onRegister(final RegistryEvent.Register<Item> event) {
		ITEMS.forEach(item -> {
			event.getRegistry().register(item);
		});
		UselessMod.LOGGER.debug("Registered useless items");
	}
	
	private static <T extends Item> T registerItem(String name, T item) {
		ResourceLocation location = new ResourceLocation(UselessMod.MOD_ID, name);
		item.setRegistryName(location);
		ITEMS.add(item);
		return item;
	}

	private static Item registerItem(String name) {
		return registerItem(name, new Item(new Item.Properties().group(GROUP)));
	}
	
	private static BlockItem registerBlockItem(String name, Block block, ItemGroup groupIn) {
		ResourceLocation location = new ResourceLocation(UselessMod.MOD_ID, name);
		BlockItem blockItem = new BlockItem(block, new Item.Properties().group(groupIn));
		blockItem.setRegistryName(location);
		ITEMS.add(blockItem);
		return blockItem;
	}
	
	private static BlockItem registerBlockItem(String name, Block block) {
		return registerBlockItem(name, block, GROUP);
	}
	
}
