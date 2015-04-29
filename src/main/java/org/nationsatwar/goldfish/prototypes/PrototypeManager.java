package org.nationsatwar.goldfish.prototypes;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.DimensionManager;

public class PrototypeManager {
	
	private static Map<Integer, Prototype> prototypeList = new HashMap<Integer, Prototype>();
	
	public static void addPrototype(String prototypeName) {
		
		int prototypeID = DimensionManager.getNextFreeDimId();
		
		DimensionManager.registerProviderType(prototypeID, PrototypeProvider.class, false);
		DimensionManager.registerDimension(prototypeID, prototypeID);
		
		Prototype prototype = new Prototype(prototypeName, prototypeID);
		prototypeList.put(prototypeID, prototype);
	}
	
	public static Prototype getPrototype(String prototypeName) {
		
		for (Prototype prototype : prototypeList.values())
			if (prototype.getPrototypeName().equalsIgnoreCase(prototypeName))
				return prototype;
		
		System.out.println(prototypeName);
		
		return null;
	}
	
	public static Prototype getPrototype(int prototypeID) {
		
		return prototypeList.get(prototypeID);
	}
}