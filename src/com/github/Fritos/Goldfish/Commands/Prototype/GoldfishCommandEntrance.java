package com.github.Fritos.Goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

public class GoldfishCommandEntrance {

	private Goldfish plugin;
	
	public GoldfishCommandEntrance(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {

		if (!plugin.goldfishManager.prototypeExists(worldName)) {
			
			player.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
			return;
		}
		
		plugin.goldfishManager.findPrototype(worldName).setNewEntranceLocation(player.getLocation());

		player.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + "'s entrance location has been set.");
		
		GoldfishUtility.saveInstance(worldName);
	}
}
