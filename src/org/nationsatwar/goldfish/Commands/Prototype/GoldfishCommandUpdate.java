package org.nationsatwar.goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishPrototype;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishCommandUpdate {

	private Goldfish plugin;
	
	public GoldfishCommandUpdate(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {

		if (!plugin.goldfishManager.prototypeExists(worldName)) {
			
			player.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
			return;
		}
		
		GoldfishPrototype prototype = plugin.goldfishManager.findPrototype(worldName);
		
		GoldfishUtility.savePrototype(plugin, worldName);
		
		if (prototype.isActivated()) {
			
			for (Location entranceLocation : plugin.goldfishManager.getEntranceLocations(worldName))
				entranceLocation.getBlock().setTypeId(90);
			for (Location entranceLocation : plugin.goldfishManager.getExitLocations(worldName))
				entranceLocation.getBlock().setTypeId(90);
		}
		else {
			
			for (Location entranceLocation : plugin.goldfishManager.getEntranceLocations(worldName))
				entranceLocation.getBlock().setTypeId(1);
			for (Location entranceLocation : plugin.goldfishManager.getExitLocations(worldName))
				entranceLocation.getBlock().setTypeId(1);
		}
		
		player.sendMessage(ChatColor.YELLOW + "Prototype: " + worldName + " has been successfully updated!");
	}
}