package com.kyleplo.curses_and_cryptics;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CrypticEnchantedBookItem extends Item {
    public CrypticEnchantedBookItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).stacksTo(1));
    }
}
