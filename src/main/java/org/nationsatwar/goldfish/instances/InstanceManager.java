package org.nationsatwar.goldfish.instances;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.teleports.createinstance.PacketCreateInstance;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportPoint;
import org.nationsatwar.goldfish.util.FileUtil;
import org.nationsatwar.palette.PlayerUtil;
import org.nationsatwar.palette.WorldLocation;

public class InstanceManager {
	
	private static Map<Integer, Instance> instanceList = new HashMap<Integer, Instance>();
	
	public static Map<EntityPlayer, WorldLocation> prepPlayers = new HashMap<EntityPlayer, WorldLocation>();
	
	// Server Calls this
	public static void enterInstance(String messageUUID, int prototypeID, int teleportID) {
		
		// Get relevant variables
		UUID playerUUID = UUID.fromString(messageUUID);
		Prototype prototype = PrototypeManager.getPrototype(prototypeID);
		TeleportPoint teleportPoint = prototype.getTeleportPoint(teleportID);
		WorldLocation destination = teleportPoint.getDestPoint();
		EntityPlayer player = PlayerUtil.getPlayerByUUID(playerUUID);
		
		System.out.println("1");
		
		// If there's no destination, there's no point
		if (destination == null)
			return;
		
		// If destination isn't a prototype, then simply teleport player
		if (!isDestinationAPrototype(destination)) {
			
			prepPlayers.put(player, destination);
			return;
		}
		
		System.out.println("2");
		
		// If destination is a prototype, then create an instance and teleport there instead
		String prototypeName = prototype.getPrototypeName();
		Instance instance = null;
		
		if (isPlayerInsideInstance(prototypeName, playerUUID))
			instance = getPlayerInstance(prototypeName, playerUUID);
		else
			instance = createInstance(prototypeName, playerUUID);
		
		if (instance == null)
			return;
		
		System.out.println("3");
		
		destination.setWorldID(instance.getInstanceID());
		prepPlayers.put(player, destination);
	}
	
	public static Instance getInstance(int instanceID) {
		
		return instanceList.get(instanceID);
	}

	// Client Calls this
	public static void createInstance(int prototypeID, int instanceID) {
		
		if (instanceList.containsKey(instanceID))
			return;

		
		System.out.println("4");
		DimensionManager.registerProviderType(instanceID, InstanceProvider.class, false);
		DimensionManager.registerDimension(instanceID, instanceID);
		
		String prototypeName = PrototypeManager.getPrototype(prototypeID).getPrototypeName();
		
		Instance instance = new Instance(prototypeName, instanceID);
		instanceList.put(instanceID, instance);
	}
	
	private static boolean isDestinationAPrototype(WorldLocation destination) {
		
		if (PrototypeManager.prototypeExists(destination.getWorldID()))
			return true;
		else
			return false;
	}

	// Server Calls this
	private static Instance createInstance(String prototypeName, UUID playerUUID) {
		
		int instanceID = DimensionManager.getNextFreeDimId();
		
		System.out.println("5");
		
		// Register Dimension for server
		DimensionManager.registerProviderType(instanceID, InstanceProvider.class, false);
		DimensionManager.registerDimension(instanceID, instanceID);
		
		Instance instance = new Instance(prototypeName, instanceID);
		instance.addPlayerUUID(playerUUID);
		
		instanceList.put(instanceID, instance);

		String saveDirectory = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/";
		
		File prototypeFile = new File(saveDirectory + Goldfish.prototypePath + 
				"Prototype_" + prototypeName);
		File instanceFile = new File(saveDirectory + Goldfish.instancePath + 
				"Instance_" + prototypeName + "_" + instanceID);
		
		FileUtil.copyDirectory(prototypeFile, instanceFile);
		
		int prototypeID = PrototypeManager.getPrototype(prototypeName).getPrototypeID();
		
		System.out.println("6");
		
		// Register Dimension for all players
		for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
			for (Object playerEntity : worldServer.playerEntities) {
				
				EntityPlayerMP player = (EntityPlayerMP) playerEntity;
				Goldfish.channel.sendTo(new PacketCreateInstance(prototypeID, instanceID), player);
			}
		}
		
		System.out.println("7");
		
		// Initialize the new world
		WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(instanceID);
		
		if (worldServer == null) {
			
			System.out.println("worldServer is null");
			return null;
		}
		
		if (!DimensionManager.isDimensionRegistered(instanceID)) {
			
			System.out.println("Dimension is not registered");
			return null;
		}
		
		System.out.println("8");
		
		worldServer.init();
		
		return instance;
	}
	
	private static boolean isPlayerInsideInstance(String prototypeName, UUID playerUUID) {
		
		for (Instance instance : instanceList.values())
			if (instance.getPrototypeName() == prototypeName && instance.playerExists(playerUUID))
				return true;
		
		return false;
	}
	
	private static Instance getPlayerInstance(String prototypeName, UUID playerUUID) {
		
		for (Instance instance : instanceList.values()) {
			
			if (instance.playerExists(playerUUID))
				return instance;
		}
		
		return null;
	}
}