package com.liquidpotions.wrink.init;

import com.liquidpotions.wrink.LiquidPotions;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipesLP
{
  public static void init()
  {
    OreDictionary.registerOre("gemRedstone", LiquidPotions.itemRedstoneRuby);
    OreDictionary.registerOre("gemGlowstone", LiquidPotions.itemGlowpaz);

    GameRegistry.addShapelessRecipe(new ItemStack(LiquidPotions.brewingStandLP), new Object[] { new ItemStack(Items.dye, 1, 3), Items.brewing_stand });
    GameRegistry.addShapelessRecipe(new ItemStack(Items.blaze_powder), new Object[] { LiquidPotions.itemNetherPepper });
    GameRegistry.addShapelessRecipe(new ItemStack(LiquidPotions.itemNetherPepperStuffed), new Object[] { LiquidPotions.itemNetherPepper, Items.cooked_porkchop, Items.bread });

    GameRegistry.addRecipe(new ShapedOreRecipe(LiquidPotions.sandRedstone, new Object[] { "121", "212", "121", Character.valueOf('1'), "dustRedstone", Character.valueOf('2'), Blocks.sand }));
    GameRegistry.addRecipe(new ShapedOreRecipe(LiquidPotions.sandGlowstone, new Object[] { "121", "212", "121", Character.valueOf('1'), "dustGlowstone", Character.valueOf('2'), Blocks.sand }));
    GameRegistry.addRecipe(new ShapedOreRecipe(LiquidPotions.itemGoldFeather, new Object[] { "111", "121", "111", Character.valueOf('1'), "nuggetGold", Character.valueOf('2'), Items.feather }));
    
    GameRegistry.addSmelting(LiquidPotions.sandRedstone, new ItemStack(LiquidPotions.itemRedstoneRuby), 0.6F);
    GameRegistry.addSmelting(LiquidPotions.sandGlowstone, new ItemStack(LiquidPotions.itemGlowpaz), 0.6F);
  }
}