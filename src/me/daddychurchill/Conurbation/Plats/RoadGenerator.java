package me.daddychurchill.Conurbation.Plats;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.Generator;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class RoadGenerator extends PlatGenerator {

	public RoadGenerator(Generator noise) {
		super(noise);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int streetLevel = noise.getStreetLevel();
		
		chunk.setLayer(streetLevel, byteStone);
//		chunk.setBlocks(0, 16, 1, streetLevel + 1, 0, 16, byteStone);
//		chunk.setBlocks(1, 15, streetLevel + 1, streetLevel + floors * floorHeight + 1, 1, 15, stuff);
	}

	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
