package org.nationsatwar.goldfish.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.GoldfishPortalTimer;
import org.nationsatwar.goldfish.Commands.GoldfishCommandEnter;
import org.nationsatwar.goldfish.Commands.GoldfishCommandLeave;

public class GoldfishInstanceListener implements Listener {
	
	public Goldfish plugin;
    
    public GoldfishInstanceListener(Goldfish plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
    	
    	if (event.getBlock().getTypeId() == 90)
        	event.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
    	
    	if (!event.getEntity().getType().equals(EntityType.PLAYER))
    		return;
    	
    	Player player = (Player) event.getEntity();
    	
    	if (!plugin.goldfishManager.doesPortalTimerExist(player.getName()))
    		return;
    	
    	plugin.goldfishManager.getPortalTimer(player.getName()).setTimer(4);
    }
    
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
    	
    	Player player = event.getPlayer();
    	
    	if (!plugin.goldfishManager.doesPortalTimerExist(player.getName())) {
    		
    		if (!new GoldfishCommandEnter(plugin).execute(player))
    			new GoldfishCommandLeave(plugin).execute(player);
    	}

		GoldfishPortalTimer portalTimer = new GoldfishPortalTimer(plugin, player.getName(), 4);
    	portalTimer.runTaskTimer(plugin, 0, 20);
    	
    	plugin.goldfishManager.addPortalTimer(player.getName(), portalTimer);
    	
    	event.setCancelled(true);
    }
}