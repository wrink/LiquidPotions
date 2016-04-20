package com.liquidpotions.wrink.world;

import cpw.mods.fml.common.IWorldGenerator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldLP implements IWorldGenerator
{
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
    switch (world.provider.dimensionId)
    {
    case -1:
      generateNether(world, random, chunkX * 16, chunkZ * 16);
      break;
    case 0:
      generateOverworld(world, random, chunkX * 16, chunkZ * 16);
    }
  }

  private void generateOverworld(World world, Random random, int x, int z)
  {
  }

  private void generateNether(World world, Random random, int x, int z)
  {
  }
}