package com.github.Fritos.Goldfish.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.Fritos.Goldfish.Goldfish;

public class GoldfishCommandList {

	private Goldfish plugin;
	
	public GoldfishCommandList(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player) {
		
		if (plugin.goldfishManager.getPrototypeNames().size() == 0) {
			
			player.sendMessage(ChatColor.YELLOW + "There are no instances yet.");
			return;
		}
		
		for ( String instanceName : plugin.goldfishManager.getPrototypeNames() )
			player.sendMessage(ChatColor.YELLOW + instanceName);
	}
}