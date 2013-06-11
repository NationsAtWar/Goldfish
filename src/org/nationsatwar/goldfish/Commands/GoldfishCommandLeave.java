package org.nationsatwar.goldfish.Commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishCommandLeave {

	private Goldfish plugin;
	
	public GoldfishCommandLeave(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player) {
		
		String worldName = player.getWorld().getName();
		
		if (GoldfishUtility.isPrototype(worldName) || GoldfishUtility.isInstance(worldName))
			worldName = GoldfishUtility.getPrototypeName(player.getWorld().getName());
		
		for ( String prototypeName : plugin.goldfishManager.getPrototypeNames() ) {
			
			FileConfiguration prototypeConfig = plugin.goldfishManager.getPrototypeConfig(prototypeName);
			
			int exitID = 1;
			boolean foundExit = false;
			
			// Attempts to find a nearby entrance from the cycled prototype
			while (true) {
				
				// Breaks if all numbered locations have been cycled through
				if (!prototypeConfig.contains("exits.location" + exitID))
					break;
				
				// Checks to see if the entrance is active and matches the player world
				if (prototypeConfig.getBoolean("exits.location" + exitID + ".active")) {
					
					float exitX = prototypeConfig.getInt("exits.location" + exitID + ".exitx");
					float exitY = prototypeConfig.getInt("exits.location" + exitID + ".exity");
					float exitZ = prototypeConfig.getInt("exits.location" + exitID + ".exitz");
					
					Location exitLocation = new Location(player.getWorld(), exitX, exitY, exitZ);

					double entranceDistance = player.getLocation().distance(exitLocation);
					
					// If the player is close enough, break and continue
					if (entranceDistance < 10) {
						
						foundExit = true;
						break;
					}
				}
				
				exitID++;
			}
			
			if (!foundExit)
				continue;
			
			// Sets the instance location to teleport the player to
			float instanceX = prototypeConfig.getInt("exits.location" + exitID + ".instancex");
			float instanceY = prototypeConfig.getInt("exits.location" + exitID + ".instancey");
			float instanceZ = prototypeConfig.getInt("exits.location" + exitID + ".instancez");
			
			String instanceName = prototypeConfig.getString("exits.location" + exitID + ".exitworld");
		    
		    // Changes name of instance depending on whether or not it's a static instance
		    if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.staticInstance))
		    	instanceName = plugin.goldfishManager.createInstance(prototypeName, player.getName(), true);
		    else {
			
				// Finds the instance associated with the player name, returns the first one found, null if none found
		    	instanceName = getExistingInstance(instanceName, player.getName());
				
				if (instanceName == null)
					instanceName = plugin.goldfishManager.createInstance(prototypeName, player.getName(), false);
		    }
		    
		    // This file damages world loading, get rid of it
		    File uidFile = new File(instanceName + "/uid.dat");
		    uidFile.delete();
			
			World instanceWorld = plugin.getServer().getWorld(instanceName);
			
			// Create world if it doesn't exist yet
			if (instanceWorld == null) {
				
				plugin.getServer().createWorld(new WorldCreator(instanceName).environment(Environment.NORMAL));
				instanceWorld = plugin.getServer().getWorld(instanceName);
			}
			
			Location instanceLocation = new Location(instanceWorld, instanceX, instanceY, instanceZ);
			
			instanceName = prototypeName + "_" + GoldfishUtility.getInstanceID(instanceName);
			
			if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.equipmentStore)) {
				
				if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.equipmentBooty))
					loadInventory(instanceName, player, instanceLocation, true);
				else
					loadInventory(instanceName, player, instanceLocation, false);
			}
				
			
			player.teleport(instanceLocation);
			
			return;
		}
		
		player.sendMessage(ChatColor.YELLOW + "There is no exit located here.");
	}
	
	private static String getExistingInstance(String checkName, String userName) {
		
	    String[] folderList = new File(Goldfish.instancePath).list();
		
		for (String fileName : folderList) {
			
			if (fileName.contains(checkName)) {
				
				File dataFile = new File(Goldfish.instancePath + fileName + "/instancedata.yml");
				
				if (dataFile.exists()) {
					
					FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
					
					if (config.contains("user." + userName))
						return Goldfish.instancePath + fileName;
				}
			}
		}
		
		return checkName;
	}
	
	private void loadInventory(String instanceName, Player player, Location exitLocation, boolean dropBooty) {
		
		FileConfiguration instanceConfig = plugin.goldfishManager.getInstanceConfig(instanceName);
		
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			
			String section = "user." + player.getName() + ".inventory" + i;

			ItemStack playerItemStack = player.getInventory().getItem(i);
			player.getInventory().clear(i);
			
			if (playerItemStack != null && dropBooty)
				exitLocation.getWorld().dropItem(exitLocation, playerItemStack);
				
			ItemStack itemStack = instanceConfig.getItemStack(section);
			
			if (itemStack != null)
				player.getInventory().setItem(i, itemStack);
		}
		
		plugin.goldfishManager.saveInstanceConfig(instanceConfig, instanceName);
	}
}