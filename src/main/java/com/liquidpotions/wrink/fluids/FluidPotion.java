package com.liquidpotions.wrink.fluids;

import cpw.mods.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.liquidpotions.wrink.LiquidPotions;
import com.liquidpotions.wrink.blocks.BlockPotion;
import com.liquidpotions.wrink.helpers.LiquidPotionsEventHooks;
import com.liquidpotions.wrink.helpers.PotionHelperLP;
import com.liquidpotions.wrink.items.BucketPotion;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidPotion extends Fluid
{
  public int potionValue;
  private static final Map blockMap = new LinkedHashMap();
  private static final Map fluidMap = new LinkedHashMap();

  public FluidPotion(String fluidName, int val)
  {
    super(fluidName);
    potionValue = val;
    
  }

  public int getPotion()
  {
    return potionValue;
  }

  public int getColor()
  {
    return PotionHelper.func_77915_a(potionValue, false);
  }

  public static FluidPotion getFluidFromVal(int i1)
  {
    return (FluidPotion)fluidMap.get(Integer.valueOf(i1));
  }

  public static BlockPotion getBlockFromVal(int i1)
  {
    return (BlockPotion)blockMap.get(Integer.valueOf(i1));
  }

  public static void getSubFluids()
  {
    Iterator iterator = PotionHelperLP.getRegisteredPotions().iterator();

    while (iterator.hasNext())
    {
      int j = ((Integer)iterator.next()).intValue();

      BlockPotion block = null;
      FluidPotion fluid = (FluidPotion)new FluidPotion(PotionHelperLP.getPotionName(j) + ":" + j, j).setBlock(block);
      //FluidPotion fluid = (FluidPotion)new FluidPotion("a"+j, j).setBlock(block);
      fluidMap.put(Integer.valueOf(j), fluid);
      FluidRegistry.registerFluid(fluid);
      block = (BlockPotion)new BlockPotion(fluid, Material.water, j).setBlockName(PotionHelperLP.getPotionName(j) + j);
      blockMap.put(Integer.valueOf(j), block);
      GameRegistry.registerBlock(block, PotionHelperLP.getPotionName(j) + j);
      BucketPotion bucket = (BucketPotion)new BucketPotion(block, j).setContainerItem(Items.bucket).setUnlocalizedName("Bucket of " + PotionHelperLP.getPotionName(j));
      GameRegistry.registerItem(bucket, "Bucket of " + PotionHelperLP.getPotionName(j) + j);
      FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, 999), new ItemStack(bucket), new ItemStack(Items.bucket));
      FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, 333), new ItemStack(LiquidPotions.itemPotionLP, 1, j), new ItemStack(Items.glass_bottle));
      LiquidPotionsEventHooks.INSTANCE.buckets.put(block, new ItemStack(bucket));
      LiquidPotionsEventHooks.INSTANCE.bucketmap.put(Integer.valueOf(j), new ItemStack(bucket));
    }
  }
}