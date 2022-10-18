package com.sammy.pebblemaker.data_types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;

public class EmptyItemData extends HeldData {

    public EmptyItemData() {
    }

    @Override
    public boolean matches(Player player, InteractionHand hand, ItemStack heldItem) {
        return player.getItemInHand(hand).isEmpty();
    }
}