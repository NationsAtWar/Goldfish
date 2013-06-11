package org.nationsatwar.goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishCommandExit {

	private Goldfish plugin;
	
	public GoldfishCommandExit(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {
		
		for ( String prototypeName : plugin.goldfishManager.getPrototypeNames() )
			if (player.getWorld().getName().equals(Goldfish.prototypePath + prototypeName)) {
				
				plugin.goldfishManager.findPrototype(prototypeName).setNewExitLocation(player.getLocation());
			    
				player.sendMessage(ChatColor.YELLOW + "Prototype " + prototypeName + "'s exit location has been set.");
				GoldfishUtility.savePrototype(plugin, prototypeName);
				return;
			}

		player.sendMessage(ChatColor.YELLOW + "You are not inside an instance prototype.");
	}
}
