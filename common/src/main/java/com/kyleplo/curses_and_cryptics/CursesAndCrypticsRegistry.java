package com.kyleplo.curses_and_cryptics;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.mojang.serialization.codecs.PrimitiveCodec;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attribute.Sentiment;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;

public class CursesAndCrypticsRegistry {
    public static final ResourceKey<Enchantment> IMMUTABILITY_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, "immutability_curse"));
    public static final ResourceKey<Enchantment> OBSCURING_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, "obscuring_curse"));
    public static final ResourceKey<Enchantment> QUIXOTISM_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, "quixotism_curse"));
    public static final ResourceKey<Enchantment> INSTABILITY_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, "instability_curse"));
    public static final ResourceKey<Enchantment> MIASMA_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, "miasma_curse"));

    public static final Holder<Attribute> DETECTABLE_RANGE = registerAttribute("detectable_range",
                (new RangedAttribute("attribute.name.detectable_range", 0.0, -16.0, 16.0)).setSyncable(true)
                        .setSentiment(Sentiment.NEGATIVE));;

    public static Holder<DataComponentType<Integer>> POST_ANVIL_PROCESSING;
    public static Holder<DataComponentType<Boolean>> POST_GRINDSTONE_PROCESSING;

    public static Holder<SoundEvent> ANVIL_APPLY_CURSE;

    public static Holder<Item> WHETSTONE = registerItem("whetstone", Item::new, new Item.Properties());
    public static Holder<Item> BLESSED_WHETSTONE = registerItem("blessed_whetstone", Item::new, new Item.Properties());
    public static Holder<Item> ENCHANTED_BLESSED_WHETSTONE = registerItem("enchanted_blessed_whetstone", Item::new, new Item.Properties().rarity(Rarity.RARE).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));

    public static TagKey<Item> WHETSTONES = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, "whetstones"));

    public static void initialize() {
        POST_ANVIL_PROCESSING = registerDataComponentType("post_anvil_processing", (builder -> {
            return builder.persistent(ExtraCodecs.intRange(0, 2));
        }));
        POST_GRINDSTONE_PROCESSING = registerDataComponentType("post_grindstone_processing", (builder -> {
            return builder.persistent(PrimitiveCodec.BOOL);
        }));

        ANVIL_APPLY_CURSE = registerSoundEvent("block.anvil.apply_curse");
    }

    @ExpectPlatform
    public static Holder<Attribute> registerAttribute(String name, Attribute attribute) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> Holder<DataComponentType<T>> registerDataComponentType(String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOp) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Holder<SoundEvent> registerSoundEvent(String name) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Holder<Item> registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        throw new AssertionError();
    }
}
