package com.liquidpotions.wrink.fluids;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public abstract interface IPotionContainer
{
  public abstract int getPotionVolume();

  public abstract PotionStack getPotionStack(ItemStack paramItemStack);

  public abstract int getId(ItemStack paramItemStack);

  public abstract FluidPotion getFluid(ItemStack paramItemStack);

  public abstract PotionEffect getEffect(ItemStack paramItemStack);

  public abstract void setPotion(int paramInt, ItemStack paramItemStack);
}