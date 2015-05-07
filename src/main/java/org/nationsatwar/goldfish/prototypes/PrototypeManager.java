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
import org.nationsatwar.goldfish.packets.PacketCreatePrototype;

public class PrototypeManager {
	
	public final static int MAX_PROTOTYPE_NAME_LENGTH = 20;
	
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
		
		// Functionality for saving world data. Will be useful later.
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
		File oldPrototypeFile = new File(saveDirectory + Goldfish.prototypePath + "Prototype_" + oldPrototypeName);
		File newPrototypeFile = new File(saveDirectory + Goldfish.prototypePath + "Prototype_" + newPrototypeName);

		oldPrototypeFile.renameTo(newPrototypeFile);
		
		// If the client isn't the same as the server, then the server still needs to change the name internally
		Prototype prototype = getPrototype(oldPrototypeName);
		
		if (prototype == null)
			return;
		
		prototype.renamePrototype(newPrototypeName);
	}
	
	/**
	 * Deletes a prototype
	 * 
	 * @param prototypeName The prototype to be deleted
	 */
	public static void deletePrototype(String prototypeName) {

		String saveDirectory = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/";
		File prototypeFile = new File(saveDirectory + Goldfish.prototypePath + "Prototype_" + prototypeName);
		
		prototypeFile.delete();
		
		Prototype prototype = getPrototype(prototypeName);
		
		if (prototype == null)
			return;
		
		prototypeList.remove(prototype.getPrototypeID());
	}
}