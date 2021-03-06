package io.github.simplycmd.terracraft;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import io.github.simplycmd.terracraft.armor.ArmorMaterials;
import io.github.simplycmd.terracraft.tools.ToolMaterials;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.block.Material;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class Terracraft extends Registers implements ModInitializer {
	public static final String MOD_ID = "terracraft";
    public static final String MOD_NAME = "Terracraft";

    private HashMap<EquipmentSlot, Item> wooden_armor = new HashMap<EquipmentSlot, Item>();
    private HashMap<EquipmentSlot, Item> mining_armor = new HashMap<EquipmentSlot, Item>();

	@Override
	public void onInitialize() {
        ServerTickCallback.EVENT.register(this::onServerTick);
        // Tools
        registerNew(Types.WEAPON_SWORD, "cactus_sword", ToolMaterials.CACTUS, 4, 1.6F);
        registerNew(Types.TOOL_PICKAXE, "cactus_pickaxe", ToolMaterials.CACTUS, 2, 1.2F);
        registerNew(Types.TOOL_AXE, "cactus_axe", ToolMaterials.CACTUS, 7, 0.8F);
        
        registerNew(Types.WEAPON_SWORD, "copper_broadsword", ToolMaterials.COPPER, 5, 1.6F);
        registerNew(Types.TOOL_PICKAXE, "copper_pickaxe", ToolMaterials.COPPER, 3, 1.2F);
        registerNew(Types.TOOL_AXE, "copper_axe", ToolMaterials.COPPER, 9, 0.8F);

        registerNew(Types.WEAPON_SWORD, "tin_broadsword", ToolMaterials.TIN, 5, 1.6F);
        registerNew(Types.TOOL_PICKAXE, "tin_pickaxe", ToolMaterials.TIN, 3, 1.2F);
        registerNew(Types.TOOL_AXE, "tin_axe", ToolMaterials.TIN, 9, 0.8F);

        registerNew(Types.WEAPON_SWORD, "lead_broadsword", ToolMaterials.LEAD, 6, 1.6F);
        registerNew(Types.TOOL_PICKAXE, "lead_pickaxe", ToolMaterials.LEAD, 4, 1.2F);
        registerNew(Types.TOOL_AXE, "lead_axe", ToolMaterials.LEAD, 9, 0.9F);

        registerNew(Types.WEAPON_SWORD, "silver_broadsword", ToolMaterials.SILVER, 7, 1.6F);
        registerNew(Types.TOOL_PICKAXE, "silver_pickaxe", ToolMaterials.SILVER, 5, 1.2F);
        registerNew(Types.TOOL_AXE, "silver_axe", ToolMaterials.SILVER, 9, 1.0F);

        registerNew(Types.WEAPON_SWORD, "tungsten_broadsword", ToolMaterials.TUNGSTEN, 7, 1.6F);
        registerNew(Types.TOOL_PICKAXE, "tungsten_pickaxe", ToolMaterials.TUNGSTEN, 5, 1.2F);
        registerNew(Types.TOOL_AXE, "tungsten_axe", ToolMaterials.TUNGSTEN, 9, 1.0F);

        registerNew(Types.WEAPON_SWORD, "platinum_broadsword", ToolMaterials.PLATINUM, 8, 1.6F);
        registerNew(Types.TOOL_PICKAXE, "platinum_pickaxe", ToolMaterials.PLATINUM, 6, 1.2F);
        registerNew(Types.TOOL_AXE, "platinum_axe", ToolMaterials.PLATINUM, 10, 1.0F);
        // Weapons

        // Ammunition
        registerNew(Types.AMMUNITION_BULLET, "musket_ball");
        registerNew(Types.AMMUNITION_BULLET, "silver_bullet");
        registerNew(Types.AMMUNITION_BULLET, "tungsten_bullet");
        registerNew(Types.AMMUNITION_BULLET, "meteor_shot");

        registerNew(Types.AMMUNITION_ARROW, "flaming_arrow");
        registerNew(Types.AMMUNITION_ARROW, "frostburn_arrow");
        registerNew(Types.AMMUNITION_ARROW, "bone_arrow");
        registerNew(Types.AMMUNITION_ARROW, "unholy_arrow");
        registerNew(Types.AMMUNITION_ARROW, "jesters_arrow");
        registerNew(Types.AMMUNITION_ARROW, "hellfire_arrow");

        registerNew(Types.AMMUNITION_DART, "poison_dart"); // "Inflicts poison on enemies"

        registerNew(Types.AMMUNITION_SPECIAL, "flare");
        registerNew(Types.AMMUNITION_SPECIAL, "blue_flare");

        registerNew(Types.AMMUNITION_SPECIAL, "seed"); // "For use with Blowpipe"
        // Armor
        wooden_armor.put(EquipmentSlot.HEAD, registerNew(Types.ARMOR_HELMET, "wooden_helmet", ArmorMaterials.WOOD, "+1 Defense").getItem());
        wooden_armor.put(EquipmentSlot.CHEST, registerNew(Types.ARMOR_CHESTPLATE, "wooden_chestplate", ArmorMaterials.WOOD, "+1 Defense").getItem());
        wooden_armor.put(EquipmentSlot.LEGS, registerNew(Types.ARMOR_LEGGINGS, "wooden_leggings", ArmorMaterials.WOOD, "+1 Defense").getItem());
        wooden_armor.put(EquipmentSlot.FEET, registerNew(Types.ARMOR_BOOTS, "wooden_boots", ArmorMaterials.WOOD, "+1 Defense").getItem());

        mining_armor.put(EquipmentSlot.HEAD, registerNew(Types.ARMOR_HELMET, "mining_helmet", ArmorMaterials.MINE, "+20% Mining Speed").getItem()); //Not 30% because haste goes in steps of 20%.
        mining_armor.put(EquipmentSlot.CHEST, registerNew(Types.ARMOR_CHESTPLATE, "mining_chestplate", ArmorMaterials.MINE, "+20% Mining Speed").getItem());
        mining_armor.put(EquipmentSlot.LEGS, registerNew(Types.ARMOR_LEGGINGS, "mining_leggings", ArmorMaterials.MINE, "+20% Mining Speed").getItem());
        mining_armor.put(EquipmentSlot.FEET, registerNew(Types.ARMOR_BOOTS, "mining_boots", ArmorMaterials.MINE, "+20% Mining Speed").getItem());
        // Furniture

        // Crafting Stations

        // Coins
        registerNew(Types.COIN, "copper_coin");
        registerNew(Types.COIN, "silver_coin");
        registerNew(Types.COIN, "gold_coin");
        registerNew(Types.COIN, "platinum_coin");
        // Ores
        ModType tin_ore = registerNew(Types.ORE, "tin_ore", Material.METAL);
        registerCustomOre(tin_ore, customOre(tin_ore, 11, 0, 0, 128, 10));

        ModType lead_ore = registerNew(Types.ORE, "lead_ore", Material.METAL);
        registerCustomOre(lead_ore, customOre(lead_ore, 9, 0, 0, 64, 10));

        ModType silver_ore = registerNew(Types.ORE, "silver_ore", Material.METAL);
        registerCustomOre(silver_ore, customOre(silver_ore, 8, 0, 0, 48, 5));
        ModType tungsten_ore = registerNew(Types.ORE, "tungsten_ore", Material.METAL);
        registerCustomOre(tungsten_ore, customOre(tungsten_ore, 8, 0, 0, 48, 5));

        ModType platinum_ore = registerNew(Types.ORE, "platinum_ore", Material.METAL);
        registerCustomOre(platinum_ore, customOre(platinum_ore, 9, 0, 0, 32, 2));
        
        // Bars
        registerNew(Types.BAR, "copper_bar", Material.METAL);
        registerNew(Types.BAR, "tin_bar", Material.METAL);

        registerNew(Types.BAR, "lead_bar", Material.METAL);

        registerNew(Types.BAR, "silver_bar", Material.METAL);
        registerNew(Types.BAR, "tungsten_bar", Material.METAL);

        registerNew(Types.BAR, "platinum_bar", Material.METAL);
        // Accessories

        // Blocks

        // Walls

        // Paint

        // Gems

        // Vanity Items

        // Dyes

        // Potions

        // Statues

        // Wire

        // Pets

        // Mounts

        // Minions

        // Wings

        // Miscellaneous

    }

    private ConfiguredFeature<?, ?> customOre(ModType ore, Integer vein_size, Integer bottom_offset, Integer min_y, Integer max_y, Integer vein_count) {
        return Feature.ORE
            .configure(new OreFeatureConfig(
            OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
            ore.getBlock().getDefaultState(),
            vein_size)) // vein size
            .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(
            bottom_offset, // bottom offset
            min_y, // min y level
            max_y))) // max y level
            .spreadHorizontally()
            .repeat(vein_count); // number of veins per chunk
    }

    private void registerCustomOre(ModType ore, ConfiguredFeature<?, ?> ore_gen) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
        new Identifier(MOD_ID, ore.getBlockItem().getTranslationKey() + "_overworld")).getValue(), ore_gen);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
        new Identifier(MOD_ID, ore.getBlockItem().getTranslationKey() + "_overworld")));
    }

	// Listener
    private void onServerTick(MinecraftServer server) {
        Iterator<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList().iterator();
        while(players.hasNext()) {
            ServerPlayerEntity player = players.next();
            setBonus(player, wooden_armor, EntityAttributes.GENERIC_ARMOR, 1.0D, Operation.ADDITION, Types.GENERIC_ARMOR_UUID, "wooden_armor");
            setBonus(player, mining_armor, StatusEffects.HASTE, 209, 0);
        }
    }

    // Set Bonuses
    private void setBonus(ServerPlayerEntity player, HashMap armor_set, EntityAttribute attribute, Double value, Operation operation, UUID uuid, String name) {
        Item[] slots = {player.getEquippedStack(EquipmentSlot.HEAD).getItem(),player.getEquippedStack(EquipmentSlot.CHEST).getItem(),player.getEquippedStack(EquipmentSlot.LEGS).getItem(),player.getEquippedStack(EquipmentSlot.FEET).getItem()};
        if (slots[0] == armor_set.get(EquipmentSlot.HEAD) && slots[1] == armor_set.get(EquipmentSlot.CHEST) && slots[2] == armor_set.get(EquipmentSlot.LEGS) && slots[3] == armor_set.get(EquipmentSlot.FEET)) {
            if(player.getAttributeInstance(attribute).getModifier(uuid) == null) {
                player.getAttributeInstance(attribute).addTemporaryModifier(new EntityAttributeModifier(uuid, name, value, operation));
            }
        } else {
            if(player.getAttributeInstance(attribute).getModifier(uuid) != null) {
                player.getAttributeInstance(attribute).removeModifier(new EntityAttributeModifier(uuid, name, value, operation));
            }
        }
    }
    private void setBonus(ServerPlayerEntity player, HashMap armor_set, StatusEffect effect, Integer duration, Integer amplifier) {
        Item[] slots = {player.getEquippedStack(EquipmentSlot.HEAD).getItem(),player.getEquippedStack(EquipmentSlot.CHEST).getItem(),player.getEquippedStack(EquipmentSlot.LEGS).getItem(),player.getEquippedStack(EquipmentSlot.FEET).getItem()};
        if (slots[0] == armor_set.get(EquipmentSlot.HEAD) && slots[1] == armor_set.get(EquipmentSlot.CHEST) && slots[2] == armor_set.get(EquipmentSlot.LEGS) && slots[3] == armor_set.get(EquipmentSlot.FEET)) {
            player.applyStatusEffect(new StatusEffectInstance(effect, duration, amplifier, false, false));
        }
    }
}
