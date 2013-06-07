package org.nationsatwar.goldfish.Commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.nationsatwar.goldfish.Goldfish;
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
			
			FileConfiguration prototypeConfig = plugin.goldfishManager.getPrototypeConfig(prototypeName);
			
			int entranceID = 1;
			boolean foundEntrance = false;
			
			// Attempts to find a nearby entrance from the cycled prototype
			while (true) {
				
				// Breaks if all numbered locations have been cycled through
				if (!prototypeConfig.contains("entrances.location" + entranceID))
					break;
				
				// Checks to see if the entrance is active and matches the player world
				if (prototypeConfig.getBoolean("entrances.location" + entranceID + ".active") &&
						worldName.equals(prototypeConfig.getString("entrances.location" + entranceID + ".entranceworld"))) {
					
					float entranceX = prototypeConfig.getInt("entrances.location" + entranceID + ".entrancex");
					float entranceY = prototypeConfig.getInt("entrances.location" + entranceID + ".entrancey");
					float entranceZ = prototypeConfig.getInt("entrances.location" + entranceID + ".entrancez");
					
					Location entranceLocation = new Location(player.getWorld(), entranceX, entranceY, entranceZ);

					double entranceDistance = player.getLocation().distance(entranceLocation);
					
					// If the player is close enough, break and continue
					if (entranceDistance < 10) {
						
						foundEntrance = true;
						break;
					}
				}
				
				entranceID++;
			}
			
			if (!foundEntrance)
				continue;
			
			// Sets the instance location to teleport the player to
			float instanceX = prototypeConfig.getInt("entrances.location" + entranceID + ".instancex");
			float instanceY = prototypeConfig.getInt("entrances.location" + entranceID + ".instancey");
			float instanceZ = prototypeConfig.getInt("entrances.location" + entranceID + ".instancez");
			
			String instanceName = prototypeName;
		    
		    // Changes name of instance depending on whether or not it's a static instance
		    if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.staticInstance))
		    	instanceName = plugin.goldfishManager.createInstance(prototypeName, player.getName(), true);
		    else {
			
				// Finds the instance associated with the player name, returns the first one found, null if none found
		    	instanceName = GoldfishUtility.getExistingInstance(instanceName, player.getName());
				
				if (instanceName == null)
					instanceName = plugin.goldfishManager.createInstance(prototypeName, player.getName(), false);
		    }
		    
		    // This file damages world loading, get rid of it
		    File uidFile = new File(instanceName + "\\uid.dat");
		    uidFile.delete();
			
			World instanceWorld = plugin.getServer().getWorld(instanceName);
			
			// Create world if it doesn't exist yet
			if (instanceWorld == null) {
				
				plugin.getServer().createWorld(new WorldCreator(instanceName).environment(Environment.NORMAL));
				instanceWorld = plugin.getServer().getWorld(instanceName);
			}
			
			Location instanceLocation = new Location(instanceWorld, instanceX, instanceY, instanceZ);
			
			player.teleport(instanceLocation);
			
			return;
		}
		
		player.sendMessage(ChatColor.YELLOW + "There is no entrance located here.");
	}
}