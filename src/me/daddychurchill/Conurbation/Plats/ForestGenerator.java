package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class ForestGenerator extends PlatGenerator {

	private Material dirt = Material.DIRT;
	private Material grass = Material.GRASS;
	private byte byteDirt = (byte) dirt.getId();
	private byte byteGrass = (byte) grass.getId();
	
	int groundLevel;
	int maximumLevel;
	double groundRange;
	
	public final static double xGroundFactor = 50.0;
	public final static double zGroundFactor = 50.0;
	public final static double scaleGround = 5.0;
	public final static double rangeRural = 0.10;
	
	private final static double oddsOfGrass = 0.30;
	private final static double oddsOfFlower = 0.05;
	private final static double oddsOfTree = 0.02;
	
	private SimplexNoiseGenerator noiseGround;
	
	public ForestGenerator(Generator noise) {
		super(noise);
		
		noiseGround = new SimplexNoiseGenerator(noise.getNextSeed());
		
		groundLevel = noise.getStreetLevel();
		maximumLevel = noise.getMaximumLevel();
		groundRange = (maximumLevel - groundLevel) * rangeRural; 
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		boolean roadToNorth = noise.isRoad(chunkX, chunkZ - 1);
		boolean roadToSouth = noise.isRoad(chunkX, chunkZ + 1);
		boolean roadToWest = noise.isRoad(chunkX - 1, chunkZ);
		boolean roadToEast = noise.isRoad(chunkX + 1, chunkZ);
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int y = getGroundSurfaceY(chunkX, chunkZ, x, z);
				if ((roadToNorth && z == 0) || (roadToSouth && z == 15) ||
					(roadToWest && x == 0) || (roadToEast && x == 15)) {
					chunk.setBlocks(x, groundLevel, y + 1, z, byteStoneWall);
				} else {
					chunk.setBlocks(x, groundLevel, y, z, byteDirt);
					chunk.setBlock(x, y, z, byteGrass);
				}
			}
		}
	}
	
	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		int blockY = getGroundSurfaceY(chunkX, chunkZ, blockX, blockZ);
		chunk.setBlocks(blockX, groundLevel, blockY, blockZ, byteDirt);
		chunk.setBlock(blockX, blockY, blockZ, byteGrass);
		return blockY;
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		World world = noise.getWorld();
		boolean roadToNorth = noise.isRoad(chunkX, chunkZ - 1);
		boolean roadToSouth = noise.isRoad(chunkX, chunkZ + 1);
		boolean roadToWest = noise.isRoad(chunkX - 1, chunkZ);
		boolean roadToEast = noise.isRoad(chunkX + 1, chunkZ);
		
		// cover the ground
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int y = getGroundSurfaceY(chunkX, chunkZ, x, z) + 1;
				if ((roadToNorth && z == 0) || (roadToSouth && z == 15) ||
					(roadToWest && x == 0) || (roadToEast && x == 15)) {
					
					// do something on top of the brick walls?
					
				} else {
					
					// plant some plants
					double plantNoise = random.nextDouble();
					
					if (plantNoise < oddsOfTree) {
						world.generateTree(chunk.getBlockLocation(x, y, z), getRandomTreeType(random));
					} else if (plantNoise < oddsOfFlower) {
						chunk.setBlock(x, y, z, getRandomFlowerType(random), (byte) 0, false);
					} else if (plantNoise < oddsOfGrass) {
						chunk.setBlock(x, y, z, intGrassBlades, (byte) 1, false);
					}
				}
			}
		}
	}
	
	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		double x = chunkX * 16 + blockX;
		double z = chunkZ * 16 + blockZ;
		double noiseLevel = noiseGround.noise(x / xGroundFactor, z / zGroundFactor);
		return groundLevel + Math.max(1, Math.abs(NoiseGenerator.floor(noiseLevel * scaleGround)));
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return dirt;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return noise.isRural(chunkX, chunkZ) && noise.isGreenBelt(chunkX, chunkZ);
	}

}
