package me.daddychurchill.Conurbation.Plats;

import org.bukkit.Material;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.Generator;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class RoadGenerator extends PlatGenerator {

	protected final static int sidewalkWidth = 3;
	
	protected final static byte pavementId = (byte) Material.STONE.getId();
	protected final static byte sidewalkId = (byte) Material.STEP.getId();
	protected final static byte bridgeId = (byte) Material.SMOOTH_BRICK.getId();
	protected final static byte railingId = (byte) Material.FENCE.getId();
	protected final static byte railingBaseId = (byte) Material.DOUBLE_STEP.getId();
	
	public RoadGenerator(Generator noise) {
		super(noise);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		int streetLevel = noise.getStreetLevel();
		int seabedLevel = noise.getSeabedLevel();
		int sidewalkLevel = streetLevel + 1;
		
		boolean toNorth = noise.isRoad(chunkX, chunkZ - 1);
		boolean toSouth = noise.isRoad(chunkX, chunkZ + 1);
		boolean toWest = noise.isRoad(chunkX - 1, chunkZ);
		boolean toEast = noise.isRoad(chunkX + 1, chunkZ);
		boolean isBridge = noise.isWater(chunkX, chunkZ);
		
		// draw pavement
		chunk.setLayer(streetLevel, pavementId);
		
		// sidewalk corners
		chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, sidewalkId);
		chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, ByteChunk.Width - sidewalkWidth, ByteChunk.Width, sidewalkId);
		chunk.setBlocks(ByteChunk.Width - sidewalkWidth, ByteChunk.Width, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, sidewalkId);
		chunk.setBlocks(ByteChunk.Width - sidewalkWidth, ByteChunk.Width, sidewalkLevel, sidewalkLevel + 1, ByteChunk.Width - sidewalkWidth, ByteChunk.Width, sidewalkId);
		
		// sidewalk edges
		if (!toWest)
			chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, sidewalkWidth, ByteChunk.Width - sidewalkWidth, sidewalkId);
		if (!toEast)
			chunk.setBlocks(ByteChunk.Width - sidewalkWidth, ByteChunk.Width, sidewalkLevel, sidewalkLevel + 1, sidewalkWidth, ByteChunk.Width - sidewalkWidth, sidewalkId);
		if (!toNorth)
			chunk.setBlocks(sidewalkWidth, ByteChunk.Width - sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, sidewalkId);
		if (!toSouth)
			chunk.setBlocks(sidewalkWidth, ByteChunk.Width - sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, ByteChunk.Width - sidewalkWidth, ByteChunk.Width, sidewalkId);
		
		// round things out
		if (!toWest && toEast && !toNorth && toSouth)
			generateRoundedOut(chunk, sidewalkLevel, sidewalkWidth, sidewalkWidth, 
					false, false);
		if (!toWest && toEast && toNorth && !toSouth)
			generateRoundedOut(chunk, sidewalkLevel, sidewalkWidth, ByteChunk.Width - sidewalkWidth - 4, 
					false, true);
		if (toWest && !toEast && !toNorth && toSouth)
			generateRoundedOut(chunk, sidewalkLevel, ByteChunk.Width - sidewalkWidth - 4, sidewalkWidth, 
					true, false);
		if (toWest && !toEast && toNorth && !toSouth)
			generateRoundedOut(chunk, sidewalkLevel, ByteChunk.Width - sidewalkWidth - 4, ByteChunk.Width - sidewalkWidth - 4, 
					true, true);
		
		//TODO need to create more complex bridge styles
		//TODO ramping up and down
		if (isBridge) {
			
			// thicken it up
			chunk.setLayer(streetLevel - 1, bridgeId);
			
			// support columns
			chunk.setBlocks(0, 2, seabedLevel, streetLevel - 1, 0, 2, bridgeId);
			chunk.setBlocks(14, 16, seabedLevel, streetLevel - 1, 0, 2, bridgeId);
			chunk.setBlocks(0, 2, seabedLevel, streetLevel - 1, 14, 16, bridgeId);
			chunk.setBlocks(14, 16, seabedLevel, streetLevel - 1, 14, 16, bridgeId);
			
			// railing
			if (!toNorth) {
				chunk.setBlocks(0, 16, sidewalkLevel, sidewalkLevel + 1, 0, 1, railingBaseId);
				chunk.setBlocks(0, 16, sidewalkLevel + 1, sidewalkLevel + 2, 0, 1, railingId);
			} 
			if (!toSouth) {
				chunk.setBlocks(0, 16, sidewalkLevel, sidewalkLevel + 1, 15, 16, railingBaseId);
				chunk.setBlocks(0, 16, sidewalkLevel + 1, sidewalkLevel + 2, 15, 16, railingId);
			}
			if (!toWest) {
				chunk.setBlocks(0, 1, sidewalkLevel, sidewalkLevel + 1, 0, 16, railingBaseId);
				chunk.setBlocks(0, 1, sidewalkLevel + 1, sidewalkLevel + 2, 0, 16, railingId);
			}
			if (!toEast) {
				chunk.setBlocks(15, 16, sidewalkLevel, sidewalkLevel + 1, 0, 16, railingBaseId);
				chunk.setBlocks(15, 16, sidewalkLevel + 1, sidewalkLevel + 2, 0, 16, railingId);
			}
		}
	}

	protected void generateRoundedOut(ByteChunk chunk, int sidewalkLevel, int x, int z, boolean toNorth, boolean toEast) {

		// long bits
		for (int i = 0; i < 4; i++) {
			chunk.setBlock(toNorth ? x + 3 : x, sidewalkLevel, z + i, sidewalkId);
			chunk.setBlock(x + i, sidewalkLevel, toEast ? z + 3 : z, sidewalkId);
		}
		
		// little notch
		chunk.setBlock(toNorth ? x + 2 : x + 1, 
					   sidewalkLevel, 
					   toEast ? z + 2 : z + 1, 
					   sidewalkId);
	}
	
	@Override
	public void populateChunk(RealChunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
