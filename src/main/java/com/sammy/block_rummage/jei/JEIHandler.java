package com.sammy.block_rummage.jei;

import com.sammy.block_rummage.PebbleMod;
import com.sammy.block_rummage.listener.PebbleCreationDataListener;
import com.sammy.block_rummage.listener.PebbleCreationEntry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;


@JeiPlugin
public class JEIHandler implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(PebbleMod.MODID, "main");

    public static final RecipeType<PebbleCreationEntry> PEBBLE_MAKING = new RecipeType<>(PebbleMakerJEICategory.UID, PebbleCreationEntry.class);

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new PebbleMakerJEICategory(guiHelper));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            registry.addRecipes(PEBBLE_MAKING, PebbleCreationDataListener.PEBBLE_CREATION_DATA.values().stream().toList());
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
