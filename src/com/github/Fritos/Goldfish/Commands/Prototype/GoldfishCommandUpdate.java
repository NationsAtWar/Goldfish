package com.github.Fritos.Goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

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
		
		GoldfishUtility.saveInstance(worldName);
		
		player.sendMessage(ChatColor.YELLOW + "Prototype: " + worldName + " has been successfully updated!");
	}
}