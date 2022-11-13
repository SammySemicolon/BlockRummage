package com.sammy.block_rummage.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.block_rummage.PebbleMod;
import com.sammy.block_rummage.listener.PebbleCreationEntry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;

public class PebbleMakerJEICategory implements IRecipeCategory<PebbleCreationEntry> {

    public static final ResourceLocation UID = PebbleMod.path("pebble_making");
    private final IDrawable background;
    private final IDrawable overlay;
    private final IDrawable icon;

    public PebbleMakerJEICategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(146, 98);
        overlay = guiHelper.createDrawable(new ResourceLocation(PebbleMod.MODID, "textures/gui/rummaging_jei.png"), 0, 0, 144, 96);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.GRAVEL));
    }

    @Override
    public void draw(PebbleCreationEntry recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        overlay. draw(stack);
    }

    @Override
    public RecipeType<PebbleCreationEntry> getRecipeType() {
        return JEIHandler.PEBBLE_MAKING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block_rummage.jei." + UID.getPath());
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PebbleCreationEntry recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 48)
                .addIngredients(recipe.block());
    }
}
