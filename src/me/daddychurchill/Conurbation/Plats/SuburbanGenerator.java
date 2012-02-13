package me.daddychurchill.Conurbation.Plats;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.Generator;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class SuburbanGenerator extends PlatGenerator {

	public SuburbanGenerator(Generator noise) {
		super(noise);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int streetLevel = noise.getStreetLevel();
		
		chunk.setLayer(0, byteBedrock);
		chunk.setBlocks(0, 16, 1, streetLevel, 0, 16, byteStone);
		chunk.setBlocks(0, 16, streetLevel, streetLevel + 1, 0, 16, byteGrass);
	}

	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
