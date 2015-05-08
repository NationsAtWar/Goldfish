package org.nationsatwar.goldfish.prototypes;

import java.util.ArrayList;
import java.util.List;

import org.nationsatwar.goldfish.teleports.TeleportPoint;

public class Prototype {
	
	private String prototypeName;
	private int prototypeID;
	
	private PrototypeMapData mapData;
	
	private boolean activated;
	
	private List<TeleportPoint> teleportPoints = new ArrayList<TeleportPoint>();
	
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
}