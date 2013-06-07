package org.nationsatwar.goldfish.Commands.Prototype;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishPrototype;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


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
		
		instance.toggleActivated();
		
		// Set entrance and exit location to the config file
	    FileConfiguration prototypeConfig = plugin.goldfishManager.getPrototypeConfig(worldName);
	    
	    if (instance.isActivated()) {
	    	
	    	prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1Active, true);
	    	prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1Active, true);
	    } else {
	    	
	    	prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1Active, false);
	    	prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1Active, false);
	    }
	    
	    String entranceWorld = GoldfishUtility.getPrototypeName(instance.getEntranceWorld());
	    
	    // Fixes the exit location to match the entrance world (Messy, I know)
	    Location exitLocation = instance.getExitLocation();
	    exitLocation.setWorld(plugin.getServer().getWorld(entranceWorld));
	    instance.setNewExitLocation(exitLocation);
	    
	    prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1EntranceWorld, entranceWorld);
	    prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1EntranceX, (int) instance.getEntranceLocation().getX());
	    prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1EntranceY, (int) instance.getEntranceLocation().getY());
	    prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1EntranceZ, (int) instance.getEntranceLocation().getZ());
	    prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1InstanceX, (int) instance.getExitLocation().getX());
	    prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1InstanceY, (int) instance.getExitLocation().getY());
	    prototypeConfig.set(GoldfishPrototypeConfig.entrancesLocation1InstanceZ, (int) instance.getExitLocation().getZ());
	    
	    prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1ExitWorld, entranceWorld);
	    prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1ExitX, (int) instance.getExitLocation().getX());
	    prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1ExitY, (int) instance.getExitLocation().getY());
	    prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1ExitZ, (int) instance.getExitLocation().getZ());
	    prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1InstanceX, (int) instance.getEntranceLocation().getX());
	    prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1InstanceY, (int) instance.getEntranceLocation().getY());
	    prototypeConfig.set(GoldfishPrototypeConfig.exitsLocation1InstanceZ, (int) instance.getEntranceLocation().getZ());
	    
	    plugin.goldfishManager.savePrototypeConfig(prototypeConfig, worldName);
		GoldfishUtility.saveInstance(worldName);
		
		if (instance.isActivated())
			player.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been activated");
		else
			player.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been deactivated");
	}
}
