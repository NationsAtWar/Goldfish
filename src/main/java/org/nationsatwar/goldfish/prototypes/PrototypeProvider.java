package org.nationsatwar.goldfish.prototypes;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;

public class PrototypeProvider extends WorldProvider {
	
	private String dimensionName;
	
	public PrototypeProvider() {}
	
	public void registerWorldChunkManager() {
		
		worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.taiga, 0.8F);
		
		Prototype prototype = PrototypeManager.getPrototype(this.dimensionId);
		this.dimensionName = prototype.getPrototypeName();
	}
	
	public String getDimensionName() {
		
		return dimensionName;
	}
	
	public String getInternalNameSuffix() {
		
		return "_prototype";
	}
	
	public String getSaveFolder() {
		
		return (dimensionId == 0 ? null : "/proto_" + dimensionName);
	}
}