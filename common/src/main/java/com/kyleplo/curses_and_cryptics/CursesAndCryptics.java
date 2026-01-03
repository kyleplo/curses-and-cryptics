package com.kyleplo.curses_and_cryptics;

public final class CursesAndCryptics {
    public static final String MOD_ID = "curses_and_cryptics";
    public static CursesAndCrypticsConfig config;

    public static void init() {
        config = CursesAndCrypticsConfig.init();
        CursesAndCrypticsRegistry.initialize();
        CursesAndCrypticsLoot.setTrades();
    }
}
