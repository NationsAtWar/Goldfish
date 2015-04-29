package org.nationsatwar.goldfish.Listeners;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishBlockListener implements Listener {
	
	// Public to evade warning (debug purposes)
	public Goldfish plugin;
    
    public GoldfishBlockListener(Goldfish plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	
    	String worldName = event.getBlock().getWorld().getName();
    	
    	// Cancels if not in instance
    	if (!worldName.contains(Goldfish.instancePath))
    		return;
    	
    	worldName = GoldfishUtility.getPrototypeName(worldName);
    	
    	if (determinePermission(event.getBlock().getTypeId(), "break", worldName))
    		return;
    	else
    		event.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
    	
    	String worldName = event.getBlock().getWorld().getName();
    	
    	// Cancels if not in instance
    	if (!worldName.contains(Goldfish.instancePath))
    		return;
    	
    	worldName = GoldfishUtility.getPrototypeName(worldName);
    	
    	if (determinePermission(event.getBlock().getTypeId(), "place", worldName))
    		return;
    	else
    		event.setCancelled(true);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    	
    	if (event.getClickedBlock() == null)
    		return;
    	
    	String worldName = event.getClickedBlock().getWorld().getName();
    	
    	// Cancels if not in instance
    	if (!worldName.contains(Goldfish.instancePath))
    		return;
    	
    	worldName = GoldfishUtility.getPrototypeName(worldName);
    	
    	if (determinePermission(event.getClickedBlock().getTypeId(), "use", worldName))
    		return;
    	else
    		event.setCancelled(true);
    }
    
    private boolean determinePermission(int blockID, String permissionType, String prototypeName) {
    	
    	File dataFile = new File(Goldfish.prototypePath + prototypeName + "/prototypedata.yml");
    	FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
    	
    	String permission = "block." + permissionType + "." + blockID;
    	boolean allowAll = config.getBoolean("block." + permissionType + ".allowall");
    	
    	// If no ID permission exists, it will check to see if the config allows all or not
    	if (!config.contains(permission)) {
    		
    		if (allowAll)
    			return true;
    		else
    			return false;
    	} else { // Otherwise check specific permission
    		
    		return config.getBoolean(permission);
    	}
    }
}