package com.kyleplo.curses_and_crusades;

import java.util.function.UnaryOperator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attribute.Sentiment;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.enchantment.Enchantment;

public class CursesAndCrusadesRegistry {
    public static final ResourceKey<Enchantment> IMMUTABILITY_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, "immutability_curse"));
    public static final ResourceKey<Enchantment> OBSCURING_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, "obscuring_curse"));
    public static final ResourceKey<Enchantment> QUIXOTISM_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, "quixotism_curse"));
    public static final ResourceKey<Enchantment> INSTABILITY_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, "instability_curse"));
    public static final ResourceKey<Enchantment> MIASMA_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, "miasma_curse"));

    public static Holder<Attribute> DETECTABLE_RANGE;

    public static Holder<DataComponentType<Integer>> POST_ANVIL_PROCESSING;

    public static Holder<SoundEvent> ANVIL_APPLY_CURSE;

    public static void initialize() {
        DETECTABLE_RANGE = registerAttribute("detectable_range",
                (new RangedAttribute("attribute.name.detectable_range", 0.0, -16.0, 16.0)).setSyncable(true)
                        .setSentiment(Sentiment.NEGATIVE));

        POST_ANVIL_PROCESSING = registerDataComponentType("post_anvil_processing", (builder -> {
            return builder.persistent(ExtraCodecs.intRange(0, 2));
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
}
