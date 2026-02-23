package com.kyleplo.curses_and_cryptics;

import java.util.List;
import java.util.Set;

import org.jspecify.annotations.NonNull;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class SetCrypticEnchantedBookLevelFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetCrypticEnchantedBookLevelFunction> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
      return commonFields(instance).and(NumberProviders.CODEC.fieldOf("level").forGetter((setItemDamageFunction) -> {
         return setItemDamageFunction.level;
      })).apply(instance, SetCrypticEnchantedBookLevelFunction::new);
   });
   private final NumberProvider level;

   private SetCrypticEnchantedBookLevelFunction(List<LootItemCondition> list, NumberProvider numberProvider) {
      super(list);
      this.level = numberProvider;
   }

   public @NonNull LootItemFunctionType<SetCrypticEnchantedBookLevelFunction> getType() {
      return (LootItemFunctionType<SetCrypticEnchantedBookLevelFunction>) CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_SET_LEVEL.value();
   }

   public @NonNull Set<ContextKey<?>> getReferencedContextParams() {
      return this.level.getReferencedContextParams();
   }

   public @NonNull ItemStack run(@NonNull ItemStack itemStack, @NonNull LootContext lootContext) {
      itemStack.set(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), this.level.getInt(lootContext));
      return itemStack;
   }

   public static LootItemConditionalFunction.Builder<?> setLevel(NumberProvider numberProvider) {
      return simpleBuilder((list) -> {
         return new SetCrypticEnchantedBookLevelFunction(list, numberProvider);
      });
   }
}
