package com.liquidpotions.wrink.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.liquidpotions.wrink.LiquidPotions;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.StatCollector;

//Potion Data:
//bits 0-7 (1, 2, 4, 8, 16, 32, 64, 128) are Potion ID bits
//bits 8-10 (256, 512, 1024) are amplifier bits
//bits 11-13 (2048, 4096, 8192) are duration bits
//bit  14 (16384) is the splash bit
//bit  15 (sign) is unused

public class PotionHelperLP extends PotionHelper
{
  public static HashMap<Integer, Integer> durationMap = new HashMap();
  public static HashMap<ItemStack, Integer> typeMap = new HashMap();
  public static HashMap<ItemStack, Integer> amplifierMap = new HashMap();
  public static HashMap<ItemStack, Integer> extenderMap = new HashMap();
  public static HashMap<Integer, Integer> inversionMap = new HashMap();
  public static HashMap<Integer, Integer> colorMap = new HashMap();

  public static ArrayList registry = new ArrayList();
  public static ArrayList splashRegistry = new ArrayList();
  public static ArrayList instantRegistry = new ArrayList();
  public static ArrayList flatRegistry = new ArrayList();

  public static PotionEffect getPotionEffects(int damage)
  {
    if (hasEffect(damage))
    {
      int ID = getPotionID(damage);
      int dur = getPotionDuration(ID, damage);
      int amp = getPotionAmplifier(damage);
      return new PotionEffect(ID, dur, amp);
    }

    return null;
  }

  private static boolean hasEffect(int damage)
  {
    int id = damage % 256;
    if (id == 0)
    {
      return false;
    }
    return net.minecraft.potion.Potion.potionTypes[id] != null;
  }

  public static int getPotionAmplifier(int damage)
  {
    return (damage & 0b11100000000) / 0b100000000;
  }

  public static int getPotionDurationModifier(int damage)
  {
    return damage % 16384 / 2048;
  }

  public static int getPotionDuration(int id, int damage)
  {
    return (int)(((Integer)durationMap.get(Integer.valueOf(id))).intValue() * (3.0D + 5.0D * getPotionDurationModifier(damage)) / (3.0D + 3.0D * getPotionAmplifier(damage)));
  }

  private static int getPotionID(int damage) {
    return damage % 256;
  }

  public static boolean canApplyIngredient(ItemStack ingredient, int damage)
  {
    Item item = ingredient.getItem();

    if ((item == Items.nether_wart) && (damage == 0))
    {
      return true;
    }
    if ((item == Items.gunpowder) && (!isSplash(damage)))
    {
      return true;
    }
    if ((isAmplifierIngredient(ingredient) > 0) && (getPotionAmplifier(damage) < ((Integer)amplifierMap.get(item)).intValue()) && (!isFlat(damage)))
    {
      return true;
    }
    if ((isExtenderIngredient(ingredient) > 0) && (getPotionDurationModifier(damage) < ((Integer)extenderMap.get(item)).intValue()) && (!isInstant(damage)))
    {
      return true;
    }
    if ((item == Items.fermented_spider_eye) && (isInvertable(damage)))
    {
      return true;
    }

    return (isPotionTypeIngredient(ingredient) > 0) && (isBasePotion(damage));
  }

  private static int isAmplifierIngredient(ItemStack ingredient)
  {
    Iterator iterator = amplifierMap.keySet().iterator();

    while (iterator.hasNext())
    {
      ItemStack key = (ItemStack)iterator.next();
      if (key.equals(ingredient)) return ((Integer)amplifierMap.get(key)).intValue();
    }
    return -1;
  }

  private static int isExtenderIngredient(ItemStack ingredient)
  {
    Iterator iterator = extenderMap.keySet().iterator();

    while (iterator.hasNext())
    {
      ItemStack key = (ItemStack)iterator.next();
      if (key.equals(ingredient)) return ((Integer)extenderMap.get(key)).intValue();
    }
    return -1;
  }

  private static int isPotionTypeIngredient(ItemStack ingredient)
  {
    Iterator iterator = typeMap.keySet().iterator();

    while (iterator.hasNext())
    {
      ItemStack key = (ItemStack)iterator.next();
      if (key.getItem() == ingredient.getItem() && key.getItemDamage() == ingredient.getItemDamage()) return ((Integer)typeMap.get(key)).intValue();
    }
    return -1;
  }

  public static boolean isBasePotion(int damage)
  {
    return (damage & 0x2000) != 0;
  }

  public static boolean isInvertable(int damage)
  {
    return inversionMap.get(Integer.valueOf(getPotionID(damage))) != null;
  }

  public static boolean isSplash(int damage)
  {
    return (damage & 0x4000) != 0;
  }

  public static boolean isInstant(int damage)
  {
    return instantRegistry.contains(Integer.valueOf(damage));
  }

  public static boolean isFlat(int damage)
  {
    return flatRegistry.contains(Integer.valueOf(damage));
  }

  public static int applyIngredient(int damage, ItemStack itemStack)
  {
    int ampVal = isAmplifierIngredient(itemStack);
    int extVal = isExtenderIngredient(itemStack);
    int typeVal = isPotionTypeIngredient(itemStack);

    int newDamage = damage;
    Item item = itemStack.getItem();

    if (item == Items.nether_wart)
    {
      newDamage += 8192;
      return newDamage;
    }
    if ((item == Items.gunpowder) && (!isSplash(damage)))
    {
      newDamage += 16384;
    }
    if (item == Items.fermented_spider_eye)
    {
      int newID = ((Integer)inversionMap.get(Integer.valueOf(getPotionID(damage)))).intValue();
      int postDur = newDamage / 8192 * 8192;
      int newDur = isInstant(newID) ? 0 : newDamage % 8192 / 2048 * 2048;
      int newAmp = isFlat(newID) ? 0 : newDamage % 2048 / 256 * 256;
      newDamage = newID + newAmp + newDur + postDur;
    }
    if (ampVal > 0)
    {
      int preAmp = newDamage % 256;
      int postAmp = newDamage / 2048;

      newDamage = preAmp + ampVal * 256 + postAmp * 2048;
    }
    if (extVal > 0)
    {
      int preDur = newDamage % 2048;
      int postDur = newDamage / 8192;

      newDamage = preDur + extVal * 2048 + postDur * 8192;
    }
    if (typeVal > 0)
    {
      newDamage = newDamage / 256 * 256 + typeVal - 8192;
    }

    return newDamage;
  }

  public static boolean isPotionIngredient(ItemStack itemStack)
  {
    Item item = itemStack.getItem();
    return (item != null) && ((item == Items.nether_wart) || (item == Items.gunpowder) || (item == Items.fermented_spider_eye) || (isAmplifierIngredient(itemStack) > 0) || (isExtenderIngredient(itemStack) > 0) || (isPotionTypeIngredient(itemStack) > 0));
  }

  public static int getColor(int damage)
  {
    Integer color = (Integer)colorMap.get(Integer.valueOf(getPotionID(damage)));
    return color != null ? color.intValue() : 3693002;
  }

  public static ArrayList getRegisteredPotions()
  {
    return registry;
  }

  public static ArrayList getRegisteredSplashPotions()
  {
    return splashRegistry;
  }

  public static void registerPotion(int id, boolean base, Integer duration, boolean flat)
  {
    boolean instant = (duration != null) && (duration.intValue() == 1);
    if (base)
    {
      int value = id + 8192;
      registry.add(Integer.valueOf(value));
      splashRegistry.add(Integer.valueOf(value + 16384));
    }
    else
    {
      for (int i = instant ? 0 : 3; i >= 0; i--)
      {
        for (int j = flat ? 0 : 3; j >= 0; j--)
        {
          int value = id + (j << 8) + (i << 11);
          registry.add(Integer.valueOf(value));
          splashRegistry.add(Integer.valueOf(value + 16384));
          if (instant)
          {
            instantRegistry.add(Integer.valueOf(value));
            instantRegistry.add(Integer.valueOf(value + 16384));
          }
          if (flat)
          {
            flatRegistry.add(Integer.valueOf(value));
            flatRegistry.add(Integer.valueOf(value + 16384));
          }
          durationMap.put(Integer.valueOf(id), duration);
        }
      }
    }
  }

  public static String getPotionName(int itemDamage) {
    if (itemDamage == 0)
    {
      return StatCollector.translateToLocal("item.emptyPotion.name").trim();
    }

    String s = "";

    if (isSplash(itemDamage))
    {
      s = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
    }

    PotionEffect effect = getPotionEffects(itemDamage);

    if (effect != null)
    {
      String s1 = "Potion of " + effect.getEffectName();
      return s + StatCollector.translateToLocal(s1).trim();
    }

    String s1 = PotionHelper.func_77905_c(itemDamage);
    return StatCollector.translateToLocal(s1).trim() + " " + "Potion";
  }

  static
  {
    registerPotion(0, true, null, true);
    registerPotion(1, false, Integer.valueOf(3600), false);
    registerPotion(2, false, Integer.valueOf(1800), false);
    registerPotion(5, false, Integer.valueOf(3600), false);
    registerPotion(6, false, Integer.valueOf(1), false);
    registerPotion(7, false, Integer.valueOf(1), false);
    registerPotion(10, false, Integer.valueOf(900), false);
    registerPotion(11, false, Integer.valueOf(3600), false);
    registerPotion(12, false, Integer.valueOf(3600), true);
    registerPotion(13, false, Integer.valueOf(3600), true);
    registerPotion(14, false, Integer.valueOf(3600), true);
    registerPotion(15, false, Integer.valueOf(1800), true);
    registerPotion(16, false, Integer.valueOf(3600), true);
    registerPotion(17, false, Integer.valueOf(1800), false);
    registerPotion(18, false, Integer.valueOf(1800), false);
    registerPotion(19, false, Integer.valueOf(900), false);
    registerPotion(20, false, Integer.valueOf(900), false);
    registerPotion(21, false, Integer.valueOf(3600), false);
    registerPotion(22, false, Integer.valueOf(3600), false);
    registerPotion(23, false, Integer.valueOf(3600), false);
    registerPotion(LiquidPotions.ID_FERTILITY, false, Integer.valueOf(3600), false);
    registerPotion(LiquidPotions.ID_GROWTH, false, Integer.valueOf(3600), false);
    registerPotion(LiquidPotions.ID_MAGNETIC, false, Integer.valueOf(3600), false);
    registerPotion(LiquidPotions.ID_CHARGED, false, Integer.valueOf(1), true);
    registerPotion(LiquidPotions.ID_DROWN, false, Integer.valueOf(900), false);
    registerPotion(LiquidPotions.ID_FLIGHT, false, Integer.valueOf(3600), true);
    registerPotion(LiquidPotions.ID_GROUNDING, false, Integer.valueOf(1800), true);
    registerPotion(LiquidPotions.ID_RECIPROCATION, false, Integer.valueOf(3600), false);
    registerPotion(LiquidPotions.ID_BINDING, false, Integer.valueOf(1800), true);
    registerPotion(LiquidPotions.ID_FLAME, false, Integer.valueOf(3600), false);
    registerPotion(LiquidPotions.ID_FROST, false, Integer.valueOf(3600), false);
    registerPotion(LiquidPotions.ID_PROTECTION, false, Integer.valueOf(3600), false);

    typeMap.put(new ItemStack(Items.sugar), Integer.valueOf(1));
    typeMap.put(new ItemStack(Items.blaze_powder), Integer.valueOf(5));
    typeMap.put(new ItemStack(Items.speckled_melon), Integer.valueOf(6));
    typeMap.put(new ItemStack(Items.ghast_tear), Integer.valueOf(10));
    typeMap.put(new ItemStack(Blocks.obsidian), Integer.valueOf(11)); //TODO change to obsidian salmon
    typeMap.put(new ItemStack(Items.magma_cream), Integer.valueOf(12));
    typeMap.put(new ItemStack(Items.fish, 1, 3), Integer.valueOf(13));
    typeMap.put(new ItemStack(Items.golden_carrot), Integer.valueOf(16));
    typeMap.put(new ItemStack(Items.spider_eye), Integer.valueOf(19));
    typeMap.put(new ItemStack(Items.diamond), Integer.valueOf(21)); //TODO change to diamond chip cookie
    typeMap.put(new ItemStack(Items.emerald), Integer.valueOf(22)); //TODO change to emerald honeydew
    typeMap.put(new ItemStack(Items.poisonous_potato), Integer.valueOf(23));
    typeMap.put(new ItemStack(LiquidPotions.itemOyster), Integer.valueOf(LiquidPotions.ID_FERTILITY));
    typeMap.put(new ItemStack(LiquidPotions.itemGoldFeather), Integer.valueOf(LiquidPotions.ID_FLIGHT));
    typeMap.put(new ItemStack(Blocks.web), Integer.valueOf(LiquidPotions.ID_PROTECTION));
    typeMap.put(new ItemStack(Items.slime_ball), Integer.valueOf(LiquidPotions.ID_BINDING)); //TODO change to ender potato
    typeMap.put(new ItemStack(Items.fire_charge), Integer.valueOf(LiquidPotions.ID_FLAME));

    amplifierMap.put(new ItemStack(Items.glowstone_dust), Integer.valueOf(1));
    amplifierMap.put(new ItemStack(LiquidPotions.itemGlowpaz), Integer.valueOf(2));

    extenderMap.put(new ItemStack(Items.redstone), Integer.valueOf(1));
    extenderMap.put(new ItemStack(LiquidPotions.itemRedstoneRuby), Integer.valueOf(2));

    inversionMap.put(Integer.valueOf(1), Integer.valueOf(2));
    inversionMap.put(Integer.valueOf(5), Integer.valueOf(18));
    inversionMap.put(Integer.valueOf(6), Integer.valueOf(7));
    inversionMap.put(Integer.valueOf(10), Integer.valueOf(20));
    inversionMap.put(Integer.valueOf(16), Integer.valueOf(14));
    inversionMap.put(Integer.valueOf(23), Integer.valueOf(17));
    inversionMap.put(Integer.valueOf(LiquidPotions.ID_FERTILITY), Integer.valueOf(LiquidPotions.ID_GROWTH));
    inversionMap.put(Integer.valueOf(LiquidPotions.ID_MAGNETIC), Integer.valueOf(LiquidPotions.ID_CHARGED));
    inversionMap.put(Integer.valueOf(13), Integer.valueOf(LiquidPotions.ID_DROWN));
    inversionMap.put(Integer.valueOf(LiquidPotions.ID_FLIGHT), Integer.valueOf(LiquidPotions.ID_GROUNDING));
    inversionMap.put(Integer.valueOf(11), Integer.valueOf(LiquidPotions.ID_RECIPROCATION));
    inversionMap.put(Integer.valueOf(LiquidPotions.ID_FLAME), Integer.valueOf(LiquidPotions.ID_FROST));

    colorMap.put(Integer.valueOf(1), Integer.valueOf(8171462));
    colorMap.put(Integer.valueOf(2), Integer.valueOf(5926017));
    colorMap.put(Integer.valueOf(5), Integer.valueOf(9643043));
    colorMap.put(Integer.valueOf(6), Integer.valueOf(16262179));
    colorMap.put(Integer.valueOf(7), Integer.valueOf(4393481));
    colorMap.put(Integer.valueOf(10), Integer.valueOf(13458603));
    colorMap.put(Integer.valueOf(11), Integer.valueOf(10044730));
    colorMap.put(Integer.valueOf(12), Integer.valueOf(14981690));
    colorMap.put(Integer.valueOf(13), Integer.valueOf(3035801));
    colorMap.put(Integer.valueOf(14), Integer.valueOf(8356754));
    colorMap.put(Integer.valueOf(15), Integer.valueOf(2039587));
    colorMap.put(Integer.valueOf(16), Integer.valueOf(2039713));
    colorMap.put(Integer.valueOf(17), Integer.valueOf(5797459));
    colorMap.put(Integer.valueOf(18), Integer.valueOf(4738376));
    colorMap.put(Integer.valueOf(19), Integer.valueOf(5149489));
    colorMap.put(Integer.valueOf(20), Integer.valueOf(3484199));
    colorMap.put(Integer.valueOf(21), Integer.valueOf(16284963));
    colorMap.put(Integer.valueOf(22), Integer.valueOf(2445989));
    colorMap.put(Integer.valueOf(23), Integer.valueOf(16262179));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_FERTILITY), Integer.valueOf(12924326));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_GROWTH), Integer.valueOf(7796494));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_MAGNETIC), Integer.valueOf(7372944));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_CHARGED), Integer.valueOf(8239359));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_DROWN), Integer.valueOf(1793670));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_FLIGHT), Integer.valueOf(16316380));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_GROUNDING), Integer.valueOf(6050578));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_RECIPROCATION), Integer.valueOf(7411233));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_BINDING), Integer.valueOf(667948));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_FLAME), Integer.valueOf(14769453));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_FROST), Integer.valueOf(11988725));
    colorMap.put(Integer.valueOf(LiquidPotions.ID_PROTECTION), Integer.valueOf(15266995));
  }
}