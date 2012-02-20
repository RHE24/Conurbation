package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public abstract class PlatGenerator {
	
	public Generator noise;
	public PlatGenerator(Generator noise) {
		super();
		this.noise = noise;
	}

	// what you need to do
	public abstract void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ);
	public abstract void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ);
	public abstract int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ);
	public abstract Material getGroundSurfaceMaterial(int chunkX, int chunkZ);
	public abstract boolean isChunk(int chunkX, int chunkZ);
	
	// some reasonable globals
	protected final static byte byteBedrock = (byte) Material.BEDROCK.getId();
	protected final static byte byteWater = (byte) Material.STATIONARY_WATER.getId();
	protected final static byte byteStone = (byte) Material.STONE.getId();
	protected final static byte byteDirt = (byte) Material.DIRT.getId();
	protected final static byte byteGrass = (byte) Material.GRASS.getId();
	protected final static byte byteIron = (byte) Material.IRON_BLOCK.getId();
	protected final static byte byteGlass = (byte) Material.GLASS.getId();
	
	protected double calcBlock(double chunkI, double i) {
		return chunkI + (i / 16.0);
	}

	protected Long getChunkKey(int chunkX, int chunkZ) {
		return Long.valueOf((long) chunkX * (long) Integer.MAX_VALUE + (long) chunkZ);
	}
}
