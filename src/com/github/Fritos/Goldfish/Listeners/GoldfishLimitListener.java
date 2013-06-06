package com.github.Fritos.Goldfish.Listeners;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.github.Fritos.Goldfish.Goldfish;
import com.github.Fritos.Goldfish.Utility.GoldfishPrototypeConfig;
import com.github.Fritos.Goldfish.Utility.GoldfishUtility;

public class GoldfishLimitListener implements Listener {
	
	// Public to evade warning (debug purposes)
	public Goldfish plugin;
    
    public GoldfishLimitListener(Goldfish plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
    	
    	// Fires only if the world is in the instance folder
    	if (GoldfishUtility.isInstance(event.getWorld().getName())) {
    		
    		String prototypeName = GoldfishUtility.getPrototypeName(event.getWorld().getName());
    		
    		if (checkLimit(event, prototypeName))
    			event.getChunk().unload(true);
    	}
    }
    
    /*
     *  Checks to see if chunk is outside the config limit. True if it is, false otherwise
     */
    private boolean checkLimit(ChunkLoadEvent event, String prototypeName) {
	    
	    File dataFile = new File(Goldfish.prototypePath + prototypeName + "\\prototypedata.yml");
		
	    FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
	    
	    if (!config.getBoolean(GoldfishPrototypeConfig.limitActive))
	    	return false;
	    	
		int startX = config.getInt(GoldfishPrototypeConfig.limitOneX);
		int startZ = config.getInt(GoldfishPrototypeConfig.limitOneZ);
		int endX = config.getInt(GoldfishPrototypeConfig.limitTwoX);
		int endZ = config.getInt(GoldfishPrototypeConfig.limitTwoZ);
		
	    if (startZ > endZ) {
	    	
	    	int replace = startZ;
	    	startZ = endZ;
	    	endZ = replace;
	    }
		
	    if (startX > endX) {
	    	
	    	int replace = startX;
	    	startX = endX;
	    	endX = replace;
	    }
		
		int chunkX = event.getChunk().getX();
		int chunkZ = event.getChunk().getZ();
		
		if (chunkX > startX && chunkX < endX && chunkZ > startZ && chunkZ < endZ)
			return false;
		
		return true;
    }
}