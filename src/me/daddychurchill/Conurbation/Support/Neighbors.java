package me.daddychurchill.Conurbation.Support;

import me.daddychurchill.Conurbation.Generator;
import me.daddychurchill.Conurbation.Plats.PlatGenerator;

public class Neighbors {
	
	private PlatGenerator[][] neighbors;
	
	public Neighbors() {
		
	}
	
	public Neighbors(Generator generator, int chunkX, int chunkZ) {
		super();
		
		neighbors = new PlatGenerator[3][3];
		
		neighbors[0][0] = generator.getPlatGenerator(chunkX - 1, chunkZ - 1);
		neighbors[0][1] = generator.getPlatGenerator(chunkX - 1, chunkZ    );
		neighbors[0][2] = generator.getPlatGenerator(chunkX - 1, chunkZ + 1);
		neighbors[1][0] = generator.getPlatGenerator(chunkX    , chunkZ - 1);
		neighbors[1][1] = generator.getPlatGenerator(chunkX    , chunkZ    );
		neighbors[1][2] = generator.getPlatGenerator(chunkX    , chunkZ + 1);
		neighbors[2][0] = generator.getPlatGenerator(chunkX + 1, chunkZ - 1);
		neighbors[2][1] = generator.getPlatGenerator(chunkX + 1, chunkZ    );
		neighbors[2][2] = generator.getPlatGenerator(chunkX + 1, chunkZ + 1);
		
//		double noiseUrban = (generator.generatorUrban.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
//		double noiseWater = (generator.generatorWater.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
//		double noiseRoad = (generator.generatorRoad.noise(chunkX / xFactor, chunkZ / zFactor) + 1) / 2;
//		double noiseUnfinished = (generator.generatorUnfinished.noise(chunkX / (xFactor / 10), chunkZ / (zFactor / 10)) + 1) / 2;
//		double noiseSelect = noiseWater > 0.3 ? noiseUrban : 1.0; 
	}
	
	
}
