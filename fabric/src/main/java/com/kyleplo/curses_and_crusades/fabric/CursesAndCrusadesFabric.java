package com.kyleplo.curses_and_crusades.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;

public final class CursesAndCrusadesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CursesAndCrusades.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> {
            CursesAndCrusadesRegistryImpl.itemsForCreativeTab.forEach((item) -> {
                itemGroup.accept(item);
            });
        });
    }
}
