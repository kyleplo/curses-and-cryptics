package com.kyleplo.curses_and_cryptics.fabric;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.kyleplo.curses_and_cryptics.CursesAndCryptics;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;

public class CursesAndCrypticsRegistryImpl {
    public static final List<Item> itemsForCreativeTab = new ArrayList<>();

    public static Holder<Attribute> registerAttribute(String name, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE,
                ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, name),
                attribute);
    }

    public static <T> Holder<DataComponentType<T>> registerDataComponentType(String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOp) {
        DataComponentType<T> component = builderOp.apply(DataComponentType.builder()).build();
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, name, component);
        return Holder.direct(component);
    }

    public static Holder<SoundEvent> registerSoundEvent(String name) {
        return Holder.direct(
                Registry.register(BuiltInRegistries.SOUND_EVENT,
                        ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, name),
                        SoundEvent.createVariableRangeEvent(
                                ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID,
                                        name))));
    }

    public static Holder<Item> registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, name))));
        itemsForCreativeTab.add(item);
        return Registry.registerForHolder(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID, name), item);
    }
}
