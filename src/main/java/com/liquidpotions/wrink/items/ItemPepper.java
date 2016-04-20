package com.liquidpotions.wrink.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class ItemPepper extends ItemSeedFood
{
  public ItemPepper(int p_i45351_1_, float p_i45351_2_, Block p_i45351_3_, Block p_i45351_4_)
  {
    super(p_i45351_1_, p_i45351_2_, p_i45351_3_, p_i45351_4_);
  }

  public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
  {
    return EnumPlantType.Nether;
  }
}