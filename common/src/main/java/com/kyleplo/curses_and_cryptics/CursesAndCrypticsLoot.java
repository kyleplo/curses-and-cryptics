package com.kyleplo.curses_and_cryptics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class CursesAndCrypticsLoot {
    public static void injectLoot(ResourceKey<LootTable> key, LootTable table, HolderLookup.Provider registries,
            Consumer<LootPool.Builder> addPool) {
        if (CursesAndCryptics.config.whetstoneLoot) {
            if (BuiltInLootTables.VILLAGE_TOOLSMITH.equals(key) || BuiltInLootTables.VILLAGE_ARMORER.equals(key)
                    || BuiltInLootTables.VILLAGE_WEAPONSMITH.equals(key)) {
                addPool.accept(new LootPool.Builder()
                        .setRolls(UniformGenerator.between(0, 2))
                        .add(EmptyLootItem.emptyItem().setWeight(16))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.WHETSTONE.value()).setWeight(10))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.BLESSED_WHETSTONE.value()).setWeight(1)));
            } else if (BuiltInLootTables.SIMPLE_DUNGEON.equals(key) || BuiltInLootTables.ABANDONED_MINESHAFT.equals(key)
                    || BuiltInLootTables.STRONGHOLD_LIBRARY.equals(key) || BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(key)
                    || BuiltInLootTables.WOODLAND_MANSION.equals(key)
                    || BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR.equals(key)) {
                addPool.accept(new LootPool.Builder()
                        .add(EmptyLootItem.emptyItem().setWeight(8))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.WHETSTONE.value()).setWeight(5))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.BLESSED_WHETSTONE.value()).setWeight(3))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.ENCHANTED_BLESSED_WHETSTONE.value()).setWeight(1)));
            }
        }

        if (CursesAndCryptics.config.crypticEnchantedBookLoot) {
            if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(key)) {
                addPool.accept(new LootPool.Builder()
                        .add(EmptyLootItem.emptyItem().setWeight(1))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK.value()).setWeight(2).apply(SetCrypticEnchantedBookLevelFunction.setLevel(UniformGenerator.between(30, 40)))));
            } else if (BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(key) || BuiltInLootTables.STRONGHOLD_CROSSING.equals(key) || BuiltInLootTables.JUNGLE_TEMPLE.equals(key)) {
                addPool.accept(new LootPool.Builder()
                        .add(EmptyLootItem.emptyItem().setWeight(20))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK.value()).setWeight(1).apply(SetCrypticEnchantedBookLevelFunction.setLevel(UniformGenerator.between(30, 40)))));
            } else if (BuiltInLootTables.SIMPLE_DUNGEON.equals(key) || BuiltInLootTables.ABANDONED_MINESHAFT.equals(key) || BuiltInLootTables.UNDERWATER_RUIN_SMALL.equals(key) || BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(key)) {
                addPool.accept(new LootPool.Builder()
                        .add(EmptyLootItem.emptyItem().setWeight(10))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK.value()).setWeight(1).apply(SetCrypticEnchantedBookLevelFunction.setLevel(UniformGenerator.between(8, 20)))));
            } else if (BuiltInLootTables.ANCIENT_CITY.equals(key) || BuiltInLootTables.PILLAGER_OUTPOST.equals(key) || BuiltInLootTables.DESERT_PYRAMID.equals(key) || BuiltInLootTables.WOODLAND_MANSION.equals(key)) {
                addPool.accept(new LootPool.Builder()
                        .add(EmptyLootItem.emptyItem().setWeight(8))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK.value()).setWeight(1).apply(SetCrypticEnchantedBookLevelFunction.setLevel(UniformGenerator.between(15, 32)))));
            }
        }

        if (CursesAndCryptics.config.misappropriationSigilLoot) {
            if (BuiltInLootTables.RUINED_PORTAL.equals(key) || BuiltInLootTables.BASTION_OTHER.equals(key) || BuiltInLootTables.BASTION_BRIDGE.equals(key) || BuiltInLootTables.BASTION_TREASURE.equals(key) || BuiltInLootTables.TRIAL_CHAMBERS_ENTRANCE.equals(key) || BuiltInLootTables.ANCIENT_CITY.equals(key) || BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(key) || BuiltInLootTables.SHIPWRECK_TREASURE.equals(key) || BuiltInLootTables.BURIED_TREASURE.equals(key)) {
                addPool.accept(new LootPool.Builder()
                        .add(EmptyLootItem.emptyItem().setWeight(7))
                        .add(LootItem.lootTableItem(CursesAndCrypticsRegistry.MISAPPROPRIATION_SIGIL.value()).setWeight(1)));
            }
        }
    }

    public static void setTrades () {
        if (CursesAndCryptics.config.whetstoneTrade) {
            setTradesFor(VillagerProfession.WEAPONSMITH);
            setTradesFor(VillagerProfession.ARMORER);
            setTradesFor(VillagerProfession.TOOLSMITH);
        }
    }

    public static void setTradesFor(ResourceKey<VillagerProfession> villager) {
        Int2ObjectMap<ItemListing[]> trades = VillagerTrades.TRADES.get(villager);
        ArrayList<ItemListing> newSecondTrades = new ArrayList<>();
        Collections.addAll(newSecondTrades, trades.get(2));
        newSecondTrades.add(new ItemListing () {
            @Override
            public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
                return new MerchantOffer(new ItemCost(Items.EMERALD, 8), new ItemStack(CursesAndCrypticsRegistry.WHETSTONE, 1), 3, 5, 0.2f);
            }
        });
        ItemListing[] newSecondTradesArray = new ItemListing[newSecondTrades.size()];
        for (int i = 0; i < newSecondTrades.size(); i++) {
            newSecondTradesArray[i] = newSecondTrades.get(i);
        }
        trades.put(2, newSecondTradesArray);
    }
}
