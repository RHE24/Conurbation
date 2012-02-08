package me.daddychurchill.Conurbation.Plats;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.NoiseMakers;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class WaterGenerator extends PlatGenerator {

	public WaterGenerator(Generator context, NoiseMakers noise) {
		super(context, noise);
	}
	

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int seabedLevel = context.getSeabedLevel();
		int streetLevel = context.getStreetLevel();
		
		chunk.setLayer(0, byteBedrock);
		
		chunk.setBlocks(0, 16, 1, seabedLevel + 1, 0, 16, byteStone);
		chunk.setBlocks(0, 16, seabedLevel, streetLevel, 0, 16, byteWater);
	}

	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
