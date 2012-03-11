package me.daddychurchill.Conurbation;

import org.bukkit.configuration.file.FileConfiguration;

public class WorldConfig {
	private Conurbation plugin;
	private String worldname;
	private String worldstyle;
	
	private int streetLevel;
	private int seabedLevel;
	private int maximumFloors;
	private double decrepitLevel;
	private boolean includeAgricultureZones;
	private boolean includeResidentialZones;
	private boolean includeUrbanZones;
	
	public final static int defaultStreetLevel = -1;
	public final static int defaultSeabedLevel = -1;
	public final static int defaultMaximumFloors = 20;
	public final static double defaultDecrepitLevel = 0.1;
	public final static boolean defaultIncludeAgricultureZones = true;
	public final static boolean defaultIncludeResidentialZones = true;
	public final static boolean defaultIncludeUrbanZones = true;
	
	public WorldConfig(Conurbation plugin, String name, String style) {
		super();
		
		this.plugin = plugin;
		this.worldname = name;
		this.worldstyle = style;
		
		// remember the globals
		int globalStreetLevel = defaultStreetLevel;
		int globalSeabedLevel = defaultSeabedLevel;
		int globalMaximumFloors = defaultMaximumFloors;
		double globalDecrepitLevel = defaultDecrepitLevel;
		boolean globalIncludeAgricultureZones = defaultIncludeAgricultureZones;
		boolean globalIncludeResidentialZones = defaultIncludeResidentialZones;
		boolean globalIncludeUrbanZones = defaultIncludeUrbanZones;
		
		// global read yet?
		FileConfiguration config = plugin.getConfig();
		config.options().header("Conurbation Global Options");
		config.addDefault("Global.StreetLevel", defaultStreetLevel);
		config.addDefault("Global.SeabedLevel", defaultSeabedLevel);
		config.addDefault("Global.MaximumFloors", defaultMaximumFloors);
		config.addDefault("Global.DecrepitLevel", defaultDecrepitLevel);
		config.addDefault("Global.IncludeAgricultureZones", defaultIncludeAgricultureZones);
		config.addDefault("Global.IncludeResidentialZones", defaultIncludeResidentialZones);
		config.addDefault("Global.IncludeUrbanZones", defaultIncludeUrbanZones);
		config.options().copyDefaults(true);
		plugin.saveConfig();
		
		// now read out the bits for real
		globalStreetLevel = config.getInt("Global.StreetLevel");
		globalSeabedLevel = config.getInt("Global.SeabedLevel");
		globalMaximumFloors = config.getInt("Global.MaximumFloors");
		globalDecrepitLevel = config.getDouble("Global.DecrepitLevel");
		globalIncludeAgricultureZones = config.getBoolean("Global.IncludeAgricultureZones");
		globalIncludeResidentialZones = config.getBoolean("Global.IncludeResidentialZones");
		globalIncludeUrbanZones = config.getBoolean("Global.IncludeUrbanZones");
		
//		// copy over the defaults
//		streetLevel = globalStreetLevel;
//		seabedLevel = globalSeabedLevel;
//		decrepitLevel = globalDecrepitLevel;
//		includeAgricultureZones = globalIncludeAgricultureZones;
//		includeResidentialZones = globalIncludeResidentialZones;
//		includeUrbanZones = globalIncludeUrbanZones;
		
		
		// grab the world specific values else use the globals
		streetLevel = getWorldInt(config, "StreetLevel", globalStreetLevel);
		seabedLevel = getWorldInt(config, "SeabedLevel", globalSeabedLevel);
		maximumFloors = getWorldInt(config, "MaximumFloors", globalMaximumFloors);
		decrepitLevel = getWorldDouble(config, "DecrepitLevel", globalDecrepitLevel);
		includeAgricultureZones = getWorldBoolean(config, "IncludeAgricultureZones", globalIncludeAgricultureZones);
		includeResidentialZones = getWorldBoolean(config, "IncludeResidentialZones", globalIncludeResidentialZones);
		includeUrbanZones = getWorldBoolean(config, "IncludeUrbanZones", globalIncludeUrbanZones);
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
	
	private boolean getWorldBoolean(FileConfiguration config, String option, boolean global) {
		boolean result = global;
		String path = worldname + "." + option;
		if (config.isSet(path))
			result = config.getBoolean(path);
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
	
	public final static int minimumMaximumFloors = 5;
	public int getMaximumFloors() {
		return Math.max(maximumFloors, minimumMaximumFloors);
	}
	
	public double getDecrepitLevel() {
		return decrepitLevel;
	}
	
	public boolean getIncludeAgricultureZones() {
		return includeAgricultureZones;
	}
	
	public boolean getIncludeResidentialZones() {
		return includeResidentialZones;
	}
	
	public boolean getIncludeUrbanZones() {
		return includeUrbanZones;
	}
	
}
