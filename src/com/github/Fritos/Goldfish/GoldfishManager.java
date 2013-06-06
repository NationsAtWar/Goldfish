package com.github.Fritos.Goldfish;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
			plugin.logger("Failed");
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
}
