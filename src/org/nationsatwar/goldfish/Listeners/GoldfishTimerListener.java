package org.nationsatwar.goldfish.Listeners;

import java.io.File;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishInstance;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishTimerListener implements Listener {
	
	// Public to evade warning (debug purposes)
	public Goldfish plugin;
    
    public GoldfishTimerListener(Goldfish plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	
    	// Handles timers dependent on the world that the player is teleporting from
    	String fromWorldName = event.getFrom().getWorld().getName();
    	
    	// Only works if the 'from world' is an instance
    	if (GoldfishUtility.isInstance(fromWorldName)) {
    		
    		String prototypeName = GoldfishUtility.getPrototypeName(fromWorldName);
    		
        	File dataFile = new File(Goldfish.prototypePath + prototypeName + "\\prototypedata.yml");
        	FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
    		
    		World world = event.getFrom().getWorld();
    		
    		// Sees if the world is now empty
    		if (world != null && world.getPlayers().size() == 1) {
    			
    			String instanceName = "";
    			
    			if (!config.getBoolean(GoldfishPrototypeConfig.staticInstance)) {
    				
	    			int instanceID = GoldfishUtility.getInstanceID(fromWorldName);
	    			instanceName = prototypeName + "_" + instanceID;
    			} else
	    			instanceName = prototypeName + "_static";
    		
        		GoldfishInstance instance = plugin.goldfishManager.findInstance(instanceName);
        		
        		int instanceTimerAmount = config.getInt(GoldfishPrototypeConfig.instanceTimerAmount);
        		boolean instanceTimerActiveWhenEmpty = config.getBoolean(GoldfishPrototypeConfig.instanceTimerActiveWhenEmpty);
        		
        		// Attempts to stop instance timer if instance is empty and timer is set to deactivate on empty
        		if (instance != null && instanceTimerAmount > 0 && !instanceTimerActiveWhenEmpty)
        			instance.stopInstanceTimer();
        		
        		int timeoutTimerAmount = config.getInt(GoldfishPrototypeConfig.timeoutTimerAmount);
        		
        		// Starts timeout timer if applicable
        		if (instance != null && timeoutTimerAmount > 0)
        			instance.startTimeoutTimer();
    		}
    	}
    	
    	// Handles timers dependent on the world that the player is teleporting to
    	String toWorldName = event.getTo().getWorld().getName();
    	
    	// Only works if the 'to world' is an instance
    	if (GoldfishUtility.isInstance(toWorldName)) {
    		
    		String instanceName = "";
    		
    		String prototypeName = GoldfishUtility.getPrototypeName(toWorldName);
    		
        	File dataFile = new File(Goldfish.prototypePath + prototypeName + "\\prototypedata.yml");
        	FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        	
    		if (!config.getBoolean(GoldfishPrototypeConfig.staticInstance)) {
    			
				int instanceID = GoldfishUtility.getInstanceID(toWorldName);
				instanceName = prototypeName + "_" + instanceID;
    		} else
    			instanceName = prototypeName + "_static";
    		
    		GoldfishInstance instance = plugin.goldfishManager.findInstance(instanceName);
    		
    		// Starts the instance timer and stops the timeout timer regardless of whether or not they're already active
    		if (instance != null) {
    			
    			if (config.getInt(GoldfishPrototypeConfig.instanceTimerAmount) > 0)
    				instance.startInstanceTimer();
    			
    			if (config.getInt(GoldfishPrototypeConfig.timeoutTimerAmount) > 0)
    				instance.stopTimeoutTimer();
    		}
    	}
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	for (String instanceName : plugin.goldfishManager.getInstanceNames()) {
    		
			File instanceDataFile = new File(Goldfish.instancePath + instanceName + "\\" + "instancedata.yml");
						
			if (instanceDataFile.exists()) {
				
				FileConfiguration instanceConfig = YamlConfiguration.loadConfiguration(instanceDataFile);
				
				// Checks all instances related to the joining user
				if (instanceConfig.contains("user." + event.getPlayer().getName())) {
					
					GoldfishInstance instance = plugin.goldfishManager.findInstance(instanceName);
					
					String prototypeName = GoldfishUtility.getPrototypeName(instanceName);
		    		
					File prototypeDataFile = new File(Goldfish.prototypePath + prototypeName + "\\" + "prototypedata.yml");
					FileConfiguration prototypeConfig = YamlConfiguration.loadConfiguration(prototypeDataFile);
					
					plugin.logger("Attempting to start timer");
					plugin.logger("Instance Name: " + instance.getName());
					
					// Starts instance timer if applicable
					if (prototypeConfig.getBoolean(GoldfishPrototypeConfig.instanceTimerActiveWhenEmpty) ||
							event.getPlayer().getWorld().getName().equals(Goldfish.instancePath + instanceName))
						instance.startInstanceTimer();
					
					// Starts timeout timer if applicable
					if (prototypeConfig.getInt(GoldfishPrototypeConfig.timeoutTimerAmount) > 0 &&
							!event.getPlayer().getWorld().getName().equals(Goldfish.instancePath + instanceName))
						instance.startTimeoutTimer();
				}
			}
    	}
    }
}