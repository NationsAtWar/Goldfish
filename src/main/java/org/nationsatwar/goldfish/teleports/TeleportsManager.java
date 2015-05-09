package org.nationsatwar.goldfish.teleports;

import net.minecraft.entity.Entity;

import org.nationsatwar.goldfish.prototypes.Prototype;

public class TeleportsManager {
	
	public static void addTeleport(Prototype prototype, Entity entity, int teleportID) {
		
		TeleportPoint teleportPoint = new TeleportPoint(entity);
		prototype.addTeleportPoint(teleportID, teleportPoint);
	}
	
	public static void syncAddTeleport(Prototype prototype, Entity entity, int teleportID) {
		
		TeleportPoint teleportPoint = new TeleportPoint(entity);	
		
		if (!prototype.teleportPointExists(teleportID))
			prototype.addTeleportPoint(teleportID, teleportPoint);
	}
	
	public static void removeTeleport(Prototype prototype, int teleportID, int amountOfTeleports) {
		
		// Prevents double removal
		if (prototype.numberofTeleportPoints() < amountOfTeleports)
			return;
		
		prototype.removeTeleportPoint(teleportID);
	}
	
	public static void setSourcePoint(Prototype prototype, Entity entity, int teleportID) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setSourcePoint(entity);
	}
	
	public static void setDestPoint(Prototype prototype, Entity entity, int teleportID) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setDestPoint(entity);
	}
	
	public static void setMessageRadius(Prototype prototype, int messageRadius, int teleportID) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setMessageRadius(messageRadius);
	}
	
	public static void setTeleportRadius(Prototype prototype, int teleportRadius, int teleportID) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setTeleportRadius(teleportRadius);
	}
	
	public static void setMessage(Prototype prototype, String message, int teleportID) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setMessage(message);
	}
	
	public static void setLabel(Prototype prototype, String label, int teleportID) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setLabel(label);
	}
}