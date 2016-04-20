package com.liquidpotions.wrink.entities;

import java.util.Iterator;
import java.util.List;

import com.liquidpotions.wrink.LiquidPotions;
import com.liquidpotions.wrink.helpers.PotionHelperLP;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityPotionLP extends EntityThrowable
{
  private ItemStack potionDamage;

  public EntityPotionLP(World par1World)
  {
    super(par1World);
  }

  public EntityPotionLP(World par1World, EntityLivingBase par2EntityLivingBase) {
    super(par1World, par2EntityLivingBase);
  }

  public EntityPotionLP(World par1World, double par2, double par4, double par6) {
    super(par1World, par2, par4, par6);
  }

  public EntityPotionLP(World p_i1789_1_, EntityLivingBase p_i1789_2_, int p_i1789_3_)
  {
    super(p_i1789_1_, p_i1789_2_);
    this.potionDamage = new ItemStack(LiquidPotions.itemPotionLP, 1, p_i1789_3_);
  }

  public void setPotionDamage(int p_82340_1_)
  {
    if (this.potionDamage == null)
    {
      this.potionDamage = new ItemStack(LiquidPotions.itemPotionLP, 1, 0);
    }

    this.potionDamage.setItemDamage(p_82340_1_);
  }

  public int getPotionDamage()
  {
    if (this.potionDamage == null)
    {
      this.potionDamage = new ItemStack(LiquidPotions.itemPotionLP, 1, 0);
    }

    return this.potionDamage.getItemDamage();
  }

  protected void onImpact(MovingObjectPosition p_70184_1_)
  {
    if (!this.worldObj.isRemote)
    {
      PotionEffect effect = PotionHelperLP.getPotionEffects(this.potionDamage.getItemDamage());

      if (effect != null)
      {
        AxisAlignedBB axisalignedbb = this.boundingBox.expand(4.0D, 2.0D, 4.0D);
        List list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        if ((list1 != null) && (!list1.isEmpty()))
        {
          Iterator iterator = list1.iterator();

          while (iterator.hasNext())
          {
            EntityLivingBase entitylivingbase = (EntityLivingBase)iterator.next();
            double d0 = getDistanceToEntity(entitylivingbase);

            if (d0 < 16.0D)
            {
              double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

              if (entitylivingbase == p_70184_1_.entityHit)
              {
                d1 = 1.0D;
              }

              int j = (int)(d1 * effect.getDuration() + 0.5D);
              boolean instant = PotionHelperLP.isInstant(effect.getPotionID());

              if ((j > 20) || (instant))
              {
                entitylivingbase.addPotionEffect(new PotionEffect(effect.getPotionID(), instant ? 1 : j, effect.getDuration()));
              }
            }
          }
        }
      }

      this.worldObj.playAuxSFX(2002, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), getPotionDamage());
      this.setDead();
    }
  }

  protected float getGravityVelocity()
  {
    return 0.05F;
  }

  protected float func_70182_d()
  {
    return 0.5F;
  }

  protected float func_70183_g()
  {
    return -20.0F;
  }

  public void readEntityFromNBT(NBTTagCompound p_70037_1_)
  {
    super.readEntityFromNBT(p_70037_1_);

    if (p_70037_1_.hasKey("Potion", 10))
    {
      this.potionDamage = ItemStack.loadItemStackFromNBT(p_70037_1_.getCompoundTag("Potion"));
    }
    else
    {
      setPotionDamage(p_70037_1_.getInteger("potionValue"));
    }

    if (this.potionDamage == null)
    {
      this.setDead();
    }
  }

  public void writeEntityToNBT(NBTTagCompound p_70014_1_)
  {
    super.writeEntityToNBT(p_70014_1_);

    if (this.potionDamage != null)
    {
      p_70014_1_.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
    }
  }
}