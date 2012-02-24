package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class GroundGenerator extends PlatGenerator {

	private int streetLevel;
	
	public GroundGenerator(Generator noise) {
		super(noise);

		streetLevel = noise.getStreetLevel();
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		
		chunk.setLayer(0, byteBedrock);
		chunk.setBlocks(0, 16, 1, streetLevel - 4, 0, 16, byteStone);
		chunk.setBlocks(0, 16, streetLevel - 4, streetLevel, 0, 16, byteDirt);
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlocks(blockX, 1, streetLevel - 4, blockZ, byteStone);
		chunk.setBlocks(blockX, streetLevel - 4, streetLevel, blockZ, byteDirt);
		return streetLevel - 1;
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return Material.DIRT;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
		return false;
	}

}
