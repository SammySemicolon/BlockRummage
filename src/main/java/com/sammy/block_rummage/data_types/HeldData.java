package com.sammy.block_rummage.data_types;

import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public abstract class HeldData {
    public abstract boolean matches(Player player, InteractionHand hand, ItemStack heldItem);

    @NonNull
    public abstract Ingredient getIngredient();

    /**
     * Used exclusively for JEI integration to show extra data depending on held data type.
     */
    public ItemStack getModifiedItemStack(ItemStack stack) {
        return stack;
    }
    /**
     * Used exclusively for JEI integration to add a tooltip to the displayed ingredient.
     */
    public void addTooltipInfoToIngredient(List<Component> tooltip) {

    }
}
