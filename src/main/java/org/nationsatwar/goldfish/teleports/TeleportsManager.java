package org.nationsatwar.goldfish.teleports;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.teleports.add.PacketAddTeleport;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.prototypes.PrototypeMapData;
import org.nationsatwar.palette.PlayerUtil;
import org.nationsatwar.palette.WorldLocation;

public class TeleportsManager {
	
	public final static String TELEPORT_PREFIX_KEY = "GF_Teleport_";
	
	private static int activeTeleportPointID;
	
	public static void addTeleport(String messageUUID, int prototypeID, int teleportID, boolean syncClients) {
		
		Prototype prototype = PrototypeManager.getPrototype(prototypeID);
		
		if (!prototype.teleportPointExists(teleportID)) {
			
			UUID playerUUID = UUID.fromString(messageUUID);
			EntityPlayer player = PlayerUtil.getPlayerByUUID(playerUUID);
			
			TeleportPoint teleportPoint = new TeleportPoint(player);
			prototype.addTeleportPoint(teleportID, teleportPoint);
		}
		
		// Sync the teleport for all clients as well
		if (syncClients) {
			
			TeleportsManager.saveTeleportData(prototype, teleportID);
			
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketAddTeleport(messageUUID, prototypeID, teleportID), clientPlayer);
				}
			}
		}
	}
	
	public static void saveTeleportData(Prototype prototype, int teleportID) {
		
		// Saves the data for the prototype world
		String prefix = TELEPORT_PREFIX_KEY + teleportID + "_";
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		PrototypeMapData mapData = prototype.getMapData();
		
		// Saves source location data
		if (teleportPoint.getSourcePoint() != null) {
			
			WorldLocation worldLoc = teleportPoint.getSourcePoint();
			String worldName = worldLoc.getWorldName();
			double posX = worldLoc.getPosX();
			double posY = worldLoc.getPosY();
			double posZ = worldLoc.getPosZ();
			
			mapData.getData().setString(prefix + "Source_WorldName", worldName);
			mapData.getData().setDouble(prefix + "Source_PosX", posX);
			mapData.getData().setDouble(prefix + "Source_PosY", posY);
			mapData.getData().setDouble(prefix + "Source_PosZ", posZ);
		}
		
		// Saves destination location data
		if (teleportPoint.getDestPoint() != null) {
			
			WorldLocation worldLoc = teleportPoint.getSourcePoint();
			String worldName = worldLoc.getWorldName();
			double posX = worldLoc.getPosX();
			double posY = worldLoc.getPosY();
			double posZ = worldLoc.getPosZ();
			
			mapData.getData().setString(prefix + "Dest_WorldName", worldName);
			mapData.getData().setDouble(prefix + "Dest_PosX", posX);
			mapData.getData().setDouble(prefix + "Dest_PosY", posY);
			mapData.getData().setDouble(prefix + "Dest_PosZ", posZ);
		}
		
		String label = teleportPoint.getLabel();
		mapData.getData().setString(prefix + "Label", label);
		
		String message = teleportPoint.getMessage();
		mapData.getData().setString(prefix + "Message", message);
		
		int messageRadius = teleportPoint.getMessageRadius();
		mapData.getData().setInteger(prefix + "MessageRadius", messageRadius);
		
		int teleportRadius = teleportPoint.getTeleportRadius();
		mapData.getData().setInteger(prefix + "TeleportRadius", teleportRadius);
		mapData.setDirty(true);
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
	
	/**
	 * @return Returns the active teleport point ID
	 */
	public static int getActiveTeleportPointID() {
		
		return activeTeleportPointID;
	}

	/**
	 * @return Sets the active teleport point
	 */
	public static void setActiveTeleportPointID(int activeTeleportPointID) {
		
		TeleportsManager.activeTeleportPointID = activeTeleportPointID;
	}
}