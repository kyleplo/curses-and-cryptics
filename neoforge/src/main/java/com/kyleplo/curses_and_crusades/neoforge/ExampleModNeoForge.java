package com.kyleplo.curses_and_crusades.neoforge;

import net.neoforged.fml.common.Mod;

import com.kyleplo.curses_and_crusades.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
