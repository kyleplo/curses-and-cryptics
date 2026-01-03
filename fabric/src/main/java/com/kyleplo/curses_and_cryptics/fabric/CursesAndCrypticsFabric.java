package com.kyleplo.curses_and_cryptics.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.storage.loot.LootPool;

import com.kyleplo.curses_and_cryptics.CursesAndCryptics;
import com.kyleplo.curses_and_cryptics.CursesAndCrypticsLoot;

public final class CursesAndCrypticsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CursesAndCryptics.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> {
            CursesAndCrypticsRegistryImpl.itemsForCreativeTab.forEach((item) -> {
                itemGroup.accept(item);
            });
        });

        LootTableEvents.MODIFY.register((id, tableBuilder, source, registries) -> {
            CursesAndCrypticsLoot.injectLoot(id, tableBuilder.build(), registries, (LootPool.Builder pool) -> {
                tableBuilder.withPool(pool);
            });
        });
    }
}
