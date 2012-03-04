package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Neighbors.CityNeighbors;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class CityGenerator extends PlatGenerator {

	private final static double xHeightFactor = 2.0;
	private final static double zHeightFactor = 2.0;
	public final static double floorDeviance = 4.0;
	private SimplexNoiseGenerator noiseHeightDeviance; // add/subtract a little from the normal height for this building

	private int firstFloorY;
	
	private final static int featureWallMaterial = 0;
	private final static int featureFloorMaterial = 1;
	private final static int featureRoofMaterial = 2;
	private final static int featureGlassStyle = 3;
	
	public CityGenerator(Generator noise) {
		super(noise);
		
		noiseHeightDeviance = new SimplexNoiseGenerator(noise.getNextSeed());
		firstFloorY = noise.getStreetLevel() + 1;
	}
	
	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(firstFloorY - 1, byteStone);
		chunk.setLayer(firstFloorY, RoadGenerator.byteSidewalk);
		
		// who is where?
		CityNeighbors neighbors = new CityNeighbors(this, chunkX, chunkZ);
		int glassSkip = randomFeatureAt(chunkX, chunkZ, featureGlassStyle, 4) + 1;
		
		for (int i = 0; i < neighbors.floors; i++) {
			int y1 = firstFloorY + i * Generator.floorHeight;
			int y2 = y1 + Generator.floorHeight;
			
			// look around!
			int insetNorth = neighbors.insetToNorth();
			int insetSouth = neighbors.insetToSouth();
			int insetWest = neighbors.insetToWest();
			int insetEast = neighbors.insetToEast();
			
			// place floor
			if (i == 0)
				generateFloor(chunk, neighbors, insetNorth, insetSouth, insetWest, insetEast, y1, neighbors.floorMaterial);
			
			// do the walls... maybe
			if (insetNorth > 0) {
				//wallMaterial = (byte) Material.BOOKSHELF.getId();
				generateNSWall(chunk, random, insetWest, chunk.width - insetEast, y1, y2, insetNorth, insetNorth + 1, neighbors.wallMaterial, glassSkip);
			}
			if (insetSouth > 0) {
				//wallMaterial = (byte) Material.GOLD_BLOCK.getId();
				generateNSWall(chunk, random, insetWest, chunk.width - insetEast, y1, y2, chunk.width - insetSouth - 1, chunk.width - insetSouth, neighbors.wallMaterial, glassSkip);
			}
			if (insetWest > 0) {
				//wallMaterial = (byte) Material.LAPIS_BLOCK.getId();
				generateEWWall(chunk, random, insetWest, insetWest + 1, y1, y2, insetNorth, chunk.width - insetSouth, neighbors.wallMaterial, glassSkip);
			}
			if (insetEast > 0) {
				//wallMaterial = (byte) Material.IRON_BLOCK.getId();
				generateEWWall(chunk, random, chunk.width - insetEast - 1, chunk.width - insetEast, y1, y2, insetNorth, chunk.width - insetSouth, neighbors.wallMaterial, glassSkip);
			}
			
			// top it off
			if (i == neighbors.floors - 1)
				generateFloor(chunk, neighbors, insetNorth, insetSouth, insetWest, insetEast, y2, neighbors.roofMaterial);
			else
				generateFloor(chunk, neighbors, insetNorth, insetSouth, insetWest, insetEast, y2, neighbors.floorMaterial);
			
			// move up
			neighbors.decrement();
		}
	}
	
	private void generateFloor(ByteChunk chunk, CityNeighbors neighbors, int insetNorth, int insetSouth, int insetWest, int insetEast, int y1, byte material) {
		int y2 = y1 + 1;
		
		// center part
		chunk.setBlocks(insetWest, chunk.width - insetEast, y1, y2, insetNorth, chunk.width - insetSouth, material);
		
		// now the outer bits
		if (insetNorth > 0 || insetSouth > 0 || insetWest > 0 || insetEast > 0) {
			
			// cardinal bits
			if (neighbors.toNorth())
				chunk.setBlocks(insetEast, chunk.width - insetWest, y1, y2, 0, insetNorth, material);
			if (neighbors.toSouth())
				chunk.setBlocks(insetEast, chunk.width - insetWest, y1, y2, chunk.width - insetSouth, chunk.width, material);
			if (neighbors.toWest())
				chunk.setBlocks(0, insetWest, y1, y2, insetNorth, chunk.width - insetSouth, material);
			if (neighbors.toEast())
				chunk.setBlocks(chunk.width - insetEast, chunk.width, y1, y2, insetNorth, chunk.width - insetSouth, material);
		
			// corner bits
			if (neighbors.toNorthWest())
				chunk.setBlocks(0, insetWest, y1, y2, 0, insetNorth, material);
			if (neighbors.toNorthEast())
				chunk.setBlocks(chunk.width - insetEast, chunk.width, y1, y2, 0, insetNorth, material);
			if (neighbors.toSouthWest())
				chunk.setBlocks(0, insetWest, y1, y2, chunk.width - insetSouth, chunk.width, material);
			if (neighbors.toSouthEast())
				chunk.setBlocks(chunk.width - insetEast, chunk.width, y1, y2, chunk.width - insetSouth, chunk.width, material);
		}
	}
	
	private void generateNSWall(ByteChunk chunk, Random random, int x1, int x2, int y1, int y2, int z1, int z2, byte wallMaterial, int glassSkip) {
		chunk.setBlocks(x1, x2, y1 + 1, y1 + 2, z1, z2, wallMaterial);
		for (int x = x1; x < x2; x++) {
			byte material = byteWindow;
			if (x == x1 || x == x2 - 1 || 
					(glassSkip == 1 && random.nextBoolean() || 
					(glassSkip > 1 && x % glassSkip == 0)))
				material = wallMaterial;
			chunk.setBlocks(x, x + 1, y1 + 2, y2, z1, z2, material);
		}
	}
	
	private void generateEWWall(ByteChunk chunk, Random random, int x1, int x2, int y1, int y2, int z1, int z2, byte wallMaterial, int glassSkip) {
		chunk.setBlocks(x1, x2, y1 + 1, y1 + 2, z1, z2, wallMaterial);
		for (int z = z1; z < z2; z++) {
			byte material = byteWindow;
			if (z == z1 || z == z2 - 1 || 
					(glassSkip == 1 && random.nextBoolean() || 
					(glassSkip > 1 && z % glassSkip == 0)))
				material = wallMaterial;
			chunk.setBlocks(x1, x2, y1 + 2, y2, z, z + 1, material);
		}
	}
	
	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, firstFloorY, blockZ, RoadGenerator.byteSidewalk);
		return firstFloorY;
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return firstFloorY;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return Material.STONE;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return noise.isUrban(chunkX, chunkZ) && !noise.isGreenBelt(chunkX, chunkZ);
	}
	
	public int getUrbanHeight(int x, int z) {
		double devianceAmount = (noiseHeightDeviance.noise(x / xHeightFactor, z / zHeightFactor) + 1) / 2 * floorDeviance;
		return Math.max(1, NoiseGenerator.floor(noise.getUrbanLevel(x, z) * noise.getMaximumFloors() - devianceAmount));
	}
	
	// connected buildings
	// wall material 
	// window material
	// floor material
	// roof treatment
	// room layout
	// furniture treatment
	// crop material
	//TODO Unfinished building (wall and floor material?)
	//TODO Inset Walls NS/EW
	//TODO Inset Floors NS/EW
	
	public byte getWallMaterial(int chunkX, int chunkZ) {
		switch(randomFeatureAt(chunkX, chunkZ, featureWallMaterial, 4)) {
		case 1:
			return (byte) Material.BRICK.getId();
		case 2:
			return (byte) Material.SAND.getId();
		case 3:
			return (byte) Material.CLAY.getId();
		default:
			return (byte) Material.SMOOTH_BRICK.getId();
		}
	}
	
	public byte getFloorMaterial(int chunkX, int chunkZ) {
		switch(randomFeatureAt(chunkX, chunkZ, featureFloorMaterial, 3)) {
		case 1:
			return (byte) Material.WOOD.getId();
		case 2:
			return (byte) Material.WOOL.getId();
		default:
			return (byte) Material.SMOOTH_BRICK.getId();
		}
	}
	
	public byte getRoofMaterial(int chunkX, int chunkZ) {
		switch(randomFeatureAt(chunkX, chunkZ, featureRoofMaterial, 4)) {
		case 1:
			return (byte) Material.WOOD.getId();
		case 2:
			return (byte) Material.COBBLESTONE.getId();
		case 3:
			return (byte) Material.SANDSTONE.getId();
		default:
			return (byte) Material.SMOOTH_BRICK.getId();
		}
	}
	
}
