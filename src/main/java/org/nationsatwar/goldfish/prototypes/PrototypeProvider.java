package org.nationsatwar.goldfish.prototypes;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;

import org.nationsatwar.goldfish.Goldfish;

public class PrototypeProvider extends WorldProvider {
	
	private String dimensionName;
	
	public PrototypeProvider() {}
	
	public void registerWorldChunkManager() {
		
		worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.extremeHills, 0.8F);
		
		worldChunkMgr.cleanupCache();
		
		Prototype prototype = PrototypeManager.getPrototype(this.dimensionId);
		this.dimensionName = prototype.getPrototypeName();
		this.setDimension(prototype.getPrototypeID());
	}
	
	public String getDimensionName() {
		
		return dimensionName;
	}
	
	public String getInternalNameSuffix() {
		
		return "_prototype";
	}
	
	public String getSaveFolder() {
		
		return (dimensionId == 0 ? null : Goldfish.prototypePath + "Prototype_" + dimensionName);
	}
}