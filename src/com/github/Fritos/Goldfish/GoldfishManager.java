package com.github.Fritos.Goldfish;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;

public class GoldfishManager {
	
	private static String path = "plugins\\instances\\";
	
	private HashMap<String, GoldfishInstance> collection;
	private Goldfish plugin;

	public GoldfishManager(Goldfish plugin) {
		
		this.plugin = plugin;
		collection = new HashMap<String, GoldfishInstance>();
	}
	
	public void saveAll() throws Exception {
		
		for (String instanceName : getInstanceNames()) {
			
			GoldfishInstance instance = find(instanceName);
			save(instance);
		}
	}
	
	public void loadAll() throws Exception {
		
		File instanceDirectory = new File(path);
		
		for (File instanceFile : instanceDirectory.listFiles()) {
			
			int dot = instanceFile.getName().lastIndexOf(".");
			
			if (!instanceFile.isDirectory() && instanceFile.getName().substring(dot + 1).equals("gf")) {
				
				String fileName = instanceFile.getName().substring(0, dot);
				load(fileName);
			}
		}
	}
	
	public void save(GoldfishInstance instance) throws Exception {
		
		String fullPath = path + instance.name + ".gf";
		
		FileOutputStream fileOut = new FileOutputStream(fullPath);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		
		out.writeObject(instance);
		
		out.close();
		fileOut.close();
		
		plugin.getServer().unloadWorld(instance.name, true);
	}
	
	public void load(String fileName) throws Exception {
		
		GoldfishInstance instance = null;
		
		String fullPath = path + fileName + ".gf";
		
		FileInputStream fileIn = new FileInputStream(fullPath);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		
		instance = (GoldfishInstance) in.readObject();
		in.close();
		fileIn.close();
		
		if (instance == null)
			plugin.logger("Failed");
		else {
			
			instance.setPlugin(plugin);
			plugin.getServer().createWorld(new WorldCreator(fileName).environment(Environment.NORMAL));
			add(instance);
		}
	}
	
	public void add(GoldfishInstance instance) {
		
		collection.put(instance.name, instance);
	}
	
	public void remove(GoldfishInstance instance) {
		
		collection.remove(instance.name);
	}
	
	public GoldfishInstance find(String instanceName) {
		
		return collection.get(instanceName);
	}
	
	public boolean exists(String instanceName) {
		
		return collection.containsKey(instanceName);
	}
	
	public ArrayList<String> getInstanceNames() {
		
		ArrayList<String> instanceNames = new ArrayList<String>();
		instanceNames.addAll(collection.keySet());
		
		return instanceNames;
	}
}
