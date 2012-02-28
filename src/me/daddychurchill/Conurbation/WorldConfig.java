package me.daddychurchill.Conurbation;

import org.bukkit.configuration.file.FileConfiguration;

public class WorldConfig {
	private static FileConfiguration config;
	
	private Conurbation plugin;
	private String worldname;
	private String worldstyle;
	private int streetLevel;
	private int seabedLevel;
	private double decrepitLevel;
	
	public final static int defaultStreetLevel = -1;
	public final static int defaultSeabedLevel = -1;
	public final static double defaultDecrepitLevel = 0.1;
	
	public WorldConfig(Conurbation plugin, String name, String style) {
		super();
		
		this.plugin = plugin;
		this.worldname = name;
		this.worldstyle = style;
		
		// remember the globals
		int globalStreetLevel = defaultStreetLevel;
		int globalSeabedLevel = defaultSeabedLevel;
		double globalDecrepitLevel = defaultDecrepitLevel;
		
		// global read yet?
		if (config == null) {
			config = plugin.getConfig();
			config.options().header("Conurbation Global Options");
			config.addDefault("Global.StreetLevel", defaultStreetLevel);
			config.addDefault("Global.SeabedLevel", defaultSeabedLevel);
			config.addDefault("Global.DecrepitLevel", defaultDecrepitLevel);
			config.options().copyDefaults(true);
			plugin.saveConfig();
			
			// now read out the bits for real
			globalStreetLevel = config.getInt("Global.StreetLevel");
			globalSeabedLevel = config.getInt("Global.SeabedLevel");
			globalDecrepitLevel = config.getDouble("Global.DecrepitLevel");
		}
		
		// copy over the defaults
		streetLevel = globalStreetLevel;
		seabedLevel = globalSeabedLevel;
		decrepitLevel = globalDecrepitLevel;
		
		// grab the world specific values
		streetLevel = getWorldInt(config, "StreetLevel", globalStreetLevel);
		seabedLevel = getWorldInt(config, "SeabedLevel", globalSeabedLevel);
		decrepitLevel = getWorldDouble(config, "DecrepitLevel", globalDecrepitLevel);
	}

	private int getWorldInt(FileConfiguration config, String option, int global) {
		int result = global;
		String path = worldname + "." + option;
		if (config.isSet(path))
			result = config.getInt(path);
		return result;
	}
	
	private double getWorldDouble(FileConfiguration config, String option, double global) {
		double result = global;
		String path = worldname + "." + option;
		if (config.isSet(path))
			result = config.getDouble(path);
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
	
	public double getDecrepitLevel() {
		return decrepitLevel ;
	}
	
}
