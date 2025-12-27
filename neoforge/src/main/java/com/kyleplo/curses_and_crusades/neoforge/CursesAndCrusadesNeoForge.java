package com.kyleplo.curses_and_crusades.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;

@Mod(CursesAndCrusades.MOD_ID)
public final class CursesAndCrusadesNeoForge {
    public CursesAndCrusadesNeoForge(IEventBus modBus) {
        // Run our common setup.
        CursesAndCrusades.init();

        CursesAndCrusadesRegistryImpl.ATTRIBUTES.register(modBus);
        CursesAndCrusadesRegistryImpl.DATA_COMPONENT_TYPES.register(modBus);
        CursesAndCrusadesRegistryImpl.SOUND_EVENTS.register(modBus);
    }
}
