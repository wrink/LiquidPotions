package com.liquidpotions.wrink.tileentities;

import com.liquidpotions.wrink.fluids.FluidPotion;
import com.liquidpotions.wrink.fluids.PotionStack;
import com.liquidpotions.wrink.helpers.PotionHelperLP;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileCauldron extends TileEntity implements IFluidHandler{
	
	private static final float MIN_RENDER_CAPACITY = 0.20f;
	private static final float MAX_RENDER_CAPACITY = 0.95f;
	
	FluidTank tank;
	
	public TileCauldron(int capacity) {
		tank = new FluidTank(capacity);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (canFill(from, resource.fluid)) return tank.fill(resource, doFill);
		else return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (canDrain(from, resource.fluid)) return drain(from, resource.amount, doDrain);
		else return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(from != ForgeDirection.UP && from != ForgeDirection.UNKNOWN) return tank.drain(maxDrain, doDrain);
		else return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return from == ForgeDirection.UP && (tank.getFluid() == null || tank.getFluid().fluid.equals(fluid));
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return fluid.equals(tank.getFluid().fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] info = {tank.getInfo()};
		return info;
	}

	public void applyPotionIngredient(ItemStack current) {
		FluidStack fluid = tank.getFluid();
		int volume = fluid.amount;
		
		if (tank != null && current != null)
		{
			if(fluid.getFluid() == FluidRegistry.WATER && PotionHelperLP.canApplyIngredient(current, 0))
			{
				tank.drain(Integer.MAX_VALUE, true);
				
				tank.fill(new PotionStack(8192, volume), true);
			}
			else if(fluid.getFluid() instanceof FluidPotion)
			{
				System.out.println("flag3");
				int damage = ((FluidPotion) fluid.getFluid()).getPotion();
				System.out.println(damage);
				if (PotionHelperLP.canApplyIngredient(current, damage))
				{
					tank.drain(Integer.MAX_VALUE, true);
					
					damage = PotionHelperLP.applyIngredient(damage, current);
					
					tank.fill(new PotionStack(damage, volume), true);
				}
			}
		}
	}

	public float getPercentFilled() {
		return ((float)tank.getFluidAmount())/tank.getCapacity();
	}

	public float getAdjustedVolume() {
		float volume = getPercentFilled();
		float capacity = MAX_RENDER_CAPACITY - MIN_RENDER_CAPACITY;
		return volume * capacity + MIN_RENDER_CAPACITY;
	}
}
