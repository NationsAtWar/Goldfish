package org.nationsatwar.goldfish;

import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;

public class GoldfishHook {
	
	private Goldfish plugin;
	
	public HashMap<String, String> prohibitedInstances;
	
	public GoldfishHook(Goldfish plugin) {
		
		this.plugin = plugin;
		
		prohibitedInstances = new HashMap<String, String>();
	}
	
	public List<String> getPrototypes() {
		
		List<String> prototypeList = plugin.goldfishManager.getPrototypeNames();
		
		return prototypeList;
	}
	
	public void prohibitInstance(String prototypeName, String reason) {
		
		prohibitedInstances.put(prototypeName, reason);
		
		FileConfiguration prototypeConfig = plugin.goldfishManager.getPrototypeConfig(prototypeName);
		prototypeConfig.set(GoldfishPrototypeConfig.conditionAllow, false);
		
		plugin.goldfishManager.savePrototypeConfig(prototypeConfig, prototypeName);
	}
	
	public void allowInstance(String instanceName) {
		
		prohibitedInstances.remove(instanceName);
	}
}