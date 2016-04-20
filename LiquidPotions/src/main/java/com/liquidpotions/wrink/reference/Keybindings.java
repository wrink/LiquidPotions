package com.liquidpotions.wrink.reference;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;

public class Keybindings {
	public static final String NAME_CATEGORY = "Liquid Potions";
	public static final String NAME_CAST = "Cast Potion Spell";
	
	public static final KeyBinding FIRE = new KeyBinding(NAME_CAST, Keyboard.KEY_C, NAME_CATEGORY);
}
