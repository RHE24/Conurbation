package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Material;

public class EstateGenerator extends PlatGenerator {

	public final static Material matGround = Material.GOLD_BLOCK;
	public final static byte byteGround = (byte) matGround.getId();
	int groundLevel;
	
	public EstateGenerator(Generator noise) {
		super(noise);

		groundLevel = noise.getStreetLevel();
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return noise.isSuburban(chunkX, chunkZ) && noise.isGreenBelt(chunkX, chunkZ);
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(groundLevel, byteGround);
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, groundLevel, blockZ, byteGround);
		return groundLevel;
	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return groundLevel;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return matGround;
	}

}
