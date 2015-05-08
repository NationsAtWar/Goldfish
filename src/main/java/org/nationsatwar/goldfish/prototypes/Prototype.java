package org.nationsatwar.goldfish.prototypes;

import java.util.HashMap;
import java.util.Map;

import org.nationsatwar.goldfish.teleports.TeleportPoint;

public class Prototype {
	
	private String prototypeName;
	private int prototypeID;
	
	private PrototypeMapData mapData;
	
	private boolean activated;
	
	private Map<Integer, TeleportPoint> teleportPoints = new HashMap<Integer, TeleportPoint>();
	
	public Prototype(String prototypeName, int prototypeID) {
		
		this.prototypeName = prototypeName;
		this.prototypeID = prototypeID;
	}
	
	public String getPrototypeName() {
		
		return prototypeName;
	}
	
	public void renamePrototype(String prototypeName) {
		
		this.prototypeName = prototypeName;
	}
	
	public int getPrototypeID() {
		
		return prototypeID;
	}
	
	public PrototypeMapData getMapData() {
		
		return mapData;
	}
	
	public void setMapData(PrototypeMapData mapData) {
		
		this.mapData = mapData;
	}
	
	public boolean isActivated() {
		
		return activated;
	}
	
	public void toggleActivated() {
		
		this.activated = !this.activated;
	}
	
	public TeleportPoint getTeleportPoint(int index) {
		
		return teleportPoints.get(index);
	}
	
	public boolean teleportPointExists(int teleportID) {
		
		if (teleportPoints.containsKey(teleportID))
			return true;
		else
			return false;
	}
	
	public void addTeleportPoint(int teleportID, TeleportPoint teleportPoint) {

		teleportPoints.put(teleportID, teleportPoint);
	}
	
	public int numberofTeleportPoints() {
		
		return teleportPoints.size();
	}
}