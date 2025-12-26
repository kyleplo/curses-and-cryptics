package com.kyleplo.curses_and_crusades;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute.Sentiment;
import net.minecraft.world.item.enchantment.Enchantment;

public final class CursesAndCrusades {
    public static final String MOD_ID = "curses_and_crusades";
    
    public static final ResourceKey<Enchantment> IMMUTABILITY_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "immutability_curse"));
    public static final ResourceKey<Enchantment> OBSCURING_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "obscuring_curse"));
    public static final ResourceKey<Enchantment> QUIXOTISM_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "quixotism_curse"));
    public static final ResourceKey<Enchantment> INSTABILITY_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "instability_curse"));
    public static final ResourceKey<Enchantment> MIASMA_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "miasma_curse"));

    public static final Holder<Attribute> DETECTABLE_RANGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "detectable_range"),
            (new RangedAttribute("attribute.name.detectable_range", 0.0, -16.0, 16.0)).setSyncable(true)
                    .setSentiment(Sentiment.NEGATIVE));

    public static void init() {
        // Write common init code here.
    }
}
