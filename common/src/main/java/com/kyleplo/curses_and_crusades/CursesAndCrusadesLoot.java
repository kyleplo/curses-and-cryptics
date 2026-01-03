package com.kyleplo.curses_and_crusades;

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

public class CursesAndCrusadesLoot {
    public static void injectLoot(ResourceKey<LootTable> key, LootTable table, HolderLookup.Provider registries,
            Consumer<LootPool.Builder> addPool) {
        if (!CursesAndCrusades.config.whetstoneLoot) {
            return;
        }
        
        if (BuiltInLootTables.VILLAGE_TOOLSMITH.equals(key) || BuiltInLootTables.VILLAGE_ARMORER.equals(key)
                || BuiltInLootTables.VILLAGE_WEAPONSMITH.equals(key)) {
            addPool.accept(new LootPool.Builder()
                    .setRolls(UniformGenerator.between(0, 2))
                    .add(EmptyLootItem.emptyItem().setWeight(16))
                    .add(LootItem.lootTableItem(CursesAndCrusadesRegistry.WHETSTONE.value()).setWeight(10))
                    .add(LootItem.lootTableItem(CursesAndCrusadesRegistry.BLESSED_WHETSTONE.value()).setWeight(1)));
        } else if (BuiltInLootTables.SIMPLE_DUNGEON.equals(key) || BuiltInLootTables.ABANDONED_MINESHAFT.equals(key)
                || BuiltInLootTables.STRONGHOLD_LIBRARY.equals(key) || BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(key)
                || BuiltInLootTables.WOODLAND_MANSION.equals(key)
                || BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR.equals(key)) {
            addPool.accept(new LootPool.Builder()
                    .add(EmptyLootItem.emptyItem().setWeight(8))
                    .add(LootItem.lootTableItem(CursesAndCrusadesRegistry.WHETSTONE.value()).setWeight(5))
                    .add(LootItem.lootTableItem(CursesAndCrusadesRegistry.BLESSED_WHETSTONE.value()).setWeight(3))
                    .add(LootItem.lootTableItem(CursesAndCrusadesRegistry.ENCHANTED_BLESSED_WHETSTONE.value()).setWeight(1)));
        }
    }

    public static void setTrades () {
        if (CursesAndCrusades.config.whetstoneTrade) {
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
                return new MerchantOffer(new ItemCost(Items.EMERALD, 8), new ItemStack(CursesAndCrusadesRegistry.WHETSTONE, 1), 3, 5, 0.2f);
            }
        });
        ItemListing[] newSecondTradesArray = new ItemListing[newSecondTrades.size()];
        for (int i = 0; i < newSecondTrades.size(); i++) {
            newSecondTradesArray[i] = newSecondTrades.get(i);
        }
        trades.put(2, newSecondTradesArray);
    }
}
