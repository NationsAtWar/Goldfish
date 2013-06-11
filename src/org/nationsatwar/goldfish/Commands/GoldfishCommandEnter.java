package org.nationsatwar.goldfish.Commands;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishHook;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;

public class GoldfishCommandEnter {

	private Goldfish plugin;
	
	public GoldfishCommandEnter(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public boolean execute(Player player) {
		
		String worldName = player.getWorld().getName();
		
		if (GoldfishUtility.isPrototype(worldName) || GoldfishUtility.isInstance(worldName))
			worldName = GoldfishUtility.getPrototypeName(player.getWorld().getName());
		
		for ( String prototypeName : plugin.goldfishManager.getPrototypeNames() ) {
			
			boolean foundEntrance = false;
			int entranceID = 0;
			
			for (Location entranceLocation : plugin.goldfishManager.getEntranceLocations(prototypeName)) {
				
				entranceID++;
				
				if (!entranceLocation.getWorld().getName().equals(worldName))
					continue;
				
				double entranceDistance = player.getLocation().distance(entranceLocation);
				
				// If the player is close enough, break and continue
				if (entranceDistance < 10) {
					
					foundEntrance = true;
					break;
				}
			}
			
			if (!foundEntrance)
				continue;
			
			// Check to see if all the conditions have been met
			if (!checkConditions(prototypeName, player))
				return false;
			
			FileConfiguration prototypeConfig = plugin.goldfishManager.getPrototypeConfig(prototypeName);
			
			// Sets the instance location to teleport the player to
			float instanceX = prototypeConfig.getInt("entrances.location" + entranceID + ".instancex");
			float instanceY = prototypeConfig.getInt("entrances.location" + entranceID + ".instancey");
			float instanceZ = prototypeConfig.getInt("entrances.location" + entranceID + ".instancez");
			
			String instanceName = prototypeName;
		    
		    // Creates instance if it doesn't already exist dependent on whether or not it's set to static
		    if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.staticInstance))
		    	instanceName = plugin.goldfishManager.createInstance(prototypeName, player.getName(), true);
		    else
		    	instanceName = plugin.goldfishManager.createInstance(prototypeName, player.getName(), false);
		    
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
			
			if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.equipmentStore))
				storeInventory(instanceName, player);
			
			instanceLocation.getBlock().setTypeId(90);
			
			player.teleport(instanceLocation);
			
			return true;
		}
		
		player.sendMessage(ChatColor.YELLOW + "There is no entrance located here.");
		
		return false;
	}
	
	private void storeInventory(String instanceName, Player player) {
		
		FileConfiguration instanceConfig = plugin.goldfishManager.getInstanceConfig(instanceName);
		
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			
			String section = "user." + player.getName() + ".inventory" + i;
			
			ItemStack itemStack = player.getInventory().getItem(i);
			
			if (itemStack != null) {
				
				instanceConfig.set(section, itemStack);
				player.getInventory().clear(i);
			} else
				instanceConfig.set(section, null);
		}
		
		plugin.goldfishManager.saveInstanceConfig(instanceConfig, instanceName);
	}
	
	private boolean checkConditions(String prototypeName, Player player) {
		
		FileConfiguration prototypeConfig = plugin.goldfishManager.getPrototypeConfig(prototypeName);

		// General condition allowance
		boolean conditionAllow = prototypeConfig.getBoolean(GoldfishPrototypeConfig.conditionAllow);
		
		if (!conditionAllow) {
			
			for (GoldfishHook goldfishHook : plugin.goldfishHooks.values()) {
				
				if (goldfishHook.prohibitedInstances.containsKey(prototypeName)) {
					
					player.sendMessage(ChatColor.YELLOW + goldfishHook.prohibitedInstances.get(prototypeName));
					return false;
				}
			}
			
			player.sendMessage(ChatColor.YELLOW + "You are not allowed in this instance."); // STUB: Handle message via hook if viable
			return false;
		}
		
		// Game Time condition allowance
		boolean conditionGameTimeActive = prototypeConfig.getBoolean(GoldfishPrototypeConfig.conditionGameTimeActive);
		
		if (conditionGameTimeActive) {
			
			int conditionGameTimeBegin = prototypeConfig.getInt(GoldfishPrototypeConfig.conditionGameTimeBegin) * 10;
			int conditionGameTimeEnd = prototypeConfig.getInt(GoldfishPrototypeConfig.conditionGameTimeEnd) * 10;
			
			int playerTime = (int) (player.getWorld().getTime() + 6000);
			
			if (playerTime >= 24000)
				playerTime -= 24000;
			
			if ((conditionGameTimeBegin < conditionGameTimeEnd && (playerTime < conditionGameTimeBegin || playerTime > conditionGameTimeEnd)) ||
					(conditionGameTimeBegin > conditionGameTimeEnd && (playerTime < conditionGameTimeBegin && playerTime > conditionGameTimeEnd))) {
				
				player.sendMessage(ChatColor.YELLOW + "You must wait between " + (conditionGameTimeBegin / 10) + " and " +
						(conditionGameTimeEnd / 10) + " to enter this instance.");
				return false;
			}
		}
		
		// Server Time condition allowance
		boolean conditionServerTimeActive = prototypeConfig.getBoolean(GoldfishPrototypeConfig.conditionServerTimeActive);
		
		if (conditionServerTimeActive) {
			
			int conditionServerTimeBegin = prototypeConfig.getInt(GoldfishPrototypeConfig.conditionServerTimeBegin);
			int conditionServerTimeEnd = prototypeConfig.getInt(GoldfishPrototypeConfig.conditionServerTimeEnd);
			
			Date date = new Date();
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(date);
			
			calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
			
			int playerTime = (int) ((calendar.get(Calendar.HOUR_OF_DAY) * 100) + calendar.get(Calendar.MINUTE));
			
			if ((conditionServerTimeBegin < conditionServerTimeEnd && (playerTime < conditionServerTimeBegin || playerTime > conditionServerTimeEnd)) ||
					(conditionServerTimeBegin > conditionServerTimeEnd && (playerTime < conditionServerTimeBegin && playerTime > conditionServerTimeEnd))) {
				
				player.sendMessage(ChatColor.YELLOW + "You must wait between " + conditionServerTimeBegin + " and " +
						conditionServerTimeEnd + " to enter this instance.");
				return false;
			}
		}

		// Item Require condition allowance
		for (String section : prototypeConfig.getConfigurationSection("condition.itemrequire").getKeys(false)) {
			
			int itemID = Integer.parseInt(section);
			
			int itemAmount = prototypeConfig.getInt("condition.itemrequire." + itemID);
			boolean conditionMet = false;
			
			for (ItemStack itemStack : player.getInventory()) {
				
				if (itemStack == null)
					continue;
				
				if (itemStack.getTypeId() == itemID)
					itemAmount -= itemStack.getAmount();
				
				if (itemAmount <= 0) {
					
					conditionMet = true;
					break;
				}
			}
			
			if (!conditionMet) {
				
				player.sendMessage(ChatColor.YELLOW + "You must have " + itemAmount + " more " +
						Material.getMaterial(itemID) + " to enter this instance.");
				return false;
			}
		}
		
		return true;
	}
}