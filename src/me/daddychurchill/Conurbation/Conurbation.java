package me.daddychurchill.Conurbation;


import java.util.logging.Logger;

import me.daddychurchill.Conurbation.Support.ByteChunk;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Conurbation extends JavaPlugin{
	
	public static final Logger log = Logger.getLogger("Minecraft.CityWorld");
   	
	private int streetLevel;
	private int seabedLevel;
	public final static int defaultStreetLevel = 40;
	public final static int defaultSeabedLevel = 20;
	
    public Conurbation() {
		super();
		
		this.streetLevel = defaultStreetLevel;
		this.seabedLevel = defaultSeabedLevel;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String name, String style){
		return new Generator(this, name, style);
	}
	
	@Override
	public void onDisable() {
		// remember for the next time
		saveConfig();
		
		// tell the world we are out of here
		log.info(getDescription().getFullName() + " has been disabled" );
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.addPermission(new Permission("conurbation.command", PermissionDefault.OP));
		
		addCommand("conurbation", new CreateCMD(this));

		// add/get the configuration
		FileConfiguration config = getConfig();
		config.options().header("Conurbation Global Options");
		config.addDefault("Global.StreetLevel", defaultStreetLevel);
		config.addDefault("Global.SeabedLevel", defaultSeabedLevel);
		config.options().copyDefaults(true);
		saveConfig();
		
		// now read out the bits for real
		streetLevel = validateStreetLevel(config.getInt("Global.StreetLevel"), seabedLevel);
		seabedLevel = validateSeabedLevel(config.getInt("Global.SeabedLevel"), streetLevel);
		
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
				creator.generator(new Generator(this, WORLD_NAME, ""));
				conurbationPrime = Bukkit.getServer().createWorld(creator);
			}
		}
		return conurbationPrime;
	}

	private final static int minimumSeabedStreetLevelDifference = 10;
	
	public int getStreetLevel() {
		return streetLevel;
	}

	public int getSeabedLevel() {
		return seabedLevel;
	}

	public int validateStreetLevel(int aStreetLevel, int aSeabedLevel) {
		return Math.max(aStreetLevel, Math.min(ByteChunk.Height - 1, aSeabedLevel + minimumSeabedStreetLevelDifference));
	}

	public int validateSeabedLevel(int aSeabedLevel, int aStreetLevel) {
		return Math.min(aSeabedLevel, Math.max(1, aStreetLevel - minimumSeabedStreetLevelDifference));
	}
}

