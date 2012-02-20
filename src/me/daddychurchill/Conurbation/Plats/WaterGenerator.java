package me.daddychurchill.Conurbation.Plats;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;

public abstract class WaterGenerator extends PlatGenerator {

	protected final static byte byteSand = (byte) Material.SAND.getId();
	protected final static byte byteDirt = (byte) Material.DIRT.getId();
	protected final static byte byteSeawall = (byte) Material.SMOOTH_BRICK.getId();
	
	public final static int shoreHeight = 3;
	public final static int seawallHeight = 1;
	protected int streetLevel;
	protected int waterLevel;
	
	public WaterGenerator(Generator noise) {
		super(noise);

		streetLevel = noise.getStreetLevel();
		waterLevel = streetLevel - shoreHeight;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return Material.SAND;
	}
	
	protected void generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int x, int z, int waterbedY) {
		chunk.setBlocks(x, 1, waterbedY, z, byteSand);
		if (waterbedY <= waterLevel)
			chunk.setBlocks(x, waterbedY, waterLevel + 1, z, byteWater);
	}
	
	protected void generateSeawalls(ByteChunk chunk, int chunkX, int chunkZ) {
		boolean toNorth = !noise.isWater(chunkX, chunkZ - 1);
		boolean toSouth = !noise.isWater(chunkX, chunkZ + 1);
		boolean toWest = !noise.isWater(chunkX - 1, chunkZ);
		boolean toEast = !noise.isWater(chunkX + 1, chunkZ);
		byte topLayer;
		boolean isBridge = noise.isRoad(chunkX, chunkZ);
		
		int walltopLevel = isBridge ? streetLevel - 1 : streetLevel + seawallHeight + 1;
		
		if (toNorth && toWest && !toSouth && !toEast) {
			topLayer = (byte) noise.getTopMaterial(chunkX, chunkZ - 1).getId();
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16 - x; z++) {
					fillSpot(chunk, x, z, topLayer, z == 16 - x - 1, walltopLevel);
				}
			}
		} else if (toNorth && toEast && !toSouth && !toWest) {
			topLayer = (byte) noise.getTopMaterial(chunkX + 1, chunkZ).getId();
			for (int z = 0; z < 16; z++) {
				for (int x = z; x < 16; x++) {
					fillSpot(chunk, x, z, topLayer, x == z, walltopLevel);
				}
			}
		} else if (toSouth && toWest && !toNorth && !toEast) {
			topLayer = (byte) noise.getTopMaterial(chunkX - 1, chunkZ).getId();
			for (int x = 0; x < 16; x++) {
				for (int z = x; z < 16; z++) {
					fillSpot(chunk, x, z, topLayer, z == x, walltopLevel);
				}
			}
		} else if (toSouth && toEast && !toNorth && !toWest) {
			topLayer = (byte) noise.getTopMaterial(chunkX, chunkZ + 1).getId();
			for (int z = 0; z < 16; z++) {
				for (int x = 15 - z; x < 16; x++) {
					fillSpot(chunk, x, z, topLayer, x == 15 - z, walltopLevel);
				}
			}
		} else {
			if (toNorth) {
				chunk.setBlocks(0, 16, 1, walltopLevel, 0, 1, byteSeawall);
				chunk.setBlocks(0, 1, 1, walltopLevel, 1, 2, byteSeawall);
				chunk.setBlocks(15, 16, 1, walltopLevel, 1, 2, byteSeawall);
			} 
			if (toSouth) {
				chunk.setBlocks(0, 16, 1, walltopLevel, 15, 16, byteSeawall);
				chunk.setBlocks(0, 1, 1, walltopLevel, 14, 15, byteSeawall);
				chunk.setBlocks(15, 16, 1, walltopLevel, 14, 15, byteSeawall);
			} 
			if (toWest) {
				chunk.setBlocks(0, 1, 1, walltopLevel, 0, 16, byteSeawall);
				chunk.setBlocks(1, 2, 1, walltopLevel, 0, 1, byteSeawall);
				chunk.setBlocks(1, 2, 1, walltopLevel, 15, 16, byteSeawall);
			} 
			if (toEast) {
				chunk.setBlocks(15, 16, 1, walltopLevel, 0, 16, byteSeawall);
				chunk.setBlocks(14, 15, 1, walltopLevel, 0, 1, byteSeawall);
				chunk.setBlocks(14, 15, 1, walltopLevel, 15, 16, byteSeawall);
			}
		}
	}

	private void fillSpot(ByteChunk chunk, int x, int z, 
			byte topFill, boolean wallFill, int walltopLevel) {
		
		if (wallFill) {
			chunk.setBlocks(x, 1, walltopLevel, z, byteSeawall);
		} else {
			chunk.setBlocks(x, 1, streetLevel - 4, z, byteStone);
			chunk.setBlocks(x, streetLevel - 4, streetLevel, z, byteGrass);
			chunk.setBlock(x, streetLevel, z, topFill);
		}
	}

}
