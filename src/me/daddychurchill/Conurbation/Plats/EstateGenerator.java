package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.HouseFactory;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Material;

public class EstateGenerator extends PlatGenerator {

	private final static Material matGround = Material.DIRT;
	private final static byte byteGround = (byte) matGround.getId();
	private final static Material matGrass = Material.GRASS;
	private final static byte byteGrass = (byte) matGrass.getId();
	
	private final static double oddsOfHouse = 0.50;

	private final static int slotHouse = 0;
	
	int groundLevel;
	
	public EstateGenerator(Generator noise) {
		super(noise);

		groundLevel = noise.getStreetLevel();
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return noise.isSuburban(chunkX, chunkZ) && noise.isGreenBelt(chunkX, chunkZ);
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(groundLevel, byteGround);
		chunk.setLayer(groundLevel + 1, byteGrass);
		
		// do one?
		if (isHouse(chunkX, chunkZ)) {
			byte byteFloor = pickFloorMaterial(chunkX, chunkZ);
			byte byteWall = pickWallMaterial(chunkX, chunkZ);
			byte byteRoof = pickRoofMaterial(chunkX, chunkZ);
			
			//TODO I really need to make the estates fancier
			HouseFactory.generateColonial(chunk, random, chunkX, chunkZ, groundLevel + 2, byteFloor, byteWall, byteRoof, 3);
		}
	}
	
	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		if (!isHouse(chunkX, chunkZ))
			noise.generatorParks.populateChunk(chunk, random, chunkX, chunkZ);
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, groundLevel, blockZ, byteGround);
		chunk.setBlock(blockX, groundLevel + 1, blockZ, byteGrass);
		
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

	private boolean isHouse(int chunkX, int chunkZ) {
		
		// what does the stars say?
		boolean possibleHouse = ifFeatureAt(chunkX, chunkZ, slotHouse, oddsOfHouse);
		if (possibleHouse) {

			// look around
			boolean roadToNorth = noise.isRoad(chunkX, chunkZ - 1);
			boolean roadToSouth = noise.isRoad(chunkX, chunkZ + 1);
			boolean roadToWest = noise.isRoad(chunkX - 1, chunkZ);
			boolean roadToEast = noise.isRoad(chunkX + 1, chunkZ);
		
			// OK, make sure there is a road near by
			possibleHouse = roadToNorth || roadToSouth || roadToWest || roadToEast;
		}
		return possibleHouse;
	}
}
