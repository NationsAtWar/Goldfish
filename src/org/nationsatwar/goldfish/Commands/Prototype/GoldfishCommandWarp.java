package org.nationsatwar.goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishPrototype;


public class GoldfishCommandWarp {

	private Goldfish plugin;
	
	public GoldfishCommandWarp(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {

		if (!plugin.goldfishManager.prototypeExists(worldName)) {
			
			player.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
			return;
		}
		
		World protoWorld = plugin.getServer().getWorld(Goldfish.prototypePath + worldName);
		
		if (protoWorld == null) {
		    
		    plugin.getServer().createWorld(new WorldCreator(Goldfish.prototypePath + worldName).environment(Environment.NORMAL));
		    protoWorld = plugin.getServer().getWorld(Goldfish.prototypePath + worldName);
		}
		
		GoldfishPrototype instance = plugin.goldfishManager.findPrototype(worldName);
		
		if (!instance.isExitSet())
			player.teleport(protoWorld.getSpawnLocation());
		else
			player.teleport(new Location(protoWorld, instance.getExitLocation().getX(), 
					instance.getExitLocation().getY(), instance.getExitLocation().getZ()));
	}
}