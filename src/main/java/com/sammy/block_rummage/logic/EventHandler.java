package com.sammy.block_rummage.logic;

import com.sammy.block_rummage.data_types.HeldData;
import com.sammy.block_rummage.listener.PebbleCreationEntry;
import com.sammy.block_rummage.listener.PebbleCreationDataListener;
import com.sammy.block_rummage.PebbleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = PebbleMod.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void registerListeners(AddReloadListenerEvent event) {
        PebbleCreationDataListener.register(event);
    }

    @SubscribeEvent
    public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof Player player) {
            Level level = player.getLevel();
            if (!level.isClientSide && (player.isCrouching() || player instanceof FakePlayer)) {
                Random random = level.random;
                BlockPos pos = event.getPos();
                BlockState blockState = level.getBlockState(pos);
                Block block = blockState.getBlock();
                Optional<PebbleCreationEntry> optional = PebbleCreationDataListener.PEBBLE_CREATION_DATA.values()
                        .stream()
                        .filter(d -> d.block().test(block.asItem().getDefaultInstance()))
                        .findFirst();

                optional.ifPresent(entry -> {
                    InteractionHand hand = event.getHand();
                    HeldData data = entry.data();
                    if (data == null || data.matches(player, hand, player.getItemInHand(hand))) {
                        List<PebbleCreationEntry.LootEntry> lootEntries = entry.lootEntries();
                        Direction direction = event.getFace();
                        //noinspection ConstantConditions, it's never null in case of block events
                        Vec3 offsetPos = new Vec3(pos.getX() + direction.getStepX() * 0.75f, pos.getY() + direction.getStepY() * 0.75f, pos.getZ() + direction.getStepZ() * 0.75f);
                        float offset = 0.15f;
                        for (PebbleCreationEntry.LootEntry lootEntry : lootEntries) {
                            Vec3 itemPosition = new Vec3(offsetPos.x + 0.5f, offsetPos.y + 0.5f, offsetPos.z + 0.5f);
                            itemPosition = itemPosition.add(Mth.nextFloat(random, -offset, offset), Mth.nextFloat(random, -offset, offset), Mth.nextFloat(random, -offset, offset));

                            if (lootEntry.chance() == 1 || random.nextFloat() < lootEntry.chance()) {
                                ItemStack[] items = lootEntry.output().getItems();
                                ItemEntity itemEntity = new ItemEntity(level, itemPosition.x, itemPosition.y, itemPosition.z, items[random.nextInt(items.length)].copy());
                                itemEntity.setPickUpDelay(10);
                                itemEntity.setDeltaMovement(direction.getStepX() * 0.1f + random.nextFloat() * 0.05f, direction.getStepY() * 0.2f + random.nextFloat() * 0.05f, direction.getStepZ() * 0.1f + random.nextFloat() * 0.05f);
                                level.addFreshEntity(itemEntity);
                            }
                        }

                        level.levelEvent(2001, pos, Block.getId(blockState));
                        player.swing(hand, true);
                    }
                });
            }
        }
    }
}