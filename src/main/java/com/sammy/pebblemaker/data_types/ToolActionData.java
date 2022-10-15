package com.sammy.pebblemaker.data_types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;

public class ToolActionData extends HeldData {
    public final ToolAction toolAction;
    public final int durabilityCost;

    public ToolActionData(ToolAction toolAction, int durabilityCost) {
        this.toolAction = toolAction;
        this.durabilityCost = durabilityCost;
    }

    @Override
    public boolean matches(Player player, InteractionHand hand, ItemStack heldItem) {
        boolean test = heldItem.canPerformAction(toolAction);
        if (test && durabilityCost != 0) {
            heldItem.hurtAndBreak(durabilityCost, player, (e) -> e.broadcastBreakEvent(hand));
        }
        return test;
    }
}