package me.daddychurchill.Conurbation.Plats;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.Generator;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class RiverGenerator extends WaterGenerator {

	public RiverGenerator(Generator noise) {
		super(noise);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int seabedLevel = noise.getSeabedLevel();
		int streetLevel = noise.getStreetLevel();
		
		chunk.setLayer(0, byteBedrock);
		
		chunk.setBlocks(0, 16, 1, seabedLevel + 1, 0, 16, byteStone);
		chunk.setBlocks(0, 16, seabedLevel, streetLevel - shoreHeight, 0, 16, byteWater);
	}

	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
