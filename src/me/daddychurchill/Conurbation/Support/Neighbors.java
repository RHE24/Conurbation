package me.daddychurchill.Conurbation.Support;

import me.daddychurchill.Conurbation.Plats.PlatGenerator;

public class Neighbors {
	
	public PlatGenerator[][] neighbors;
	
	public Neighbors() {
		
	}
	
	public Neighbors(Generator generator, int x, int z) {
		super();
		
		neighbors = new PlatGenerator[3][3];
		
//		neighbors[0][0] = generator.getGenerator(x - 1, z - 1);
//		neighbors[0][1] = generator.getGenerator(x - 1, z    );
//		neighbors[0][2] = generator.getGenerator(x - 1, z + 1);
//		neighbors[1][0] = generator.getGenerator(x    , z - 1);
//		neighbors[1][1] = generator.getGenerator(x    , z    );
//		neighbors[1][2] = generator.getGenerator(x    , z + 1);
//		neighbors[2][0] = generator.getGenerator(x + 1, z - 1);
//		neighbors[2][1] = generator.getGenerator(x + 1, z    );
//		neighbors[2][2] = generator.getGenerator(x + 1, z + 1);
	}
}
