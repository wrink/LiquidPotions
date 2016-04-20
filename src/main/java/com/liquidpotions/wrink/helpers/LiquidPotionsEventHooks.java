package com.liquidpotions.wrink.helpers;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.liquidpotions.wrink.LiquidPotions;
import com.liquidpotions.wrink.reference.Keybindings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class LiquidPotionsEventHooks
{
  public HashMap<Block, ItemStack> buckets = new HashMap();
  public HashMap<Block, ItemStack> bottles = new HashMap();
  public HashMap<Integer, ItemStack> bucketmap = new HashMap();
  public static Map<String, Boolean> playerFlightBuff = new HashMap();

  public static LiquidPotionsEventHooks INSTANCE = new LiquidPotionsEventHooks();

  @SubscribeEvent
  public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
  {
	  if(Keybindings.FIRE.getIsKeyPressed())
	  {
		//TODO add spells  
	  }
  }
  
  @SubscribeEvent
  public void onLightningStrikeEntityEvent(EntityStruckByLightningEvent event)
  {
	  if(event.entity instanceof EntityItem)
	  {
		  EntityItem item = (EntityItem)event.entity;
		  if(item.getEntityItem().getItem() == LiquidPotions.itemGlowpaz || item.getEntityItem().getItem() == LiquidPotions.itemGlowpazCharged)
		  {
			 ItemStack itemstack = new ItemStack(LiquidPotions.itemGlowpazCharged, item.getEntityItem().stackSize);
			 item.worldObj.spawnEntityInWorld(new EntityItem(item.worldObj, item.posX, item.posY, item.posZ, itemstack));
		  }
		  else if(item.getEntityItem().getItem() == LiquidPotions.itemRedstoneRuby || item.getEntityItem().getItem() == LiquidPotions.itemRedstoneRubyCharged)
		  {
			 ItemStack itemstack = new ItemStack(LiquidPotions.itemRedstoneRubyCharged, item.getEntityItem().stackSize);
			 item.worldObj.spawnEntityInWorld(new EntityItem(item.worldObj, item.posX, item.posY, item.posZ, itemstack));
		  }
	  }
  }
  
  @SubscribeEvent
  public void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
    if (event.entityLiving.isPotionActive(LiquidPotions.ID_GROUNDING))
    {
      event.entityLiving.jumpMovementFactor = 0.0F;
    }
  }

  @SubscribeEvent
  public void onEndermanTeleportEvent(EnderTeleportEvent event) {
    if ((event.entityLiving.isPotionActive(LiquidPotions.ID_BINDING)) && (event.isCancelable()))
    {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onEntityDamaged(LivingAttackEvent event) {
    EntityLivingBase entityAttacked = event.entityLiving;

    if (entityAttacked.isPotionActive(LiquidPotions.ID_RECIPROCATION))
    {
      Entity entityAttacking = event.source.getEntity();

      if ((entityAttacking != null) && ((entityAttacking instanceof EntityLivingBase)))
      {
        int i = event.entityLiving.getActivePotionEffect(LiquidPotions.potionReciprocation).getAmplifier();
        float damageRecieve = event.ammount / 2.0F * (i + 1);
        ((EntityLivingBase)entityAttacking).attackEntityFrom(DamageSource.cactus, damageRecieve);
      }
    }

    if (entityAttacked.isPotionActive(LiquidPotions.ID_FLAME))
    {
      int i = event.entityLiving.getActivePotionEffect(LiquidPotions.potionFlame).getAmplifier();

      Entity entityAttacking = event.source.getEntity();

      if ((entityAttacking != null) && ((entityAttacking instanceof EntityLivingBase)) && (!entityAttacking.isImmuneToFire()) && (!((EntityLivingBase)entityAttacking).isPotionActive(Potion.fireResistance)))
      {
        entityAttacking.attackEntityFrom(DamageSource.inFire, 2 * i + 2);
        entityAttacking.setFire(3);
      }
    }
    
    if (entityAttacked.isPotionActive(LiquidPotions.ID_FROST))
    {
    	PotionEffect effect = event.entityLiving.getActivePotionEffect(LiquidPotions.potionFlame);
        Entity entityAttacking = event.source.getEntity();
        
        if ((entityAttacking != null) && ((entityAttacking instanceof EntityLivingBase)))
        {
        	((EntityLivingBase)entityAttacking).addPotionEffect(new PotionEffect(4, 60, effect.getAmplifier()));
        }

    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingEvent.LivingUpdateEvent e) {
    EntityLivingBase entity = e.entityLiving;
    World world = entity.worldObj;
    if ((entity.isPotionActive(LiquidPotions.ID_FERTILITY)) && ((entity instanceof EntityPlayerMP)))
    {
      int amp = entity.getActivePotionEffect(LiquidPotions.potionFertility).getAmplifier();
      int radius = 10 + amp * 3;
      if (world.getWorldTime() % 20L == 0L)
      {
        List list = world.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius));
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
          EntityAnimal animal = (EntityAnimal)iter.next();
          if (world.rand.nextInt(40) <= amp) animal.func_146082_f(null);
        }
      }
    }
    if ((entity.isPotionActive(LiquidPotions.ID_GROWTH)) && ((entity instanceof EntityPlayerMP)))
    {
      int amp = entity.getActivePotionEffect(LiquidPotions.potionGrowth).getAmplifier();
      int radius = 10 + amp * 3;
      if (world.getWorldTime() % 20L == 0L)
      {
        for (int x = (int)(entity.posX - radius); x <= entity.posX + radius; x++)
        {
          for (int y = (int)(entity.posY - radius); y <= entity.posY + radius; y++)
          {
            for (int z = (int)(entity.posZ - radius); z <= entity.posZ + radius; z++)
            {
              Block block = world.getBlock(x, y, z);
              if ((!(block instanceof IGrowable)) && (!(block instanceof IPlantable)) && (!(block instanceof BlockVine)))
                continue;
              if (world.rand.nextInt(20) >= amp) {
                block.updateTick(world, x, y, z, world.rand);
              }
            }
          }
        }
      }
    }
    if (entity.isPotionActive(LiquidPotions.ID_MAGNETIC))
    {
      int amp = entity.getActivePotionEffect(LiquidPotions.potionMagnetic).getAmplifier();
      int radius = 5 + amp * 3;
      double vel = 0.3D;
      List list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius));
      Iterator iter = list.iterator();
      while (iter.hasNext())
      {
        Entity entity1 = (Entity)iter.next();
        if (((entity1 instanceof EntityItem)) || ((entity1 instanceof IProjectile)))
        {
          double distX = entity.posX - entity1.posX;
          double distY = entity.posY - entity1.posY;
          double distZ = entity.posZ - entity1.posZ;
          double dist = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
          if ((dist < radius) && (dist > 0.8D))
          {
            double velX = Math.abs(distX) > 0.8D ? -vel : distX > 0.0D ? vel : 0.0D;
            double velY = Math.abs(distY) > 0.8D ? -vel : distY > 0.0D ? vel : 0.0D;
            double velZ = Math.abs(distZ) > 0.8D ? -vel : distZ > 0.0D ? vel : 0.0D;
            entity1.setVelocity(velX, velY, velZ);
          }
        }
      }
    }
    if (entity.isPotionActive(LiquidPotions.ID_CHARGED))
    {
      int radius = 3;
      double maxDist = 0.0D;
      Entity furthest = entity;
      List list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius));
      Iterator iter = list.iterator();
      while (iter.hasNext())
      {
        Entity entity1 = (Entity)iter.next();
        double distX = entity.posX - entity1.posX;
        double distY = entity.posY - entity1.posY;
        double distZ = entity.posZ - entity1.posZ;
        double dist = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
        if ((dist < radius) && (dist > maxDist))
        {
          furthest = entity1;
          maxDist = dist;
        }
      }

      world.spawnEntityInWorld(new EntityLightningBolt(world, furthest.posX, furthest.posY, furthest.posZ));
    }
    if (e.entityLiving.isPotionActive(LiquidPotions.ID_DROWN))
    {
      int i = e.entityLiving.getActivePotionEffect(LiquidPotions.potionDrown).getAmplifier();
      
      System.out.println(i);
      
      if (e.entityLiving.worldObj.getWorldTime() % (20 ) == 0)
      {
        e.entityLiving.attackEntityFrom(DamageSource.drown, 2.0F);
        e.entityLiving.hurtResistantTime = Math.min(e.entityLiving.hurtResistantTime, 20 / (i + 1));
      }
    }
    int d0;
    if (e.entityLiving.isPotionActive(LiquidPotions.ID_PROTECTION))
    {
      int i = e.entityLiving.getActivePotionEffect(LiquidPotions.potionProtection).getAmplifier();
      int posX = (int)Math.round(entity.posX - 0.5D);
      int posY = (int)Math.round(entity.posY);
      int posZ = (int)Math.round(entity.posZ - 0.5D);
      d0 = i;
      AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(posX - 0.5D, posY - 0.5D, posZ - 0.5D, posX + 0.5D, posY + 0.5D, posZ + 0.5D).expand(d0, d0, d0);
      List list = e.entityLiving.worldObj.getEntitiesWithinAABB(Entity.class, axisalignedbb);
      Iterator iterator = list.iterator();

      while (iterator.hasNext())
      {
        Entity projectile = (Entity)iterator.next();

        if ((projectile == null) || 
          (!(projectile instanceof IProjectile)))
        {
          continue;
        }

        if (((projectile instanceof EntityArrow)) && 
          (((EntityArrow)projectile).shootingEntity != null) && (((EntityArrow)projectile).shootingEntity.equals(entity)))
        {
          break;
        }

        projectile.setVelocity(projectile.motionX / 2.0D, projectile.motionY, projectile.motionZ / 2.0D);
      }
    }

    if (e.entityLiving.isPotionActive(LiquidPotions.ID_FLIGHT))
    {
      if ((e.entityLiving instanceof EntityPlayer))
      {
        EntityPlayer entityPlayer = (EntityPlayer)e.entityLiving;
        String ownerName = entityPlayer.getDisplayName();
        playerFlightBuff.put(ownerName, Boolean.valueOf(true));
        entityPlayer.capabilities.allowFlying = true;
      }

    }
    else if ((e.entityLiving instanceof EntityPlayer))
    {
      EntityPlayer entityPlayer = (EntityPlayer)e.entityLiving;
      String ownerName = entityPlayer.getDisplayName();

      if (!playerFlightBuff.containsKey(ownerName))
      {
        playerFlightBuff.put(ownerName, Boolean.valueOf(false));
      }

      if (((Boolean)playerFlightBuff.get(ownerName)).booleanValue())
      {
        playerFlightBuff.put(ownerName, Boolean.valueOf(false));

        if (!entityPlayer.capabilities.isCreativeMode)
        {
          entityPlayer.capabilities.allowFlying = false;
          entityPlayer.capabilities.isFlying = false;
          entityPlayer.setJumping(true);
        }
      }

    }
  }

  @SubscribeEvent
  public void onMobDrop(LivingDropsEvent e)
  {
    if (((e.entityLiving instanceof EntitySquid)) && (e.entityLiving.worldObj.rand.nextInt(3) > 0))
    {
      System.out.println((e.entityLiving instanceof EntitySquid) ? "squid" : "not squid");
      e.entityLiving.dropItem(LiquidPotions.itemOyster, 1);
    }
  }

  @SubscribeEvent
  public void fillBucket(FillBucketEvent e) {
    if (e.current.getItem() != Items.bucket)
    {
      return;
    }

    ItemStack fullBucket = getLiquid(e.world, e.target);

    if (fullBucket == null) return;

    e.world.setBlockToAir(e.target.blockX, e.target.blockY, e.target.blockZ);
    e.result = fullBucket;
    e.setResult(Event.Result.ALLOW);
  }

  private ItemStack getLiquid(World world, MovingObjectPosition pos)
  {
    Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
    if (block == null) return null;
    return (ItemStack)INSTANCE.buckets.get(block);
  }
}