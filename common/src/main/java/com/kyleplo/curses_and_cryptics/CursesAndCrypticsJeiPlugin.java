package com.kyleplo.curses_and_cryptics;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiGrindstoneRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@JeiPlugin
public class CursesAndCrypticsJeiPlugin implements IModPlugin {
    public CursesAndCrypticsJeiPlugin() {

    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, CursesAndCryptics.MOD_ID);
    }
    
    public void registerRecipes(IRecipeRegistration registration) {
        ArrayList<IJeiAnvilRecipe> anvilRecipes = new ArrayList<>();
        ArrayList<IJeiGrindstoneRecipe> grindstoneRecipes = new ArrayList<>();

        if (CursesAndCryptics.config.crypticEnchantedBookCombining) {
            anvilRecipes.add(registration.getVanillaRecipeFactory().createAnvilRecipe(
                new ItemStack(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK),
                List.of(new ItemStack(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK)),
                List.of(new ItemStack(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK, 1, DataComponentPatch.builder().set(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), 2).build())),
                null
            ));
        }

        if (CursesAndCryptics.config.crypticEnchantedBookGrindstoning) {
            grindstoneRecipes.add(registration.getVanillaRecipeFactory().createGrindstoneRecipe(
                List.of(new ItemStack(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK)), 
                List.of(ItemStack.EMPTY),
                List.of(new ItemStack(Items.BOOK)), 7, 7, 
                null
            ));
        }

        for (ItemStack itemStack : registration.getIngredientManager().getAllItemStacks()) {
            if (itemStack.has(DataComponents.ENCHANTABLE) && ((!itemStack.is(Items.BOOK) && !itemStack.has(DataComponents.STORED_ENCHANTMENTS)) || CursesAndCryptics.config.crypticEnchantedBookWorksOnBooks)) {
                ItemStack result = itemStack.copy();
                result.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.ENCHANTMENTS, true).withHidden(DataComponents.STORED_ENCHANTMENTS, true).withHidden(DataComponents.ATTRIBUTE_MODIFIERS, true));
                result.set(DataComponents.LORE, ItemLore.EMPTY.withLineAdded(Component.translatable("jei.hint.curses_and_cryptics.cryptic_enchanted_book").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))));
                anvilRecipes.add(registration.getVanillaRecipeFactory().createAnvilRecipe(
                    itemStack,
                    List.of(new ItemStack(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK)),
                    List.of(result),
                    null
                ));
            }

            if (itemStack.has(DataComponents.ENCHANTABLE) && itemStack.has(DataComponents.DAMAGE)) {
                ItemStack actualItem = itemStack.copy();
                actualItem.set(DataComponents.LORE, ItemLore.EMPTY.withLineAdded(Component.translatable("jei.hint.curses_and_cryptics.misappropriation_sigil_input").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))));
                actualItem.set(DataComponents.REPAIR_COST, 1);
                ItemStack result = itemStack.copy();
                result.remove(DataComponents.REPAIR_COST);
                result.set(DataComponents.LORE, ItemLore.EMPTY.withLineAdded(Component.translatable("jei.hint.curses_and_cryptics.misappropriation_sigil_output").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))));
                // if (CursesAndCryptics.config.misappropriationCurse) {
                //     result.enchant(CursesAndCrypticsRegistry.MISAPPROPRIATION_CURSE, 1);
                // }
                anvilRecipes.add(registration.getVanillaRecipeFactory().createAnvilRecipe(
                    actualItem,
                    List.of(new ItemStack(CursesAndCrypticsRegistry.MISAPPROPRIATION_SIGIL)),
                    List.of(result),
                    null
                ));
            }

            if (EnchantmentHelper.hasAnyEnchantments(itemStack)) {
                if (EnchantmentHelper.hasTag(itemStack, EnchantmentTags.CURSE) || itemStack.get(DataComponents.STORED_ENCHANTMENTS).keySet().stream().anyMatch((enchantment) -> {
                    return enchantment.is(EnchantmentTags.CURSE);
                })) {
                    ItemStack input = itemStack.copy();
                    input.set(DataComponents.ITEM_NAME, Component.translatable("jei.hint.curses_and_cryptics.any_cursed_item"));
                    ItemStack result = itemStack.copy();
                    result.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                    result.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
                    result.set(DataComponents.LORE, ItemLore.EMPTY.withLineAdded(Component.translatable("jei.hint.curses_and_cryptics.enchanted_blessed_whetstone").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))));
                    if (result.is(Items.ENCHANTED_BOOK)) {
                        result = result.transmuteCopy(Items.BOOK);
                    }
                    grindstoneRecipes.add(registration.getVanillaRecipeFactory().createGrindstoneRecipe(
                        List.of(input), 
                        List.of(new ItemStack(CursesAndCrypticsRegistry.ENCHANTED_BLESSED_WHETSTONE)),
                        List.of(result),
                         0,
                         0,
                         null));
                } else {
                    ItemStack input = itemStack.copy();
                    input.set(DataComponents.ITEM_NAME, Component.translatable("jei.hint.curses_and_cryptics.any_enchanted_item"));
                    ItemStack result = itemStack.copy();
                    result.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                    result.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
                    result.set(DataComponents.LORE, ItemLore.EMPTY.withLineAdded(Component.translatable("jei.hint.curses_and_cryptics.whetstone").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))));
                    if (result.is(Items.ENCHANTED_BOOK)) {
                        result = result.transmuteCopy(Items.BOOK);
                    }
                    grindstoneRecipes.add(registration.getVanillaRecipeFactory().createGrindstoneRecipe(
                        List.of(input), 
                        List.of(new ItemStack(CursesAndCrypticsRegistry.WHETSTONE)),
                        List.of(result),
                         0,
                         0,
                         null));
                }

                ItemStack input = itemStack.copy();
                    input.set(DataComponents.ITEM_NAME, Component.translatable("jei.hint.curses_and_cryptics.any_enchanted_item"));
                ItemStack result = itemStack.copy();
                result.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                result.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
                result.set(DataComponents.LORE, ItemLore.EMPTY.withLineAdded(Component.translatable("jei.hint.curses_and_cryptics.blessed_whetstone").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))));
                if (result.is(Items.ENCHANTED_BOOK)) {
                    result = result.transmuteCopy(Items.BOOK);
                }
                grindstoneRecipes.add(registration.getVanillaRecipeFactory().createGrindstoneRecipe(
                    List.of(input), 
                    List.of(new ItemStack(CursesAndCrypticsRegistry.BLESSED_WHETSTONE)),
                    List.of(result),
                        0,
                        0,
                        null));
            }
        }

        registration.addRecipes(RecipeTypes.ANVIL, anvilRecipes);
        registration.addRecipes(RecipeTypes.GRINDSTONE, grindstoneRecipes);

        registration.addIngredientInfo(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK.value(), Component.translatable("jei.info.curses_and_cryptics.cryptic_enchanted_book"));
        registration.addIngredientInfo(CursesAndCrypticsRegistry.MISAPPROPRIATION_SIGIL.value(), Component.translatable("jei.info.curses_and_cryptics.misappropriation_sigil"));
        registration.addIngredientInfo(CursesAndCrypticsRegistry.ENCHANTED_BLESSED_WHETSTONE.value(), Component.translatable("jei.info.curses_and_cryptics.enchanted_blessed_whetstone"));
        registration.addIngredientInfo(CursesAndCrypticsRegistry.BLESSED_WHETSTONE.value(), Component.translatable("jei.info.curses_and_cryptics.blessed_whetstone"));
        registration.addIngredientInfo(CursesAndCrypticsRegistry.WHETSTONE.value(), Component.translatable("jei.info.curses_and_cryptics.whetstone"));
    }
}
