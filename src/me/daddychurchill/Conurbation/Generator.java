package me.daddychurchill.Conurbation;


import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import me.daddychurchill.Conurbation.Plats.FalseGenerator;
import me.daddychurchill.Conurbation.Plats.FarmGenerator;
import me.daddychurchill.Conurbation.Plats.ParkGenerator;
import me.daddychurchill.Conurbation.Plats.PlatGenerator;
import me.daddychurchill.Conurbation.Plats.WaterGenerator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.NoiseMakers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class Generator extends ChunkGenerator {

	private Conurbation plugin;
	private Populator populator;
	private String worldname;
	private String worldstyle;
	
	private NoiseMakers noisemakers;
	private int streetLevel;
	private int seabedLevel;
	
	public Generator(Conurbation instance, String name, String style){
		this.plugin = instance;
		this.worldname = name;
		this.worldstyle = style;
		this.streetLevel = plugin.getStreetLevel();
		this.seabedLevel = plugin.getSeabedLevel();
		
		// load configuration
		FileConfiguration config = plugin.getConfig();
		
		streetLevel = plugin.validateStreetLevel(getWorldInt(config, "StreetLevel", streetLevel), seabedLevel);
		seabedLevel = plugin.validateSeabedLevel(getWorldInt(config, "SeabedLevel", seabedLevel), streetLevel);
	}
	
	private int getWorldInt(FileConfiguration config, String option, int global) {
		int result = global;
		String path = worldname + "." + option;
		if (config.isSet(path))
			result = config.getInt(path);
		return result;
	}
	
	public Conurbation getPlugin() {
		return plugin;
	}
	
	public Populator getPopulator() {
		return populator;
	}
	
	public String getWorldname() {
		return worldname;
	}

	public String getWorldstyle() {
		return worldstyle;
	}
	
	public NoiseMakers getNoiseMakers() {
		return noisemakers;
	}
	
	public int getStreetLevel() {
		return streetLevel;
	}

	public int getSeabedLevel() {
		return seabedLevel;
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		populator = new Populator(this);
		
		return Arrays.asList((BlockPopulator) populator);
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
		
		// make some noise!
		if (noisemakers == null)
			noisemakers = new NoiseMakers(world);
		
		// figure out what everything looks like
		PlatGenerator plat = getPlatGenerator(chunkX, chunkZ);
		if (plat != null)
			plat.generateChunk(byteChunk, chunkX, chunkZ);
		 
		return byteChunk.blocks;
	}
	
	private Hashtable<Long, PlatGenerator> plats;
	public PlatGenerator getPlatGenerator(int chunkX, int chunkZ) {
		
		// get the plat map collection
		if (plats == null)
			plats = new Hashtable<Long, PlatGenerator>();

		// calculate the plat's key
		Long platkey = Long.valueOf(((long) chunkX * (long) Integer.MAX_VALUE + (long) chunkZ));
		
		// get the right plat
		PlatGenerator plat = plats.get(platkey);

		// doesn't exist? then make it!
		if (plat == null) {
			
//			switch(NoiseGenerator.floor(noiseSelect * 13)) {
//			case 0:
//				// city center
//				return new FalseGenerator(Material.DIAMOND_BLOCK, 2);
//			case 1:
//			case 2:
//				if (noiseUnfinished > 0.3)
//					// highrise
//					return new FalseGenerator(Material.IRON_BLOCK, 15);
//				else
//					// unfinished highrise
//					return new FalseGenerator(Material.GLASS, 14);
//			case 3:
//			case 4:
//				// midrise
//				if (noiseUnfinished > 0.2)
//					return new FalseGenerator(Material.IRON_BLOCK, 10);
//				else
//					// unfinished midrise
//					return new FalseGenerator(Material.GLASS, 9);
//			case 5:
//				if (noiseUnfinished > 0.1)
//					// lowrise
//					return new FalseGenerator(Material.COBBLESTONE, 5);
//				else
//					// unfinished lowrise
//					return new FalseGenerator(Material.GLASS, 4);
//			case 6:
//				if (noiseUnfinished > 0.1)
//					// residential
//					return new FalseGenerator(Material.SMOOTH_BRICK, 1);
//				else
//					// unfinished residential
//					return new FalseGenerator(Material.GLASS, 1);
//			case 7:
//				// mall
//				return new FalseGenerator(Material.GOLD_BLOCK, 2);
//			case 8:
//				// park
//				return new ParkGenerator();
//			case 9:
//			case 10:
//				if (noiseUnfinished > 0.1)
//					// residential
//					return new FalseGenerator(Material.SMOOTH_BRICK, 1);
//				else
//					// unfinished residential
//					return new FalseGenerator(Material.GLASS, 1);
//			case 11:
//			case 12:
//				// farm
//				return new FarmGenerator();
//			default:
//				return new WaterGenerator(generatorWater);
//			}
		}
		return plat;
		
		
	}
}
