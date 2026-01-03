package com.kyleplo.curses_and_crusades.neoforge;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;
import com.kyleplo.curses_and_crusades.CursesAndCrusadesLoot;
import com.kyleplo.curses_and_crusades.CursesAndCrusadesRegistry;

@Mod(CursesAndCrusades.MOD_ID)
public final class CursesAndCrusadesNeoForge {
    public CursesAndCrusadesNeoForge(IEventBus modBus) {
        // Run our common setup.
        CursesAndCrusades.init();

        CursesAndCrusadesRegistryImpl.ATTRIBUTES.register(modBus);
        CursesAndCrusadesRegistryImpl.DATA_COMPONENT_TYPES.register(modBus);
        CursesAndCrusadesRegistryImpl.SOUND_EVENTS.register(modBus);
        CursesAndCrusadesRegistryImpl.ITEMS.register(modBus);
        
        modBus.addListener(CursesAndCrusadesNeoForge::onBuildCreativeModeTabContents);
        NeoForge.EVENT_BUS.addListener(CursesAndCrusadesNeoForge::onGrindstoneTake);
        NeoForge.EVENT_BUS.addListener(CursesAndCrusadesNeoForge::onLootTableLoad);
    }

    private static void onBuildCreativeModeTabContents (BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            CursesAndCrusadesRegistryImpl.itemsForCreativeTab.forEach(item -> {
                event.accept(item);
            });
        }
    }

    private static void onGrindstoneTake (GrindstoneEvent.OnTakeItem event) {
        if (event.getTopItem().is(CursesAndCrusadesRegistry.WHETSTONES)) {
            ItemStack top = event.getTopItem().copy();
            top.shrink(1);
            event.setNewTopItem(top);
        } else if (event.getBottomItem().is(CursesAndCrusadesRegistry.WHETSTONES)) {
            ItemStack bottom = event.getBottomItem().copy();
            bottom.shrink(1);
            event.setNewBottomItem(bottom);
        }
    }

    private static void onLootTableLoad (LootTableLoadEvent event) {
        CursesAndCrusadesLoot.injectLoot(event.getKey(), event.getTable(), event.getRegistries(), (LootPool.Builder pool) -> {
            event.getTable().addPool(pool.build());
        });
    }
}
