package org.nationsatwar.goldfish.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Instance {
	
	private String prototypeName;
	private int instanceID;
	
	private List<UUID> playerUUIDs;
	
	public Instance(String prototypeName, int instanceID) {
		
		this.prototypeName = prototypeName;
		this.instanceID = instanceID;
		
		playerUUIDs = new ArrayList<UUID>();
	}
	
	public void addPlayerUUID(UUID playerUUID) {
		
		playerUUIDs.add(playerUUID);
	}
	
	public String getPrototypeName() {
		
		return prototypeName;
	}
	
	public int getInstanceID() {
		
		return instanceID;
	}
	
	public boolean playerExists(UUID playerUUID) {
		
		if (playerUUIDs.contains(playerUUID))
			return true;
		else
			return false;
	}
}