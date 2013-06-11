package org.nationsatwar.goldfish;

import org.bukkit.scheduler.BukkitRunnable;

public class GoldfishPortalTimer extends BukkitRunnable {
	
	private Goldfish plugin;
	
	private String playerName;
	private int timer;

	public GoldfishPortalTimer(Goldfish plugin, String playerName, int timer) {
		
		this.plugin = plugin;
		
		this.playerName = playerName;
		this.timer = timer;
	}

	public void run() {
		
		timer--;
		
		if (timer <= 0) {
			
			plugin.goldfishManager.removePortalTimer(playerName);
			cancelTask();
		}
	}
	
	public void cancelTask() {
		
		plugin.getServer().getScheduler().cancelTask(getTaskId());
	}
	
	public void setTimer(int newTimer) {
		
		timer = newTimer;
	}
}