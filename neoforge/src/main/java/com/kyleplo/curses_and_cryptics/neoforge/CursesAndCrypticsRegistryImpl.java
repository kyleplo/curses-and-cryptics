package com.kyleplo.curses_and_cryptics.neoforge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.kyleplo.curses_and_cryptics.CursesAndCryptics;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CursesAndCrypticsRegistryImpl {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, CursesAndCryptics.MOD_ID);
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CursesAndCryptics.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, CursesAndCryptics.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CursesAndCryptics.MOD_ID);
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, CursesAndCryptics.MOD_ID);

    public static final List<DeferredItem<Item>> itemsForCreativeTab = new ArrayList<>();
    
    public static Holder<Attribute> registerAttribute(String name, Attribute attribute) {
        return ATTRIBUTES.register(name, () -> attribute);
    }

    public static <T> Holder<DataComponentType<?>> registerDataComponentType(String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOp) {
        return DATA_COMPONENT_TYPES.registerComponentType(name, builderOp).getDelegate();
    }

    public static Holder<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                                ResourceLocation.fromNamespaceAndPath(CursesAndCryptics.MOD_ID,
                                        name)));
    }

    public static Holder<Item> registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        DeferredItem<Item> deferredItem = ITEMS.registerItem(name, itemFactory, settings);
        itemsForCreativeTab.add(deferredItem);
        return deferredItem;
    }

    public static <T extends LootItemFunction> Holder<LootItemFunctionType<?>> registerLootFunction(String name, MapCodec<T> codec) {
        return LOOT_FUNCTIONS.register(name, () -> new LootItemFunctionType<>(codec));
    }
}
