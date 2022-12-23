package com.sammy.block_rummage.data_types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class MatchingIngredientData extends HeldData {
    public final Ingredient ingredient;
    public final int durabilityCost;

    public MatchingIngredientData(Ingredient ingredient, int durabilityCost) {
        this.ingredient = ingredient;
        this.durabilityCost = durabilityCost;
    }

    @Override
    public boolean matches(Player player, InteractionHand hand, ItemStack heldItem) {
        boolean test = ingredient.test(heldItem);
        if (test && durabilityCost != 0) {
            heldItem.hurtAndBreak(durabilityCost, player, (e) -> e.broadcastBreakEvent(hand));
        }
        return test;
    }

    @Override
    public @NotNull Ingredient getIngredient() {
        return ingredient;
    }
}