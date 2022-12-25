package com.sammy.block_rummage.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.block_rummage.PebbleMod;
import com.sammy.block_rummage.data_types.EmptyItemData;
import com.sammy.block_rummage.data_types.HeldData;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PebbleMakerJEICategory implements IRecipeCategory<PebbleCreationEntry> {

    public static final ResourceLocation UID = PebbleMod.path("rummaging");
    private final IDrawable background;
    private final IDrawable overlay;
    private final IDrawable overlayEmpty;
    private final IDrawable icon;

    private final IDrawable slot;
    private final IDrawable slotChance;

    public PebbleMakerJEICategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(101, 55);
        ResourceLocation backgroundImage = new ResourceLocation(PebbleMod.MODID, "textures/gui/rummaging_jei.png");
        overlay = guiHelper.createDrawable(backgroundImage, 0, 0, 101, 55);
        overlayEmpty = guiHelper.createDrawable(backgroundImage, 0, 56, 101, 55);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.GRAVEL));

        slot = guiHelper.createDrawable(backgroundImage, 101, 0, 18, 18);
        slotChance = guiHelper.createDrawable(backgroundImage, 101, 18, 18, 18);
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
    public void draw(PebbleCreationEntry recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        if (recipe.data() instanceof EmptyItemData) {
            overlayEmpty.draw(stack);
        }
        else {
            overlay.draw(stack);
        }
        List<PebbleCreationEntry.LootEntry> entries = recipe.lootEntries();
        int size = entries.size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            if (recipe.lootEntries().get(i).chance() < 1f) {
                slotChance.draw(stack, 57 + xOffset, 8 + yOffset);
            } else {
                slot.draw(stack, 57 + xOffset, 8 + yOffset);
            }
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PebbleCreationEntry recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 3, 35)
                .addIngredients(recipe.block());
        HeldData data = recipe.data();
        Ingredient ingredient = data.getIngredient();
        if (!ingredient.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 3, 3)
                    .addItemStacks(Arrays.stream(ingredient.getItems()).map(data::getModifiedItemStack).collect(Collectors.toList()))
                    .addTooltipCallback((view, tooltip) -> {
                        data.addTooltipInfoToIngredient(tooltip);
                    });
        }
        List<PebbleCreationEntry.LootEntry> entries = recipe.lootEntries();
        int size = entries.size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            PebbleCreationEntry.LootEntry lootEntry = recipe.lootEntries().get(i);

            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            builder.addSlot(RecipeIngredientRole.OUTPUT, 58 + xOffset, 9 + yOffset)
                    .addItemStacks(copyWithNewCount(List.of(lootEntry.output().getItems()), lootEntry.amount()))
                    .addTooltipCallback((view, tooltip) -> {
                        float chance = lootEntry.chance();
                        if (chance != 1) {
                            tooltip.add(1, Component.literal("" + (chance < 0.01 ? "<1" : (int) (chance * 100))).append("% ").append(Component.translatable("block_rummage.jei.chance"))
                                    .withStyle(ChatFormatting.GOLD));
                        }
                    });
        }
    }

    public static ArrayList<ItemStack> copyWithNewCount(List<ItemStack> stacks, int newCount) {
        ArrayList<ItemStack> newStacks = new ArrayList<>();
        for (ItemStack stack : stacks) {
            ItemStack copy = stack.copy();
            copy.setCount(newCount);
            newStacks.add(copy);
        }
        return newStacks;
    }
}