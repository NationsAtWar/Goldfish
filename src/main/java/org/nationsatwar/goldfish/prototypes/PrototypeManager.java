package org.nationsatwar.goldfish.prototypes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.PacketPrototype;

public class PrototypeManager {
	
	public static Map<Integer, Prototype> prototypeList = new HashMap<Integer, Prototype>();
	
	/**
	 * This method should only be accessed by the server
	 * 
	 * @param prototypeName The new prototype name to be added
	 */
	public static void addPrototype(String prototypeName) {
		
		if (prototypeExists(prototypeName))
			return;
		
		int prototypeID = DimensionManager.getNextFreeDimId();
		
		// Register Dimension for server
		DimensionManager.registerProviderType(prototypeID, PrototypeProvider.class, false);
		DimensionManager.registerDimension(prototypeID, prototypeID);
		
		Prototype prototype = new Prototype(prototypeName, prototypeID);
		prototypeList.put(prototypeID, prototype);
		
		// Register Dimension for all players
		for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
			for (Object playerEntity : worldServer.playerEntities) {
				
				EntityPlayerMP player = (EntityPlayerMP) playerEntity;
				Goldfish.channel.sendTo(new PacketPrototype(prototypeName, prototypeID), player);
			}
		}
		
		// Initialize the new world
		WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(prototypeID);
		
		if (worldServer == null) {
			
			System.out.println("worldServer is null");
			return;
		}
		
		if (!DimensionManager.isDimensionRegistered(prototypeID)) {
			
			System.out.println("Dimension is not registered");
			return;
		}
		
		worldServer.init();
		
		// Sets the map data for the new world
		PrototypeMapData mapData = (PrototypeMapData) worldServer.getPerWorldStorage().loadData(PrototypeMapData.class, prototypeName);
		
		if (mapData == null) {
			
			System.out.println("Map Data Null");
			mapData = new PrototypeMapData(prototypeName);
			worldServer.getPerWorldStorage().setData(prototypeName, mapData);
		}
		
		System.out.println(mapData.getData().getString("lol"));
		
		//mapData.getData().setString("lol", "You fucker");
		//mapData.setDirty(true);
		
		prototype.setMapData(mapData);
		
		System.out.println("Server loaded: " + prototypeName + " under ID: " + prototypeID);
	}

	/**
	 * This method should only be accessed by the client
	 * 
	 * @param prototypeName The new prototype name to be added
	 * @param prototypeID The id of the prototype being added
	 */
	public static void addPrototype(String prototypeName, int prototypeID) {
		
		if (prototypeExists(prototypeName))
			return;

		DimensionManager.registerProviderType(prototypeID, PrototypeProvider.class, false);
		DimensionManager.registerDimension(prototypeID, prototypeID);
		
		Prototype prototype = new Prototype(prototypeName, prototypeID);
		prototypeList.put(prototypeID, prototype);
		
		System.out.println("Client loaded: " + prototypeName + " under ID: " + prototypeID);
	}
	
	public static boolean prototypeExists(String prototypeName) {
		
		for (Prototype prototype : prototypeList.values())
			if (prototype.getPrototypeName().equalsIgnoreCase(prototypeName))
				return true;
		
		return false;
	}
	
	public static Prototype getPrototype(String prototypeName) {
		
		for (Prototype prototype : prototypeList.values())
			if (prototype.getPrototypeName().equalsIgnoreCase(prototypeName))
				return prototype;
		
		return null;
	}
	
	public static Prototype getPrototype(int prototypeID) {
		
		return prototypeList.get(prototypeID);
	}
	
	public static void loadPrototypes() {
		
		// Create necessary folders
		String saveDirectory = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/";
		File goldfishDirectory = new File(saveDirectory + Goldfish.goldfishPath);
		File prototypeDirectory = new File(saveDirectory + Goldfish.prototypePath);
		File instanceDirectory = new File(saveDirectory + Goldfish.instancePath);
		
		if (!goldfishDirectory.exists())
			goldfishDirectory.mkdir();
		if (!prototypeDirectory.exists())
			prototypeDirectory.mkdir();
		if (!instanceDirectory.exists())
			instanceDirectory.mkdir();
		
		for (File file : prototypeDirectory.listFiles())
			if (file.isDirectory() && file.getName().length() > 10 && 
					file.getName().substring(0, 10).equals("Prototype_"))
				addPrototype(file.getName().substring(10));
	}
}