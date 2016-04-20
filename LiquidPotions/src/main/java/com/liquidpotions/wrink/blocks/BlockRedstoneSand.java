package com.liquidpotions.wrink.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;

public class BlockRedstoneSand extends BlockFalling
{
  public BlockRedstoneSand()
  {
    setCreativeTab(CreativeTabs.tabRedstone);
  }

  public boolean canProvidePower()
  {
    return true;
  }

  public int isProvidingStrongPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
  {
    return 15;
  }
}