package org.nationsatwar.goldfish.Listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.nationsatwar.goldfish.Goldfish;
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
    	
    	String prototypeName = GoldfishUtility.getPrototypeName(worldName);

	    FileConfiguration prototypeConfig = plugin.goldfishManager.getPrototypeConfig(prototypeName);
    	
    	boolean respawnInstance = prototypeConfig.getBoolean(GoldfishPrototypeConfig.respawnInstance);
    	
    	// Will respawn the player at the specified respawn location or the default exit location if not specified
    	if (respawnInstance) {
    		
    		String respawnWorldName = prototypeConfig.getString(GoldfishPrototypeConfig.respawnLocationWorld);
    		
    		if (plugin.goldfishManager.prototypeExists(respawnWorldName))
    			respawnWorldName = Goldfish.prototypePath + respawnWorldName;
			
			int respawnX = prototypeConfig.getInt(GoldfishPrototypeConfig.respawnLocationX);
			int respawnY = prototypeConfig.getInt(GoldfishPrototypeConfig.respawnLocationY);
			int respawnZ = prototypeConfig.getInt(GoldfishPrototypeConfig.respawnLocationZ);
    		
			// Changes respawn to the instance entrance if no respawn world is set
    		if (respawnWorldName == null) {
    			
    			respawnWorldName = worldName;
    			
    			respawnX = prototypeConfig.getInt(GoldfishPrototypeConfig.exitsLocation1InstanceX);
    			respawnY = prototypeConfig.getInt(GoldfishPrototypeConfig.exitsLocation1InstanceY);
    			respawnZ = prototypeConfig.getInt(GoldfishPrototypeConfig.exitsLocation1InstanceZ);
    		}
        	
        	if (prototypeConfig.getInt(GoldfishPrototypeConfig.respawnCounter) > 0) {
        		
        		int instanceID = GoldfishUtility.getInstanceID(worldName);
        		String instanceName = prototypeName + "_" + instanceID;
        	    FileConfiguration instanceConfig = plugin.goldfishManager.getInstanceConfig(instanceName);
        	    
        	    String playerName = event.getPlayer().getName();
            	
            	int playerLives = instanceConfig.getInt("user." + playerName + ".lives") - 1;
            	
            	instanceConfig.set("user." + playerName + ".lives", playerLives);
            	plugin.goldfishManager.saveInstanceConfig(instanceConfig, instanceName);
            	
            	// Destroys instance if there are no more player lives
            	if (playerLives == 0) {
            		
            		plugin.goldfishManager.destroyInstance(worldName);
            		
            		// If respawn is inside instance, change respawn to default respawn entrance
            		if (respawnWorldName.equals(Goldfish.prototypePath + prototypeName)) {
            			
            			respawnWorldName = prototypeConfig.getString(GoldfishPrototypeConfig.entrancesLocation1EntranceWorld);

            			respawnX = prototypeConfig.getInt(GoldfishPrototypeConfig.entrancesLocation1EntranceX);
            			respawnY = prototypeConfig.getInt(GoldfishPrototypeConfig.entrancesLocation1EntranceY);
            			respawnZ = prototypeConfig.getInt(GoldfishPrototypeConfig.entrancesLocation1EntranceZ);
            		}
            	}
        	}
        	
        	String instanceName = GoldfishUtility.getExistingInstance(prototypeName, event.getPlayer().getName());
        	
        	if (instanceName != null)
        		respawnWorldName = instanceName;
			
			World respawnWorld = plugin.getServer().getWorld(respawnWorldName);
			
			if (respawnWorld == null) {
				
				plugin.getServer().createWorld(new WorldCreator(respawnWorldName).environment(Environment.NORMAL));
				respawnWorld = plugin.getServer().getWorld(respawnWorldName);
			}
    		
			event.setRespawnLocation(new Location(respawnWorld, respawnX, respawnY, respawnZ));
    	}
    }
}