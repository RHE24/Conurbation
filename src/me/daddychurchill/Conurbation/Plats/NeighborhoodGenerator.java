package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.HouseFactory;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class NeighborhoodGenerator extends PlatGenerator {

	public final static Material matGround = Material.DIRT;
	public final static byte byteGround = (byte) matGround.getId();
	public final static Material matGrass = Material.GRASS;
	public final static byte byteGrass = (byte) matGrass.getId();
	
	public final static double oddsOfHouse = 0.70;

	private final static int slotHouse = 0;
	
	int groundLevel;
	
	public NeighborhoodGenerator(Generator noise) {
		super(noise);

		groundLevel = noise.getStreetLevel();
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return noise.isSuburban(chunkX, chunkZ) && !noise.isGreenBelt(chunkX, chunkZ);
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(groundLevel, byteGround);
		chunk.setLayer(groundLevel + 1, byteGrass);
		
		// do one?
		if (isHouse(chunkX, chunkZ))
			HouseFactory.generate(chunk, random, chunkX, chunkZ, groundLevel + 2);
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		if (!isHouse(chunkX, chunkZ))
			noise.generatorParks.populateChunk(chunk, random, chunkX, chunkZ);
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, groundLevel, blockZ, byteGround);
		return groundLevel;
	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return groundLevel;
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
