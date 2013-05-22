package com.github.Fritos.Goldfish;

import org.bukkit.World;

public class GoldfishThread extends Thread {
	
	public boolean stop;
	
	private Goldfish plugin;

	private String instanceName;
	private int delay;

	public GoldfishThread(Goldfish plugin, String instanceName, int delay) {
		
		super();
		
		this.plugin = plugin;
		this.instanceName = instanceName;
		this.delay = delay;
		
		setPriority(MIN_PRIORITY);
		
		setName(instanceName);	
		stop = false;
	}

	@Override
	public void run() {
		
		World instanceWorld = plugin.getServer().getWorld(instanceName);
		
		do {
			try {
				sleep(delay);
			} catch (InterruptedException e) { }
		} while (!stop && instanceWorld.getPlayers().size() >= 1);

		plugin.getServer().unloadWorld(instanceName, true);
	}
	
	@Override
	public boolean isInterrupted() {

		for (StackTraceElement stackTrack : this.getStackTrace())
			plugin.logger(this.getName() + " thread: " + stackTrack.getClassName());
		
		dumpStack();
		
		return true;
	}
}
