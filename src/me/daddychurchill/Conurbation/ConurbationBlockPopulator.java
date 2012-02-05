package me.daddychurchill.Conurbation;


import java.util.Random;

import me.daddychurchill.Conurbation.Plats.PlatGenerator;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class ConurbationBlockPopulator extends BlockPopulator {

	private ConurbationChunkGenerator chunkGen;
	
	public ConurbationBlockPopulator(ConurbationChunkGenerator chunkGen){
		this.chunkGen = chunkGen;
	}
	
	@Override
	public void populate(World world, Random random, Chunk source) {
		int chunkX = source.getX();
		int chunkZ = source.getZ();
		
		// place to work
		RealChunk chunk = new RealChunk(source);
		
		// figure out what everything looks like
		PlatGenerator plat = chunkGen.getPlatGenerator(world, random, chunkX, chunkZ);
		if (plat != null) {
			plat.generateBlocks(chunkGen.getWorld(), chunk, chunkX, chunkZ);
		}
	}
}
