package org.nationsatwar.goldfish.prototypes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.prototypes.activate.PacketActivatePrototype;
import org.nationsatwar.goldfish.packets.prototypes.create.PacketCreatePrototype;
import org.nationsatwar.goldfish.packets.prototypes.delete.PacketDeletePrototype;
import org.nationsatwar.goldfish.packets.prototypes.rename.PacketRenamePrototype;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.goldfish.util.FileUtil;

public class PrototypeManager {
	
	public final static int MAX_PROTOTYPE_NAME_LENGTH = 20;
	
	public final static String PROTOTYPE_ACIVATED_KEY = "GF_Activated";
	
	public static String overlayText = "";
	
	public static Map<Integer, EntityPlayer> prepPlayers = new HashMap<Integer, EntityPlayer>();
	
	private static String createPrototypeName;
	private static Prototype activePrototype;
	private static int activePrototypePage;
	
	private static Map<Integer, Prototype> prototypeList = new HashMap<Integer, Prototype>();
	
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
				Goldfish.channel.sendTo(new PacketCreatePrototype(prototypeName, prototypeID), player);
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
			
			mapData = new PrototypeMapData(prototypeName);
			worldServer.getPerWorldStorage().setData(prototypeName, mapData);
		}
		
		prototype.setMapData(mapData);
		
		PrototypeManager.loadPrototypeData(prototype);
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
	}
	
	/**
	 * Loads the data saved in the prototype data folder back into this instance of the prototype
	 * 
	 * @param prototype The prototype which you want to load data from
	 */
	public static void loadPrototypeData(Prototype prototype) {
		
		PrototypeMapData mapData = prototype.getMapData();
		int prototypeID = prototype.getPrototypeID();
		
		// Load prototype properties
		boolean activated = mapData.getData().getBoolean(PROTOTYPE_ACIVATED_KEY);
		PrototypeManager.setPrototypeActivation(prototypeID, activated, true);
		
		TeleportsManager.loadTeleportData(prototype);
	}
	
	/**
	 * Checks to see if the prototype with the supplied prototypeName already exists
	 * 
	 * @param prototypeName The name of the prototype you're checking
	 * @return
	 */
	public static boolean prototypeExists(String prototypeName) {
		
		for (Prototype prototype : prototypeList.values())
			if (prototype.getPrototypeName().equalsIgnoreCase(prototypeName))
				return true;
		
		return false;
	}
	
	/**
	 * Checks to see if the prototype with the supplied prototypeID already exists
	 * 
	 * @param prototypeID The ID of the prototype you're checking
	 * @return
	 */
	public static boolean prototypeExists(int prototypeID) {
		
		if (prototypeList.containsKey(prototypeList))
			return true;
		
		return false;
	}
	
	/**
	 * I intend to deprecate this, be warned
	 */
	public static Map<Integer, Prototype> getAllPrototypes() {
		
		return prototypeList;
	}
	
	/**
	 * Gets prototype by its name
	 * 
	 * @param prototypeName The name of the prototype you want to retrieve
	 * @return The prototype you're trying to retrieve, default is null
	 */
	public static Prototype getPrototype(String prototypeName) {
		
		for (Prototype prototype : prototypeList.values())
			if (prototype.getPrototypeName().equalsIgnoreCase(prototypeName))
				return prototype;
		
		return null;
	}
	
	/**
	 * Gets prototype by its ID
	 * 
	 * @param prototypeID The ID of the prototype you want to retrieve
	 * @return
	 */
	public static Prototype getPrototype(int prototypeID) {
		
		return prototypeList.get(prototypeID);
	}
	
	/**
	 * Since the prototypes are sorted by their dimensionId, this method is useful
	 * for assigning a binary index number to each prototype
	 * 
	 * @param prototypeIndex The index of the prototype you want to retrieve
	 * @return The prototype you're trying to retrieve, default is null
	 */
	public static Prototype getPrototypeByIndex(int prototypeIndex) {
		
		int index = 0;
		
		for (int prototypeID : prototypeList.keySet()) {
			
			if (index == prototypeIndex)
				return getPrototype(prototypeID);
			
			index++;
		}
		
		return null;
	}
	
	/**
	 * @return Returns the active prototype
	 */
	public static Prototype getActivePrototype() {
		
		return activePrototype;
	}

	/**
	 * @return Sets the active prototype
	 */
	public static void setActivePrototype(Prototype activePrototype) {
		
		PrototypeManager.activePrototype = activePrototype;
	}

	/**
	 * @return Returns the active prototype page number
	 */
	public static int getActivePrototypePage() {
		
		return activePrototypePage;
	}

	/**
	 * Sets the active prototype page number
	 */
	public static void setActivePrototypePage(int activePrototypePage) {
		
		PrototypeManager.activePrototypePage = activePrototypePage;
	}

	/**
	 * @return Returns the prototype name trying to be created
	 */
	public static String getCreatePrototypeName() {
		
		return createPrototypeName;
	}

	/**
	 * Sets the prototype name trying to be created
	 */
	public static void setCreatePrototypeName(String createPrototypeName) {
		
		PrototypeManager.createPrototypeName = createPrototypeName;
	}

	/**
	 * @return Returns true if there is currently an active Prototype, false otherwise
	 */
	public static boolean activePrototypeIsSet() {
		
		if (activePrototype != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Loads all prototypes based on folder names of worlds in the appropriate folder
	 */
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
	
	/**
	 * Renames a prototype to the newPrototypeName
	 * 
	 * @param prototype The old prototype to be renamed
	 * @param newPrototypeName The new name you want the prototype to be set to
	 */
	public static void renamePrototype(String oldPrototypeName, String newPrototypeName) {

		String saveDirectory = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/";
		File oldPrototypeFile = new File(saveDirectory + Goldfish.prototypePath + 
				"Prototype_" + oldPrototypeName);
		File newPrototypeFile = new File(saveDirectory + Goldfish.prototypePath + 
				"Prototype_" + newPrototypeName);
		
		// This should only be called on the server
		if (oldPrototypeFile.exists()) {
			
			// Renames the folder to the new name
			oldPrototypeFile.renameTo(newPrototypeFile);
			
			File oldPrototypeData = new File(saveDirectory + Goldfish.prototypePath + 
					"Prototype_" + newPrototypeName + "/data/" + oldPrototypeName + ".dat");
			File newPrototypeData = new File(saveDirectory + Goldfish.prototypePath + 
					"Prototype_" + newPrototypeName + "/data/" + newPrototypeName + ".dat");
			
			// Renames the data file to the new name
			oldPrototypeData.renameTo(newPrototypeData);
			
			// Makes sure all clients also get their prototype renamed
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP player = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketRenamePrototype(oldPrototypeName, newPrototypeName), player);
				}
			}
		}
		
		// Makes sure everyone who still has the old prototype name has it changed to the new one internally
		Prototype prototype = getPrototype(oldPrototypeName);
		
		if (prototype == null)
			prototype = getPrototype(newPrototypeName);
		
		prototype.renamePrototype(newPrototypeName);
		DimensionManager.initDimension(prototype.getPrototypeID());
		
		WorldServer worldServer = DimensionManager.getWorld(prototype.getPrototypeID());
		
		// Sets the map data for the new world name
		PrototypeMapData mapData = (PrototypeMapData) worldServer.getPerWorldStorage().loadData(PrototypeMapData.class, newPrototypeName);
		mapData.setData(prototype.getMapData().getData());
		mapData.setDirty(true);
		prototype.setMapData(mapData);
	}
	
	/**
	 * Deletes a prototype
	 * 
	 * @param prototypeName The prototype to be deleted
	 */
	public static void deletePrototype(String prototypeName) {

		String saveDirectory = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/";
		File prototypeFile = new File(saveDirectory + Goldfish.prototypePath + "Prototype_" + prototypeName);
		
		// This should only be called on the server
		if (prototypeFile.exists()) {
			
			// Deletes the folder
			FileUtil.deleteDirectory(prototypeFile);
			
			// Makes sure all clients also get their prototype deleted internally
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP player = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketDeletePrototype(prototypeName), player);
				}
			}
		}
		
		Prototype prototype = getPrototype(prototypeName);
		
		if (prototype == null)
			return;
		
		int prototypeID = prototype.getPrototypeID();
		DimensionManager.unregisterDimension(prototypeID);
		DimensionManager.unloadWorld(prototypeID);
		
		prototypeList.remove(prototype.getPrototypeID());
	}
	
	/**
	 * Sets whether or not a prototype is activated, syncs clients if necessary.
	 * 
	 * @param prototypeID The ID of the prototype
	 * @param activated Set to true to activate, false otherwise
	 * @param syncClients Server Only, syncs all clients to the server's settings
	 */
	public static void setPrototypeActivation(int prototypeID, boolean activated, boolean syncClients) {
		
		Prototype prototype = PrototypeManager.getPrototype(prototypeID);
		prototype.setActivated(activated);
		
		// Syncs activation for all clients
		if (syncClients) {
			
			// Saves the data for the prototype world
			PrototypeMapData mapData = prototype.getMapData();
			mapData.getData().setBoolean(PrototypeManager.PROTOTYPE_ACIVATED_KEY, activated);
			mapData.setDirty(true);
			
			// Sync activation for all clients
			for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
				for (Object playerEntity : worldServer.playerEntities) {
					
					EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
					Goldfish.channel.sendTo(new PacketActivatePrototype(prototypeID, activated), clientPlayer);
				}
			}
		}
	}
}