package io.github.simplycmd.terracraft;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import io.github.simplycmd.terracraft.armor.ModArmor;
import io.github.simplycmd.terracraft.items.ModItem;
import io.github.simplycmd.terracraft.tools.ModAxe;
import io.github.simplycmd.terracraft.tools.ModHoe;
import io.github.simplycmd.terracraft.tools.ModPickaxe;
import io.github.simplycmd.terracraft.tools.ModShovel;
import io.github.simplycmd.terracraft.tools.ModSword;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Registers {
    static HashMap<Item, List<Text>> tooltip_list = new HashMap<Item, List<Text>>();

    public static ModType registerNew(Float type, String name) {
        // If you are registering an item
        return registerNew(type, name, null, null, null, null, null);
    }

    public static ModType registerNew(Float type, String name, Material tool_material) {
        // If you are registering a block
        return registerNew(type, name, null, null, null, null, tool_material);
    }

    public static ModType registerNew(Float type, String name, ToolMaterial tool_material,
            Integer attack_damage, Float attack_speed) {
        // If you are registering a tool
        return registerNew(type, name, tool_material, null, attack_damage, attack_speed, null);
    }

    public static ModType registerNew(Float type, String name, ArmorMaterial armor_material,
            String set_bonus) {
        // If you are registering a piece of armor
        return registerNew(type, name, null, armor_material, null, null, null);
    }

    public static ModType registerNew(Float type, String name, ToolMaterial tool_material,
            ArmorMaterial armor_material, Integer attack_damage, Float attack_speed,
            Material block_material) {

        ModType new_type = new ModType();

        ItemGroup group = ItemGroup.MISC;
        Integer stack = 64;
        EquipmentSlot slot = EquipmentSlot.CHEST;
        if (attack_speed != null) {
            attack_speed = attack_speed - 4.0F; // Translate actual attack speed to weird code attack speed
        }

        // Determine stack size
        /*
         * if ((type <= Types.WEAPON_WHIP) || (type >= Types.ARMOR_HELMET && type <=
         * Types.DYE && type != Types.COIN && type != Types.GEM) || (type >= 12.0F &&
         * type < 15.0F)) { stack = 1; }
         */

        // Determine item group
        if (isBetween(type, Types.TOOL_AXE, Types.TOOL_SHOVEL)) {
            group = ItemGroup.TOOLS;
        } else if (isBetween(type, Types.WEAPON_SWORD, Types.ARMOR_BOOTS)) {
            group = ItemGroup.COMBAT;
        } else if (isBetween(type, Types.FURNITURE, Types.CRAFTING_STATION)) {
            group = ItemGroup.DECORATIONS;
        } else if (isBetween(type, Types.PAINT, Types.DYE) || type == Types.COIN || type == Types.ACCESSORY
                || type >= Types.PET) {
            group = ItemGroup.MISC;
        } else if (isBetween(type, Types.POTION_RECOVERY, Types.POTION_OTHER)) {
            group = ItemGroup.BREWING;
        } else if (type == Types.ORE || type == Types.BAR || type == Types.BLOCK || type == Types.WALL) {
            group = ItemGroup.BUILDING_BLOCKS;
        }

        // Determine armor slot
        if (type == Types.ARMOR_HELMET) {
            slot = EquipmentSlot.HEAD;
        } else if (type == Types.ARMOR_CHESTPLATE) {
            slot = EquipmentSlot.CHEST;
        } else if (type == Types.ARMOR_LEGGINGS) {
            slot = EquipmentSlot.LEGS;
        } else if (type == Types.ARMOR_BOOTS) {
            slot = EquipmentSlot.FEET;
        }

        // Determine type and create item
        if (type == Types.TOOL_AXE) {
            final ModAxe item = new ModAxe(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            finishItemRegistry(new_type, item, name);
        } else if (type == Types.TOOL_HOE) {
            final ModHoe item = new ModHoe(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            finishItemRegistry(new_type, item, name);
        } else if (type == Types.TOOL_PICKAXE) {
            final ModPickaxe item = new ModPickaxe(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            finishItemRegistry(new_type, item, name);
        } else if (type == Types.TOOL_SHOVEL) {
            final ModShovel item = new ModShovel(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            finishItemRegistry(new_type, item, name);
        } else if (type == Types.WEAPON_SWORD) {
            final ModSword item = new ModSword(tool_material, attack_damage, attack_speed,
                    (new Item.Settings()).group(group));
            finishItemRegistry(new_type, item, name);
        } else if (type >= Types.ARMOR_HELMET && type <= Types.ARMOR_BOOTS) {
            final ModArmor item = new ModArmor(armor_material, slot, (new Item.Settings()).group(group));
            finishItemRegistry(new_type, item, name);
        } else if (type == Types.BAR || type == Types.BLOCK || type == Types.ORE) {
            final Block block = Registry.register(Registry.BLOCK, new Identifier(Terracraft.MOD_ID, name),
                    new Block(FabricBlockSettings.of(Material.METAL).hardness(1.5f)));
            final BlockItem item = Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name),
                    new BlockItem(block, new Item.Settings().group(group).maxCount(stack)));
            new_type.setBlockItem(item);
            new_type.setBlock(block);
        } else {
            final ModItem item = new ModItem(new Item.Settings().group(group).maxCount(stack));
            finishItemRegistry(new_type, item, name);
        }
        return new_type;
    }

    public static boolean isBetween(float value, float lower, float upper) {
        return lower <= value && value <= upper;
    }

    public static ModItem convertToModItem(Item item) {
        ModItem moditem = new ModItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64));
        try {
            BeanUtils.copyProperties(item, moditem);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return moditem;
    }

    public static void finishItemRegistry(ModType mod_type, Item item, String name) {
            final Item final_item = Registry.register(Registry.ITEM, new Identifier(Terracraft.MOD_ID, name), item);
            mod_type.setItem(convertToModItem(final_item));
    }
}