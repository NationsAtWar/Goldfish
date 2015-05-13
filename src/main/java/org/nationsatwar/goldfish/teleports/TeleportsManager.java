package org.nationsatwar.goldfish.teleports;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.teleports.add.PacketAddTeleport;
import org.nationsatwar.goldfish.packets.teleports.label.PacketSetTeleportLabel;
import org.nationsatwar.goldfish.packets.teleports.message.PacketSetTeleportMessage;
import org.nationsatwar.goldfish.packets.teleports.messageradius.PacketSetMessageRadius;
import org.nationsatwar.goldfish.packets.teleports.teleportdest.PacketSetTeleportDest;
import org.nationsatwar.goldfish.packets.teleports.teleportradius.PacketSetTeleportRadius;
import org.nationsatwar.goldfish.packets.teleports.teleportsource.PacketSetTeleportSource;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.prototypes.PrototypeMapData;
import org.nationsatwar.palette.PlayerUtil;
import org.nationsatwar.palette.WorldLocation;

public class TeleportsManager {
	
	public final static String TELEPORT_PREFIX_KEY = "GF_Teleport_";
	public final static String TELEPORT_AMOUNT_KEY = "GF_Teleport_Amount";
	
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
		
		// Saves Teleport Amount data
		mapData.getData().setInteger(TELEPORT_AMOUNT_KEY, prototype.numberofTeleportPoints());
		
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
			
			WorldLocation worldLoc = teleportPoint.getDestPoint();
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
		
		// Makes sure nothing crashes and all data is saved immediately
		WorldServer worldServer = DimensionManager.getWorld(prototype.getPrototypeID());
		
		if (worldServer == null) {
			
			System.out.println("World is null");
			return;
		}
		
		MapStorage mapStorage = worldServer.getMapStorage();
		
		if (mapStorage == null) {
			
			System.out.println("Map Storage is null");
			return;
		}
		
		mapStorage.saveAllData();
	}
	
	public static void loadTeleportData(Prototype prototype) {
		
		PrototypeMapData mapData = prototype.getMapData();
		int amountOfTeleports = mapData.getData().getInteger(TELEPORT_AMOUNT_KEY);
		
		if (amountOfTeleports == 0)
			return;
		
		for (int i = 0; i < amountOfTeleports; i++) {
			
			String prefix = TELEPORT_PREFIX_KEY + i + "_";
			TeleportPoint teleportPoint = null;
			
			if (mapData.getData().hasKey(prefix + "Source_WorldName")) {
				
				String worldName = mapData.getData().getString(prefix + "Source_WorldName");
				double posX = mapData.getData().getDouble(prefix + "Source_PosX");
				double posY = mapData.getData().getDouble(prefix + "Source_PosY");
				double posZ = mapData.getData().getDouble(prefix + "Source_PosZ");
				
				WorldLocation worldLocation = new WorldLocation(worldName, posX, posY, posZ);
				teleportPoint = new TeleportPoint(worldLocation);
				
				prototype.addTeleportPoint(i, teleportPoint);
			}
			
			if (teleportPoint == null)
				return;
			
			if (mapData.getData().hasKey(prefix + "Dest_WorldName")) {
				
				String worldName = mapData.getData().getString(prefix + "Dest_WorldName");
				double posX = mapData.getData().getDouble(prefix + "Dest_PosX");
				double posY = mapData.getData().getDouble(prefix + "Dest_PosY");
				double posZ = mapData.getData().getDouble(prefix + "Dest_PosZ");
				
				WorldLocation worldLocation = new WorldLocation(worldName, posX, posY, posZ);
				teleportPoint.setDestPoint(worldLocation);
			}
			
			String label = mapData.getData().getString(prefix + "Label");
			teleportPoint.setLabel(label);
			
			String message = mapData.getData().getString(prefix + "Message");
			teleportPoint.setMessage(message);
			
			int messageRadius = mapData.getData().getInteger(prefix + "MessageRadius");
			teleportPoint.setMessageRadius(messageRadius);
			System.out.println(prefix + "MessageRadius: " + messageRadius);
			
			int teleportRadius = mapData.getData().getInteger(prefix + "TeleportRadius");
			teleportPoint.setTeleportRadius(teleportRadius);
		}
	}
	
	public static void removeTeleport(Prototype prototype, int teleportID, int amountOfTeleports) {
		
		// Prevents double removal
		if (prototype.numberofTeleportPoints() < amountOfTeleports)
			return;
		
		prototype.removeTeleportPoint(teleportID);
	}
	
	public static void setSourcePoint(Prototype prototype, WorldLocation worldLocation, int teleportID, boolean syncClients) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setSourcePoint(worldLocation);
		
		// Sync the teleport for all clients as well
		if (syncClients) {
			
			TeleportsManager.saveTeleportData(prototype, teleportID);
			
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketSetTeleportSource(worldLocation.getWorldName(), 
							worldLocation.getPosX(), worldLocation.getPosY(), worldLocation.getPosZ(), 
							prototype.getPrototypeID(), teleportID), clientPlayer);
				}
			}
		}
	}
	
	public static void setDestPoint(Prototype prototype, WorldLocation worldLocation, int teleportID, boolean syncClients) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setDestPoint(worldLocation);
		
		// Sync the teleport for all clients as well
		if (syncClients) {
			
			TeleportsManager.saveTeleportData(prototype, teleportID);
			
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketSetTeleportDest(worldLocation.getWorldName(), 
							worldLocation.getPosX(), worldLocation.getPosY(), worldLocation.getPosZ(), 
							prototype.getPrototypeID(), teleportID), clientPlayer);
				}
			}
		}
	}
	
	public static void setMessageRadius(Prototype prototype, int messageRadius, int teleportID, boolean syncClients) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setMessageRadius(messageRadius);
		
		// Sync the teleport for all clients as well
		if (syncClients) {
			
			TeleportsManager.saveTeleportData(prototype, teleportID);
			
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketSetMessageRadius(messageRadius, 
							prototype.getPrototypeID(), teleportID), clientPlayer);
				}
			}
		}
	}
	
	public static void setTeleportRadius(Prototype prototype, int teleportRadius, int teleportID, boolean syncClients) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setTeleportRadius(teleportRadius);
		
		// Sync the teleport for all clients as well
		if (syncClients) {
			
			TeleportsManager.saveTeleportData(prototype, teleportID);
			
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketSetTeleportRadius(teleportRadius, 
							prototype.getPrototypeID(), teleportID), clientPlayer);
				}
			}
		}
	}
	
	public static void setMessage(Prototype prototype, String message, int teleportID, boolean syncClients) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setMessage(message);
		
		// Sync the teleport for all clients as well
		if (syncClients) {
			
			TeleportsManager.saveTeleportData(prototype, teleportID);
			
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketSetTeleportMessage(prototype.getPrototypeID(), 
							teleportID, message), clientPlayer);
				}
			}
		}
	}
	
	public static void setLabel(Prototype prototype, String label, int teleportID, boolean syncClients) {
		
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		teleportPoint.setLabel(label);
		
		// Sync the teleport for all clients as well
		if (syncClients) {
			
			TeleportsManager.saveTeleportData(prototype, teleportID);
			
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketSetTeleportLabel(prototype.getPrototypeID(), 
							teleportID, label), clientPlayer);
				}
			}
		}
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