package org.nationsatwar.goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;

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
		GoldfishUtility.savePrototype(plugin, worldName);
	}
}