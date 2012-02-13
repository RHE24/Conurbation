package me.daddychurchill.Conurbation.Support;

import java.util.Random;

import me.daddychurchill.Conurbation.WorldConfig;
import me.daddychurchill.Conurbation.Plats.GroundGenerator;
import me.daddychurchill.Conurbation.Plats.LakeGenerator;
import me.daddychurchill.Conurbation.Plats.PlatGenerator;
import me.daddychurchill.Conurbation.Plats.RiverGenerator;
import me.daddychurchill.Conurbation.Plats.RoadGenerator;
import me.daddychurchill.Conurbation.Plats.RuralGenerator;
import me.daddychurchill.Conurbation.Plats.SuburbanGenerator;
import me.daddychurchill.Conurbation.Plats.UrbanGenerator;

import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class Generator {
	private World world;
	private WorldConfig config;
	
	public static int floorHeight = 4;
	private static double xUrbanFactor = 30.0;
	private static double zUrbanFactor = 30.0;
	private static double threshholdUrban = 0.50;
	private static double xRuralFactor = 30.0;
	private static double zRuralFactor = 30.0;
	private static double threshholdRural = 0.50;
	
	private static int roadCellSize = 4;
	private static double xIntersectionFactor = 6;
	private static double zIntersectionFactor = 6;
	private static double threshholdRoad = 0.75;
	private static double threshholdBridge = 1.20;
	private static double threshholdBridgeLength = 0.10;
	
	private static double xHeightFactor = 20.0;
	private static double zHeightFactor = 20.0;
	private static double maxHeight = 5;
	private static double xUnfinishedFactor = xHeightFactor / 10;
	private static double zUnfinishedFactor = zHeightFactor / 10;
	private static double threshholdUnfinished = 0.15;
	
	private static double xWaterFactor = 40.0;
	private static double zWaterFactor = 40.0;
	private static double threshholdWater = 0.3;
	private static double xRiverFactor = 40.0;
	private static double zRiverFactor = 40.0;
	private static double threshholdMinRiver = 0.40;
	private static double threshholdMaxRiver = 0.50;
	
	private SimplexNoiseGenerator noiseUrban;
	private SimplexNoiseGenerator noiseRural;
	private SimplexNoiseGenerator noiseUnfinished;
	private SimplexNoiseGenerator noiseRiver;
	private SimplexNoiseGenerator noiseWater;
	private SimplexNoiseGenerator noiseIntersection;
	
	private SimplexNoiseGenerator noiseHeightDeviance; // add/subtract a little from the normal height for this building
//	private SimplexNoiseGenerator noiseUrbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseSuburbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseRuralMaterial; // which building/plat material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseBridgeStyle; // search to the previous (west or north) starting intersection and use it's location for generator
	
	private PlatGenerator generatorLake;
	private PlatGenerator generatorRiver;
	private PlatGenerator generatorGround;
	private PlatGenerator generatorRural;
	private PlatGenerator generatorSuburban;
	private PlatGenerator generatorUrban;
	private PlatGenerator generatorRoad;
//	private PlatGenerator generatorUnknown;
	
	public Generator(World world, WorldConfig config) {
		super();
		this.world = world;
		this.config = config;
		
		long seed = this.world.getSeed();
		noiseUrban = new SimplexNoiseGenerator(seed);
		noiseRural = new SimplexNoiseGenerator(seed + 1);
		noiseUnfinished = new SimplexNoiseGenerator(seed + 2);
		noiseWater = new SimplexNoiseGenerator(seed + 3);
		noiseRiver = new SimplexNoiseGenerator(seed + 4);
		noiseIntersection = new SimplexNoiseGenerator(seed + 5);
		noiseHeightDeviance = new SimplexNoiseGenerator(seed + 6);
		
		generatorLake = new LakeGenerator(this);
		generatorRiver = new RiverGenerator(this);
		generatorGround = new GroundGenerator(this);
		generatorRoad = new RoadGenerator(this);
		generatorUrban = new UrbanGenerator(this);
		generatorSuburban = new SuburbanGenerator(this);
		generatorRural = new RuralGenerator(this);
//		generatorUnknown = new UnknownGenerator(this);
	}
	
	public int getStreetLevel() {
		return config.getStreetLevel();
	}

	public int getSeabedLevel() {
		return config.getSeabedLevel();
	}
	
	public void Generate(ByteChunk byteChunk, Random random, int chunkX, int chunkZ) {
		if (isLake(chunkX, chunkZ))
			generatorLake.generateChunk(byteChunk, chunkX, chunkZ);
		else if (isRiver(chunkX, chunkZ))
			generatorRiver.generateChunk(byteChunk, chunkX, chunkZ);
		else 
			generatorGround.generateChunk(byteChunk, chunkX, chunkZ);

		if (isRoad(chunkX, chunkZ))
			generatorRoad.generateChunk(byteChunk, chunkX, chunkZ);
		else if (isBuildable(chunkX, chunkZ))
			if (isUrban(chunkX, chunkZ))
				generatorUrban.generateChunk(byteChunk, chunkX, chunkZ);
			else if (isRural(chunkX, chunkZ))
				generatorRural.generateChunk(byteChunk, chunkX, chunkZ);
			else //isSuburban
				generatorSuburban.generateChunk(byteChunk, chunkX, chunkZ);
//		else
//			
//			// all else fails
//			generatorUnknown.generateChunk(byteChunk, chunkX, chunkZ);
	}
	
	public void Populate(RealChunk realChunk, Random random, int chunkX, int chunkZ) {
		
	}
	
	public Neighbors getNeighbors(int x, int z) {
		return new Neighbors(this, x, z);
	}
	
	// connected buildings
	// wall material 
	// window material
	// floor material
	// roof treatment
	// room layout
	// furniture treatment
	// crop material
	// Sculpted terrain... gradual slopes, maybe key off of generatorWater but not stretch the value as much? 
	
	public boolean isLake(int x, int z) {
		double noiseLevel = (noiseWater.noise(x / xWaterFactor, z / zWaterFactor) + 1) / 2;
		return noiseLevel < threshholdWater;
	}

	public boolean isRiver(int x, int z) {
		double noiseLevel = (noiseRiver.noise(x / xRiverFactor, z / zRiverFactor) + 1) / 2;
		return noiseLevel > threshholdMinRiver && noiseLevel < threshholdMaxRiver && !isLake(x, z);
	}
	
	public boolean isWater(int x, int z) {
		return isLake(x, z) || isRiver(x, z);
	}
	
	public boolean isBuildable(int x, int z) {
		return !isWater(x, z) && !isRoad(x, z);
	}
	
	public boolean isUrban(int x, int z) {
		double noiseLevel = (noiseUrban.noise(x / xUrbanFactor, z / zUrbanFactor) + 1) / 2;
		return noiseLevel > threshholdUrban;
	}
	
	public boolean isSuburban(int x, int z) {
		return !isUrban(x, z) && !isRural(x, z);
	}
	
	public boolean isRural(int x, int z) {
		double noiseLevel = (noiseRural.noise(x / xRuralFactor, z / zRuralFactor) + 1) / 2;
		return noiseLevel > threshholdRural;
	}

	public int getUrbanHeight(int x, int z) {
		return (int) ((noiseHeightDeviance.noise(x / xHeightFactor, z / zHeightFactor) + 1) / 2 * maxHeight);
	}
	
	public boolean isUrbanUnfinished(int x, int z) {
		double noiseLevel = (noiseUnfinished.noise(x / xUnfinishedFactor, z / zUnfinishedFactor) + 1) / 2;
		return noiseLevel < threshholdUnfinished;
	}
	
	public boolean isRoad(int x, int z) {
		
		// quick test!
		boolean roadExists = x % roadCellSize == 0 || z % roadCellSize == 0;
		
		// not so quick test
		if (roadExists) {
			
			// is this an intersection?
			if (x % roadCellSize == 0 && z % roadCellSize == 0) {
				
				// if roads exists to any of the cardinal directions then we exist
				roadExists = isRoad(x - 1, z) ||
							 isRoad(x + 1, z) ||
							 isRoad(x, z - 1) ||
							 isRoad(x, z + 1);
				
			} else {
				
				// bridge?
				boolean isBridge = false;
				int previousX, previousZ, nextX, nextZ;
				double previousNoise, nextNoise, lengthNoise = 0.0;
				
				// north/south road?
				if (x % roadCellSize == 0) {
					
					// find previous intersection not in water
					previousX = x;
					previousZ = (z / roadCellSize) * roadCellSize;
					while (isWater(previousX, previousZ)) {
						previousZ -= roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for northward road
					if (isWater(previousX, previousZ + 1))
						previousNoise = 0.0;
					else
						previousNoise = getIntersectionNoise(previousX, previousZ + 1);
						
					// find next intersection not in water
					nextX = x;
					nextZ = ((z + roadCellSize) / roadCellSize) * roadCellSize;
					while (isWater(nextX, nextZ)) {
						nextZ += roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for southward road
					if (isWater(nextX, nextZ - 1))
						nextNoise = 0.0;
					else
						nextNoise = getIntersectionNoise(nextX, nextZ - 1);
					
				// east/west road?
				} else { // if (z % roadCellSize == 0)
	
					// find previous intersection not in water
					previousX = (x / roadCellSize) * roadCellSize;
					previousZ = z;
					while (isWater(previousX, previousZ)) {
						previousX -= roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for westward road
					if (isWater(previousX + 1, previousZ))
						previousNoise = 0.0;
					else
						previousNoise = getIntersectionNoise(previousX + 1, previousZ);
						
					// find next intersection not in water
					nextX = ((x + roadCellSize) / roadCellSize) * roadCellSize;
					nextZ = z;
					while (isWater(nextX, nextZ)) {
						nextX += roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for eastward road
					if (isWater(nextX - 1, nextZ))
						nextNoise = 0.0;
					else
						nextNoise = getIntersectionNoise(nextX - 1, nextZ);
				}
				
				// overwater?
				if (isBridge)
					roadExists = (previousNoise + nextNoise) > (threshholdBridge + lengthNoise); // longer bridges are "harder"
				else
					roadExists = (previousNoise + nextNoise) > threshholdRoad;
			}
		}
		
		// tell the world
		return roadExists;
	}
	
	private double getIntersectionNoise(int x, int z) {
		return (noiseIntersection.noise(x / xIntersectionFactor, z / zIntersectionFactor) + 1) / 2;
	}
}
