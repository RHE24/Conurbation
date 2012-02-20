package me.daddychurchill.Conurbation.Plats;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class RuralGenerator extends PlatGenerator {

	private Material dirt = Material.DIRT;
	private byte byteDirt = (byte) dirt.getId();
	int groundLevel;
	
	public final static double xRuralFactor = 30.0;
	public final static double zRuralFactor = 30.0;
	public final static double threshholdRural = 0.50;
	
	private SimplexNoiseGenerator noiseRural;
	
	public RuralGenerator(Generator noise) {
		super(noise);
		
		noiseRural = new SimplexNoiseGenerator(noise.getNextSeed());
		groundLevel = noise.getStreetLevel();
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		chunk.setLayer(0, byteBedrock);
		chunk.setBlocks(0, 16, 1, groundLevel, 0, 16, byteStone);
		chunk.setBlocks(0, 16, groundLevel, groundLevel + 1, 0, 16, byteDirt);
	}

	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return noise.getStreetLevel() + 1;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return dirt;
	}

	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		double noiseLevel = (noiseRural.noise(chunkX / xRuralFactor, chunkX / zRuralFactor) + 1) / 2;
		return noiseLevel > threshholdRural;
	}

}
