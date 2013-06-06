package com.github.Fritos.Goldfish.Commands;

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

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.GoldfishInstance;
import com.github.Fritos.Goldfish.GoldfishPrototype;
import com.github.Fritos.Goldfish.GoldfishThread;
import com.github.Fritos.Goldfish.Utility.GoldfishInstanceConfig;
import com.github.Fritos.Goldfish.Utility.GoldfishPrototypeConfig;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

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
					
					// If entrance world is an instance, this will find the appropriate instance for the player
					if (GoldfishUtility.isPrototype(exitName) || GoldfishUtility.isInstance(exitName))
						exitName = GoldfishUtility.getPrototypeName(exitName);
					
					// Commence teleportation
					exitName = getExistingInstance(exitName, player.getName());
					
					if (exitName == null)
						exitName = createInstance(prototypeName, player.getName());
				    
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
	
	private String createInstance(String prototypeName, String userName) {
		
		int newID = 0;
		
		while (true) {
			
			String instanceName = prototypeName + "_" + newID;
			
			String directoryName = Goldfish.instancePath + instanceName;

			File instanceDir = new File(directoryName + "\\");
			
			if (instanceDir.exists()) {
				
				newID++;
				continue;
			}

			// Copy prototype directory
		    File protoDir = new File(Goldfish.prototypePath + prototypeName + "\\");
			
		    GoldfishUtility.copyDirectory(protoDir, instanceDir);
		    
		    File uidFile = new File(directoryName + "\\uid.dat");
		    uidFile.delete();
		    
		    // Create Config file
			GoldfishInstanceConfig.saveInstanceConfig(instanceName);

		    File prototypeDataFile = new File(directoryName + "\\prototypedata.yml");
		    File instanceDataFile = new File(directoryName + "\\instancedata.yml");

		    FileConfiguration prototypeConfig = YamlConfiguration.loadConfiguration(prototypeDataFile);
		    FileConfiguration instanceConfig = YamlConfiguration.loadConfiguration(instanceDataFile);
		    
		    // Set instance config defaults
		    int defaultTimer = prototypeConfig.getInt(GoldfishPrototypeConfig.timerAmount);
		    
		    instanceConfig.set(GoldfishInstanceConfig.timerAmount, defaultTimer);
		    
		    instanceConfig.createSection("user." + userName);
			
		    // Sets the timer if applicable
		    if (defaultTimer > 0) {
		    	
		    	GoldfishInstance instance = new GoldfishInstance(plugin, instanceName);
		    	plugin.goldfishManager.addInstance(instance);
		    	
				GoldfishThread instanceTimer = new GoldfishThread(plugin, directoryName, 25, true);
		    	instance.setTimer(instanceTimer);
				instanceTimer.runTaskTimer(plugin, 0, 20);
		    }
		    
		    prototypeDataFile.delete();
		    
		    try { instanceConfig.save(instanceDataFile); }
		    catch (IOException e) { plugin.logger(e.getMessage()); }
		    
		    return directoryName;
		}
	}
}
