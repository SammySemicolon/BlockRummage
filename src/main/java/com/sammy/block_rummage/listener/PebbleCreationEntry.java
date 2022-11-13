package com.sammy.block_rummage.listener;

import com.google.gson.JsonObject;
import com.sammy.block_rummage.data_types.HeldData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public record PebbleCreationEntry(ResourceLocation id, Ingredient block, @Nullable HeldData data, List<LootEntry> lootEntries) {

    public static record HeldDataType<T extends HeldData>(ResourceLocation id, Function<JsonObject, T> serializer) {

    }

    public static record LootEntry(Ingredient output, int amount, float chance) {}

}
