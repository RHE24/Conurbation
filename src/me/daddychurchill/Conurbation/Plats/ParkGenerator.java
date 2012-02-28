package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Material;
import org.bukkit.World;

public class ParkGenerator extends PlatGenerator {

	public final static Material matGround = Material.DIRT;
	public final static byte byteGround = (byte) matGround.getId();
	public final static Material matTurf = Material.GRASS;
	public final static byte byteTurf = (byte) matTurf.getId();
	public final static Material matTrunk = Material.LOG;
	
	int groundLevel;
	int fenceLevel;
	
	private final static double oddsOfGrass = 0.20;
	private final static double oddsOfFlower = 0.05;
	private final static double oddsOfTree = 0.005;
	private final static double oddsCenterplace = 0.20;
	
	public ParkGenerator(Generator noise) {
		super(noise);

		groundLevel = noise.getStreetLevel() + 1;
		fenceLevel = groundLevel + 1;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return noise.isUrban(chunkX, chunkZ) && noise.isGreenBelt(chunkX, chunkZ);
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		// look around
		boolean roadToNorth = noise.isRoad(chunkX, chunkZ - 1);
		boolean roadToSouth = noise.isRoad(chunkX, chunkZ + 1);
		boolean roadToWest = noise.isRoad(chunkX - 1, chunkZ);
		boolean roadToEast = noise.isRoad(chunkX + 1, chunkZ);
		
		// assume the best
		boolean sidewalkNorth = true;
		boolean sidewalkSouth = true;
		boolean sidewalkWest = true;
		boolean sidewalkEast = true;
		
		// draw the underlayment
		chunk.setLayer(groundLevel - 1, byteGround);
		chunk.setLayer(groundLevel, byteTurf);
		
		// draw the fences
		if (roadToNorth)
			sidewalkNorth = generateFence(chunk, random, 0, 16, fenceLevel, 0, 1);
		if (roadToSouth)
			sidewalkSouth = generateFence(chunk, random, 0, 16, fenceLevel, 15, 16);
		if (roadToWest) 
			sidewalkWest = generateFence(chunk, random, 0, 1, fenceLevel, 0, 16);
		if (roadToEast) 
			sidewalkEast = generateFence(chunk, random, 15, 16, fenceLevel, 0, 16);
		
		// draw the sidewalks
		if (sidewalkNorth)
			generateParkwalk(chunk, 7, 9, groundLevel, 0, 7);
		if (sidewalkSouth)
			generateParkwalk(chunk, 7, 9, groundLevel, 9, 16);
		if (sidewalkWest)
			generateParkwalk(chunk, 0, 7, groundLevel, 7, 9);
		if (sidewalkEast)
			generateParkwalk(chunk, 9, 16, groundLevel, 7, 9);
		
		// center needed?
		if (sidewalkNorth || sidewalkSouth || sidewalkWest || sidewalkEast)
			if (!generateCenterplace(chunk, chunkX, chunkZ))
				generateParkwalk(chunk, 7, 9, groundLevel, 7, 9);
	}
	
	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		World world = noise.getWorld();
		
		// cover the ground
		int y = groundLevel + 1;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				
				// only plant in the right place
				Material surface = chunk.getBlock(x, y - 1, z);
				if (surface == matTurf) {
					
					// plant some plants
					double plantNoise = random.nextDouble();
					
					// dead or not?
					if (noise.isDecrepit(random)) {
						if (plantNoise < oddsOfTree) {
							chunk.setBlocks(x, y, y + random.nextInt(4), z, matTrunk);
						} else if (plantNoise < oddsOfFlower) {
							chunk.setBlock(x, y, z, FarmGenerator.deadOnDirt);
						}
						
					// its ALIVE!
					} else {
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
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, groundLevel, blockZ, byteGround);
		chunk.setBlock(blockX, groundLevel + 1, blockZ, byteTurf);
		return groundLevel + 1;
	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return groundLevel + 1;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return matTurf;
	}

	//TODO change this to an enum... maybe
	private final static int featureCenterplace = 0;
	
	private boolean generateCenterplace(ByteChunk chunk, int chunkX, int chunkZ) {
		boolean doCenterplace = ifFeatureAt(chunkX, chunkZ, featureCenterplace, oddsCenterplace);
		if (doCenterplace) {
			generateParkwalk(chunk, 4, 12, groundLevel, 4, 12);
			chunk.setCircle(8, 8, 2, groundLevel + 1, byteCobblestone);
		}
		return doCenterplace;
	}
}
