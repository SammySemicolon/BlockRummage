package com.sammy.block_rummage.data_types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class HeldData {
    public abstract boolean matches(Player player, InteractionHand hand, ItemStack heldItem);
}
