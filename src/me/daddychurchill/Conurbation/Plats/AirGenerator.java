package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

import org.bukkit.Material;

public class AirGenerator extends PlatGenerator {

	private int streetLevel;
	
	public AirGenerator(Generator noise) {
		super(noise);

		streetLevel = noise.getStreetLevel();
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		// pretty much if there isn't something else then it must be us!
		return true;
	}

	@Override
	public boolean isCompatibleEdgeChunk(PlatGenerator generator) {
		return true;
	}
	
	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		// Nothing to do
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// Nothing to do
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		return streetLevel;
	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return streetLevel;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
		return Material.AIR;
	}

}
