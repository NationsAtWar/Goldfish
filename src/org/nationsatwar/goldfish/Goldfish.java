package org.nationsatwar.goldfish;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.nationsatwar.goldfish.Listeners.GoldfishBlockListener;
import org.nationsatwar.goldfish.Listeners.GoldfishInstanceListener;
import org.nationsatwar.goldfish.Listeners.GoldfishLimitListener;
import org.nationsatwar.goldfish.Listeners.GoldfishRespawnListener;
import org.nationsatwar.goldfish.Listeners.GoldfishTimerListener;
import org.nationsatwar.goldfish.Utility.GoldfishInstanceConfig;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;

public class Goldfish extends JavaPlugin {
	
	public HashMap<String, GoldfishHook> goldfishHooks;
	
	public GoldfishManager goldfishManager = new GoldfishManager(this);
	public GoldfishHook goldfishAPI = new GoldfishHook(this);
	
	public static String goldfishPath = "plugins/Goldfish/";
	public static String prototypePath = "plugins/Goldfish/prototypes/";
	public static String instancePath = "plugins/Goldfish/instances/";
	
	protected GoldfishCommandExecutor commandExecutor;
	
	Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		
		goldfishHooks = new HashMap<String, GoldfishHook>();
		
		getServer().getPluginManager().registerEvents(new GoldfishBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new GoldfishLimitListener(this), this);
		getServer().getPluginManager().registerEvents(new GoldfishInstanceListener(this), this);
		getServer().getPluginManager().registerEvents(new GoldfishRespawnListener(this), this);
		getServer().getPluginManager().registerEvents(new GoldfishTimerListener(this), this);
		
		commandExecutor = new GoldfishCommandExecutor(this);
		
    	getCommand("goldfish").setExecutor(commandExecutor);
    	getCommand("instance").setExecutor(commandExecutor);
    	
    	saveDefaultConfig();
    	loadInstances();
    	
    	for (World world : this.getServer().getWorlds())
    		logger(world.getName());
    	
    	logger("Goldfish has been enabled.");
	}
	
	public void onDisable() {
		
		saveInstances();
		logger("Goldfish has been disabled.");
	}
	
	public void logger(String logMessage) {
		
		log.info("Goldfish: " + logMessage);
	}
	
	public GoldfishHook fishhook(JavaPlugin externalPlugin) {
		
		GoldfishHook newGoldfishHook = new GoldfishHook(this);
		goldfishHooks.put(externalPlugin.getName(), newGoldfishHook);
		
		return newGoldfishHook;
	}
	
	private void loadInstances() {
		
		// Create necessary folders
		File goldfishDirectory = new File(goldfishPath);
		File prototypeDirectory = new File(prototypePath);
		File instanceDirectory = new File(instancePath);
		
		if (!goldfishDirectory.exists())
			goldfishDirectory.mkdir();
		
		if (!prototypeDirectory.exists())
			prototypeDirectory.mkdir();
		
		if (!instanceDirectory.exists())
			instanceDirectory.mkdir();
	   
	   // Load all prototypes into manager
	   try { goldfishManager.loadAll(); }
	   catch (Exception e) { logger("Error: " + e.getMessage()); }
	   
	   // Load all instances into manager and load the appropriate worlds
		File baseInstanceDir = new File(instancePath);
		
		for (String checkFile : baseInstanceDir.list()) {
		    
			GoldfishInstanceConfig.saveInstanceConfig(checkFile);
			
			try {
				GoldfishInstance instance = new GoldfishInstance(this, checkFile);
				goldfishManager.addInstance(instance);
				
				File instanceDataFile = new File(instancePath + checkFile + "/instancedata.yml");
				FileConfiguration instanceConfig = YamlConfiguration.loadConfiguration(instanceDataFile);
				
				String prototypeName = GoldfishUtility.getPrototypeName(checkFile);
				
				File prototypeDataFile = new File(prototypePath + prototypeName + "/prototypedata.yml");
				FileConfiguration prototypeConfig = YamlConfiguration.loadConfiguration(prototypeDataFile);
		    	
				// Reloads instance timers
				int instanceTimerAmount = instanceConfig.getInt(GoldfishInstanceConfig.instanceTimerAmount);
				
				if (instanceTimerAmount > 0) {
					
					GoldfishTimers instanceTimer = new GoldfishTimers(this, instancePath + checkFile, instanceTimerAmount, false);
			    	instance.setInstanceTimer(instanceTimer);
					instanceTimer.runTaskTimer(this, 0, 20);
				}
				
				// Reloads timeout timers
				int timeoutTimerAmount = prototypeConfig.getInt(GoldfishPrototypeConfig.timeoutTimerAmount);

				logger("Timer Amount: " + timeoutTimerAmount);
				logger("Data Location: " + instancePath + checkFile + "/prototypedata.yml");
				
				if (timeoutTimerAmount > 0) {
					
					logger("Creating timeout timer");
					logger("Instance Name: " + instance.getName());
					logger("Timer Name: " + instancePath + checkFile);
					
					GoldfishTimers timeoutTimer = new GoldfishTimers(this, instancePath + checkFile, timeoutTimerAmount, false);
			    	instance.setTimeoutTimer(timeoutTimer);
			    	timeoutTimer.runTaskTimer(this, 0, 20);
				}
				
				// Loads instance worlds that previously had players in them
				if (instanceConfig.getBoolean(GoldfishInstanceConfig.instanceLoadOnStartup))
					getServer().createWorld(new WorldCreator(instancePath + checkFile).environment(Environment.NORMAL));
			}
			catch (Exception e) { continue; }
		}
   }

   private void saveInstances() {
	   
	   try {
		   
		   goldfishManager.saveAll();
		   
		   // Determines whether or not an instance should be loaded on server startup
		   for (String instanceName : goldfishManager.getInstanceNames()) {
			   
			   World world = getServer().getWorld(instancePath + instanceName);
			   
			   if (world == null)
				   continue;
			   
			   File dataFile = new File(instancePath + instanceName + "/instancedata.yml");
			   FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
			   
			   // Saves current instance timers
			   int timerAmount = goldfishManager.findInstance(instanceName).getInstanceTimer().getTimerAmount();
			   config.set(GoldfishInstanceConfig.instanceTimerAmount, timerAmount);
			   
			   // Saves current instance player status (Determines if world should load on startup)
			   if (world.getPlayers().size() == 0)
				   config.set(GoldfishInstanceConfig.instanceLoadOnStartup, false);
			   else
				   config.set(GoldfishInstanceConfig.instanceLoadOnStartup, true);
			   
			   config.save(dataFile);
		   }
	   }
	   catch (Exception e) { logger(e.getMessage()); }
   }
}