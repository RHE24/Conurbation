package me.daddychurchill.Conurbation.Plats;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.NoiseMakers;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class FalseGenerator extends PlatGenerator {

	private static int floorHeight = 4;
	private byte stuff;
	private int floors;
	
	public FalseGenerator(Generator context, NoiseMakers noise, Material stuff, int floors) {
		super(context, noise);
		
		this.stuff = (byte) stuff.getId();
		this.floors = floors;
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int streetLevel = context.getStreetLevel();
		
		chunk.setLayer(0, byteBedrock);
		chunk.setBlocks(0, 16, 1, streetLevel + 1, 0, 16, byteStone);
		chunk.setBlocks(1, 15, streetLevel + 1, streetLevel + floors * floorHeight + 1, 1, 15, stuff);
	}

	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
