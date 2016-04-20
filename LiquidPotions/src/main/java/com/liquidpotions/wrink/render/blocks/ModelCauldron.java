package com.liquidpotions.wrink.render.blocks;

import com.liquidpotions.wrink.LiquidPotions;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelCauldron extends ModelBase{

	private static final ResourceLocation[] textures = 
	{
		new ResourceLocation("liquidpotions", "textures/blocks/ModelCauldronClay.png"),
		new ResourceLocation("liquidpotions", "textures/blocks/ModelCauldronIron.png"),
		new ResourceLocation("liquidpotions", "textures/blocks/ModelCauldronGold.png")
	};
	
	ModelRenderer Bottom;
    ModelRenderer Side1;
    ModelRenderer Side2;
    ModelRenderer Side3;
    ModelRenderer Side4;
    ModelRenderer Leg1;
    ModelRenderer Leg2;
    ModelRenderer Leg3;
    ModelRenderer Leg4;
    
    public ModelCauldron() {
    	textureWidth = 128;
	    textureHeight = 128;
	    
	    Bottom = new ModelRenderer(this, 0, 70);
	    Bottom.addBox(0F, 0F, 0F, 14, 1, 14);
	    Bottom.setRotationPoint(-7F, 21F, -7F);
	    Bottom.setTextureSize(64, 32);
	    Bottom.mirror = true;
	    setRotation(Bottom, 0F, 0F, 0F);
	
	    Side1 = new ModelRenderer(this, 0, 0);
	    Side1.addBox(0F, 0F, 0F, 16, 13, 1);
	    Side1.setRotationPoint(-8F, 8F, -8F);
    	Side1.setTextureSize(64, 32);
    	Side1.mirror = true;
    	setRotation(Side1, 0F, 0F, 0F);
    	Side2 = new ModelRenderer(this, 0, 0);
    	Side2.addBox(0F, 0F, 0F, 16, 13, 1);
    	Side2.setRotationPoint(-8F, 8F, 7F);
    	Side2.setTextureSize(64, 32);
    	Side2.mirror = true;
    	setRotation(Side2, 0F, 0F, 0F);
    	Side3 = new ModelRenderer(this, 0, 35);
    	Side3.addBox(0F, 0F, 0F, 1, 13, 14);
    	Side3.setRotationPoint(7F, 8F, -7F);
    	Side3.setTextureSize(64, 32);
    	Side3.mirror = true;
    	setRotation(Side3, 0F, 0F, 0F);
    	Side4 = new ModelRenderer(this, 0, 35);
    	Side4.addBox(0F, 0F, 0F, 1, 13, 14);
    	Side4.setRotationPoint(-8F, 8F, -7F);
    	Side4.setTextureSize(64, 32);
    	Side4.mirror = true;
    	setRotation(Side4, 0F, 0F, 0F);
    	Leg1 = new ModelRenderer(this, 70, 0);
    	Leg1.addBox(0F, 0F, 0F, 1, 3, 1);
    	Leg1.setRotationPoint(-8F, 21F, -8F);
    	Leg1.setTextureSize(64, 32);
    	Leg1.mirror = true;
    	setRotation(Leg1, 0F, 0F, 0F);
    	Leg2 = new ModelRenderer(this, 70, 0);
    	Leg2.addBox(0F, 0F, 0F, 1, 3, 1);
    	Leg2.setRotationPoint(7F, 21F, 7F);
    	Leg2.setTextureSize(64, 32);
    	Leg2.mirror = true;
    	setRotation(Leg2, 0F, 0F, 0F);
    	Leg3 = new ModelRenderer(this, 70, 0);
    	Leg3.addBox(0F, 0F, 0F, 1, 3, 1);
    	Leg3.setRotationPoint(-8F, 21F, 7F);
    	Leg3.setTextureSize(64, 32);
    	Leg3.mirror = true;
    	setRotation(Leg3, 0F, 0F, 0F);
    	Leg4 = new ModelRenderer(this, 70, 0);
    	Leg4.addBox(0F, 0F, 0F, 1, 3, 1);
    	Leg4.setRotationPoint(7F, 21F, -8F);
    	Leg4.setTextureSize(64, 32);
    	Leg4.mirror = true;
    	setRotation(Leg4, 0F, 0F, 0F);
    }
    
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
    	super.render(entity, f, f1, f2, f3, f4, f5);
    	setRotationAngles(f, f1, f2, f3, f4, f5);
    	Bottom.render(f5);
    	Side1.render(f5);
    	Side2.render(f5);
    	Side3.render(f5);
    	Side4.render(f5);
    	Leg1.render(f5);
    	Leg2.render(f5);
    	Leg3.render(f5);
    	Leg4.render(f5);
    }

    public void simpleRender(float scale)
    {
    	Bottom.render(scale);
    	Side1.render(scale);
    	Side2.render(scale);
    	Side3.render(scale);
    	Side4.render(scale);
    	Leg1.render(scale);
    	Leg2.render(scale);
    	Leg3.render(scale);
    	Leg4.render(scale);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
    	model.rotateAngleX = x;
    	model.rotateAngleY = y;
    	model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
    	super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
    }


    public ResourceLocation getCauldronTexture(Block block, int meta) {
    	return textures[meta];
    }
}
