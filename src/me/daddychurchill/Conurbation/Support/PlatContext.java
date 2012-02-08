package me.daddychurchill.Conurbation.Support;

public class PlatContext {

	private static final double xFactor = 25.0;
	private static final double zFactor = 25.0;
	private static final int roadFactor = 5;
	private static final double roadChance = 0.80;
	
	private double noiseUrban;
	private double noiseWater;
	private double noiseRoad;
	private double noiseUnfinished;
	private double noiseSelect; 
	
	public PlatContext(NoiseMakers noise, int chunkX, int chunkZ) {
		noiseUrban = (noise.generatorUrban.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
		noiseWater = (noise.generatorWater.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
		noiseRoad = (noise.generatorRoad.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
		noiseUnfinished = (noise.generatorUnfinished.noise(chunkX / (xFactor / 10), chunkZ / (zFactor / 10)) + 1) / 2;
		noiseSelect = noiseWater > 0.3 ? noiseUrban : 1.0; 
	}
}
