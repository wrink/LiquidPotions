package com.liquidpotions.wrink.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import com.liquidpotions.wrink.LiquidPotions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockPepper extends BlockBush
{
  IIcon[] icons = new IIcon[4];

  public BlockPepper()
  {
    setTickRandomly(true);
    float f = 0.5F;
    setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    setCreativeTab((CreativeTabs)null);
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister iconRegister)
  {
    for (int i = 0; i < 4; i++)
    {
      this.icons[i] = iconRegister.registerIcon("liquidpotions:peppers_" + i);
    }
  }

  protected boolean canPlaceBlockOn(Block p_149854_1_)
  {
    System.out.println("wtf");
    return p_149854_1_ == Blocks.soul_sand;
  }

  public boolean canPlaceBlockAt(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_)
  {
    return super.canPlaceBlockAt(p_149718_1_, p_149718_2_, p_149718_3_, p_149718_4_);
  }

  public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
  {
    int l = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

    if ((l < 3) && (p_149674_5_.nextInt(10) == 0))
    {
      l++;
      p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, l, 2);
    }

    super.updateTick(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, p_149674_5_);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int p_149691_1_, int p_149691_2_)
  {
    return p_149691_2_ > 3 ? this.icons[3] : this.icons[p_149691_2_];
  }

  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
  {
    return null;
  }

  public int quantityDropped(Random p_149745_1_)
  {
    return 0;
  }

  @SideOnly(Side.CLIENT)
  public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
  {
    return LiquidPotions.itemNetherPepper;
  }

  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
  {
    ArrayList ret = new ArrayList();
    int count = 1;

    if (metadata >= 3)
    {
      count = 2 + world.rand.nextInt(3) + (fortune > 0 ? world.rand.nextInt(fortune + 1) : 0);
    }

    for (int i = 0; i < count; i++)
    {
      ret.add(new ItemStack(LiquidPotions.itemNetherPepper));
    }

    return ret;
  }

  public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
  {
    if (!(p_149670_5_ instanceof EntityLivingBase)) return;

    if (getPlantMetadata(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_) > 2)
    {
      EntityLivingBase entity = (EntityLivingBase)p_149670_5_;
      entity.attackEntityFrom(DamageSource.cactus, 1.0F);
    }
  }
}