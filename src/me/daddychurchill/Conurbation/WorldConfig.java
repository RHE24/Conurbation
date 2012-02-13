package me.daddychurchill.Conurbation;

import me.daddychurchill.Conurbation.Support.ByteChunk;
import org.bukkit.configuration.file.FileConfiguration;

public class WorldConfig {
	private static FileConfiguration config;
	
	private Conurbation plugin;
	private String worldname;
	private String worldstyle;
	private int streetLevel;
	private int seabedLevel;
	
	private static int globalStreetLevel;
	private static int globalSeabedLevel;
	public final static int defaultStreetLevel = 40;
	public final static int defaultSeabedLevel = 20;
	
	public WorldConfig(Conurbation plugin, String name, String style) {
		super();
		
		this.plugin = plugin;
		this.worldname = name;
		this.worldstyle = style;
		
		// global read yet?
		if (config == null) {
			config = plugin.getConfig();
			config.options().header("Conurbation Global Options");
			config.addDefault("Global.StreetLevel", defaultStreetLevel);
			config.addDefault("Global.SeabedLevel", defaultSeabedLevel);
			config.options().copyDefaults(true);
			plugin.saveConfig();
			
			// now read out the bits for real
			globalStreetLevel = validateStreetLevel(config.getInt("Global.StreetLevel"), globalSeabedLevel);
			globalSeabedLevel = validateSeabedLevel(config.getInt("Global.SeabedLevel"), globalStreetLevel);
		}
		
		// copy over the defaults
		streetLevel = globalStreetLevel;
		seabedLevel = globalSeabedLevel;
		
		// grab the world specific values
		streetLevel = validateStreetLevel(getWorldInt(config, "StreetLevel", globalStreetLevel), seabedLevel);
		seabedLevel = validateSeabedLevel(getWorldInt(config, "SeabedLevel", globalSeabedLevel), streetLevel);
	}

	private int getWorldInt(FileConfiguration config, String option, int global) {
		int result = global;
		String path = worldname + "." + option;
		if (config.isSet(path))
			result = config.getInt(path);
		return result;
	}
	
	public Conurbation getPlugin() {
		return plugin;
	}
	
	public String getWorldname() {
		return worldname;
	}

	public String getWorldstyle() {
		return worldstyle;
	}
	
	public int getStreetLevel() {
		return streetLevel;
	}

	public int getSeabedLevel() {
		return seabedLevel;
	}
	
	private final static int minimumSeabedStreetLevelDifference = 10;
	
	public int getGlobalStreetLevel() {
		return globalStreetLevel;
	}

	public int getGlobalSeabedLevel() {
		return globalSeabedLevel;
	}

	private int validateStreetLevel(int aStreetLevel, int aSeabedLevel) {
		return Math.max(aStreetLevel, Math.min(ByteChunk.Height - 1, aSeabedLevel + minimumSeabedStreetLevelDifference));
	}

	private int validateSeabedLevel(int aSeabedLevel, int aStreetLevel) {
		return Math.min(aSeabedLevel, Math.max(1, aStreetLevel - minimumSeabedStreetLevelDifference));
	}
}
