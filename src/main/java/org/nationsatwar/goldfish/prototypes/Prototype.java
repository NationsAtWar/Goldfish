package org.nationsatwar.goldfish.prototypes;

public class Prototype {
	
	private String prototypeName;
	private int prototypeID;
	
	private PrototypeMapData mapData;
	
	public Prototype(String prototypeName, int prototypeID) {
		
		this.prototypeName = prototypeName;
		this.prototypeID = prototypeID;
	}
	
	public String getPrototypeName() {
		
		return prototypeName;
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
}