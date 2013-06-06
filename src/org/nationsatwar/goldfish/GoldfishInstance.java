package org.nationsatwar.goldfish;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;


public class GoldfishInstance implements java.io.Serializable {
	
	public transient Goldfish plugin;
	
	private static final long serialVersionUID = 1L;

	private String name;

	private boolean timerActive;
	private int timerAmount;
	
	private GoldfishThread timer;

	public GoldfishInstance(Goldfish plugin, String name) {
		
		this.plugin = plugin;
		
		this.name = name;
		
		File dataFile = new File(Goldfish.prototypePath + name + "\\prototypedata.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
		
		timerAmount = config.getInt(GoldfishPrototypeConfig.timerAmount);
		
		if (timerAmount > 0)
			timerActive = true;
	}
	
	public String getName() {
		
		return name;
	}
	
	public int getTimerAmount() {
		
		return timerAmount;
	}
	
	public boolean isTimerActive() {
		
		return timerActive;
	}
	
	public GoldfishThread getTimer() {
		
		return timer;
	}
	
	public void setTimer(GoldfishThread timer) {
		
		this.timer = timer;
	}
	
	public void startTimer() {
		
		timerActive = true;
		timer.activate();
	}
	
	public void stopTimer() {
		
		timerActive = false;
		timer.deactivate();
	}
}