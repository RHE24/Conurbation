package me.daddychurchill.Conurbation.Plats;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.Generator;
import me.daddychurchill.Conurbation.Support.RealChunk;

public abstract class PlatGenerator {
	
	protected Generator noise;
	public PlatGenerator(Generator noise) {
		super();
		this.noise = noise;
	}

	// what you need to do
	public abstract void generateChunk(ByteChunk chunk, int chunkX, int chunkZ);
	public abstract void populateChunk(RealChunk chunk, int chunkX, int chunkZ);
	
	// some reasonable globals
	protected static byte byteBedrock = (byte) Material.BEDROCK.getId();
	protected static byte byteWater = (byte) Material.STATIONARY_WATER.getId();
	protected static byte byteStone = (byte) Material.STONE.getId();
	protected static byte byteDirt = (byte) Material.DIRT.getId();
	protected static byte byteGrass = (byte) Material.GRASS.getId();
	protected static byte byteIron = (byte) Material.IRON_BLOCK.getId();
	protected static byte byteGlass = (byte) Material.GLASS.getId();
	
	protected static byte byteUrban = byteIron;
	protected static byte byteSuburban = (byte) Material.SANDSTONE.getId();
	protected static byte byteRural = byteGrass;
}
