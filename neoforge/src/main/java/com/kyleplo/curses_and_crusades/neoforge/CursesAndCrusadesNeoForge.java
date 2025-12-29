package com.kyleplo.curses_and_crusades.neoforge;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;

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
    }

    private static void onBuildCreativeModeTabContents (BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            CursesAndCrusadesRegistryImpl.itemsForCreativeTab.forEach(item -> {
                event.accept(item);
            });
        }
    }
}
