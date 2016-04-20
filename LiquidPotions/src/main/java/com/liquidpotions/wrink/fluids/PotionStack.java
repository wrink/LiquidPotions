package com.liquidpotions.wrink.fluids;

import com.liquidpotions.wrink.helpers.PotionHelperLP;

import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fluids.FluidStack;

public class PotionStack extends FluidStack
{
  private int id;
  private int amp;
  private int dur;
  private PotionEffect effect;

  public PotionStack(int id, int ammount)
  {
    super(FluidPotion.getFluidFromVal(id), ammount);
    this.id = id;
    this.amp = PotionHelperLP.getPotionAmplifier(id);
    this.dur = PotionHelperLP.getPotionDurationModifier(id);
    this.effect = PotionHelperLP.getPotionEffects(id);
  }

  public int getId()
  {
    return this.id;
  }

  public int getAmp()
  {
    return this.amp;
  }

  public int getDur()
  {
    return this.dur;
  }

  public PotionEffect getEffect()
  {
    return this.effect;
  }
}