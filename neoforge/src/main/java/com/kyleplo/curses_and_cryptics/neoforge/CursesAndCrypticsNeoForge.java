package com.kyleplo.curses_and_cryptics.neoforge;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;

import com.kyleplo.curses_and_cryptics.CursesAndCryptics;
import com.kyleplo.curses_and_cryptics.CursesAndCrypticsLoot;
import com.kyleplo.curses_and_cryptics.CursesAndCrypticsRegistry;

@Mod(CursesAndCryptics.MOD_ID)
public final class CursesAndCrypticsNeoForge {
    public CursesAndCrypticsNeoForge(IEventBus modBus) {
        // Run our common setup.
        CursesAndCryptics.init();

        CursesAndCrypticsRegistryImpl.ATTRIBUTES.register(modBus);
        CursesAndCrypticsRegistryImpl.DATA_COMPONENT_TYPES.register(modBus);
        CursesAndCrypticsRegistryImpl.SOUND_EVENTS.register(modBus);
        CursesAndCrypticsRegistryImpl.ITEMS.register(modBus);
        CursesAndCrypticsRegistryImpl.LOOT_FUNCTIONS.register(modBus);
        
        modBus.addListener(CursesAndCrypticsNeoForge::onBuildCreativeModeTabContents);
        NeoForge.EVENT_BUS.addListener(CursesAndCrypticsNeoForge::onGrindstoneTake);
        NeoForge.EVENT_BUS.addListener(CursesAndCrypticsNeoForge::onLootTableLoad);
    }

    private static void onBuildCreativeModeTabContents (BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            CursesAndCrypticsRegistryImpl.itemsForCreativeTab.forEach(item -> {
                event.accept(item);
            });
        }
    }

    private static void onGrindstoneTake (GrindstoneEvent.OnTakeItem event) {
        if (event.getTopItem().is(CursesAndCrypticsRegistry.WHETSTONES)) {
            ItemStack top = event.getTopItem().copy();
            top.shrink(1);
            event.setNewTopItem(top);
        } else if (event.getBottomItem().is(CursesAndCrypticsRegistry.WHETSTONES)) {
            ItemStack bottom = event.getBottomItem().copy();
            bottom.shrink(1);
            event.setNewBottomItem(bottom);
        }
    }

    private static void onLootTableLoad (LootTableLoadEvent event) {
        CursesAndCrypticsLoot.injectLoot(event.getKey(), event.getTable(), event.getRegistries(), (LootPool.Builder pool) -> {
            event.getTable().addPool(pool.build());
        });
    }
}
