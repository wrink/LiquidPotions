package com.liquidpotions.wrink.blocks;

import java.util.List;
import java.util.Random;

import com.liquidpotions.wrink.LiquidPotions;
import com.liquidpotions.wrink.fluids.FluidPotion;
import com.liquidpotions.wrink.helpers.PotionHelperLP;
import com.liquidpotions.wrink.tileentities.TileCauldron;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockCauldronLP extends BlockContainer {
    
	
	public BlockCauldronLP() {
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabBrewing);
		
		GameRegistry.registerTileEntity(TileCauldron.class, this.getUnlocalizedName());
	}

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
        float f = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
        this.setBlockBoundsForItemRender();
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return -1;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if (meta == 2) return new TileCauldron(999 * 8);
		else if (meta == 1) return new TileCauldron(999 * 4);
		else return new TileCauldron(999);
	}
	
	 /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	ItemStack current =  entityPlayer.inventory.getCurrentItem();
    	
    	if (current != null)
    	{
    		TileEntity tile = world.getTileEntity(x, y, z);
    		
    		if(tile instanceof TileCauldron)
    		{
    			TileCauldron tank = (TileCauldron) tile;
    			
    			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(current);
    			//FILL
				if (fluid != null)
				{

					int capacity = tank.fill(ForgeDirection.UP, fluid, false);

					if(capacity > 0) //&& fluid.fluidID == FluidRegistry.WATER.getID())
					{
						tank.fill(ForgeDirection.UP, fluid, true);

						if (!entityPlayer.capabilities.isCreativeMode)
						{
							{
								entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, getContainer(current));
							}
						}
					}
				}
				//DRAIN
				else if(FluidContainerRegistry.isContainer(current))
				{				
					FluidStack available = tank.drain(ForgeDirection.DOWN, Integer.MAX_VALUE, false);
					if (available != null)
					{
						ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);
						FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(filled);
						if (liquid != null) {

							if (current.stackSize > 1) {
								if (!entityPlayer.inventory.addItemStackToInventory(filled)) {
									return false;
								} else {
									current.stackSize -= 1;
								}
							} else {
								entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, filled);
							}

							tank.drain(ForgeDirection.DOWN, liquid.amount, true);
							return true;
						}
					}
				}
				else if(PotionHelperLP.isPotionIngredient(current))
				{
					System.out.println("flag1");
					fluid = tank.drain(ForgeDirection.DOWN, Integer.MAX_VALUE, false);
					int id = -1;
					if(fluid.getFluid() instanceof FluidPotion)
					{
						id = ((FluidPotion) fluid.getFluid()).getPotion();
						System.out.println(id);
					}
					else if (fluid.getFluid() == FluidRegistry.WATER)
					{
						id = 0;
					}
					
					if(id>=0 && PotionHelperLP.canApplyIngredient(current, id))
					{
						System.out.println("flag2");
						tank.applyPotionIngredient(current);
					}
				}
    		}
    	}
    	return true;
    }
    
    private ItemStack getContainer(ItemStack item)
	{
		if (item.stackSize == 1) {
			if (item.getItem().hasContainerItem(item)) 
			{
				return item.getItem().getContainerItem(item);
			} else 
			{
				return null;
			}
		} else 
		{
			item.splitStack(1);
			return item;
		}
	}
    
    /**
     * currently only used by BlockCauldron to incrament meta-data during rain
     */
    public void fillWithRain(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile != null)
        {
        	TileCauldron tank = (TileCauldron) tile;
        	
        	if(tank.canFill(ForgeDirection.UP, FluidRegistry.WATER))
        	{
        		tank.fill(ForgeDirection.UP, new FluidStack(FluidRegistry.WATER, 1), true);
        	}
        }
    }


    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile != null)
        {
        	TileCauldron tank = (TileCauldron) tile;
        	
        	if(tank.getPercentFilled() < 0.33F) return 0;
        	else if(tank.getPercentFilled() < 0.66F) return 1;
        	else if(tank.getPercentFilled() < 0.99F) return 2;
        	else return 3;
        }
        
        return 0;
    }
    
    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List subItems) {
		for (int i = 0; i < 3; i++) {
			subItems.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int damageDropped (int metadata) {
		return metadata;
	}
}
