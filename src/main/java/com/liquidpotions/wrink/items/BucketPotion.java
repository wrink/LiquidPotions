package com.liquidpotions.wrink.items;

import com.google.common.collect.HashMultimap;
import com.liquidpotions.wrink.fluids.PotionStack;
import com.liquidpotions.wrink.helpers.PotionHelperLP;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class BucketPotion extends ItemBucket
{
  private Block isFull;
  private PotionStack contents;
  private int id;
  IIcon bucket;
  IIcon liquid;

  public BucketPotion(Block p_i45331_1_, int id)
  {
    super(p_i45331_1_);
    this.id = id;
    setCreativeTab(CreativeTabs.tabBrewing);
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister)
  {
    this.bucket = iconRegister.registerIcon("liquidpotions:bucket_underlay");
    this.liquid = iconRegister.registerIcon("liquidpotions:bucket_overlay");
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int pass) {
    return pass == 0 ? this.liquid : this.bucket;
  }

  public boolean requiresMultipleRenderPasses()
  {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
  {
    return p_82790_2_ > 0 ? 16777215 : PotionHelperLP.getColor(this.id);
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
        String s1 = StatCollector.translateToLocal(effect.getEffectName().trim());
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

  public int getAmplifier(ItemStack potionStack)
  {
    return PotionHelperLP.getPotionAmplifier(this.id);
  }

  public boolean isExtendable(ItemStack potionStack)
  {
    return PotionHelperLP.getPotionDurationModifier(this.id) < 7;
  }

  public boolean isInvertable(ItemStack potionStack)
  {
    return PotionHelperLP.isInvertable(this.id);
  }

  public boolean isSplash(ItemStack potionStack)
  {
    return PotionHelperLP.isSplash(this.id);
  }

  public int getId()
  {
    return this.id;
  }
}