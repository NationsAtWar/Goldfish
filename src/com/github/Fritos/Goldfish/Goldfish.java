package com.github.Fritos.Goldfish;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Fritos.Goldfish.Listeners.GoldfishBlockListener;
import com.github.Fritos.Goldfish.Listeners.GoldfishRespawnListener;
import com.github.Fritos.Goldfish.Listeners.GoldfishLimitListener;
import com.github.Fritos.Goldfish.Listeners.GoldfishTimerListener;
import com.github.Fritos.Goldfish.Utility.GoldfishInstanceConfig;

public class Goldfish extends JavaPlugin {
	
	public GoldfishManager goldfishManager = new GoldfishManager(this);
	
	public static String goldfishPath = "plugins\\Goldfish\\";
	public static String prototypePath = "plugins\\Goldfish\\prototypes\\";
	public static String instancePath = "plugins\\Goldfish\\instances\\";
	
	private GoldfishCommandExecutor commandExecutor;
	
	Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {

		getServer().getPluginManager().registerEvents(new GoldfishBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new GoldfishLimitListener(this), this);
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
	   
	   log.info(logMessage);
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
				File dataFile = new File(instancePath + checkFile + "\\instancedata.yml");
				FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
				
				if (config.getBoolean(GoldfishInstanceConfig.instanceLoadOnStartup))
					getServer().createWorld(new WorldCreator(instancePath + checkFile).environment(Environment.NORMAL));
				
				GoldfishInstance instance = new GoldfishInstance(this, checkFile);
				goldfishManager.addInstance(instance);
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
			   
			   File dataFile = new File(instancePath + instanceName + "\\instancedata.yml");
			   FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
			   
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