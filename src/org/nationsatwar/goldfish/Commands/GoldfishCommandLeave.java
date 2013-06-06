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
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishPrototype;
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
		
		for ( String instanceName : plugin.goldfishManager.getPrototypeNames() ) {
			
			GoldfishPrototype instance = plugin.goldfishManager.findPrototype(instanceName);

			// This will turn the full instance/prototype name to its base name
			if (GoldfishUtility.isPrototype(instanceName) || GoldfishUtility.isInstance(instanceName))
				instanceName = GoldfishUtility.getPrototypeName(instanceName);
			
			// Determines if player world is the same as cycled instance
			if (worldName.equals(instanceName)) {
				
				Location exit = new Location(player.getWorld(), instance.getExitLocation().getX(), 
						instance.getExitLocation().getY(), instance.getExitLocation().getZ());

				double exitDistance = player.getLocation().distance(exit);
				
				if (exitDistance < 10) {
					
					if (!instance.isEntranceSet()) {
						
						player.sendMessage(ChatColor.YELLOW + "There is no entrance location set.");
						return;
					}
					
					String entranceName = instance.getEntranceWorld();
					
					// If entrance world is an instance, this will find the appropriate instance for the player
					if (GoldfishUtility.isPrototype(entranceName) || GoldfishUtility.isInstance(entranceName))
						entranceName = GoldfishUtility.getPrototypeName(entranceName);
					
					entranceName = getExistingInstance(entranceName, player.getName());
					
					World world = plugin.getServer().getWorld(entranceName);
					
					if (world == null) {
						
						plugin.getServer().createWorld(new WorldCreator(entranceName).environment(Environment.NORMAL));
						world = plugin.getServer().getWorld(entranceName);
					}
					
					Location entrance = new Location(world, instance.getEntranceLocation().getX(), 
							instance.getEntranceLocation().getY(), instance.getEntranceLocation().getZ());
					
					player.teleport(entrance);
					return;
				}
			}
		}
		
		player.sendMessage(ChatColor.YELLOW + "No instance locations here.");
	}
	
	private String getExistingInstance(String checkName, String userName) {
		
	    String[] folderList = new File(Goldfish.instancePath).list();
		
		for (String fileName : folderList) {
			
			if (fileName.contains(checkName)) {
				
				File dataFile = new File(Goldfish.instancePath + fileName + "\\" + "instancedata.yml");
				
				if (dataFile.exists()) {
					
					FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
					
					if (config.contains("user." + userName))
						return Goldfish.instancePath + fileName;
				}
			}
		}
		
		return checkName;
	}
}