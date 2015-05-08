package org.nationsatwar.goldfish.teleports;

import org.nationsatwar.palette.WorldLocation;

public class TeleportPoint {
	
	private WorldLocation sourcePoint;
	private WorldLocation destPoint;
	
	private String message;
	
	private int messageRadius = 10;
	private int teleportRadius = 5;
	
	public TeleportPoint() {
		
		
	}
	
	public WorldLocation getSourcePoint() {
		return sourcePoint;
	}
	
	public void setSourcePoint(WorldLocation sourcePoint) {
		this.sourcePoint = sourcePoint;
	}
	
	public WorldLocation getDestPoint() {
		return destPoint;
	}
	
	public void setDestPoint(WorldLocation destPoint) {
		this.destPoint = destPoint;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getMessageRadius() {
		return messageRadius;
	}
	
	public void setMessageRadius(int messageRadius) {
		this.messageRadius = messageRadius;
	}
	
	public int getTeleportRadius() {
		return teleportRadius;
	}
	
	public void setTeleportRadius(int teleportRadius) {
		this.teleportRadius = teleportRadius;
	}
}