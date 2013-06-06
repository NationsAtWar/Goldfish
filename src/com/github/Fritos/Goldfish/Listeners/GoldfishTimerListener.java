package com.github.Fritos.Goldfish.Listeners;

import java.io.File;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.GoldfishInstance;
import com.github.Fritos.Goldfish.Utility.GoldfishPrototypeConfig;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

public class GoldfishTimerListener implements Listener {
	
	// Public to evade warning (debug purposes)
	public Goldfish plugin;
    
    public GoldfishTimerListener(Goldfish plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	
    	// Deactivates Instance Timer if applicable
    	
    	String fromWorldName = event.getFrom().getWorld().getName();
    	
    	if (GoldfishUtility.isInstance(fromWorldName)) {
    		
    		String prototypeName = GoldfishUtility.getPrototypeName(fromWorldName);
    		
        	File dataFile = new File(Goldfish.prototypePath + prototypeName + "\\prototypedata.yml");
        	FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        	
        	if (!config.getBoolean(GoldfishPrototypeConfig.timerActiveWhenEmpty)) {
        		
        		World world = event.getFrom().getWorld();
        		
        		if (world != null && world.getPlayers().size() == 1) {
        			
        			int instanceID = GoldfishUtility.getInstanceID(fromWorldName);
        			String instanceName = prototypeName + "_" + instanceID;
        		
	        		GoldfishInstance instance = plugin.goldfishManager.findInstance(instanceName);
	        		instance.stopTimer();
        		}
        	}
    	}
    	
    	// Activates Instance Timer if applicable
    	
    	String toWorldName = event.getTo().getWorld().getName();
    	
    	if (GoldfishUtility.isInstance(toWorldName)) {
    		
    		String prototypeName = GoldfishUtility.getPrototypeName(toWorldName);
			int instanceID = GoldfishUtility.getInstanceID(toWorldName);
			String instanceName = prototypeName + "_" + instanceID;
    		
    		GoldfishInstance instance = plugin.goldfishManager.findInstance(instanceName);
    		instance.startTimer();
    	}
    }
}