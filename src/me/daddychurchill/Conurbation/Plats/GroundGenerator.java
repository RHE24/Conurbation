package me.daddychurchill.Conurbation.Plats;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.Generator;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class GroundGenerator extends PlatGenerator {

	public GroundGenerator(Generator noise) {
		super(noise);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int streetLevel = noise.getStreetLevel();
		
		chunk.setLayer(0, byteBedrock);
		chunk.setBlocks(0, 16, 1, streetLevel - 4, 0, 16, byteStone);
		chunk.setBlocks(0, 16, streetLevel - 4, streetLevel, 0, 16, byteDirt);
	}

	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
