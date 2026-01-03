package com.kyleplo.curses_and_crusades;

public final class CursesAndCrusades {
    public static final String MOD_ID = "curses_and_crusades";
    public static CursesAndCrusadesConfig config;

    public static void init() {
        config = CursesAndCrusadesConfig.init();
        CursesAndCrusadesRegistry.initialize();
        CursesAndCrusadesLoot.setTrades();
    }
}
