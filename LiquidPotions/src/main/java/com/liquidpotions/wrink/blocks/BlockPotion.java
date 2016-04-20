package com.liquidpotions.wrink.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.liquidpotions.wrink.LiquidPotions;
import com.liquidpotions.wrink.fluids.FluidPotion;
import com.liquidpotions.wrink.helpers.PotionHelperLP;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockPotion extends BlockFluidClassic
{
  int potionValue;
  private static HashMap effectCache = new HashMap();
  private static final Map field_77835_b = new LinkedHashMap();
  private static final Map blockMap = new LinkedHashMap();

  @SideOnly(Side.CLIENT)
  protected IIcon stillIcon;

  @SideOnly(Side.CLIENT)
  protected IIcon flowingIcon;

  public BlockPotion(FluidPotion fluid, Material material, int potionValue)
  { 
	super(fluid, material);
    this.potionValue = potionValue;
  }

  public void registerBlockIcons(IIconRegister iconRegister)
  {
    this.stillIcon = iconRegister.registerIcon("liquidpotions:potion_still");
    this.flowingIcon = iconRegister.registerIcon("liquidpotions:potion_flow");
    
    this.getFluid().setIcons(stillIcon, flowingIcon);
  }

  public IIcon getIcon(int side, int meta)
  {
    return (side == 0) || (side == 1) ? this.stillIcon : this.flowingIcon;
  }

  public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
  {
	PotionEffect effect = PotionHelperLP.getPotionEffects(this.potionValue);
		
	if(effect != null && effect.getPotionID() == LiquidPotions.ID_CHARGED)
	{
		p_149670_1_.spawnEntityInWorld(new EntityLightningBolt(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_));
	}
    if (!(p_149670_5_ instanceof EntityLivingBase)) return;

    EntityLivingBase entity = (EntityLivingBase)p_149670_5_;

    if (effect != null)
    {
      entity.addPotionEffect(effect);
    }
  }

  @SideOnly(Side.CLIENT)
  public int getBlockColor() {
    return PotionHelperLP.getColor(this.potionValue);
  }

  @SideOnly(Side.CLIENT)
  public int getRendercolor(int p_149741_1_)
  {
    return PotionHelperLP.getColor(this.potionValue);
  }

  @SideOnly(Side.CLIENT)
  public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
  {
    return PotionHelperLP.getColor(this.potionValue);
  }
}