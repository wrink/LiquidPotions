package com.liquidpotions.wrink.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemBlockCauldronLP extends ItemBlock {

	public ItemBlockCauldronLP(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
}
