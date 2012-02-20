package me.daddychurchill.Conurbation.Neighbors;

import me.daddychurchill.Conurbation.Plats.UrbanGenerator;

public class UrbanNeighbors {

	private UrbanGenerator[][] neighbors;
	private int[][] above;
	//private int[][] below;
	
	public UrbanNeighbors(UrbanGenerator center, int chunkX, int chunkZ) {
		super();
		
		neighbors = new UrbanGenerator[3][3];
		for (int x = 0; x < 3; x++)
			for (int z = 0; z < 3; z++) {
				int offsetX = x - 1;
				int offsetZ = z - 1;
				if (offsetX == 0 && offsetZ == 0 || center.noise.isUrbanBuilding(chunkX + offsetX, chunkZ + offsetZ))
					neighbors[x][z] = center;
			}
		
		above = new int[3][3];
		//below = new int[3][3];
	}
	
	public boolean connectedToNorthWest() {
		return above[0][0] > 0 && connectedToNorth() && connectedToWest();
	}

	public boolean connectedToNorth() {
		return above[1][0] > 0;
	}

	public boolean connectedToNorthEast() {
		return above[2][0] > 0 && connectedToNorth() && connectedToEast();
	}

	public boolean connectedToWest() {
		return above[0][1] > 0;
	}

	public boolean connectedToEast() {
		return above[2][1] > 0;
	}

	public boolean connectedToSouthWest() {
		return above[0][2] > 0 && connectedToSouth() && connectedToWest();
	}

	public boolean connectedToSouth() {
		return above[1][2] > 0;
	}

	public boolean connectedToSouthEast() {
		return above[2][2] > 0 && connectedToSouth() && connectedToEast();
	}

}
