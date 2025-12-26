package com.kyleplo.curses_and_crusades;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.enchantment.Enchantment;

public final class CursesAndCrusades {
    public static final String MOD_ID = "curses_and_crusades";

    public static final ResourceKey<Enchantment> IMMUTABILITY_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "immutability_curse"));
    public static final ResourceKey<Enchantment> OBSCURING_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "obscuring_curse"));
    public static final ResourceKey<Enchantment> QUIXOTISM_CURSE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "quixotism_curse"));

    //public static final Supplier<DataComponentType<Unit>> PREVENT_ANVIL_ENCHANTING = regEnchComponent(
    //        "prevent_anvil_enchanting", builder -> builder.persistent(Unit.CODEC));

    public static void init() {
        // Write common init code here.
    }

    // public static <R, T extends R> Supplier<T> register(String name, Supplier<T> supplier, Registry<R> reg) {
    //     T object = supplier.get();
    //     Registry.register(reg, ResourceLocation.fromNamespaceAndPath(MOD_ID, name), object);
    //     return () -> object;
    // }

    // public static <A> Supplier<DataComponentType<A>> regEnchComponent(String name,
    //         Consumer<DataComponentType.Builder<A>> stuff) {
    //     DataComponentType.Builder<A> builder = DataComponentType.builder();
    //     stuff.accept(builder);
    //     return register(name, builder::build, BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE);
    // }
}
