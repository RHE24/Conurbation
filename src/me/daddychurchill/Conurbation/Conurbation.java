package me.daddychurchill.Conurbation;


import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Conurbation extends JavaPlugin{
	
	public static final Logger log = Logger.getLogger("Minecraft.CityWorld");
   	
    public Conurbation() {
		super();
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String name, String style){
		return getChunkGenerator(name, style);
	}
	
	@Override
	public void onDisable() {
		
		// remember for the next time
		saveConfig();
		
		// tell the world we are out of here
		log.info(getDescription().getFullName() + " has been disabled" );
	}
	
//	int roadCellSize = 3;
//	private boolean checkRoadCoordinate(int i) {
//		return i % roadCellSize == 0;
//	}
//	
//	private int fixRoadCoordinate(int i) {
//		if (i < 0) {
//			return -((Math.abs(i + 1) / roadCellSize) * roadCellSize + roadCellSize);
//		} else
//			return (i / roadCellSize) * roadCellSize;
//	}

	@Override
	public void onEnable() {
		
//		for (int i = -10; i < 10; i++)
//			Conurbation.log.info("1: i = " + i + " % " + roadCellSize + " = " + (i % roadCellSize));
//		for (int i = -10; i < 10; i++)
//			Conurbation.log.info("2: i = " + i + " = " + fixRoadCoordinate(i) + " <--> " + fixRoadCoordinate (i + roadCellSize));
//		for (int i = -10; i < 10; i++) {
//			int previousI = fixRoadCoordinate(i);
//			previousI -= roadCellSize;
//			int nextI = fixRoadCoordinate (i + roadCellSize);
//			nextI += roadCellSize;
//			Conurbation.log.info("3: i = " + i + " = " + previousI + " <--> " + nextI);
//		}
		
		// figure out permissions and associated commands
		PluginManager pm = getServer().getPluginManager();
		pm.addPermission(new Permission("conurbation.command", PermissionDefault.OP));
		addCommand("conurbation", new CreateCMD(this));

		// configFile can be retrieved via getConfig()
		log.info(getDescription().getFullName() + " is enabled" );
	}
	
	private void addCommand(String keyword, CommandExecutor exec) {
		PluginCommand cmd = getCommand(keyword);
		if (cmd == null || exec == null) {
			log.info("Cannot create command for " + keyword);
		} else {
			cmd.setExecutor(exec);
		}
	}
	
    // prime world support (loosely based on ExpansiveTerrain)
	public final static String WORLD_NAME = "Conurbation";
	private static World conurbationPrime = null;
	public World getConurbation() {
		
		// created yet?
		if (conurbationPrime == null) {
			
			// built yet?
			conurbationPrime = Bukkit.getServer().getWorld(WORLD_NAME);
			if (conurbationPrime == null) {
				
				// if neither then create/build it!
				WorldCreator creator = new WorldCreator(WORLD_NAME);
				creator.environment(World.Environment.NORMAL);
				creator.generator(getChunkGenerator(WORLD_NAME, ""));
				conurbationPrime = Bukkit.getServer().createWorld(creator);
			}
		}
		return conurbationPrime;
	}
	
	private ChunkGenerator getChunkGenerator(String name, String style) {
		return new ChunkCallback(new WorldConfig(this, name, style));
	}
}

