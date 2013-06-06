package org.nationsatwar.goldfish.Commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishInstance;
import org.nationsatwar.goldfish.GoldfishPrototype;
import org.nationsatwar.goldfish.GoldfishTimers;
import org.nationsatwar.goldfish.Utility.GoldfishInstanceConfig;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishCommandEnter {

	private Goldfish plugin;
	
	public GoldfishCommandEnter(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player) {
		
		String worldName = player.getWorld().getName();
		
		if (GoldfishUtility.isPrototype(worldName) || GoldfishUtility.isInstance(worldName))
			worldName = GoldfishUtility.getPrototypeName(player.getWorld().getName());
		
		for ( String prototypeName : plugin.goldfishManager.getPrototypeNames() ) {
			
			GoldfishPrototype prototype = plugin.goldfishManager.findPrototype(prototypeName);
			
			String entranceName = prototype.getEntranceWorld();

			// This will turn the full instance/prototype name to its base name
			if (GoldfishUtility.isPrototype(entranceName) || GoldfishUtility.isInstance(entranceName))
				entranceName = GoldfishUtility.getPrototypeName(entranceName);
			
			// Determines if player world is the same as cycled instance
			if (worldName.equals(entranceName)) {
				
				Location entranceLocation = new Location(player.getWorld(), prototype.getEntranceLocation().getX(), 
						prototype.getEntranceLocation().getY(), prototype.getEntranceLocation().getZ());

				double entranceDistance = player.getLocation().distance(entranceLocation);
				
				if (entranceDistance < 10 && prototype.isActivated()) {
					
					if (!prototype.isExitSet()) {
						
						player.sendMessage(ChatColor.YELLOW + "There is no exit location set.");
						return;
					}
					
					String exitName = prototype.getExitWorld();

					// This will turn the full instance/prototype name to its base name
					if (GoldfishUtility.isPrototype(exitName) || GoldfishUtility.isInstance(exitName))
						exitName = GoldfishUtility.getPrototypeName(exitName);

				    File prototypeDataFile = new File(Goldfish.prototypePath + exitName + "\\prototypedata.yml");
				    FileConfiguration prototypeConfig = YamlConfiguration.loadConfiguration(prototypeDataFile);
				    
				    // Changes name of instance depending on whether or not it's a static instance
				    if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.staticInstance))
				    	exitName = createInstance(prototypeName, player.getName(), true);
				    else {
					
						// Finds the instance associated with the player name, returns the first one found, null if none found
						exitName = getExistingInstance(exitName, player.getName());
						
						if (exitName == null)
							exitName = createInstance(prototypeName, player.getName(), false);
				    }
				    
				    // This file damages world loading, get rid of it
				    File uidFile = new File(exitName + "\\uid.dat");
				    uidFile.delete();
					
					World world = plugin.getServer().getWorld(exitName);
					
					if (world == null) {
						
						plugin.getServer().createWorld(new WorldCreator(exitName).environment(Environment.NORMAL));
						world = plugin.getServer().getWorld(exitName);
					}
					
					player.teleport(new Location(world, prototype.getExitLocation().getX(), 
							prototype.getExitLocation().getY(), prototype.getExitLocation().getZ()));
					
					return;
				}
			}
		}
		
		player.sendMessage(ChatColor.YELLOW + "No instance locations here.");
	}
	
	private String getExistingInstance(String instanceName, String userName) {
		
	    String[] folderList = new File(Goldfish.instancePath).list();
		
		for (String fileName : folderList) {
			
			if (fileName == null)
				continue;
			
			if (fileName.substring(0, instanceName.length()).equals(instanceName) &&
					fileName.charAt(instanceName.length()) == '_') {
				
				File dataFile = new File(Goldfish.instancePath + fileName + "\\" + "instancedata.yml");
				
				if (dataFile.exists()) {
					
					FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
					
					if (config.contains("user." + userName))
						return Goldfish.instancePath + fileName;
				}
			}
		}
		
		return null;
	}
	
	private String createInstance(String prototypeName, String userName, boolean isStatic) {
		
		int newID = 0;
		
		while (true) {
			
			String instanceName = "";
			String directoryName = "";
			
			if (!isStatic) {
			
				instanceName = prototypeName + "_" + newID;
				directoryName = Goldfish.instancePath + instanceName;
				File instanceDir = new File(directoryName + "\\");
				
				if (instanceDir.exists()) {
					
					newID++;
					continue;
				}

				// Copy prototype directory
			    File protoDir = new File(Goldfish.prototypePath + prototypeName + "\\");
			    GoldfishUtility.copyDirectory(protoDir, instanceDir);
			    
			} else {
				
				instanceName = prototypeName + "_static";
				directoryName = Goldfish.instancePath + instanceName;
				
				File instanceDir = new File(directoryName + "\\");
				
				// Copy prototype directory
			    File protoDir = new File(Goldfish.prototypePath + prototypeName + "\\");
			    GoldfishUtility.copyDirectory(protoDir, instanceDir);
			}
		    
		    File uidFile = new File(directoryName + "\\uid.dat");
		    uidFile.delete();
		    
		    // Create Config file
			GoldfishInstanceConfig.saveInstanceConfig(instanceName);

		    File prototypeDataFile = new File(directoryName + "\\prototypedata.yml");
		    File instanceDataFile = new File(directoryName + "\\instancedata.yml");

		    FileConfiguration prototypeConfig = YamlConfiguration.loadConfiguration(prototypeDataFile);
		    FileConfiguration instanceConfig = YamlConfiguration.loadConfiguration(instanceDataFile);
		    
		    // Set instance config defaults
		    instanceConfig.createSection("user." + userName);
	    	
	    	GoldfishInstance instance = new GoldfishInstance(plugin, instanceName);
	    	plugin.goldfishManager.addInstance(instance);
			
		    // Sets the instance timer if applicable
		    int instanceTimerAmount = prototypeConfig.getInt(GoldfishPrototypeConfig.instanceTimerAmount);
		    instanceConfig.set(GoldfishInstanceConfig.instanceTimerAmount, instanceTimerAmount);
		    
		    if (instanceTimerAmount > 0) {
		    	
				GoldfishTimers instanceTimer = new GoldfishTimers(plugin, directoryName, instanceTimerAmount, true);
		    	instance.setInstanceTimer(instanceTimer);
				instanceTimer.runTaskTimer(plugin, 0, 20);
		    }
			
		    // Sets the timeout timer if applicable
	    	int timeoutTimerAmount = prototypeConfig.getInt(GoldfishPrototypeConfig.timeoutTimerAmount);
	    	
		    if (timeoutTimerAmount > 0) {
		    	
				GoldfishTimers timeoutTimer = new GoldfishTimers(plugin, directoryName, timeoutTimerAmount, false);
		    	instance.setTimeoutTimer(timeoutTimer);
		    	timeoutTimer.runTaskTimer(plugin, 0, 20);
		    }
		    
		    prototypeDataFile.delete();
		    
		    try { instanceConfig.save(instanceDataFile); }
		    catch (IOException e) { plugin.logger(e.getMessage()); }
		    
		    return directoryName;
		}
	}
}