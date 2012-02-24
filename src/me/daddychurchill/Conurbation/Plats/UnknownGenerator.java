package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class UnknownGenerator extends PlatGenerator {

	private int skyLevel;

	public UnknownGenerator(Generator noise) {
		super(noise);

		skyLevel = noise.getMaximumLevel();
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		
		chunk.setBlocks(0, 16, skyLevel, skyLevel + 1, 0, 16, byteGlass);
//		Conurbation.log.info("### UNKNOWN @ " + chunkX + ", " + chunkZ);
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, skyLevel, blockZ, byteGlass);
		return skyLevel;
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return skyLevel;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return Material.GLASS;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
		return false;
	}

}
