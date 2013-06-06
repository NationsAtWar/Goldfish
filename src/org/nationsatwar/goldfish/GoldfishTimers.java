package org.nationsatwar.goldfish;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishTimers extends BukkitRunnable {
	
	private Goldfish plugin;
	
	private String instanceName;
	private int timer;
	private boolean active;

	public GoldfishTimers(Goldfish plugin, String instanceName, int timer, boolean active) {
		
		this.plugin = plugin;
		
		this.instanceName = instanceName;
		this.timer = timer;
		this.active = active;
	}

	public void run() {
		
		if (!active)
			return;
		
		timer--;
		
		World instanceWorld = plugin.getServer().getWorld(instanceName);
		
		if (instanceWorld != null) {
		
			if (timer % 10 == 0 && timer > 0) {
				
				for (Player player : instanceWorld.getPlayers())
					player.sendMessage("You have " + timer + " seconds to complete this instance.");
			}
			
			if (timer < 10 && timer > 0) {
				
				for (Player player : instanceWorld.getPlayers())
					player.sendMessage(String.valueOf(timer));
			}
		}
		
		if (timer <= 0) {
			
			GoldfishPrototype instance = plugin.goldfishManager.findPrototype(GoldfishUtility.getPrototypeName(instanceName));
			
			String entranceName = instance.getEntranceWorld();
			
			// If entrance world is an instance, this will find the appropriate instance for the player
			if (GoldfishUtility.isPrototype(entranceName) || GoldfishUtility.isInstance(entranceName))
				entranceName = GoldfishUtility.getPrototypeName(entranceName);
			
			World world = plugin.getServer().getWorld(entranceName);
			
			Location entrance = new Location(world, instance.getEntranceLocation().getX(), 
					instance.getEntranceLocation().getY(), instance.getEntranceLocation().getZ());
			
			if (instanceWorld != null)
				for (Player player : instanceWorld.getPlayers())
					player.teleport(entrance);
			
			plugin.getServer().unloadWorld(instanceName, true);
			
			File instanceDir = new File(instanceName);
			
			try { GoldfishUtility.deleteDirectory(instanceDir); }
			catch (IOException e) {	plugin.logger("Couldn't delete directory: " + e.getMessage()); }
			
			plugin.getServer().getScheduler().cancelTask(getTaskId());
		}
	}
	
	public void activate() {
		
		active = true;
	}
	
	public void deactivate() {
		
		active = false;
	}
	
	public int getTimerAmount() {
		
		return timer;
	}
	
	public void setTimerAmount(int timerAmount) {
		
		timer = timerAmount;
	}
}