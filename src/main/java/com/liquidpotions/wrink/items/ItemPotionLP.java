package com.liquidpotions.wrink.items;

import com.google.common.collect.HashMultimap;
import com.liquidpotions.wrink.entities.EntityPotionLP;
import com.liquidpotions.wrink.helpers.PotionHelperLP;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPotionLP extends ItemPotion
{
  private IIcon bottle;
  private IIcon splashbottle;
  private IIcon liquid;

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister)
  {
    this.bottle = iconRegister.registerIcon("minecraft:potion_bottle_drinkable");
    this.splashbottle = iconRegister.registerIcon("minecraft:potion_bottle_splash");
    this.liquid = iconRegister.registerIcon("minecraft:potion_overlay");
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int metadata)
  {
    return PotionHelperLP.isSplash(metadata) ? this.splashbottle : this.bottle;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int pass)
  {
    return pass == 0 ? this.liquid : getIconFromDamage(stack.getItemDamage());
  }

  public boolean requiresMultipleRenderPasses()
  {
    return true;
  }

  public boolean isWater(ItemStack potionStack)
  {
    return potionStack.getItemDamage() == 0;
  }

  public int getAmplifier(ItemStack potionStack)
  {
    return PotionHelperLP.getPotionAmplifier(potionStack.getItemDamage());
  }

  public boolean isBasePotion(ItemStack potionStack)
  {
    return PotionHelperLP.isBasePotion(potionStack.getItemDamage());
  }

  public boolean isExtendable(ItemStack potionStack)
  {
    return PotionHelperLP.getPotionDurationModifier(potionStack.getItemDamage()) < 7;
  }

  public boolean isInvertable(ItemStack potionStack) {
    return PotionHelperLP.isInvertable(potionStack.getItemDamage());
  }

  public boolean isSplash(ItemStack potionStack) {
    return PotionHelperLP.isSplash(potionStack.getItemDamage());
  }

  public ItemStack onEaten(ItemStack potionStack, World world, EntityPlayer player)
  {
    if (!player.capabilities.isCreativeMode)
    {
      potionStack.stackSize -= 1;
    }

    if (!world.isRemote)
    {
      player.addPotionEffect(PotionHelperLP.getPotionEffects(potionStack.getItemDamage()));
    }

    if (!player.capabilities.isCreativeMode)
    {
      if (potionStack.stackSize <= 0)
      {
        return new ItemStack(Items.glass_bottle);
      }

      player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
    }

    return potionStack;
  }

  @SideOnly(Side.CLIENT)
  public int getColorFromDamage(int damage)
  {
    return PotionHelperLP.getColor(damage);
  }

  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
  {
    return p_82790_2_ > 0 ? 16777215 : this.getColorFromDamage(p_82790_1_.getItemDamage());
  }

  @SideOnly(Side.CLIENT)
  public boolean isEffectInstant(int damage)
  {
    return Potion.potionTypes[PotionHelperLP.getPotionEffects(damage).getPotionID()].isInstant();
  }

  public String getItemStackDisplayName(ItemStack p_77653_1_)
  {
    return PotionHelperLP.getPotionName(p_77653_1_.getItemDamage());
  }

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
    if (p_77624_1_.getItemDamage() != 0)
    {
      List list1 = Items.potionitem.getEffects(p_77624_1_);
      PotionEffect effect = PotionHelperLP.getPotionEffects(p_77624_1_.getItemDamage());
      HashMultimap hashmultimap = HashMultimap.create();

      if ((list1 != null) && (!list1.isEmpty()))
      {
        String s1 = StatCollector.translateToLocal(effect.getEffectName()).trim();
        Potion potion = Potion.potionTypes[effect.getPotionID()];

        if (effect.getAmplifier() > 0)
        {
          s1 = s1 + " " + StatCollector.translateToLocal(new StringBuilder().append("potion.potency.").append(effect.getAmplifier()).toString()).trim();
        }

        if (effect.getDuration() > 20)
        {
          s1 = s1 + " (" + Potion.getDurationString(effect) + ")";
        }

        if (potion.isBadEffect())
        {
          p_77624_3_.add(EnumChatFormatting.RED + s1);
        }
        else
        {
          p_77624_3_.add(EnumChatFormatting.GRAY + s1);
        }

        p_77624_3_.add("");
        p_77624_3_.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
      }
      else
      {
        String s = StatCollector.translateToLocal("potion.empty").trim();
        p_77624_3_.add(EnumChatFormatting.GRAY + s);
      }

      if (!hashmultimap.isEmpty())
      {
        p_77624_3_.add("");
        p_77624_3_.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
        Iterator iterator1 = hashmultimap.entries().iterator();

        while (iterator1.hasNext())
        {
          Map.Entry entry1 = (Map.Entry)iterator1.next();
          AttributeModifier attributemodifier2 = (AttributeModifier)entry1.getValue();
          double d0 = attributemodifier2.getAmount();
          double d1;
          if ((attributemodifier2.getOperation() != 1) && (attributemodifier2.getOperation() != 2))
          {
            d1 = attributemodifier2.getAmount();
          }
          else
          {
            d1 = attributemodifier2.getAmount() * 100.0D;
          }

          if (d0 > 0.0D)
          {
            p_77624_3_.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted(new StringBuilder().append("attribute.modifier.plus.").append(attributemodifier2.getOperation()).toString(), new Object[] { ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) }));
          }
          else if (d0 < 0.0D)
          {
            d1 *= -1.0D;
            p_77624_3_.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted(new StringBuilder().append("attribute.modifier.take.").append(attributemodifier2.getOperation()).toString(), new Object[] { ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) }));
          }
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return PotionHelperLP.getPotionEffects(stack.getItemDamage()) != null;
  }

  public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
  {
    Iterator iterator = PotionHelperLP.getRegisteredPotions().iterator();
    while (iterator.hasNext())
    {
      p_150895_3_.add(new ItemStack(p_150895_1_, 1, ((Integer)iterator.next()).intValue()));
    }
    iterator = PotionHelperLP.getRegisteredSplashPotions().iterator();
    while (iterator.hasNext())
    {
      p_150895_3_.add(new ItemStack(p_150895_1_, 1, ((Integer)iterator.next()).intValue()));
    }
  }

  public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
  {
    int damage = p_77659_1_.getItemDamage();
    if (isSplash(p_77659_1_))
    {
      if (!p_77659_3_.capabilities.isCreativeMode)
      {
        p_77659_1_.stackSize -= 1;
      }

      p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (p_77659_2_.rand.nextFloat() * 0.4F + 0.8F));

      if (!p_77659_2_.isRemote)
      {
        p_77659_2_.spawnEntityInWorld(new EntityPotionLP(p_77659_2_, p_77659_3_, damage));
      }

      return p_77659_1_;
    }

    p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
    return p_77659_1_;
  }
}