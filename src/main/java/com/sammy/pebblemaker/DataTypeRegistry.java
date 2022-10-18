package com.sammy.pebblemaker;

import com.sammy.pebblemaker.data_types.EmptyItemData;
import com.sammy.pebblemaker.listener.PebbleCreationEntry.HeldDataType;
import com.sammy.pebblemaker.data_types.HeldData;
import com.sammy.pebblemaker.data_types.MatchingIngredientData;
import com.sammy.pebblemaker.data_types.ToolActionData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;

public class DataTypeRegistry {

    public static final HashMap<ResourceLocation, HeldDataType<?>> DATA_TYPES = new HashMap<>();

    public static HeldDataType<EmptyItemData> EMPTY_HAND = register(new HeldDataType<>(PebbleMod.path("empty_hand"), o -> new EmptyItemData()));

    public static HeldDataType<MatchingIngredientData> MATCHING_INGREDIENT = register(new HeldDataType<>(PebbleMod.path("matching_ingredient"), o -> {
        Ingredient ingredient = Ingredient.fromJson(o.get("ingredient"));
        int durabilityCost = o.has("durability_cost") ? o.get("durability_cost").getAsInt() : 0;
        return new MatchingIngredientData(ingredient, durabilityCost);
    }));

    public static HeldDataType<ToolActionData> HAS_TOOL_ACTION = register(new HeldDataType<>(PebbleMod.path("has_tool_action"), o -> {
        net.minecraftforge.common.ToolAction toolAction = net.minecraftforge.common.ToolAction.get(o.get("tool_action").getAsString());
        int durabilityCost = o.has("durability_cost") ? o.get("durability_cost").getAsInt() : 0;
        return new ToolActionData(toolAction, durabilityCost);
    }));

    public static <T extends HeldData> HeldDataType<T> register(HeldDataType<T> heldDataType) {
        DATA_TYPES.put(heldDataType.id(), heldDataType);
        return heldDataType;
    }

}