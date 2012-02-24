package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class UrbanGenerator extends PlatGenerator {

	private final static double xUrbanFactor = 30.0;
	private final static double zUrbanFactor = 30.0;
	private final static double threshholdUrban = 0.50;
	
	private final static double xMaterialFactor = 5.0;
	private final static double zMaterialFactor = 5.0;
	
	private final static double xHeightFactor = 2.0;
	private final static double zHeightFactor = 2.0;

	public final static double floorDeviance = 4.0;
	private int firstFloorY;
	
	private SimplexNoiseGenerator noiseUrban;
	private SimplexNoiseGenerator noiseFeature; // which building material set to use (if an adjacent chunk is similar then join them)
	private SimplexNoiseGenerator noiseHeightDeviance; // add/subtract a little from the normal height for this building
	
	public UrbanGenerator(Generator noise) {
		super(noise);
		
		noiseUrban = new SimplexNoiseGenerator(noise.getNextSeed());
		noiseFeature = new SimplexNoiseGenerator(noise.getNextSeed());
		noiseHeightDeviance = new SimplexNoiseGenerator(noise.getNextSeed());
		
		firstFloorY = noise.getStreetLevel() + 1;
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(firstFloorY, RoadGenerator.byteSidewalk);
		
		int floors = getUrbanHeight(chunkX, chunkZ);
		byte wallMaterial = getWallMaterial(chunkX, chunkZ);
		byte floorMaterial = getFloorMaterial(chunkX, chunkZ);
		
		for (int i = 0; i < floors; i++) {
			int y = i * Generator.floorHeight;
			chunk.setBlocks(1, 15, firstFloorY + y, firstFloorY + y + 1, 1, 15, floorMaterial);
			chunk.setBlocks(1, 15, firstFloorY + y + 1, firstFloorY + y + Generator.floorHeight, 1, 15, wallMaterial);
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
		double noiseLevel = (noiseUrban.noise(chunkX / xUrbanFactor, chunkZ / zUrbanFactor) + 1) / 2;
		return noiseLevel > threshholdUrban;
	}
	
	public int getUrbanHeight(int x, int z) {
		double urbanLevel = (((noiseUrban.noise(x / xUrbanFactor, z / zUrbanFactor) + 1) / 2) - threshholdUrban) / (1 - threshholdUrban);
		double devianceAmount = (noiseHeightDeviance.noise(x / xHeightFactor, z / zHeightFactor) + 1) / 2 * floorDeviance;
		return Math.max(1, NoiseGenerator.floor(urbanLevel * noise.getMaximumFloors() - devianceAmount));
	}
	
	//TODO change this to an enum
	private final static int featureWallMaterial = 0;
	private final static int featureFloorMaterial = 1;
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
	
	private byte getWallMaterial(int chunkX, int chunkZ) {
		switch(randomFeatureAt(chunkX, chunkZ, featureWallMaterial, 5)) {
		case 1:
			return (byte) Material.BRICK.getId();
		case 2:
			return (byte) Material.COBBLESTONE.getId();
		case 3:
			return (byte) Material.SAND.getId();
		case 4:
			return (byte) Material.CLAY.getId();
		default:
			return (byte) Material.SMOOTH_BRICK.getId();
		}
	}
	
	private byte getFloorMaterial(int chunkX, int chunkZ) {
		switch(randomFeatureAt(chunkX, chunkZ, featureFloorMaterial, 4)) {
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
	
	public int randomFeatureAt(int chunkX, int chunkZ, int slot, int range) {
		return NoiseGenerator.floor((noiseFeature.noise(chunkX / xMaterialFactor, slot, chunkZ / zMaterialFactor) + 1) / 2 * range);
	}

}
