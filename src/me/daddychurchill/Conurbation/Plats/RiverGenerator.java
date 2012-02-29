package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class RiverGenerator extends WaterGenerator {

	public final static double xRiverFactor = 40.0;
	public final static double zRiverFactor = 40.0;
	public final static double threshholdMinRiver = 0.40;
	public final static double threshholdMaxRiver = 0.525;
	public final static double xRiverbedNoiseFactor = 2.5;
	public final static double zRiverbedNoiseFactor = 2.5;
	public final static double scaleRiverbedNoise = 3.0;
	
	private int riverDepth;
	private PlatGenerator generatorLake;
	
	private SimplexNoiseGenerator noiseRiver;
	private SimplexNoiseGenerator noiseRiverbedDeviance; // add/subtract a little from the normal river level
	
	public RiverGenerator(Generator noise, PlatGenerator aGeneratorLake) {
		super(noise);
		this.generatorLake = aGeneratorLake;

		noiseRiver = new SimplexNoiseGenerator(noise.getNextSeed());
		noiseRiverbedDeviance = new SimplexNoiseGenerator(noise.getNextSeed());

		riverDepth = Math.min(waterLevel - noise.getSeabedLevel(), NoiseGenerator.floor(scaleRiverbedNoise * 2.0));
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(0, byteBedrock);
		
		if (noise.isDelta(chunkX, chunkZ))
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++) {
					generateChunkColumn(chunk, chunkX, chunkZ, x, z, getDeltaGroundSurfaceY(chunkX, chunkZ, x, z));
				}
		else 
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++) {
					generateChunkColumn(chunk, chunkX, chunkZ, x, z, getGroundSurfaceY(chunkX, chunkZ, x, z));
				}
		generateSeawalls(chunk, random, chunkX, chunkZ);
	}
	
	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		int blockY = noise.isDelta(chunkX, chunkZ) ? getDeltaGroundSurfaceY(chunkX, chunkZ, blockX, blockZ) : getGroundSurfaceY(chunkX, chunkZ, blockX, blockZ);
		
		//TODO this is missing all the fancy bits!
		
		return generateChunkColumn(chunk, chunkX, chunkZ, blockX, blockZ, blockY);
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		double noiseLevel = (noiseRiver.noise(chunkX / xRiverFactor, chunkZ / zRiverFactor) + 1) / 2;
		return noiseLevel > threshholdMinRiver && noiseLevel < threshholdMaxRiver && !generatorLake.isChunk(chunkX, chunkZ);
	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		double x = calcBlock(chunkX, blockX);
		double z = calcBlock(chunkZ, blockZ);
		double riverNoise = noiseRiverbedDeviance.noise(x / xRiverbedNoiseFactor, z / zRiverbedNoiseFactor) * scaleRiverbedNoise;
		int riverbedLevel = NoiseGenerator.floor(riverDepth + riverNoise - scaleRiverbedNoise);
		return Math.max(0, Math.min(riverbedLevel, riverDepth - 1)) + waterLevel - riverDepth;
	}
	
	public int getDeltaGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return (generatorLake.getGroundSurfaceY(chunkX, chunkZ, blockX, blockZ) + 
				getGroundSurfaceY(chunkX, chunkZ, blockX, blockZ)) / 2;
	}

}
