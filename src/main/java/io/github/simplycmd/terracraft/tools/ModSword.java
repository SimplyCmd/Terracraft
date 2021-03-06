package io.github.simplycmd.terracraft.tools;

import java.util.List;

import io.github.simplycmd.terracraft.Terracraft;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class ModSword extends SwordItem {
	public ModSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
tooltip.add(new TranslatableText(stack.getTranslationKey() + ".tooltip"));
	}

}