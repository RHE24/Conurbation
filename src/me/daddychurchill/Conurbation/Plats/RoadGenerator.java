package me.daddychurchill.Conurbation.Plats;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Support.ByteChunk;
import me.daddychurchill.Conurbation.Support.RealChunk;

public class RoadGenerator extends PlatGenerator {

	public final static int sidewalkWidth = 3;
	public final static int roadThickness = 2;
	public final static int lightpostHeight = 3;
	
	public final static byte bytePavement = (byte) Material.STONE.getId();
	public final static byte byteSidewalk = (byte) Material.STEP.getId();
	public final static byte bytePlatwalk = (byte) Material.DOUBLE_STEP.getId(); // good for sidewalks in plats
	public final static byte byteBridge = (byte) Material.SMOOTH_BRICK.getId();
	public final static byte byteRailing = (byte) Material.FENCE.getId();
	public final static byte byteRailingBase = bytePlatwalk;
	
	public final static Material lightpostbaseMaterial = Material.DOUBLE_STEP;
	public final static Material lightpostMaterial = Material.FENCE;
	public final static Material workingLampMaterial = Material.GLOWSTONE;
	public final static Material workingTorchMaterial = Material.TORCH;
	public final static Material brokeLampMaterial = Material.GLASS;
	public final static Material brokeTorchMaterial = Material.REDSTONE_TORCH_OFF;
	
	public final static int roadCellSize = 4;
	private final static double xIntersectionFactor = 6;
	private final static double zIntersectionFactor = 6;
	private final static double threshholdRoad = 0.65;
	private final static double threshholdBridge = 1.00;
	private final static double threshholdBridgeLength = 0.10;
	
	int streetLevel;
	int seabedLevel;
	int sidewalkLevel;
	
	private SimplexNoiseGenerator noiseIntersection;
	private HashSet<Long> knownRoads;
	
	public RoadGenerator(Generator noise) {
		super(noise);

		noiseIntersection = new SimplexNoiseGenerator(noise.getNextSeed());
		knownRoads = new HashSet<Long>();
		
		streetLevel = noise.getStreetLevel();
		seabedLevel = noise.getSeabedLevel();
		sidewalkLevel = streetLevel + 1;
	}

	@Override
	public void generateChunk(ByteChunk chunk, Random random, int chunkX, int chunkZ) {
		
		// what is where?
		boolean toNorth = noise.isRoad(chunkX, chunkZ - 1);
		boolean toSouth = noise.isRoad(chunkX, chunkZ + 1);
		boolean toWest = noise.isRoad(chunkX - 1, chunkZ);
		boolean toEast = noise.isRoad(chunkX + 1, chunkZ);
		boolean isBridge = noise.isWater(chunkX, chunkZ);
		
		// draw pavement
		chunk.setLayer(streetLevel, bytePavement);
		
		// sidewalk corners
		chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, byteSidewalk);
		chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, chunk.width - sidewalkWidth, chunk.width, byteSidewalk);
		chunk.setBlocks(chunk.width - sidewalkWidth, chunk.width, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, byteSidewalk);
		chunk.setBlocks(chunk.width - sidewalkWidth, chunk.width, sidewalkLevel, sidewalkLevel + 1, chunk.width - sidewalkWidth, chunk.width, byteSidewalk);
		
		// sidewalk edges
		if (!toWest)
			chunk.setBlocks(0, sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, sidewalkWidth, chunk.width - sidewalkWidth, byteSidewalk);
		if (!toEast)
			chunk.setBlocks(chunk.width - sidewalkWidth, chunk.width, sidewalkLevel, sidewalkLevel + 1, sidewalkWidth, chunk.width - sidewalkWidth, byteSidewalk);
		if (!toNorth)
			chunk.setBlocks(sidewalkWidth, chunk.width - sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, 0, sidewalkWidth, byteSidewalk);
		if (!toSouth)
			chunk.setBlocks(sidewalkWidth, chunk.width - sidewalkWidth, sidewalkLevel, sidewalkLevel + 1, chunk.width - sidewalkWidth, chunk.width, byteSidewalk);
		
		// round things out
		if (!toWest && toEast && !toNorth && toSouth)
			generateRoundedOut(chunk, sidewalkLevel, sidewalkWidth, sidewalkWidth, 
					false, false);
		if (!toWest && toEast && toNorth && !toSouth)
			generateRoundedOut(chunk, sidewalkLevel, sidewalkWidth, chunk.width - sidewalkWidth - 4, 
					false, true);
		if (toWest && !toEast && !toNorth && toSouth)
			generateRoundedOut(chunk, sidewalkLevel, chunk.width - sidewalkWidth - 4, sidewalkWidth, 
					true, false);
		if (toWest && !toEast && toNorth && !toSouth)
			generateRoundedOut(chunk, sidewalkLevel, chunk.width - sidewalkWidth - 4, chunk.width - sidewalkWidth - 4, 
					true, true);
		
		//TODO need to create more complex bridge styles
		//TODO ramping up and down
		if (isBridge) {
			
			// thicken it up
			chunk.setLayer(streetLevel - 1, byteBridge);
			
			// support columns
			chunk.setBlocks(0, 2, seabedLevel, streetLevel - 1, 0, 2, byteBridge);
			chunk.setBlocks(14, 16, seabedLevel, streetLevel - 1, 0, 2, byteBridge);
			chunk.setBlocks(0, 2, seabedLevel, streetLevel - 1, 14, 16, byteBridge);
			chunk.setBlocks(14, 16, seabedLevel, streetLevel - 1, 14, 16, byteBridge);
			
			// railing
			if (!toNorth) {
				chunk.setBlocks(0, 16, sidewalkLevel, sidewalkLevel + 1, 0, 1, byteRailingBase);
				chunk.setBlocks(0, 16, sidewalkLevel + 1, sidewalkLevel + 2, 0, 1, byteRailing);
			} 
			if (!toSouth) {
				chunk.setBlocks(0, 16, sidewalkLevel, sidewalkLevel + 1, 15, 16, byteRailingBase);
				chunk.setBlocks(0, 16, sidewalkLevel + 1, sidewalkLevel + 2, 15, 16, byteRailing);
			}
			if (!toWest) {
				chunk.setBlocks(0, 1, sidewalkLevel, sidewalkLevel + 1, 0, 16, byteRailingBase);
				chunk.setBlocks(0, 1, sidewalkLevel + 1, sidewalkLevel + 2, 0, 16, byteRailing);
			}
			if (!toEast) {
				chunk.setBlocks(15, 16, sidewalkLevel, sidewalkLevel + 1, 0, 16, byteRailingBase);
				chunk.setBlocks(15, 16, sidewalkLevel + 1, sidewalkLevel + 2, 0, 16, byteRailing);
			}
		}
	}

	@Override
	public int generateChunkColumn(ByteChunk chunk, int chunkX, int chunkZ, int blockX, int blockZ) {
		chunk.setBlock(blockX, streetLevel, blockZ, bytePavement);
		
		//TODO all of the extra bits are missing!!!
		return streetLevel - 1;
	}

	protected void generateRoundedOut(ByteChunk chunk, int sidewalkLevel, int x, int z, boolean toNorth, boolean toEast) {

		// long bits
		for (int i = 0; i < 4; i++) {
			chunk.setBlock(toNorth ? x + 3 : x, sidewalkLevel, z + i, byteSidewalk);
			chunk.setBlock(x + i, sidewalkLevel, toEast ? z + 3 : z, byteSidewalk);
		}
		
		// little notch
		chunk.setBlock(toNorth ? x + 2 : x + 1, 
					   sidewalkLevel, 
					   toEast ? z + 2 : z + 1, 
					   byteSidewalk);
	}
	
	@Override
	public void populateChunk(RealChunk chunk, Random random, int chunkX, int chunkZ) {

		// light posts
		boolean isTorch = noise.isRural(chunkX, chunkZ);
		generateLightPost(chunk, random, sidewalkWidth - 1, sidewalkWidth - 1, isTorch);
		generateLightPost(chunk, random, chunk.width - sidewalkWidth, chunk.width - sidewalkWidth, isTorch);
		
		// tell users where they are
		generateStreetSigns(chunk, random, sidewalkWidth - 1, sidewalkWidth - 1, fixRoadCoordinate(chunkX), fixRoadCoordinate(chunkZ));
	}

	protected void generateLightPost(RealChunk chunk, Random random, int x, int z, boolean isTorch) {
		int sidewalkLevel = streetLevel + 1;
		chunk.setBlock(x, sidewalkLevel, z, lightpostbaseMaterial);
		chunk.setBlocks(x, sidewalkLevel + 1, sidewalkLevel + lightpostHeight + 1, z, lightpostMaterial);
		if (isTorch) {
			if (noise.isDecrepit(random))
				chunk.setBlock(x, sidewalkLevel + lightpostHeight + 1, z, brokeTorchMaterial.getId(), (byte)5, false);
			else
				chunk.setBlock(x, sidewalkLevel + lightpostHeight + 1, z, workingTorchMaterial.getId(), (byte)5, true);
		} else {
			if (noise.isDecrepit(random))
				chunk.setBlock(x, sidewalkLevel + lightpostHeight + 1, z, brokeLampMaterial, false);
			else
				chunk.setBlock(x, sidewalkLevel + lightpostHeight + 1, z, workingLampMaterial, true);
		}
	}
	
	protected void generateStreetSigns(RealChunk chunk, Random random, int blockX, int blockZ, int streetX, int streetZ) {
		//TODO populate streetsigns (some are missing depending on decrepitude)
	}
	
	@Override
	public int getGroundSurfaceY(int chunkX, int chunkZ, int blockX, int blockZ) {
		return sidewalkLevel;
	}

	@Override
	public Material getGroundSurfaceMaterial(int chunkX, int chunkZ) {
		return Material.DOUBLE_STEP;
	}

	//TODO put in the "cross bridge" test in once the caching works
//	private int lookupTotal = 0;
//	private int lookupSlow = 0;
	
	@Override
	public boolean isChunk(int chunkX, int chunkZ) {
		
		// quick test! 
		boolean checkedX = checkRoadCoordinate(chunkX);
		boolean checkedZ = checkRoadCoordinate(chunkZ);
		boolean roadExists = checkedX || checkedZ;
		
		// not so quick test to verify it is REALLY a road
		if (roadExists) {
			
			// check the cache, if it exists then assume we have a road
//			lookupTotal++;
			Long roadId = getChunkKey(chunkX, chunkZ);
			if (!knownRoads.contains(roadId)) {
//				lookupSlow++;
				
				// is this an intersection?
				if (checkedX && checkedZ) {
					
					// if roads exists to any of the cardinal directions then we exist
					roadExists = isChunk(chunkX - 1, chunkZ) || isChunk(chunkX + 1, chunkZ) ||
								 isChunk(chunkX, chunkZ - 1) || isChunk(chunkX, chunkZ + 1);
					
				} else {

					// bridge?
					boolean isBridge = false;
					int previousX, previousZ, nextX, nextZ;
					double previousNoise, nextNoise, lengthNoise = 0.0;
					
					// north/south road?
					if (checkedX) {
						
						// find previous intersection not in water
						previousX = chunkX;
						previousZ = fixRoadCoordinate(chunkZ);
						while (noise.isWater(previousX, previousZ)) {
							previousZ -= roadCellSize;
							isBridge = true;
							lengthNoise = lengthNoise + threshholdBridgeLength;
						}
						
						// test for northward road
						if (noise.isWater(previousX, previousZ + 1))
							previousNoise = 0.0;
						else
							previousNoise = getIntersectionNoise(previousX, previousZ + 1);
							
						// find next intersection not in water
						nextX = chunkX;
						nextZ = fixRoadCoordinate(chunkZ + roadCellSize);
						while (noise.isWater(nextX, nextZ)) {
							nextZ += roadCellSize;
							isBridge = true;
							lengthNoise = lengthNoise + threshholdBridgeLength;
						}
						
						// test for southward road
						if (noise.isWater(nextX, nextZ - 1))
							nextNoise = 0.0;
						else
							nextNoise = getIntersectionNoise(nextX, nextZ - 1);
						
					// east/west road?
					} else { // if (checkRoadCoordinate(chunkZ))
		
						// find previous intersection not in water
						previousX = fixRoadCoordinate(chunkX);
						previousZ = chunkZ;
						while (noise.isWater(previousX, previousZ)) {
							previousX -= roadCellSize;
							isBridge = true;
							lengthNoise = lengthNoise + threshholdBridgeLength;
						}
						
						// test for westward road
						if (noise.isWater(previousX + 1, previousZ))
							previousNoise = 0.0;
						else
							previousNoise = getIntersectionNoise(previousX + 1, previousZ);
							
						// find next intersection not in water
						nextX = fixRoadCoordinate(chunkX + roadCellSize);
						nextZ = chunkZ;
						while (noise.isWater(nextX, nextZ)) {
							nextX += roadCellSize;
							isBridge = true;
							lengthNoise = lengthNoise + threshholdBridgeLength;
						}
						
						// test for eastward road
						if (noise.isWater(nextX - 1, nextZ))
							nextNoise = 0.0;
						else
							nextNoise = getIntersectionNoise(nextX - 1, nextZ);
					}
					
					// overwater?
					if (isBridge)
						roadExists = (previousNoise + nextNoise) > (threshholdBridge + lengthNoise); // longer bridges are "harder"
					else
						roadExists = (previousNoise + nextNoise) > threshholdRoad;
				}
				
				// if we found one, remember it for next time
				if (roadExists)
					knownRoads.add(roadId);
			}

//			if (lookupTotal % 200 == 0) {
//				Conurbation.log.info("Road cache miss ratio: " + lookupSlow + "/" + lookupTotal);
//			}
		}
		
		// tell the world
		return roadExists;
	}

	private double getIntersectionNoise(int x, int z) {
		return (noiseIntersection.noise(x / xIntersectionFactor, z / zIntersectionFactor) + 1) / 2;
	}
	
	private boolean checkRoadCoordinate(int i) {
		return i % roadCellSize == 0;
	}
	
	private int fixRoadCoordinate(int i) {
		if (i < 0) {
			return -((Math.abs(i + 1) / roadCellSize) * roadCellSize + roadCellSize);
		} else
			return (i / roadCellSize) * roadCellSize;
	}
	

}
