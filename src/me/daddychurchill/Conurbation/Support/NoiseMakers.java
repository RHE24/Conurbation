package me.daddychurchill.Conurbation.Support;

import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class NoiseMakers {
	public SimplexNoiseGenerator generatorUrban;
	public SimplexNoiseGenerator generatorWater;
	public SimplexNoiseGenerator generatorUnfinished;
	public SimplexNoiseGenerator generatorRoad;
	
	public NoiseMakers(World world) {
		super();
		
		long seed = world.getSeed();
		generatorUrban = new SimplexNoiseGenerator(seed);
		generatorWater = new SimplexNoiseGenerator(seed + 1);
		generatorRoad = new SimplexNoiseGenerator(seed + 2);
		generatorUnfinished = new SimplexNoiseGenerator(seed + 3);
	}
	
	public Neighbors getNeighbors(int chunkX, int chunkZ) {
		
		return null;
//		double noiseUrban = (generatorUrban.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
//		double noiseWater = (generatorWater.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
//		double noiseRoad = (generatorRoad.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
//		double noiseUnfinished = (generatorUnfinished.noise(chunkX / (xFactor / 10), chunkZ / (zFactor / 10)) + 1) / 2;
//		double noiseSelect = noiseWater > 0.3 ? noiseUrban : 1.0; 
//		//if (chunkX % roadFactor == 0 || chunkZ % roadFactor == 0)
//		
//		return new Neighbors();
	}
	
	public boolean isUnfinished(int chunkX, int chunkZ, double odds) {
//		return (generatorUnfinished.noise(chunkX / (xFactor / 10), chunkZ / (zFactor / 10)) + 1) / 2 < odds; 
		return false;
	}
}
