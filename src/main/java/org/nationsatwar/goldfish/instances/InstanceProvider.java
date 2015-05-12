package org.nationsatwar.goldfish.instances;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;

import org.nationsatwar.goldfish.Goldfish;

public class InstanceProvider extends WorldProvider {
	
	private String dimensionName;
	
	public InstanceProvider() {}
	
	public void registerWorldChunkManager() {
		
		worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.extremeHills, 0.8F);
		
		worldChunkMgr.cleanupCache();
		
		Instance instance = InstanceManager.getInstance(dimensionId);
		this.dimensionName = instance.getPrototypeName();
		this.setDimension(instance.getInstanceID());
	}
	
	public String getDimensionName() {
		
		return dimensionName;
	}
	
	public String getInternalNameSuffix() {
		
		return "_instance";
	}
	
	public String getSaveFolder() {
		
		return (dimensionId == 0 ? null : Goldfish.instancePath + "Instance_" + dimensionName + "_" + dimensionId);
	}
}