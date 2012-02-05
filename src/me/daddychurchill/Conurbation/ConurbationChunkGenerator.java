package me.daddychurchill.Conurbation;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.daddychurchill.Conurbation.Plats.FalseGenerator;
import me.daddychurchill.Conurbation.Plats.FarmGenerator;
import me.daddychurchill.Conurbation.Plats.ParkGenerator;
import me.daddychurchill.Conurbation.Plats.PlatGenerator;
import me.daddychurchill.Conurbation.Plats.WaterGenerator;
import me.daddychurchill.Conurbation.Support.ByteChunk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class ConurbationChunkGenerator extends ChunkGenerator {

	private Conurbation plugin;
	public String worldname;
	public String worldstyle;
	
	private static final double xFactor = 25.0;
	private static final double zFactor = 25.0;
	//private static final int roadFactor = 5;
	//private static final double roadChance = 0.80;
	private SimplexNoiseGenerator generatorUrban;
	private SimplexNoiseGenerator generatorWater;
	//private SimplexNoiseGenerator generatorRoad;
	private SimplexNoiseGenerator generatorUnfinished;
	
	public ConurbationChunkGenerator(Conurbation instance, String name, String style){
		this.plugin = instance;
		this.worldname = name;
		this.worldstyle = style;
		
	}
	
	public Conurbation getWorld() {
		return plugin;
	}
	
	public String getWorldname() {
		return worldname;
	}

	public String getWorldstyle() {
		return worldstyle;
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new ConurbationBlockPopulator(this));
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		// see if this works any better (loosely based on ExpansiveTerrain)
		int x = random.nextInt(100) - 50;
		int z = random.nextInt(100) - 50;
		//int y = Math.max(world.getHighestBlockYAt(x, z), PlatMap.StreetLevel + 1);
		int y = world.getHighestBlockYAt(x, z);
		return new Location(world, x, y, z);
	}
	
	@Override
	public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
		
		// place to work
		ByteChunk byteChunk = new ByteChunk(chunkX, chunkZ);
		
		// figure out what everything looks like
		PlatGenerator plat = getPlatGenerator(world, random, chunkX, chunkZ);
		if (plat != null) {
			plat.generateChunk(getWorld(), byteChunk, chunkX, chunkZ);
		}
		 
		return byteChunk.blocks;
	}
	
	public PlatGenerator getPlatGenerator(World world, Random random, int chunkX, int chunkZ) {
		
		if (generatorUrban == null) {
			long seed = world.getSeed();
			generatorUrban = new SimplexNoiseGenerator(seed);
			generatorWater = new SimplexNoiseGenerator(seed + 1);
			//generatorRoad = new SimplexNoiseGenerator(seed + 2);
			generatorUnfinished = new SimplexNoiseGenerator(seed + 3);
		}
		
		double noiseUrban = (generatorUrban.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
		double noiseWater = (generatorWater.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
		//double noiseRoad = (generatorRoad.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
		double noiseUnfinished = (generatorUnfinished.noise(chunkX / (xFactor / 10), chunkZ / (zFactor / 10)) + 1) / 2;
		double noiseSelect = noiseWater > 0.3 ? noiseUrban : 1.0; 
		//if (chunkX % roadFactor == 0 || chunkZ % roadFactor == 0)
		
		switch(NoiseGenerator.floor(noiseSelect * 13)) {
		case 0:
			// city center
			return new FalseGenerator(Material.DIAMOND_BLOCK, 2);
		case 1:
		case 2:
			if (noiseUnfinished > 0.3)
				// highrise
				return new FalseGenerator(Material.IRON_BLOCK, 15);
			else
				// unfinished highrise
				return new FalseGenerator(Material.GLASS, 14);
		case 3:
		case 4:
			// midrise
			if (noiseUnfinished > 0.2)
				return new FalseGenerator(Material.IRON_BLOCK, 10);
			else
				// unfinished midrise
				return new FalseGenerator(Material.GLASS, 9);
		case 5:
			if (noiseUnfinished > 0.1)
				// lowrise
				return new FalseGenerator(Material.COBBLESTONE, 5);
			else
				// unfinished lowrise
				return new FalseGenerator(Material.GLASS, 4);
		case 6:
			if (noiseUnfinished > 0.1)
				// residential
				return new FalseGenerator(Material.SMOOTH_BRICK, 1);
			else
				// unfinished residential
				return new FalseGenerator(Material.GLASS, 1);
		case 7:
			// mall
			return new FalseGenerator(Material.GOLD_BLOCK, 2);
		case 8:
			// park
			return new ParkGenerator();
		case 9:
		case 10:
			if (noiseUnfinished > 0.1)
				// residential
				return new FalseGenerator(Material.SMOOTH_BRICK, 1);
			else
				// unfinished residential
				return new FalseGenerator(Material.GLASS, 1);
		case 11:
		case 12:
			// farm
			return new FarmGenerator();
		default:
			return new WaterGenerator();
		}
	}
}
