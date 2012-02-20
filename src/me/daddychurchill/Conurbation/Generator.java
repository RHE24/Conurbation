package me.daddychurchill.Conurbation;

import java.util.Random;

import me.daddychurchill.Conurbation.Plats.GroundGenerator;
import me.daddychurchill.Conurbation.Plats.LakeGenerator;
import me.daddychurchill.Conurbation.Plats.PlatGenerator;
import me.daddychurchill.Conurbation.Plats.RiverGenerator;
import me.daddychurchill.Conurbation.Plats.RoadGenerator;
import me.daddychurchill.Conurbation.Plats.RuralGenerator;
import me.daddychurchill.Conurbation.Plats.SuburbanGenerator;
import me.daddychurchill.Conurbation.Plats.UnknownGenerator;
import me.daddychurchill.Conurbation.Plats.UrbanGenerator;
import me.daddychurchill.Conurbation.Plats.WaterGenerator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Material;
import org.bukkit.World;

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
	public PlatGenerator generatorRural;
	public PlatGenerator generatorSuburban;
	public PlatGenerator generatorUrban;
	public PlatGenerator generatorRoad;
	public PlatGenerator generatorUnknown;
	
	private int streetLevel;
	private int seabedLevel;
	private int maximumFloors;

	public final static int floorHeight = 4;
	public final static int floorsReserved = 2;
	
	public Generator(World world, WorldConfig config) {
		super();
		this.world = world;
		this.config = config;
		
		streetLevel = this.config.getStreetLevel();
		seabedLevel = this.config.getSeabedLevel();
		if (streetLevel < 0)
			streetLevel = world.getSeaLevel() + WaterGenerator.shoreHeight;
		if (seabedLevel < 0)
			seabedLevel = streetLevel / 2;
		streetLevel = rangeCheck(streetLevel, world.getMaxHeight());
		seabedLevel = rangeCheck(seabedLevel, streetLevel);
		maximumFloors = (world.getMaxHeight() - streetLevel) / floorHeight - floorsReserved;
				
		generatorLake = new LakeGenerator(this);
		generatorRiver = new RiverGenerator(this, generatorLake);
		generatorGround = new GroundGenerator(this);
		generatorRoad = new RoadGenerator(this);
		generatorUrban = new UrbanGenerator(this);
		generatorSuburban = new SuburbanGenerator(this);
		generatorRural = new RuralGenerator(this);
		generatorUnknown = new UnknownGenerator(this);
	}
	
	private int rangeCheck(int i, int extreme) {
		int min = extreme / 4;
		int max = extreme * 3;
		return Math.max(min, Math.min(i, max));
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
	
	public void generate(ByteChunk byteChunk, Random random, int chunkX, int chunkZ) {
		
		if (isLake(chunkX, chunkZ))
			generatorLake.generateChunk(byteChunk, random, chunkX, chunkZ);
		else if (isRiver(chunkX, chunkZ))
			generatorRiver.generateChunk(byteChunk, random, chunkX, chunkZ);
		else 
			generatorGround.generateChunk(byteChunk, random, chunkX, chunkZ);

		if (isRoad(chunkX, chunkZ))
			generatorRoad.generateChunk(byteChunk, random, chunkX, chunkZ);
		else if (isBuildable(chunkX, chunkZ))
			if (isUrban(chunkX, chunkZ))
				generatorUrban.generateChunk(byteChunk, random, chunkX, chunkZ);
			else if (isRural(chunkX, chunkZ))
				generatorRural.generateChunk(byteChunk, random, chunkX, chunkZ);
			else //isSuburban
				generatorSuburban.generateChunk(byteChunk, random, chunkX, chunkZ);
		
//
//		// all else fails
//		else
//			generatorUnknown.generateChunk(byteChunk, random, chunkX, chunkZ);
	}
	
	public void populate(RealChunk realChunk, Random random, int chunkX, int chunkZ) {
		
	}
	
	public Material getTopMaterial(int chunkX, int chunkZ) {
		if (isRoad(chunkX, chunkZ))
			return generatorRoad.getGroundSurfaceMaterial(chunkX, chunkZ);
		else if (isBuildable(chunkX, chunkZ))
			if (isUrban(chunkX, chunkZ))
				return generatorUrban.getGroundSurfaceMaterial(chunkX, chunkZ);
			else if (isRural(chunkX, chunkZ))
				return generatorRural.getGroundSurfaceMaterial(chunkX, chunkZ);
			else //isSuburban
				return generatorSuburban.getGroundSurfaceMaterial(chunkX, chunkZ);

		// all else fails
		else
			return generatorUnknown.getGroundSurfaceMaterial(chunkX, chunkZ);
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
	
	public boolean isUrban(int x, int z) {
		return generatorUrban.isChunk(x, z);
	}
	
	public boolean isUrbanBuilding(int x, int z) {
		return isBuildable(x, z) && isUrban(x, z);
	}
	
	public boolean isSuburban(int x, int z) {
		return generatorSuburban.isChunk(x, z);
	}
	
	public boolean isRural(int x, int z) {
		return generatorRural.isChunk(x, z);
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
