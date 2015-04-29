package org.nationsatwar.goldfish.prototypes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.DimensionManager;

public class PrototypeManager {
	
	private static Map<Integer, Prototype> prototypeList = new HashMap<Integer, Prototype>();
	
	public static void addPrototype(String prototypeName) {
		
		if (prototypeExists(prototypeName)) {
			
			System.out.println("Prototype name already exists.");
			return;
		}
		
		int prototypeID = DimensionManager.getNextFreeDimId();
		
		DimensionManager.registerProviderType(prototypeID, PrototypeProvider.class, false);
		DimensionManager.registerDimension(prototypeID, prototypeID);
		
		Prototype prototype = new Prototype(prototypeName, prototypeID);
		prototypeList.put(prototypeID, prototype);
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
		
		File worldDirectory = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath());
		
		for (File file : worldDirectory.listFiles())
			if (file.isDirectory() && file.getName().length() > 10 && 
					file.getName().substring(0, 10).equals("Prototype_"))
					addPrototype(file.getName().substring(10));
	}
}