package me.daddychurchill.Conurbation.Plats;

import me.daddychurchill.Conurbation.Conurbation;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class WaterGenerator extends PlatGenerator {

	public WaterGenerator() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void generateChunk(Conurbation plugin, ByteChunk chunk, int chunkX, int chunkZ) {
		int seabedLevel = plugin.getSeabedLevel();
		int streetLevel = plugin.getStreetLevel();
		
		chunk.setLayer(0, byteBedrock);
		
		chunk.setBlocks(0, 16, 1, seabedLevel + 1, 0, 16, byteStone);
		chunk.setBlocks(0, 16, seabedLevel, streetLevel, 0, 16, byteWater);
	}

	@Override
	public void generateBlocks(Conurbation plugin, RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
