package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class RuralGenerator extends PlatGenerator {

	private Material dirt = Material.DIRT;
	private Material grass = Material.GRASS;
	private Material brick = Material.SMOOTH_BRICK;
	private byte byteDirt = (byte) dirt.getId();
	private byte byteGrass = (byte) grass.getId();
	private byte byteBrick = (byte) brick.getId();
	private int intGrassBlades = Material.LONG_GRASS.getId();
	private int intFlowerRed = Material.RED_ROSE.getId();
	private int intFlowerYellow = Material.YELLOW_FLOWER.getId();
	
	int groundLevel;
	int maximumLevel;
	double groundRange;
	
	public final static double xRuralFactor = 30.0;
	public final static double zRuralFactor = 30.0;
	public final static double threshholdRural = 0.50;
	public final static double rangeRural = 0.10;
	
	public final static double xGroundFactor = 50.0;
	public final static double zGroundFactor = 50.0;
	public final static double scaleGround = 5.0;
	
	private final static double threshholdGrass = 0.60;
	private final static double threshholdFlower = 0.94;
	private final static double threshholdTree = 0.98;
	
	private SimplexNoiseGenerator noiseRural;
	private SimplexNoiseGenerator noiseGround;
	
	public RuralGenerator(Generator noise) {
		super(noise);
		
		noiseRural = new SimplexNoiseGenerator(noise.getNextSeed());
		noiseGround = new SimplexNoiseGenerator(noise.getNextSeed());
		
		groundLevel = noise.getStreetLevel();
		maximumLevel = noise.getMaximumLevel();
		groundRange = (maximumLevel - groundLevel) * rangeRural; 
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		boolean toNorth = !noise.isCompatibleGenerator(chunkX, chunkZ - 1, this);
		boolean toSouth = !noise.isCompatibleGenerator(chunkX, chunkZ + 1, this);
		boolean toWest = !noise.isCompatibleGenerator(chunkX - 1, chunkZ, this);
		boolean toEast = !noise.isCompatibleGenerator(chunkX + 1, chunkZ, this);
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int y = getGroundSurfaceY(chunkX, chunkZ, x, z);
				if ((toNorth && z == 0) || (toSouth && z == 15) ||
					(toWest && x == 0) || (toEast && x == 15)) {
					chunk.setBlocks(x, groundLevel, y + 1, z, byteBrick);
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
		boolean toNorth = !noise.isCompatibleGenerator(chunkX, chunkZ - 1, this);
		boolean toSouth = !noise.isCompatibleGenerator(chunkX, chunkZ + 1, this);
		boolean toWest = !noise.isCompatibleGenerator(chunkX - 1, chunkZ, this);
		boolean toEast = !noise.isCompatibleGenerator(chunkX + 1, chunkZ, this);
		World world = noise.getWorld();
		
		// cover the ground
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int y = getGroundSurfaceY(chunkX, chunkZ, x, z) + 1;
				if ((toNorth && z == 0) || (toSouth && z == 15) ||
					(toWest && x == 0) || (toEast && x == 15)) {
					
					// do something on top of the brick walls?
					
				} else {
					
					// plant some plants
					double plantNoise = random.nextDouble();
					
					if (plantNoise > threshholdTree) {
						world.generateTree(chunk.getBlockLocation(x, y, z), getRandomTreeType(random));
					} else if (plantNoise > threshholdFlower) {
						chunk.setBlock(x, y, z, getRandomFlowerType(random), (byte) 0, false);
					} else if (plantNoise > threshholdGrass) {
						chunk.setBlock(x, y, z, intGrassBlades, (byte) 1, false);
					}
				}
			}
		}
	}
	
	public int getRandomFlowerType(Random random) {
		switch (random.nextInt(2)) {
		case 1:
			return intFlowerRed;
		default:
			return intFlowerYellow;
		}
	}
	
	public TreeType getRandomTreeType(Random random) {
		switch (random.nextInt(3)) {
		case 1:
			return TreeType.BIRCH;
		case 2:
			return TreeType.REDWOOD;
		default:
			return TreeType.TREE;
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
		double noiseLevel = (noiseRural.noise(chunkX / xRuralFactor, chunkX / zRuralFactor) + 1) / 2;
		return noiseLevel > threshholdRural;
	}

}
