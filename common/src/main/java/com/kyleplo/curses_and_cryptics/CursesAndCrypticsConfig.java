package com.kyleplo.curses_and_cryptics;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CursesAndCrypticsConfig {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String COMMENT = "Individual curses can be disabled using datapacks by removing them from the curses_and_cryptics:curses enchantment tag";

    public boolean anvilCurses = true;
    public double defaultAnvilCurseChance = 0.02;
    public double chippedAnvilCurseChance = 0.05;
    public double damagedAnvilCurseChance = 0.1;

    public boolean whetstoneLoot = true;
    public boolean whetstoneTrade = false;

    public static CursesAndCrypticsConfig init() {
        CursesAndCrypticsConfig config = null;

        try {
            Path configPath = Paths.get("", "config", "curses_and_cryptics.json");

            if (Files.exists(configPath)) {
                config = gson.fromJson(
                        new FileReader(configPath.toFile()),
                        CursesAndCrypticsConfig.class);

                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(configPath.toFile()));

                writer.write(gson.toJson(config));
                writer.close();

            } else {
                config = new CursesAndCrypticsConfig();
                Paths.get("", "config").toFile().mkdirs();

                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(configPath.toFile()));

                writer.write(gson.toJson(config));
                writer.close();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return config;
    }
}
