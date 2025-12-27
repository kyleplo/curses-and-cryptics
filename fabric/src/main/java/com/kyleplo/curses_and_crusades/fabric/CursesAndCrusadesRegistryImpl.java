package com.kyleplo.curses_and_crusades.fabric;

import java.util.function.UnaryOperator;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class CursesAndCrusadesRegistryImpl {
    public static Holder<Attribute> registerAttribute(String name, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE,
                ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, name),
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
                        ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, name),
                        SoundEvent.createVariableRangeEvent(
                                ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID,
                                        name))));
    }
}
