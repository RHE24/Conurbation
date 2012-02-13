package me.daddychurchill.Conurbation.Plats;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.Generator;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class UrbanGenerator extends PlatGenerator {

	public UrbanGenerator(Generator noise) {
		super(noise);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int streetLevel = noise.getStreetLevel();
		
		chunk.setLayer(0, byteBedrock);
		chunk.setBlocks(0, 16, 1, streetLevel + 1, 0, 16, byteStone);
		chunk.setBlocks(1, 15, streetLevel + 1, streetLevel + 5 * Generator.floorHeight + 1, 1, 15, byteIron);
//		chunk.setBlocks(1, 15, streetLevel + 1, streetLevel + floors * floorHeight + 1, 1, 15, stuff);
	}

	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
