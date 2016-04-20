package com.liquidpotions.wrink.render.blocks;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.liquidpotions.wrink.blocks.BlockCauldronLP;
import com.liquidpotions.wrink.tileentities.TileCauldron;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class RenderCauldron extends TileEntitySpecialRenderer{
	private ModelCauldron cauldron;
	private ModelCauldronInternal internal;

	public RenderCauldron(ModelCauldron model)
	{
		this.cauldron = model;
		internal = new ModelCauldronInternal();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) 
	{
		drawCauldron(tileentity, x, y, z, f);
		drawCauldronContents(tileentity, x, y, z, f);
	}


	private void drawCauldron(TileEntity tileentity, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5F,(float)y + 1.5F,(float)z + 0.5F);
		GL11.glScalef(-1F, -1F, 1F);

		bindCauldronTexture(tileentity.getBlockType(), tileentity.getBlockMetadata());
		cauldron.simpleRender(0.0625F);

		GL11.glPopMatrix();
	}

	private void drawCauldronContents(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileCauldron cauldron = (TileCauldron)tileentity;

		if (cauldron.getPercentFilled() != 0)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef((float)x + 0.5F,(float)y + cauldron.getAdjustedVolume(),(float)z + 0.5F);
			GL11.glScalef(0.92f, 1.0f, 0.92f);

			bindInternalTexture();

			Fluid content = cauldron.drain(ForgeDirection.DOWN, 1, false).getFluid();
			IIcon icon = content.getIcon();
			Color color = new Color(content.getColor());
			boolean transparency = true;
			
			if(icon != null) internal.render(color, icon, transparency);
			
			GL11.glPopMatrix();
		}


	}

	public void bindCauldronTexture(Block block, int meta)
	{
		bindTexture(cauldron.getCauldronTexture(block, meta));
	}

	public void bindInternalTexture()
	{
		ResourceLocation fluidTexture = TextureMap.locationBlocksTexture;
		bindTexture(fluidTexture);
	}
}
