package com.github.Fritos.Goldfish;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Goldfish extends JavaPlugin {
	
	public GoldfishManager goldfishManager = new GoldfishManager(this);
	
	private GoldfishCommandExecutor commandExecutor;
	
	Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		
		commandExecutor = new GoldfishCommandExecutor(this);
		
    	getCommand("goldfish").setExecutor(commandExecutor);
    	getCommand("instance").setExecutor(commandExecutor);
    	
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
	   
	   // Create necessary folders if necessary
	   File instanceDirectory = new File("plugins\\instances\\");
	   File prototypeDirectory = new File("plugins\\instances\\prototypes\\");
	   
	   if (!instanceDirectory.exists())
		   instanceDirectory.mkdir();
	   
	   if (!prototypeDirectory.exists())
		   prototypeDirectory.mkdir();
	   
	   try { goldfishManager.loadAll(); }
	   catch (Exception e) { logger(e.getMessage()); }
   }

   private void saveInstances() {
	   
	   try { goldfishManager.saveAll(); }
	   catch (Exception e) { logger(e.getMessage()); }
   }
}