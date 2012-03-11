package me.daddychurchill.Conurbation;

import java.util.Random;

import me.daddychurchill.Conurbation.Plats.AirGenerator;
import me.daddychurchill.Conurbation.Plats.EstateGenerator;
import me.daddychurchill.Conurbation.Plats.FarmGenerator;
import me.daddychurchill.Conurbation.Plats.GroundGenerator;
import me.daddychurchill.Conurbation.Plats.LakeGenerator;
import me.daddychurchill.Conurbation.Plats.ParkGenerator;
import me.daddychurchill.Conurbation.Plats.PlatGenerator;
import me.daddychurchill.Conurbation.Plats.RiverGenerator;
import me.daddychurchill.Conurbation.Plats.RoadGenerator;
import me.daddychurchill.Conurbation.Plats.ForestGenerator;
import me.daddychurchill.Conurbation.Plats.NeighborhoodGenerator;
import me.daddychurchill.Conurbation.Plats.UnknownGenerator;
import me.daddychurchill.Conurbation.Plats.CityGenerator;
import me.daddychurchill.Conurbation.Plats.WaterGenerator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class Generator {
	private World world;
	private WorldConfig config;
	
//	public SimplexNoiseGenerator noiseUrbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseSuburbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseRuralMaterial; // which building/plat material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseBridgeStyle; // search to the previous (west or north) starting intersection and use it's location for generator
	
	public PlatGenerator generatorLake;
	public PlatGenerator generatorRiver;
	public PlatGenerator generatorGround;
	public PlatGenerator generatorParks;
	public PlatGenerator generatorEstates;
	public PlatGenerator generatorForests;
	public PlatGenerator generatorCities;
	public PlatGenerator generatorNeighborhoods;
	public PlatGenerator generatorFarms;
	public PlatGenerator generatorRoad;
	public PlatGenerator generatorAir;
	public PlatGenerator generatorUnknown;
	
	private final static double xUrbanizationFactor = 30.0;
	private final static double zUrbanizationFactor = 30.0;
	private final static double threshholdUrban = 0.60;
	private final static double threshholdSuburban = 0.35;
	private final static double xGreenBeltFactor = 30.0;
	private final static double zGreenBeltFactor = 30.0;
	private final static double threshholdGreenBeltMin = 0.40;
	private final static double threshholdGreenBeltMax = 0.50;
	private SimplexNoiseGenerator noiseUrbanization;
	private SimplexNoiseGenerator noiseGreenBelt;
	
	private int maximumLevel;
	private int streetLevel;
	private int seabedLevel;
	private int maximumFloors;
	private double decrepitLevel;
	private boolean includeAgricultureZones;
	private boolean includeResidentialZones;
	private boolean includeUrbanZones;

	public final static int floorHeight = 4;
	public final static int floorsReserved = 2;
	
	public Generator(World aWorld, WorldConfig aConfig) {
		super();
		world = aWorld;
		config = aConfig;
		
		noiseUrbanization = new SimplexNoiseGenerator(getNextSeed());
		noiseGreenBelt = new SimplexNoiseGenerator(getNextSeed());
		
		streetLevel = config.getStreetLevel();
		seabedLevel = config.getSeabedLevel();
		decrepitLevel = config.getDecrepitLevel();
		includeAgricultureZones = config.getIncludeAgricultureZones();
		includeResidentialZones = config.getIncludeResidentialZones();
		includeUrbanZones = config.getIncludeUrbanZones();
		
		maximumLevel = aWorld.getMaxHeight() - 1;
		if (streetLevel < 0)
			streetLevel = aWorld.getSeaLevel() + WaterGenerator.shoreHeight;
		if (seabedLevel < 0)
			seabedLevel = streetLevel / 2;
		
		streetLevel = rangeCheck(streetLevel, maximumLevel);
		seabedLevel = rangeCheck(seabedLevel, streetLevel);
		maximumLevel = Math.min(maximumLevel, (config.getMaximumFloors() + floorsReserved) * floorHeight + streetLevel);
		maximumFloors = (maximumLevel - streetLevel) / floorHeight - floorsReserved;
				
		generatorLake = new LakeGenerator(this);
		generatorRiver = new RiverGenerator(this, generatorLake);
		generatorGround = new GroundGenerator(this);
		generatorParks = new ParkGenerator(this);
		generatorEstates = new EstateGenerator(this);
		generatorForests = new ForestGenerator(this);
		generatorCities = new CityGenerator(this);
		generatorNeighborhoods = new NeighborhoodGenerator(this);
		generatorFarms = new FarmGenerator(this);
		generatorRoad = new RoadGenerator(this);
		generatorAir = new AirGenerator(this);
		generatorUnknown = new UnknownGenerator(this);
	}
	
	public byte debugMaterialFor(PlatGenerator generator) {
		if (generator == generatorLake)
			return (byte) Material.LAPIS_BLOCK.getId();
		else if (generator == generatorRiver)
			return (byte) Material.LAPIS_ORE.getId();
		else if (generator == generatorGround)
			return (byte) Material.DIRT.getId();
		else if (generator == generatorParks)
			return (byte) Material.MYCEL.getId();
		else if (generator == generatorEstates)
			return (byte) Material.BRICK.getId();
		else if (generator == generatorForests)
			return (byte) Material.LAPIS_BLOCK.getId();
		else if (generator == generatorCities)
			return (byte) Material.IRON_BLOCK.getId();
		else if (generator == generatorNeighborhoods)
			return (byte) Material.COBBLESTONE.getId();
		else if (generator == generatorFarms)
			return (byte) Material.DIAMOND_BLOCK.getId();
		else if (generator == generatorRoad)
			return (byte) Material.COAL_ORE.getId();
		else if (generator == generatorAir)
			return (byte) Material.GLASS.getId();
		else if (generator == generatorUnknown)
			return (byte) Material.OBSIDIAN.getId();
		else
			return (byte) Material.BEDROCK.getId();
	}
	
	private int rangeCheck(int i, int extreme) {
		int min = extreme / 4;
		int max = extreme * 3;
		return Math.max(min, Math.min(i, max));
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getMaximumLevel() {
		return maximumLevel;
	}
	
	public int getStreetLevel() {
		return streetLevel;
	}

	public int getSeabedLevel() {
		return seabedLevel;
	}
	
	public int getMaximumFloors() {
		return maximumFloors;
	}
	
	public boolean isDecrepit(Random random) {
		return random.nextDouble() < decrepitLevel;
	}
	
	public void generate(ByteChunk byteChunk, Random random, int chunkX, int chunkZ) {
		
		// find the bottom one
		getBottomPlatGenerator(chunkX, chunkZ).generateChunk(byteChunk, random, chunkX, chunkZ);

		// find the top one
		PlatGenerator generator = getTopPlatGenerator(chunkX, chunkZ);
		if (generator != generatorUnknown)
			generator.generateChunk(byteChunk, random, chunkX, chunkZ);
	}
	
	public void populate(RealChunk realChunk, Random random, int chunkX, int chunkZ) {
		
		// find the bottom one
		getBottomPlatGenerator(chunkX, chunkZ).populateChunk(realChunk, random, chunkX, chunkZ);

		// find the top one
		PlatGenerator generator = getTopPlatGenerator(chunkX, chunkZ);
		if (generator != generatorUnknown)
			generator.populateChunk(realChunk, random, chunkX, chunkZ);
	}
	
	public PlatGenerator getBottomPlatGenerator(int chunkX, int chunkZ) {
		if (isLake(chunkX, chunkZ))
			return generatorLake;
		else if (isRiver(chunkX, chunkZ))
			return generatorRiver;
		else 
			return generatorGround;
	}
	
	public boolean ifBottomPlatGenerator(int chunkX, int chunkZ, PlatGenerator generator) {
		return getBottomPlatGenerator(chunkX, chunkZ) == generator;
	}
	
	public PlatGenerator getTopPlatGenerator(int chunkX, int chunkZ) {
		if (isRoad(chunkX, chunkZ))
			return generatorRoad;
		else if (isBuildable(chunkX, chunkZ))
			if (isGreenBelt(chunkX, chunkZ)) {
				if (isUrban(chunkX, chunkZ))
					if (includeUrbanZones)
						return generatorParks;
					else
						return generatorForests;
				else if (isSuburban(chunkX, chunkZ))
					if (includeResidentialZones)
						return generatorEstates;
					else
						return generatorForests;
				else if (isRural(chunkX, chunkZ))
					return generatorForests;
			} else {
				if (isUrban(chunkX, chunkZ))
					if (includeUrbanZones)
						return generatorCities;
					else
						return generatorForests;
				else if (isSuburban(chunkX, chunkZ))
					if (includeResidentialZones)
						return generatorNeighborhoods;
					else
						return generatorForests;
				else if (isRural(chunkX, chunkZ))
					if (includeAgricultureZones)
						return generatorFarms;
					else
						return generatorForests;
			}
		
		// all else fails
		return generatorAir;
	}
	
	public boolean ifTopPlatGenerator(int chunkX, int chunkZ, PlatGenerator generator) {
		return getTopPlatGenerator(chunkX, chunkZ) == generator;
	}
	
	public boolean isCompatibleGenerator(int chunkX, int chunkZ, PlatGenerator generator) {
		return getTopPlatGenerator(chunkX, chunkZ).isCompatibleEdgeChunk(generator);
	}
	
	public PlatGenerator getNeighboringPlatGenerator(int chunkX, int chunkZ, int deltaX, int deltaZ) {
		
		// we only want roads if we are on a road
		PlatGenerator generator = getTopPlatGenerator(chunkX, chunkZ);
		if (generator != generatorRoad) {
			
			// otherwise try the two neighbors
			generator = getTopPlatGenerator(chunkX + deltaX, chunkZ);
			if (generator == generatorRoad || generator == generatorAir)
				generator = getTopPlatGenerator(chunkX, chunkZ + deltaZ);
		}
		return generator;
	}
	
	public Material getTopMaterial(int chunkX, int chunkZ) {
		return getTopPlatGenerator(chunkX, chunkZ).getGroundSurfaceMaterial(chunkX, chunkZ);
	}
	
	public boolean isLake(int x, int z) {
		return generatorLake.isChunk(x, z);
	}

	public boolean isRiver(int x, int z) {
		return generatorRiver.isChunk(x, z);
	}
	
	public boolean isDelta(int x, int z) {
		return isRiver(x, z) && (isLake(x - 1, z) ||
								 isLake(x + 1, z) ||
								 isLake(x, z - 1) ||
								 isLake(x, z + 1));
	}
	
	public boolean isWater(int x, int z) {
		return isLake(x, z) || isRiver(x, z);
	}
	
	public boolean isBuildable(int x, int z) {
		return !isWater(x, z) && !isRoad(x, z);
	}
	
	public boolean isGreenBelt(int x, int z) {
		double noiseLevel = (noiseGreenBelt.noise(x / xGreenBeltFactor, z / zGreenBeltFactor) + 1) / 2;
		return noiseLevel > threshholdGreenBeltMin && noiseLevel < threshholdGreenBeltMax;
	}

	public boolean isUrban(int x, int z) {
		double noiseLevel = (noiseUrbanization.noise(x / xUrbanizationFactor, z / zUrbanizationFactor) + 1) / 2;
		return noiseLevel >= threshholdUrban;
	}
	
	public boolean isSuburban(int x, int z) {
		double noiseLevel = (noiseUrbanization.noise(x / xUrbanizationFactor, z / zUrbanizationFactor) + 1) / 2;
		return noiseLevel >= threshholdSuburban && noiseLevel < threshholdUrban;
	}
	
	public boolean isRural(int x, int z) {
		double noiseLevel = (noiseUrbanization.noise(x / xUrbanizationFactor, z / zUrbanizationFactor) + 1) / 2;
		return noiseLevel < threshholdSuburban;
	}
	
	public boolean isUrbanBuilding(int x, int z) {
		return isUrban(x, z) && isBuildable(x, z);
	}
	
	public double getUrbanLevel(int x, int z) {
		return (((noiseUrbanization.noise(x / xUrbanizationFactor, z / zUrbanizationFactor) + 1) / 2) - threshholdUrban) / (1 - threshholdUrban);
	}
	
	public boolean isRoad(int x, int z) {
		return generatorRoad.isChunk(x, z);
	}
	
	// This will return a unique random seed that is closely related to the world seed
	private int seedInc = 0;
	private long seedWorld;
	public long getNextSeed() {
		if (seedInc == 0)
			seedWorld = world.getSeed();
		seedInc++;
		return seedWorld + seedInc;
	}
	
	
}
