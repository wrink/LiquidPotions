package com.liquidpotions.wrink.proxy;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.liquidpotions.wrink.reference.Keybindings;
import com.liquidpotions.wrink.render.blocks.ModelCauldron;
import com.liquidpotions.wrink.render.blocks.RenderCauldron;
import com.liquidpotions.wrink.tileentities.TileCauldron;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerKeyBindings() {
		ClientRegistry.registerKeyBinding(Keybindings.FIRE);
	}
	
	@Override
	public void initializeRenderers() {
		ModelCauldron cauldron = new ModelCauldron();
		ClientRegistry.bindTileEntitySpecialRenderer(TileCauldron.class, new RenderCauldron(cauldron));
	}
}
