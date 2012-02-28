package me.daddychurchill.Conurbation.Neighbors;

import me.daddychurchill.Conurbation.Plats.CityGenerator;

public class CityNeighbors {

	private int[][] neighboringFloors;
	public int floors;
	public byte wallMaterial;
	public byte floorMaterial;
	public byte roofMaterial;
	
	public final static int indentNorthSouth = 2;
	public final static int indentWestEast = 2;
	
	public CityNeighbors(CityGenerator generator, int chunkX, int chunkZ) {
		super();

		// find our center
		floors = generator.getUrbanHeight(chunkX, chunkZ);
		wallMaterial = generator.getWallMaterial(chunkX, chunkZ);
		floorMaterial = generator.getFloorMaterial(chunkX, chunkZ);
		roofMaterial = generator.getRoofMaterial(chunkX, chunkZ);
		
		// make room for the neighbors
		neighboringFloors = new int[3][3];
		neighboringFloors[1][1] = floors;
		
		// survey our neighbors
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				if (!(x == 1 && z == 1)) {
					int x1 = chunkX + x - 1;
					int z1 = chunkZ + z - 1;
					if (generator.noise.isUrbanBuilding(x1, z1) &&
						generator.getWallMaterial(x1, z1) == wallMaterial &&
						generator.getFloorMaterial(x1, z1) == floorMaterial &&
						generator.getRoofMaterial(x1, z1) == roofMaterial) {
						neighboringFloors[x][z] = generator.getUrbanHeight(x1, z1);
					}
				}
			}
		}
	}
	
	public void decrement() {
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				neighboringFloors[x][z]--;
			}
		}
	}
	
	public int getNeighborCount() {
		int result = 0;
		if (neighboringFloors[0][1] > 0)
			result++;
		if (neighboringFloors[1][0] > 0)
			result++;
		if (neighboringFloors[2][1] > 0)
			result++;
		if (neighboringFloors[1][2] > 0)
			result++;
		return result;
	}
	
	public boolean isRoundable() {
		if (toSouth()) {
			if (toWest()) {
				return !toNorth() && !toEast() && floorsSame(floorsToSouth(), floorsToWest());
			} else if (toEast()) {
				return !toNorth() && floorsSame(floorsToSouth(), floorsToEast());
			}
		} else if (toNorth()) {
			if (toWest()) {
				return !toEast() && floorsSame(floorsToNorth(), floorsToWest());
			} else if (toEast()) {
				return floorsSame(floorsToNorth(), floorsToEast());
			}
		}
		return false;
	}
	
	private boolean floorsSame(int other1, int other2) {
		return neighboringFloors[1][1] == other1 && other1 == other2;
	}
	
	private int floorsToWest() {
		return neighboringFloors[0][1];
	}
	
	private int floorsToEast() {
		return neighboringFloors[2][1];
	}
	
	private int floorsToNorth() {
		return neighboringFloors[1][0];
	}
	
	private int floorsToSouth() {
		return neighboringFloors[1][2];
	}
	
	public boolean toNorthEast() {
		return neighboringFloors[2][0] > 0 && toEast() && toNorth();
	}

	public boolean toEast() {
		return neighboringFloors[2][1] > 0;
	}

	public boolean toSouthEast() {
		return neighboringFloors[2][2] > 0 && toEast() && toSouth();
	}
	
	public boolean toNorth() {
		return neighboringFloors[1][0] > 0;
	}
	
	public boolean toCenter() {
		return true;
	}
	
	public boolean toSouth() {
		return neighboringFloors[1][2] > 0;
	}

	public boolean toNorthWest() {
		return neighboringFloors[0][0] > 0 && toWest() && toNorth();
	}

	public boolean toWest() {
		return neighboringFloors[0][1] > 0;
	}

	public boolean toSouthWest() {
		return neighboringFloors[0][2] > 0 && toWest() && toSouth();
	}
	
	public int insetToNorth() {
		return toNorth() ? 0 : indentNorthSouth;
	}
	
	public int insetToSouth() {
		return toSouth() ? 0 : indentNorthSouth;
	}
	
	public int insetToWest() {
		return toWest() ? 0 : indentWestEast;
	}
	
	public int insetToEast() {
		return toEast() ? 0 : indentWestEast;
	}
}
