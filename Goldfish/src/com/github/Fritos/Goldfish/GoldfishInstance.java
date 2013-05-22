package com.github.Fritos.Goldfish;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

public class GoldfishInstance implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public String name;
	
	public boolean entranceSet;
	public boolean exitSet;
	
	private transient Goldfish plugin;
	
	private ArrayList<String> instanceCopies;
	
	private boolean activated;
	
	private ArrayList<Integer> destructibleBlocks;
	private boolean naked;
	private int minimumPlayers;
	private int timerAmount;

	private String entranceWorld;
	private double entranceCoordX;
	private double entranceCoordY;
	private double entranceCoordZ;

	private String exitWorld;
	private double exitCoordX;
	private double exitCoordY;
	private double exitCoordZ;

	public GoldfishInstance(Goldfish plugin, String newName) {
		
		this.plugin = plugin;
		
		name = newName;
		
		activated = false;
		
		destructibleBlocks = new ArrayList<Integer>();
		naked = false;
		
		minimumPlayers = 1;
		timerAmount = 0;
		
		entranceWorld = "";
		exitWorld = "";
	}
	
	public void setPlugin(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void addInstanceCopy(String copyName) {
		
		instanceCopies.add(copyName);
	}
	
	public boolean canActivate() {
		
		if (entranceSet && exitSet)
			return true;
		else
			return false;
	}
	
	public boolean isActivated() {
		
		return activated;
	}
	
	public ArrayList<Integer> getDestructibleBlocks() {
		
		return destructibleBlocks;
	}
	
	public boolean isNaked() {
		
		return naked;
	}
	
	public int getMinimumPlayers() {
		
		return minimumPlayers;
	}
	
	public int getTimerAmount() {
		
		return timerAmount;
	}
	
	public Location getEntranceLocation() {
		
		World world = plugin.getServer().getWorld(entranceWorld);
		return new Location(world, entranceCoordX, entranceCoordY, entranceCoordZ);
	}
	
	public Location getExitLocation() {

		World world = plugin.getServer().getWorld(exitWorld);
		return new Location(world, exitCoordX, exitCoordY, exitCoordZ);
	}
	
	public void toggleActivated() {
		
		activated = !activated;
	}
	
	public void setNewEntranceLocation(Location newLocation) {
		
		entranceWorld = newLocation.getWorld().getName();
		entranceCoordX = newLocation.getX();
		entranceCoordY = newLocation.getY();
		entranceCoordZ = newLocation.getZ();
		
		entranceSet = true;
	}
	
	public void setNewExitLocation(Location newLocation) {
		
		plugin.logger(newLocation.getWorld().getName());
		
		exitWorld = newLocation.getWorld().getName();
		exitCoordX = newLocation.getX();
		exitCoordY = newLocation.getY();
		exitCoordZ = newLocation.getZ();
		
		exitSet = true;
	}
}
