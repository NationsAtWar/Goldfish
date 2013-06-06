package com.github.Fritos.Goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

public class GoldfishCommandExit {

	private Goldfish plugin;
	
	public GoldfishCommandExit(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {
		
		for ( String instanceName : plugin.goldfishManager.getPrototypeNames() )
			if (player.getWorld().getName().equals(Goldfish.prototypePath + instanceName)) {
				
				plugin.goldfishManager.findPrototype(instanceName).setNewExitLocation(player.getLocation());
				player.sendMessage(ChatColor.YELLOW + "Prototype " + instanceName + "'s exit location has been set.");
				
				GoldfishUtility.saveInstance(instanceName);
				return;
			}

		player.sendMessage(ChatColor.YELLOW + "You are not inside an instance prototype.");
	}
}
