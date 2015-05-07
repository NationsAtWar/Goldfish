package org.nationsatwar.goldfish.prototypes;

public class Prototype {
	
	private String prototypeName;
	private int prototypeID;
	
	private PrototypeMapData mapData;
	
	private boolean activated;
	
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