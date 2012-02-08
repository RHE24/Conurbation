package me.daddychurchill.Conurbation.Plats;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Conurbation;
import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.NoiseMakers;
import me.daddychurchill.Conurbation.Support.RealChunk;

public abstract class PlatGenerator {
	
	protected Generator context;
	protected NoiseMakers noise;
	public PlatGenerator(Generator context, NoiseMakers noise) {
		super();
		this.context = context;
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
}
