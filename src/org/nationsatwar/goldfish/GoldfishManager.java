package org.nationsatwar.goldfish;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nationsatwar.goldfish.Utility.GoldfishInstanceConfig;
import org.nationsatwar.goldfish.Utility.GoldfishPrototypeConfig;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;

public class GoldfishManager {

	private Goldfish plugin;
	
	private HashMap<String, GoldfishPrototype> prototypes;
	private HashMap<String, GoldfishInstance> instances;

	public GoldfishManager(Goldfish plugin) {
		
		this.plugin = plugin;
		
		prototypes = new HashMap<String, GoldfishPrototype>();
		instances = new HashMap<String, GoldfishInstance>();
	}
	
	// Saving and Loading only affects Prototypes. Instances are handled completely through config files.
	
	public void saveAll() throws Exception {
		
		for (String prototypeName : getPrototypeNames()) {
			
			GoldfishPrototype prototype = findPrototype(prototypeName);
			save(prototype);
		}
	}
	
	public void loadAll() throws Exception {
		
		File prototypeDirectory = new File(Goldfish.prototypePath);
		
		for (File prototypeFile : prototypeDirectory.listFiles()) {
			
			int dot = prototypeFile.getName().lastIndexOf(".");
			
			if (!prototypeFile.isDirectory() && prototypeFile.getName().substring(dot + 1).equals("gf")) {
				
				String fileName = prototypeFile.getName().substring(0, dot);
				load(fileName);
			}
		}
	}
	
	private void save(GoldfishPrototype prototype) throws Exception {
		
		String fullPath = Goldfish.prototypePath + prototype.getName() + ".gf";
		
		FileOutputStream fileOut = new FileOutputStream(fullPath);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		
		out.writeObject(prototype);
		
		out.close();
		fileOut.close();
	}
	
	private void load(String fileName) throws Exception {
		
		GoldfishPrototype prototype = null;
		
		String fullPath = Goldfish.prototypePath + fileName + ".gf";
		
		FileInputStream fileIn = new FileInputStream(fullPath);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		
		prototype = (GoldfishPrototype) in.readObject();
		in.close();
		fileIn.close();
		
		if (prototype == null)
			plugin.logger("Failed to load: " + fileName);
		else {
			
			prototype.setPlugin(plugin);
			addPrototype(prototype);
		}
	}
	
	/*
	 *  Handles Prototype Commands
	 */
	public void addPrototype(GoldfishPrototype prototype) {
		
		prototypes.put(prototype.getName(), prototype);
	}
	
	public void removePrototype(GoldfishPrototype prototype) {
		
		prototypes.remove(prototype.getName());
	}
	
	public GoldfishPrototype findPrototype(String prototypeName) {
		
		return prototypes.get(prototypeName);
	}
	
	public boolean prototypeExists(String prototypeName) {
		
		return prototypes.containsKey(prototypeName);
	}
	
	public ArrayList<String> getPrototypeNames() {
		
		ArrayList<String> prototypeNames = new ArrayList<String>();
		prototypeNames.addAll(prototypes.keySet());
		
		return prototypeNames;
	}
	
	public FileConfiguration getPrototypeConfig(String prototypeName) {
		
	    File prototypeDataFile = new File(Goldfish.prototypePath + prototypeName + "\\prototypedata.yml");
	    FileConfiguration prototypeConfig = YamlConfiguration.loadConfiguration(prototypeDataFile);
		
		return prototypeConfig;
	}
	
	public void savePrototypeConfig(FileConfiguration prototypeConfig, String prototypeName) {
		
		try { prototypeConfig.save(Goldfish.prototypePath + prototypeName + "\\prototypedata.yml"); }
		catch (IOException e) { plugin.logger("Couldn't save config file " + prototypeName + ": " + e.getMessage()); }
	}
	
	/*
	 *  Handles Instance Commands
	 */
	public void addInstance(GoldfishInstance instance) {
		
		instances.put(instance.getName(), instance);
	}
	
	public void removeInstance(GoldfishInstance instance) {
		
		instances.remove(instance.getName());
	}
	
	public GoldfishInstance findInstance(String instanceName) {
		
		return instances.get(instanceName);
	}
	
	public boolean instanceExists(String instanceName) {
		
		return instances.containsKey(instanceName);
	}
	
	public ArrayList<String> getInstanceNames() {
		
		ArrayList<String> instanceNames = new ArrayList<String>();
		instanceNames.addAll(instances.keySet());
		
		return instanceNames;
	}
	
	public FileConfiguration getInstanceConfig(String instanceName) {
		
	    File instanceDataFile = new File(Goldfish.instancePath + instanceName + "\\instancedata.yml");
	    FileConfiguration instanceConfig = YamlConfiguration.loadConfiguration(instanceDataFile);
		
		return instanceConfig;
	}
	
	public void saveInstanceConfig(FileConfiguration instanceConfig, String instanceName) {
		
		try { instanceConfig.save(Goldfish.prototypePath + instanceName + "\\instancedata.yml"); }
		catch (IOException e) { plugin.logger("Couldn't save config file " + instanceName + ": " + e.getMessage()); }
	}
	
	// Extraneous Methods
	public String createInstance(String prototypeName, String userName, boolean isStatic) {
		
		int newID = 0;
		
		while (true) {
			
			String instanceName = "";
			String directoryName = "";
			
			if (!isStatic) {
			
				instanceName = prototypeName + "_" + newID;
				directoryName = Goldfish.instancePath + instanceName;
				File instanceDir = new File(directoryName + "\\");
				
				if (instanceDir.exists()) {
					
					newID++;
					continue;
				}

				// Copy prototype directory
			    File protoDir = new File(Goldfish.prototypePath + prototypeName + "\\");
			    GoldfishUtility.copyDirectory(protoDir, instanceDir);
			    
			} else {
				
				instanceName = prototypeName + "_static";
				directoryName = Goldfish.instancePath + instanceName;
				
				File instanceDir = new File(directoryName + "\\");
				
				// Copy prototype directory
			    File protoDir = new File(Goldfish.prototypePath + prototypeName + "\\");
			    GoldfishUtility.copyDirectory(protoDir, instanceDir);
			}
		    
		    File uidFile = new File(directoryName + "\\uid.dat");
		    uidFile.delete();
		    
		    // Create Config file
			GoldfishInstanceConfig.saveInstanceConfig(instanceName);

		    File prototypeDataFile = new File(directoryName + "\\prototypedata.yml");
		    File instanceDataFile = new File(directoryName + "\\instancedata.yml");

		    FileConfiguration prototypeConfig = YamlConfiguration.loadConfiguration(prototypeDataFile);
		    FileConfiguration instanceConfig = YamlConfiguration.loadConfiguration(instanceDataFile);
		    
		    // Set instance config defaults
		    instanceConfig.createSection("user." + userName);
	    	
	    	GoldfishInstance instance = new GoldfishInstance(plugin, instanceName);
	    	plugin.goldfishManager.addInstance(instance);
			
		    // Sets the instance timer if applicable
		    int instanceTimerAmount = prototypeConfig.getInt(GoldfishPrototypeConfig.instanceTimerAmount);
		    instanceConfig.set(GoldfishInstanceConfig.instanceTimerAmount, instanceTimerAmount);
		    
		    if (instanceTimerAmount > 0) {
		    	
				GoldfishTimers instanceTimer = new GoldfishTimers(plugin, directoryName, instanceTimerAmount, true);
		    	instance.setInstanceTimer(instanceTimer);
				instanceTimer.runTaskTimer(plugin, 0, 20);
		    }
			
		    // Sets the timeout timer if applicable
	    	int timeoutTimerAmount = prototypeConfig.getInt(GoldfishPrototypeConfig.timeoutTimerAmount);
	    	
		    if (timeoutTimerAmount > 0) {
		    	
				GoldfishTimers timeoutTimer = new GoldfishTimers(plugin, directoryName, timeoutTimerAmount, false);
		    	instance.setTimeoutTimer(timeoutTimer);
		    	timeoutTimer.runTaskTimer(plugin, 0, 20);
		    }
		    
		    prototypeDataFile.delete();
		    
		    try { instanceConfig.save(instanceDataFile); }
		    catch (IOException e) { plugin.logger(e.getMessage()); }
		    
		    return directoryName;
		}
	}
}
