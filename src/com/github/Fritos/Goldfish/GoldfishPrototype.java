package com.github.Fritos.Goldfish;

import org.bukkit.Location;
import org.bukkit.World;

public class GoldfishPrototype implements java.io.Serializable {
	
	private transient Goldfish plugin;
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private boolean entranceSet;
	private boolean exitSet;
	
	private boolean activated;

	private String entranceWorld;
	private double entranceCoordX;
	private double entranceCoordY;
	private double entranceCoordZ;

	private String exitWorld;
	private double exitCoordX;
	private double exitCoordY;
	private double exitCoordZ;

	public GoldfishPrototype(Goldfish plugin, String newName) {
		
		this.plugin = plugin;
		
		name = newName;
		
		activated = false;
		
		entranceWorld = "";
		exitWorld = "";
	}
	
	public void setPlugin(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public String getName() {
		
		return name;
	}
	
	public boolean canActivate() {
		
		if (entranceSet && exitSet)
			return true;
		else
			return false;
	}
	
	public boolean isEntranceSet() {
		
		return entranceSet;
	}
	
	public boolean isExitSet() {
		
		return exitSet;
	}
	
	public boolean isActivated() {
		
		return activated;
	}
	
	public Location getEntranceLocation() {
		
		World world = plugin.getServer().getWorld(entranceWorld);
		return new Location(world, entranceCoordX, entranceCoordY, entranceCoordZ);
	}
	
	public Location getExitLocation() {

		World world = plugin.getServer().getWorld(exitWorld);
		return new Location(world, exitCoordX, exitCoordY, exitCoordZ);
	}
	
	public String getEntranceWorld() {
		
		return entranceWorld;
	}
	
	public String getExitWorld() {
		
		return exitWorld;
	}
	
	public void toggleActivated() {
		
		activated = !activated;
	}
	
	public void toggleEntranceSet() {
		
		entranceSet = !entranceSet;
	}
	
	public void toggleExitSet() {
		
		exitSet = !exitSet;
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
