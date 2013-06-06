package org.nationsatwar.goldfish.Listeners;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishPrototype;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishRespawnListener implements Listener {
	
	// Public to evade warning (debug purposes)
	public Goldfish plugin;
    
    public GoldfishRespawnListener(Goldfish plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	
    	String worldName = event.getPlayer().getWorld().getName();
    	
    	// Cancels if not in instance
    	if (!worldName.contains(Goldfish.instancePath))
    		return;
    	
    	worldName = GoldfishUtility.getPrototypeName(worldName);
    	GoldfishPrototype instance = plugin.goldfishManager.findPrototype(worldName);
	    
	    File dataFile = new File(Goldfish.prototypePath + worldName + "\\prototypedata.yml");
	    FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
    	
    	boolean respawnInstance = config.getBoolean(GoldfishPrototypeConfig.respawnInstance);
    	boolean respawnInside = config.getBoolean(GoldfishPrototypeConfig.respawnInside);
    	
    	if (respawnInstance) {
    		
    		if (respawnInside)
    			worldName = GoldfishUtility.getPrototypeName(instance.getExitWorld());
    		else
    			worldName = GoldfishUtility.getPrototypeName(instance.getEntranceWorld());
    		
    		worldName = GoldfishUtility.getExistingInstance(worldName, event.getPlayer().getName());
    		
    		if (worldName == null)
    			worldName = instance.getEntranceWorld();
			
			World world = plugin.getServer().getWorld(worldName);
			
			if (world == null) {
				
				plugin.getServer().createWorld(new WorldCreator(worldName).environment(Environment.NORMAL));
				world = plugin.getServer().getWorld(worldName);
			}
    		
			if (respawnInside)
	    		event.setRespawnLocation(new Location(world, instance.getExitLocation().getX(), 
	    			instance.getExitLocation().getY(), instance.getExitLocation().getZ()));
			else
	    		event.setRespawnLocation(new Location(world, instance.getEntranceLocation().getX(), 
		    			instance.getEntranceLocation().getY(), instance.getEntranceLocation().getZ()));
    	}
    }
}