package me.daddychurchill.Conurbation.Plats;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Conurbation;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public abstract class PlatGenerator {

	// what you need to do
	public abstract void generateChunk(Conurbation plugin, ByteChunk chunk, int chunkX, int chunkZ);
	public abstract void generateBlocks(Conurbation plugin, RealChunk chunk, int chunkX, int chunkZ);
	
	// some reasonable globals
	protected static byte byteBedrock = (byte) Material.BEDROCK.getId();
	protected static byte byteWater = (byte) Material.STATIONARY_WATER.getId();
	protected static byte byteStone = (byte) Material.STONE.getId();
	protected static byte byteDirt = (byte) Material.DIRT.getId();
	protected static byte byteGrass = (byte) Material.GRASS.getId();
}
