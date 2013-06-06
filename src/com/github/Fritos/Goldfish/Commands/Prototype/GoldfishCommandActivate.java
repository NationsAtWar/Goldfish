package com.github.Fritos.Goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.GoldfishPrototype;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

public class GoldfishCommandActivate {

	private Goldfish plugin;
	
	public GoldfishCommandActivate(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {
		
		GoldfishPrototype instance = plugin.goldfishManager.findPrototype(worldName);
		
		if (instance == null) {
			
			player.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
			return;
		}
		
		if (!instance.canActivate()) {
			
			player.sendMessage(ChatColor.YELLOW + "You have to set this prototype's entrance and exit first.");
			return;
		}
		
		GoldfishUtility.saveInstance(worldName);
		
		instance.toggleActivated();
		
		if (instance.isActivated())
			player.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been activated");
		else
			player.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been deactivated");
	}
}
