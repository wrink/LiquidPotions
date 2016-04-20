package com.liquidpotions.wrink.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemLP extends Item
{
	private boolean hasEffect;
	
	public ItemLP(String name)
	{
		this(name, false, true);
	}
	
	public ItemLP(String name, boolean hasEffect)
	{
		this(name, hasEffect, true);
	}
	
	public ItemLP(String name, boolean hasEffect, boolean useTexture)
	{
		super();
		
		String nameFormatted = "liquidpotions:" + name;
		setUnlocalizedName(nameFormatted);
		if(useTexture) setTextureName(nameFormatted);
		
		this.hasEffect = hasEffect;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass)
	{
		return hasEffect || super.hasEffect(stack, pass);
	}
}
