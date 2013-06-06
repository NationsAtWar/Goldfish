package com.github.Fritos.Goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.GoldfishPrototype;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

public class GoldfishCommandCreate {

	private Goldfish plugin;
	
	public GoldfishCommandCreate(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {
		
		if (plugin.goldfishManager.prototypeExists(worldName)) {
			
			player.sendMessage(ChatColor.YELLOW + "A prototype with that name already exists.");
			return;
		}
		
		for (int i = 0; i < worldName.length(); i++)
			if (worldName.charAt(i) == '_') {
				
				player.sendMessage(ChatColor.YELLOW + "A prototype can not have the character: '_' in it.");
				return;
			}
		
		plugin.getServer().createWorld(new WorldCreator(Goldfish.prototypePath + worldName).environment(Environment.NORMAL));
		
		GoldfishPrototype prototype = new GoldfishPrototype(plugin, worldName);
		plugin.goldfishManager.addPrototype(prototype);

		player.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been created. You may warp there at " +
				"your convenience");
		
	    GoldfishUtility.saveInstance(worldName);
	}
}