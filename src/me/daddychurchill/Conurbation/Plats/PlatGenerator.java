package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public abstract class PlatGenerator {
	
	private final static double xFeatureFactor = 5.0;
	private final static double zFeatureFactor = 5.0;
	private SimplexNoiseGenerator noiseFeature; // reproducible randomness
	
	public Generator noise;
	public PlatGenerator(Generator noise) {
		super();
		this.noise = noise;

		noiseFeature = new SimplexNoiseGenerator(noise.getNextSeed());
	}

	// what you need to do
	public abstract boolean isChunk(int chunkX, int chunkZ);
	public abstract void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ);
	public abstract void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ);
	public abstract int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ);
	public abstract int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ);
	public abstract Material getGroundSurfaceMaterial(int chunkX, int chunkZ);
	
	// what you may want to override
	public boolean isCompatibleEdgeChunk(PlatGenerator generator) {
		return generator == this;
	}
	
	// some reasonable globals
	protected final static byte byteBedrock = (byte) Material.BEDROCK.getId();
	protected final static byte byteWater = (byte) Material.STATIONARY_WATER.getId();
	protected final static byte byteStone = (byte) Material.STONE.getId();
	protected final static byte byteDirt = (byte) Material.DIRT.getId();
	protected final static byte byteGrass = (byte) Material.GRASS.getId();
	protected final static byte byteIron = (byte) Material.IRON_BLOCK.getId();
	protected final static byte byteGlass = (byte) Material.GLASS.getId();
	
	protected final static byte byteStoneWall = (byte) Material.SMOOTH_BRICK.getId();
	protected final static byte byteFence = (byte) Material.FENCE.getId();
	protected final static byte byteFenceBase = (byte) Material.SMOOTH_BRICK.getId();
	protected final static byte byteSidewalk = (byte) Material.STEP.getId();
	protected final static byte byteParkwalk = (byte) Material.DOUBLE_STEP.getId();

	protected final static int intGrassBlades = Material.LONG_GRASS.getId();
	protected final static int intFlowerRed = Material.RED_ROSE.getId();
	protected final static int intFlowerYellow = Material.YELLOW_FLOWER.getId();
	
	protected final static double oddsOfASidewalk = 0.90;
	
	protected double calcBlock(double chunkI, double i) {
		return chunkI + (i / 16.0);
	}

	protected Long getChunkKey(int chunkX, int chunkZ) {
		return Long.valueOf((long) chunkX * (long) Integer.MAX_VALUE + (long) chunkZ);
	}
	
	// feature support
	public int randomFeatureAt(int chunkX, int chunkZ, int slot, int range) {
		return NoiseGenerator.floor(randomFeatureAt(chunkX, chunkZ, slot) * range);
	}

	public double randomFeatureAt(int chunkX, int chunkZ, int slot) {
		return (noiseFeature.noise(chunkX / xFeatureFactor, slot, chunkZ / zFeatureFactor) + 1) / 2;
	}

	public boolean ifFeatureAt(int chunkX, int chunkZ, int slot, double odds) {
		return randomFeatureAt(chunkX, chunkZ, slot) < odds;
	}
	
	// useful generator macro functions
	protected void generateFence(ByteChunk chunk, boolean hasOpening, int x1, int x2, int y, int z1, int z2) {
		for (int x = x1; x < x2; x++) {
			for (int z = z1; z < z2; z++) {
				if (!(hasOpening && (x == 7 || x == 8 || z == 7 || z == 8))) {
					chunk.setBlock(x, y - 1, z, byteFenceBase);
					chunk.setBlock(x, y, z, byteFence);
				}
			}
		}
	}
	
	protected void generateSidewalk(ByteChunk chunk, int x1, int x2, int y, int z1, int z2) {
		chunk.setBlocks(x1, x2, y, y + 1, z1, z2, byteSidewalk);
	}

	protected void generateParkwalk(ByteChunk chunk, int x1, int x2, int y, int z1, int z2) {
		chunk.setBlocks(x1, x2, y, y + 1, z1, z2, byteParkwalk);
	}

	// some simple support functions for generators to use
	protected boolean isThereASidewalk(Random random) {
		return random.nextDouble() < oddsOfASidewalk;
	}
	
	protected int getRandomFlowerType(Random random) {
		switch (random.nextInt(2)) {
		case 1:
			return intFlowerRed;
		default:
			return intFlowerYellow;
		}
	}
	
	protected TreeType getRandomTreeType(Random random) {
		switch (random.nextInt(3)) {
		case 1:
			return TreeType.BIRCH;
		case 2:
			return TreeType.REDWOOD;
		default:
			return TreeType.TREE;
		}
	}

}
