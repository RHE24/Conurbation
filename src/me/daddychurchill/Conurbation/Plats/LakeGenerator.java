package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class LakeGenerator extends WaterGenerator {

	public final static double xLakeFactor = 40.0;
	public final static double zLakeFactor = 40.0;
	public final static double threshholdLake = 0.3;
	public final static double xLakebedNoiseFactor = 2.0;
	public final static double zLakebedNoiseFactor = 2.0;
	public final static double scaleLakebedNoise = 4.0;
	
	public int lakeDepth;
	
	private SimplexNoiseGenerator noiseLake;
	private SimplexNoiseGenerator noiseLakebedDeviance; // add/subtract a little from the normal seabed level
	
	public LakeGenerator(Generator noise) {
		super(noise);
		
		noiseLake = new SimplexNoiseGenerator(noise.getNextSeed());
		noiseLakebedDeviance = new SimplexNoiseGenerator(noise.getNextSeed());
		
		lakeDepth = waterLevel - noise.getSeabedLevel();
	}
	
	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(0, byteBedrock);
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				generateChunkColumn(chunk, chunkX, chunkZ, x, z, getGroundSurfaceY(chunkX, chunkZ, x, z));
			}
		}
		generateSeawalls(chunk, random, chunkX, chunkZ);
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		
		//TODO this is missing all the fancy bits!
		
		return generateChunkColumn(chunk, chunkX, chunkZ, blockX, blockZ, getGroundSurfaceY(chunkX, chunkZ, blockX, blockZ));
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		double noiseLevel = (noiseLake.noise(chunkX / xLakeFactor, chunkZ / zLakeFactor) + 1) / 2;
		return noiseLevel < threshholdLake;
	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		double x = calcBlock(chunkX, blockX);
		double z = calcBlock(chunkZ, blockZ);
		double lakebedShape = (noiseLake.noise(x / xLakeFactor, z / zLakeFactor) + 1) / 2 / threshholdLake;
		double lakebedNoise = noiseLakebedDeviance.noise(x / xLakebedNoiseFactor, z / zLakebedNoiseFactor) * scaleLakebedNoise;
		int lakebedLevel = NoiseGenerator.floor(lakebedShape * lakeDepth + lakebedNoise - scaleLakebedNoise);
		return Math.max(0, Math.min(lakebedLevel, lakeDepth - 1)) + waterLevel - lakeDepth;
	}
}
