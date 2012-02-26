package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.HouseFactory;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Material;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class FarmGenerator extends PlatGenerator {

	public final static Material matGround = Material.DIRT;
	public final static byte byteGround = (byte) matGround.getId();
	public final static byte byteWall = (byte) Material.WOOD.getId();
	public final static byte byteRoof = (byte) Material.COBBLESTONE.getId();
	
	public final static Material matWater = Material.STATIONARY_WATER;
	public final static Material matSoil = Material.SOIL;
	public final static Material matSand = Material.SAND;
	public final static Material matDirt = Material.DIRT;
	public final static Material matAir = Material.AIR;
	public final static Material matTrellis = Material.WOOD;

	public final static Material cropNone = Material.AIR;
	public final static Material cropYellowFlower = Material.YELLOW_FLOWER;
	public final static Material cropRedFlower = Material.RED_ROSE;
	public final static Material cropWheat = Material.CROPS;
	public final static Material cropPumpkin = Material.PUMPKIN_STEM;
	public final static Material cropMelon = Material.MELON_STEM;
	public final static Material cropVine = Material.VINE;
	public final static Material cropCactus = Material.CACTUS;
	public final static Material cropSugarCane = Material.SUGAR_CANE_BLOCK;
	// BrownMushroom
	// RedMushroom
	
	private final static double xCropFactor = 15.0;
	private final static double zCropFactor = 15.0;
	private SimplexNoiseGenerator noiseCrop;
	
	private final static double oddsOfNorthSouth = 0.50;
	private final static double oddsOfFenceOpening = 0.25;
	private final static double oddsOfFarmHouse = 0.20;
	
	private final static int slotDirection = 0;
	private final static int slotFarmHouse = 1;
	
	int groundLevel;
	int cropLevel;
	
	public FarmGenerator(Generator noise) {
		super(noise);

		noiseCrop = new SimplexNoiseGenerator(noise.getNextSeed());
		groundLevel = noise.getStreetLevel() + 1;
		cropLevel = groundLevel + 1;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return noise.isRural(chunkX, chunkZ) && !noise.isGreenBelt(chunkX, chunkZ);
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		// something to build upon
		chunk.setLayer(groundLevel - 1, 2, byteGround);
		
		// what goes here?
		if (isFarmHouse(chunkX, chunkZ)) {
			HouseFactory.generate(chunk, random, chunkX, chunkZ, cropLevel, byteWall, byteRoof);
			
		} else {
		
			// look around
			boolean roadToNorth = noise.isRoad(chunkX, chunkZ - 1);
			boolean roadToSouth = noise.isRoad(chunkX, chunkZ + 1);
			boolean roadToWest = noise.isRoad(chunkX - 1, chunkZ);
			boolean roadToEast = noise.isRoad(chunkX + 1, chunkZ);
			
			// fence us in
			if (roadToNorth)
				generateFence(chunk, isFenceOpen(random), 0, 16, cropLevel, 0, 1);
			if (roadToSouth)
				generateFence(chunk, isFenceOpen(random), 0, 16, cropLevel, 15, 16);
			if (roadToWest)
				generateFence(chunk, isFenceOpen(random), 0, 1, cropLevel, 0, 16);
			if (roadToEast)
				generateFence(chunk, isFenceOpen(random), 15, 16, cropLevel, 0, 16);
		}
	}
	
	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		if (!isFarmHouse(chunkX, chunkZ)) {
		
			// direction of farm plot
			boolean directionNorthSouth = ifFeatureAt(chunkX, chunkZ, slotDirection, oddsOfNorthSouth);
			
			// what type of crop do we plant?
			Material cropType = getCrop(chunkX, chunkZ);
			if (cropType == cropYellowFlower || cropType == cropRedFlower)
				plowField(chunk, directionNorthSouth, matDirt, 0, matWater, cropType, 0, 1, 2, 1);
			else if (cropType == cropWheat)
				plowField(chunk, directionNorthSouth, matSoil, 8, matWater, cropType, 4, 1, 2, 1);
			else if (cropType == cropPumpkin || cropType == cropMelon)
				plowField(chunk, directionNorthSouth, matSoil, 8, matWater, cropType, 4, 1, 3, 1);
			else if (cropType == cropCactus)
				plowField(chunk, directionNorthSouth, matSand, 0, matSand, cropType, 0, 2, 2, 3);
			else if (cropType == cropSugarCane)
				plowField(chunk, directionNorthSouth, matSand, 0, matWater, cropType, 0, 1, 2, 3);
			else if (cropType == cropVine)
				buildVineyard(chunk, directionNorthSouth);
			else
				plowField(chunk, directionNorthSouth, matDirt, 0, matAir, cropType, 0, 1, 2, 1);
		}
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlocks(blockX, groundLevel, groundLevel + 2, blockZ, byteGround);
		return groundLevel + 1;
	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return groundLevel + 1;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return matGround;
	}
	
	private boolean isFarmHouse(int chunkX, int chunkZ) {
		
		// what does the stars say?
		boolean possibleFarmHouse = ifFeatureAt(chunkX, chunkZ, slotFarmHouse, oddsOfFarmHouse);
		if (possibleFarmHouse) {

			// look around
			boolean roadToNorth = noise.isRoad(chunkX, chunkZ - 1);
			boolean roadToSouth = noise.isRoad(chunkX, chunkZ + 1);
			boolean roadToWest = noise.isRoad(chunkX - 1, chunkZ);
			boolean roadToEast = noise.isRoad(chunkX + 1, chunkZ);
		
			// OK, make sure there is a road near by
			possibleFarmHouse = roadToNorth || roadToSouth || roadToWest || roadToEast;
		}
		return possibleFarmHouse;
	}

	private boolean isFenceOpen(Random random) {
		return random.nextDouble() < oddsOfFenceOpening;
	}
	
	private void buildVineyard(RealChunk chunk, boolean directionNorthSouth) {
		int stepCol = 3;
		if (directionNorthSouth) {
			for (int x = 1; x < 15; x += stepCol) {
				chunk.setBlocks(x, cropLevel, cropLevel + 3, 1, matTrellis);
				chunk.setBlocks(x, cropLevel, cropLevel + 3, 14, matTrellis);
				chunk.setBlocks(x, x + 1, cropLevel + 3, cropLevel + 4, 1, 15, matTrellis);
				chunk.setBlocks(x - 1, x, cropLevel + 3, cropLevel + 4, 2, 14, cropVine, (byte) 8);
				chunk.setBlocks(x + 1, x + 2, cropLevel + 3, cropLevel + 4, 2, 14, cropVine, (byte) 2);
			}
		} else {
			for (int z = 1; z < 15; z += stepCol) {
				chunk.setBlocks(1, cropLevel, cropLevel + 3, z, matTrellis);
				chunk.setBlocks(14, cropLevel, cropLevel + 3, z, matTrellis);
				chunk.setBlocks(1, 15, cropLevel + 3, cropLevel + 4, z, z + 1, matTrellis);
				chunk.setBlocks(2, 14, cropLevel + 3, cropLevel + 4, z - 1, z, cropVine, (byte) 1);
				chunk.setBlocks(2, 14, cropLevel + 3, cropLevel + 4, z + 1, z + 2, cropVine, (byte) 4);
			}
		}
	}
	
	private void plowField(RealChunk chunk, boolean directionNorthSouth, Material matRidge, int datRidge, Material matFurrow, Material matCrop, int datCrop, int stepRow, int stepCol, int maxHeight) {
		byte byteRidge = (byte) datRidge;
		byte byteFurrow = (byte) 0;
		byte byteCrop = (byte) datCrop;
		if (directionNorthSouth) {
			for (int x = 1; x < 15; x += stepCol) {
				chunk.setBlocks(x, x + 1, groundLevel, groundLevel + 1, 1, 15, matRidge, byteRidge, false);
				if (stepCol > 1)
					chunk.setBlocks(x + 1, x + 2, groundLevel, groundLevel + 1, 1, 15, matFurrow, byteFurrow, false);
				for (int z = 1; z < 15; z += stepRow)
					chunk.setBlocks(x, cropLevel, cropLevel + maxHeight, z, matCrop, byteCrop, false);//matCrop);
			}
		} else {
			for (int z = 1; z < 15; z += stepCol) {
				chunk.setBlocks(1, 15, groundLevel, groundLevel + 1, z, z + 1, matRidge, byteRidge, false);
				if (stepCol > 1)
					chunk.setBlocks(1, 15, groundLevel, groundLevel + 1, z + 1, z + 2, matFurrow, byteFurrow, false);
				for (int x = 1; x < 15; x += stepRow)
					chunk.setBlocks(x, cropLevel, cropLevel + maxHeight, z, matCrop, byteCrop, false);//, matCrop);
			}
		}
	}
	
	private Material getCrop(int chunkX, int chunkZ) {
		double noise = (noiseCrop.noise(chunkX / xCropFactor, chunkZ / zCropFactor) + 1) / 2;
		switch (NoiseGenerator.floor(noise * 9)) {
		case 1:
			return cropYellowFlower;
		case 2:
			return cropRedFlower;
		case 3:
			return cropWheat;
		case 4:
			return cropPumpkin;
		case 5:
			return cropMelon;
		case 6:
			return cropVine;
		case 7:
			return cropCactus;
		case 8:
			return cropSugarCane;
		default:
			return cropNone;
		}
	}
}
