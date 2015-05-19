package org.nationsatwar.goldfish.teleports;

import net.minecraft.entity.Entity;

import org.nationsatwar.palette.WorldLocation;

public class TeleportPoint {
	
	private WorldLocation sourcePoint;
	private WorldLocation destPoint;

	private String label = "";
	private String message = "";
	
	private int messageRadius = 10;
	private int teleportRadius = 5;
	
	public TeleportPoint(WorldLocation sourcePoint) {
		
		this.sourcePoint = sourcePoint;
		
		label = "New Label";
		message = "Entering: " + sourcePoint.getWorldName();
	}
	
	public TeleportPoint(Entity entity) {
		
		this.sourcePoint = new WorldLocation(entity);
		
		label = "New Label";
		message = "Entering: " + sourcePoint.getWorldName();
	}
	
	public WorldLocation getSourcePoint() {
		return sourcePoint;
	}
	
	public void setSourcePoint(WorldLocation sourcePoint) {
		this.sourcePoint = sourcePoint;
	}
	
	public void setSourcePoint(Entity entity) {
		this.sourcePoint = new WorldLocation(entity);
	}
	
	public WorldLocation getDestPoint() {
		return destPoint;
	}
	
	public void setDestPoint(WorldLocation destPoint) {
		this.destPoint = destPoint;
	}
	
	public void setDestPoint(Entity entity) {
		this.destPoint = new WorldLocation(entity);
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
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