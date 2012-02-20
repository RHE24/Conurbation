package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Conurbation;
import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class UnknownGenerator extends PlatGenerator {

	private int streetLevel;
	
	public UnknownGenerator(Generator noise) {
		super(noise);

		streetLevel = noise.getStreetLevel();
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		
		chunk.setLayer(0, byteBedrock);
		chunk.setBlocks(0, 16, 1, streetLevel, 0, 16, byteStone);
		chunk.setBlocks(0, 16, streetLevel, streetLevel + 1, 0, 16, byteGlass);
		Conurbation.log.info("### UNKNOWN @ " + chunkX + ", " + chunkZ);
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return streetLevel;
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
