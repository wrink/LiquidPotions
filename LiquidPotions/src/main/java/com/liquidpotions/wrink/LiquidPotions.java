package com.liquidpotions.wrink;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import java.io.PrintStream;
import java.lang.reflect.Field;

import com.liquidpotions.wrink.blocks.BlockCauldronLP;
import com.liquidpotions.wrink.blocks.BlockPepper;
import com.liquidpotions.wrink.blocks.BlockRedstoneSand;
import com.liquidpotions.wrink.entities.EntityPotionLP;
import com.liquidpotions.wrink.fluids.FluidPotion;
import com.liquidpotions.wrink.helpers.LiquidPotionsEventHooks;
import com.liquidpotions.wrink.init.RecipesLP;
import com.liquidpotions.wrink.items.ItemBlockCauldronLP;
import com.liquidpotions.wrink.items.ItemLP;
import com.liquidpotions.wrink.items.ItemPepper;
import com.liquidpotions.wrink.items.ItemPotionLP;
import com.liquidpotions.wrink.potions.PotionLP;
import com.liquidpotions.wrink.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid="liquidpotions", name="liquidpotions", version="1.7.10-A1.0.6")
public class LiquidPotions
{
  public static final String modid = "liquidpotions";
  public static final String version = "1.7.10-A1.0.6";
  public static final int BOTTLE_VOLUME = 333;
  public static final int brewingStandLPID = 11;
  public static final int entityPotionLPID = 12;
  public static int ID_FERTILITY;
  public static int ID_GROWTH;
  public static int ID_MAGNETIC;
  public static int ID_CHARGED;
  public static int ID_DROWN;
  public static int ID_FLIGHT;
  public static int ID_GROUNDING;
  public static int ID_PROTECTION;
  public static int ID_RECIPROCATION;
  public static int ID_BINDING;
  public static int ID_FLAME;
  public static int ID_FROST;

  @Mod.Instance("liquidpotions")
  public static LiquidPotions instance;
  public static Block brewingStandLP;
  public static Block sandRedstone = new BlockRedstoneSand().setBlockName("sandRedstone").setBlockTextureName("liquidpotions:sand_redstone").setHardness(0.5F).setStepSound(Block.soundTypeSand);
  public static Block sandGlowstone = new BlockFalling().setBlockName("sandGlowstone").setBlockTextureName("liquidpotions:sand_glowstone").setHardness(0.5F).setLightLevel(1.0F).setStepSound(Block.soundTypeSand);
  public static Block blockNetherPepper = new BlockPepper().setBlockName("blockNetherPepper");
  public static Block blockCauldron = new BlockCauldronLP();
  public static Item itemPotionLP;
  public static Item itemOyster = new ItemFood(2, 0.3F, true).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setUnlocalizedName("Oyster").setTextureName("liquidpotions:oyster");
  public static Item itemRedstoneRuby = new ItemLP("redstone_ruby").setCreativeTab(CreativeTabs.tabMaterials);
  public static Item itemGlowpaz = new ItemLP("glowpaz").setCreativeTab(CreativeTabs.tabMaterials);
  public static Item itemNetherPepper = new ItemPepper(3, 0.5F, blockNetherPepper, Blocks.soul_sand).setUnlocalizedName("netherPepper").setTextureName("liquidpotions:nether_pepper");
  public static Item itemNetherPepperStuffed = new ItemFood(6, 0.8F, true).setPotionEffect(Potion.damageBoost.id, 120, 0, 1.0F).setUnlocalizedName("netherPepperStuffed").setTextureName("liquidpotions:nether_pepper_stuffed");
  public static Item itemGoldFeather = new ItemLP("feather_gold").setCreativeTab(CreativeTabs.tabMaterials);
  public static Item itemRedstoneRubyCharged = new ItemLP("redstone_ruby_charged", true, false).setCreativeTab(CreativeTabs.tabMaterials).setTextureName("liquidpotions:redstone_ruby");
  public static Item itemGlowpazCharged = new ItemLP("glowpaz_charged", true, false).setCreativeTab(CreativeTabs.tabMaterials).setTextureName("liquidpotions:glowpaz");
  public static Item itemFrozenHeart = new ItemLP("frozen_heart").setCreativeTab(CreativeTabs.tabMaterials);
  
  public static Potion potionFertility;
  public static Potion potionGrowth;
  public static Potion potionMagnetic;
  public static Potion potionCharged;
  public static Potion potionDrown;
  public static Potion potionFlight;
  public static Potion potionGrounding;
  public static Potion potionProtection;
  public static Potion potionReciprocation;
  public static Potion potionBinding;
  public static Potion potionFlame;
  public static Potion potionFrost;

  @SidedProxy(clientSide="com.liquidpotions.wrink.proxy.ClientProxy", serverSide="com.liquidpotions.wrink.SeverProxy")
  public static CommonProxy proxy;

  @Mod.EventHandler
  public void PreInit(FMLPreInitializationEvent event)
  {
	proxy.registerKeyBindings();
    Potion[] potionTypes = null;

    for (Field f : Potion.class.getDeclaredFields())
    {
      f.setAccessible(true);
      try
      {
        if ((f.getName().equals("potionTypes")) || (f.getName().equals("field_76425_a"))) {
          Field modfield = Field.class.getDeclaredField("modifiers");
          modfield.setAccessible(true);
          modfield.setInt(f, f.getModifiers() & 0xFFFFFFEF);

          potionTypes = (Potion[])(Potion[])f.get(null);
          Potion[] newPotionTypes = new Potion[256];
          System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);

          int id = 0;

          for (int i = 1; i < newPotionTypes.length; i++)
          {
            if (newPotionTypes[i] != null)
              continue;
            if (id == 0)
            {
              ID_FERTILITY = i;
              id++;
            }
            else if (id == 1)
            {
              ID_GROWTH = i;
              id++;
            }
            else if (id == 2)
            {
              ID_MAGNETIC = i;
              id++;
            }
            else if (id == 3)
            {
              ID_CHARGED = i;
              id++;
            }
            else if (id == 4)
            {
              ID_DROWN = i;
              id++;
            }
            else if (id == 5)
            {
              ID_FLIGHT = i;
              id++;
            }
            else if (id == 6)
            {
              ID_GROUNDING = i;
              id++;
            }
            else if (id == 7)
            {
              ID_PROTECTION = i;
              id++;
            }
            else if (id == 8)
            {
              ID_RECIPROCATION = i;
              id++;
            }
            else if (id == 9)
            {
              ID_BINDING = i;
              id++;
            }
            else if (id == 10)
            {
              ID_FLAME = i;
              id++;
            } else {
              if (id != 11)
                break;
              ID_FROST = i;
              id++;
            }

          }

          f.set(null, newPotionTypes);
        }
      }
      catch (Exception e) {
        System.err.println("Severe error, please report this to the mod author:");
        System.err.println(e);
      }
    }

    itemPotionLP = new ItemPotionLP().setUnlocalizedName("ItemPotionLP");

    //GameRegistry.registerBlock(brewingStandLP, "BrewingStandLP");
    //GameRegistry.registerTileEntity(TileEntityBrewingStandLP.class, "BrewingStandLP");
    //NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerLP());

    GameRegistry.registerBlock(sandRedstone, "sandRedstone");
    GameRegistry.registerBlock(sandGlowstone, "sandGlowstone");
    GameRegistry.registerBlock(blockNetherPepper, "blockNetherPepper");
    GameRegistry.registerBlock(blockCauldron, ItemBlockCauldronLP.class, "cauldronLP");
    
    GameRegistry.registerItem(itemPotionLP, "ItemPotionLP");
    GameRegistry.registerItem(itemOyster, "Oyster", "liquidpotions");
    GameRegistry.registerItem(itemRedstoneRuby, "gem_redstone", "liquidpotions");
    GameRegistry.registerItem(itemGlowpaz, "gem_glowstone", "liquidpotions");
    GameRegistry.registerItem(itemNetherPepper, "nether_pepper", "liquidpotions");
    GameRegistry.registerItem(itemNetherPepperStuffed, "nether_pepper_stuffed", "liquidpotions");
    GameRegistry.registerItem(itemGoldFeather, "feather_gold", "liquidpotions");
    GameRegistry.registerItem(itemRedstoneRubyCharged, "gem_redstone_charged", "liquidpotions");
    GameRegistry.registerItem(itemGlowpazCharged, "gem_glowstone_charged", "liquidpotions");
    GameRegistry.registerItem(itemFrozenHeart, "frozen_heart", "liquidpotions");


    
    EntityRegistry.registerModEntity(EntityPotionLP.class, "entityPotionLP", 12, instance, 64, 1, true);

    proxy.initializeRenderers();

    MinecraftForge.EVENT_BUS.register(new LiquidPotionsEventHooks());
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    FluidPotion.getSubFluids();

    potionFertility = new PotionLP(ID_FERTILITY, true, 12924326).setPotionName("Fertility");
    potionGrowth = new PotionLP(ID_GROWTH, false, 7796494).setPotionName("Growth");
    potionMagnetic = new PotionLP(ID_MAGNETIC, false, 7372944).setPotionName("Magnetism");
    potionCharged = new PotionLP(ID_CHARGED, true, 8239359).setPotionName("Ionization");
    potionDrown = new PotionLP(ID_DROWN, true, 1793670).setPotionName("Drowning");
    potionFlight = new PotionLP(ID_FLIGHT, false, 16316380).setPotionName("Flight");
    potionGrounding = new PotionLP(ID_GROUNDING, true, 6050578).setPotionName("Grounding");
    potionProtection = new PotionLP(ID_PROTECTION, false, 15266995).setPotionName("Arrow Resistance");
    potionReciprocation = new PotionLP(ID_RECIPROCATION, false, 7411233).setPotionName("Reciprocation");
    potionBinding = new PotionLP(ID_BINDING, true, 667948).setPotionName("Binding");
    potionFlame = new PotionLP(ID_FLAME, false, 14769453).setPotionName("Immolation");
    potionFrost = new PotionLP(ID_FROST, false, 11988725).setPotionName("Cryogenesis");

    RecipesLP.init();
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
  }
}