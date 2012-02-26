package me.daddychurchill.Conurbation.Support;

import java.util.Random;

import me.daddychurchill.Conurbation.Generator;

import org.bukkit.Material;

public class HouseFactory {

	protected static byte byteAir = (byte) Material.AIR.getId();
	protected static byte byteGlass = (byte) Material.THIN_GLASS.getId();
	protected static byte byteRug = (byte) Material.WOOL.getId();
	
	// simple house builder
	private static class Room {
		public static final int MinSize = 5;
		public static final int MaxSize = 7;
		public static final int MissingRoomOdds = 12; // 1/n of the time a room is missing
		
		public boolean Missing;
		public int SizeX;
		public int SizeZ;
		
		public Room(Random random) {
			super();
			
			Missing = random.nextInt(MissingRoomOdds) == 0;
			SizeX = random.nextInt(MaxSize - MinSize) + MinSize;
			SizeZ = random.nextInt(MaxSize - MinSize) + MinSize;
		}
	}
	
	public static void generate(ByteChunk chunk, Random random, int chunkX, int chunkZ, int baseY) {
		generate(chunk, random, chunkX, chunkZ, baseY, pickWallMaterial(random), pickWallMaterial(random));
	}
	
	public static void generate(ByteChunk chunk, Random random, int chunkX, int chunkZ, int baseY, byte matWall, byte matRoof) {
//		chunk.setBlocks(2, 14, baseY, baseY + 4, 2, 14, matWall);

		// what are the rooms like?
		boolean missingRoom = false;
		Room[][] rooms = new Room[2][2];
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 2; z++) {
				rooms[x][z] = new Room(random);

				// only one missing room per house
				if (rooms[x][z].Missing) {
					if (!missingRoom)
						missingRoom = true;
					else
						rooms[x][z].Missing = false;
				}
			}
		}
		
		// where is the center of the house?
		int roomOffsetX = chunk.Width / 2 + random.nextInt(2) - 1;
		int roomOffsetZ = chunk.Width / 2 + random.nextInt(2) - 1;
		
		// draw the individual rooms
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 2; z++) {
				drawRoom(chunk, rooms, x, z, roomOffsetX, roomOffsetZ, baseY, matWall, matRoof);
			}
		}
		
		// extrude roof
		for (int y = 0; y < Generator.floorHeight - 1; y++) {
			for (int x = 1; x < chunk.Width - 1; x++) {
				for (int z = 1; z < chunk.Width - 1; z++) {
					int yAt = y + baseY + Generator.floorHeight - 1;
					if (chunk.getBlock(x - 1, yAt, z) != byteAir && chunk.getBlock(x + 1, yAt, z) != byteAir &&
						chunk.getBlock(x, yAt, z - 1) != byteAir && chunk.getBlock(x, yAt, z + 1) != byteAir) {
						chunk.setBlock(x, yAt + 1, z, matRoof);
					}
				}
			}
		}
		
		// carve out the attic
		for (int y = 1; y < Generator.floorHeight - 1; y++) {
			for (int x = 1; x < chunk.Width - 1; x++) {
				for (int z = 1; z < chunk.Width - 1; z++) {
					int yAt = y + baseY + Generator.floorHeight - 1;
					if (chunk.getBlock(x, yAt + 1, z) != byteAir) {
						chunk.setBlock(x, yAt, z, byteAir);
					}
				}
			}
		}
	}
	
	protected static void drawRoom(ByteChunk chunk, Room[][] rooms, int x, int z, int roomOffsetX, int roomOffsetZ, int baseY, byte matWall, byte matRoof) {
		//TODO I think this function suffers from the North = West problem
		// which room?
		Room room = rooms[x][z];
		boolean northRoom = x != 0;
		boolean eastRoom = z != 0;
		
		// is there really something here?
		if (!room.Missing) {
			int x1 = roomOffsetX - (northRoom ? 0 : room.SizeX);
			int x2 = roomOffsetX + (northRoom ? room.SizeX : 0);
			int z1 = roomOffsetZ - (eastRoom ? 0 : room.SizeZ);
			int z2 = roomOffsetZ + (eastRoom ? room.SizeZ : 0);
			int y1 = baseY;
			int y2 = baseY + Generator.floorHeight - 1;

			// draw the walls
			chunk.setBlocks(x1, x1 + 1, y1, y2, z1, z2 + 1, matWall);
			chunk.setBlocks(x2, x2 + 1, y1, y2, z1, z2 + 1, matWall);
			chunk.setBlocks(x1, x2 + 1, y1, y2, z1, z1 + 1, matWall);
			chunk.setBlocks(x1, x2 + 1, y1, y2, z2, z2 + 1, matWall);
			
			// add rug and roof
			chunk.setBlocks(x1, x2 + 1, y1 - 1, y1, z1, z2 + 1, byteRug);
			chunk.setBlocks(x1 - 1, x2 + 2, y2, y2 + 1, z1 - 1, z2 + 2, matRoof);
			
			if (northRoom) {
				if (eastRoom) {
					chunk.setBlocks(x1 + 2, y1, 	y2 - 1, z1, 	byteAir); // west
					chunk.setBlocks(x1, 	y1, 	y2 - 1, z1 + 2, byteAir); // south
					chunk.setBlocks(x1 + 2, x2 - 1, y1 + 1, y2 - 1, z2, 	z2 + 1, byteGlass); // east
					chunk.setBlocks(x2, 	x2 + 1, y1 + 1, y2 - 1, z1 + 2, z2 - 1, byteGlass); // north
				} else {
					chunk.setBlocks(x1 + 2, y1, 	y2 - 1, z2, 	byteAir); // east
					chunk.setBlocks(x1, 	y1, 	y2 - 1, z2 - 2, byteAir); // south
					chunk.setBlocks(x1 + 2, x2 - 1, y1 + 1, y2 - 1, z1, 	z1 + 1, byteGlass); // west
					chunk.setBlocks(x2, 	x2 + 1,	y1 + 1, y2 - 1, z1 + 2, z2 - 1, byteGlass); // north
				}
			} else {
				if (eastRoom) {
					chunk.setBlocks(x2 - 2, y1, 	y2 - 1, z1, 	byteAir); // west
					chunk.setBlocks(x2, 	y1, 	y2 - 1, z1 + 2, byteAir); // north
					chunk.setBlocks(x1 + 2, x2 - 1, y1 + 1, y2 - 1, z2, 	z2 + 1, byteGlass); // east
					chunk.setBlocks(x1, 	x1 + 1, y1 + 1, y2 - 1, z1 + 2, z2 - 1, byteGlass); // south
				} else {
					chunk.setBlocks(x2 - 2, y1, 	y2 - 1, z2, 	byteAir); // east
					chunk.setBlocks(x2, 	y1, 	y2 - 1, z2 - 2, byteAir); // north
					chunk.setBlocks(x1 + 2, x2 - 1, y1 + 1, y2 - 1, z1, 	z1 + 1, byteGlass); // west
					chunk.setBlocks(x1, 	x1 + 1, y1 + 1, y2 - 1, z1 + 2, z2 - 1, byteGlass); // south
				}
			}
		}
	}
	
	public static byte pickWallMaterial(Random random) {
		switch (random.nextInt(7)) {
		case 1:
			return (byte) Material.COBBLESTONE.getId();
		case 2:
			return (byte) Material.STONE.getId();
		case 3:
			return (byte) Material.SMOOTH_BRICK.getId();
		case 4:
			return (byte) Material.BRICK.getId();
		case 5:
			return (byte) Material.MOSSY_COBBLESTONE.getId();
		case 6:
			return (byte) Material.SANDSTONE.getId();
		default:
			return (byte) Material.WOOD.getId();
		}
	}

	public static byte pickRoofMaterial(Random random) {
		// TODO we should validate these against the wall materials
		switch (random.nextInt(4)) {
		case 1:
			return (byte) Material.COBBLESTONE.getId();
		case 2:
			return (byte) Material.STONE.getId();
		case 3:
			return (byte) Material.MOSSY_COBBLESTONE.getId();
		default:
			return (byte) Material.WOOD.getId();
		}
	}
}
