package com.sammy.block_rummage.listener;

import com.google.gson.*;
import com.sammy.block_rummage.DataTypeRegistry;
import com.sammy.block_rummage.PebbleMod;
import com.sammy.block_rummage.data_types.HeldData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PebbleCreationDataListener extends SimpleJsonResourceReloadListener {
    public static Map<ResourceLocation, PebbleCreationEntry> PEBBLE_CREATION_DATA = new HashMap<>();
    private static final Gson GSON = (new GsonBuilder()).create();

    public PebbleCreationDataListener() {
        super(GSON, "rummaging");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new PebbleCreationDataListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        PEBBLE_CREATION_DATA.clear();
        for (int i = 0; i < objectIn.size(); i++) {
            ResourceLocation location = (ResourceLocation) objectIn.keySet().toArray()[i];
            JsonObject object = objectIn.get(location).getAsJsonObject();
            ResourceLocation id = new ResourceLocation(object.getAsJsonPrimitive("id").getAsString());
            if (PEBBLE_CREATION_DATA.containsKey(id)) {
                PebbleMod.LOGGER.info("Block Rummaging loot entry with id : " + id + " already exists. Overwriting.");
            }
            Ingredient target = Ingredient.fromJson(object.getAsJsonObject("target"));

            HeldData heldDataInstance = null;
            if (object.has("held_item_data")) {
                JsonObject heldData = object.getAsJsonObject("held_item_data");
                PebbleCreationEntry.HeldDataType<?> heldDataType = DataTypeRegistry.DATA_TYPES.get(new ResourceLocation(heldData.get("type").getAsString()));
                if (heldDataType == null) {
                    PebbleMod.LOGGER.info("Block Rummaging loot entry with id : " + id + " attempts to reference an non existent held item data type. Skipping.");
                    continue;
                }
                heldDataInstance = heldDataType.serializer().apply(heldData);
            }
            List<PebbleCreationEntry.LootEntry> lootEntries = new ArrayList<>();
            JsonArray result = object.getAsJsonArray("result");
            result.forEach(entry -> {
                JsonObject entryObject = entry.getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(entryObject.getAsJsonObject().get("output"));
                int amount = entryObject.getAsJsonPrimitive("amount").getAsInt();
                float chance = entryObject.getAsJsonPrimitive("chance").getAsFloat();
                lootEntries.add(new PebbleCreationEntry.LootEntry(ingredient, amount, chance));
            });
            PEBBLE_CREATION_DATA.put(id, new PebbleCreationEntry(id, target, heldDataInstance, lootEntries));
        }
    }
}
