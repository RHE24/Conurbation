package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class SuburbanGenerator extends PlatGenerator {

	public final static byte byteGround = (byte) Material.SANDSTONE.getId();
	int groundLevel;
	
	public SuburbanGenerator(Generator noise) {
		super(noise);

		groundLevel = noise.getStreetLevel();
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setBlocks(0, 16, groundLevel, groundLevel + 1, 0, 16, byteGround);
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, groundLevel, blockZ, byteGround);
		return groundLevel;
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		// TODO Auto-generated method stub
		return groundLevel;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return Material.SANDSTONE;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		return !noise.isUrban(chunkX, chunkZ) && !noise.isRural(chunkX, chunkZ);
	}

}
