package org.nationsatwar.goldfish;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishInstance implements java.io.Serializable {
	
	public transient Goldfish plugin;
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private GoldfishTimers instanceTimer;
	private GoldfishTimers timeoutTimer;

	public GoldfishInstance(Goldfish plugin, String name) {
		
		this.plugin = plugin;
		
		this.name = name;
	}
	
	public String getName() {
		
		return name;
	}
	
	public GoldfishTimers getInstanceTimer() {
		
		return instanceTimer;
	}
	
	public void setInstanceTimer(GoldfishTimers instanceTimer) {
		
		this.instanceTimer = instanceTimer;
	}
	
	public void startInstanceTimer() {
		
		instanceTimer.activate();
	}
	
	public void stopInstanceTimer() {
		
		instanceTimer.deactivate();
	}
	
	public GoldfishTimers getTimeoutTimer() {
		
		return timeoutTimer;
	}
	
	public void setTimeoutTimer(GoldfishTimers timeoutTimer) {
		
		this.timeoutTimer = timeoutTimer;
	}
	
	public void startTimeoutTimer() {
		
		timeoutTimer.activate();
	}
	
	public void stopTimeoutTimer() {
		
		String prototypeName = GoldfishUtility.getPrototypeName(name);
		
    	File dataFile = new File(Goldfish.prototypePath + prototypeName + "/prototypedata.yml");
    	FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
		
		timeoutTimer.setTimerAmount(config.getInt(GoldfishPrototypeConfig.timeoutTimerAmount));
		timeoutTimer.deactivate();
	}
}