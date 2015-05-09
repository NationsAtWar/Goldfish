package org.nationsatwar.goldfish.prototypes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class PrototypeMapData extends WorldSavedData {
	
	private NBTTagCompound data = new NBTTagCompound();

	public PrototypeMapData(String name) {
		
		super(name);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		data = nbt.getCompoundTag(this.mapName);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
		nbt.setTag(this.mapName, data);
	}
	
	public NBTTagCompound getData() {
		
		return data;
	}
	
	public void setData(NBTTagCompound data) {
		
		this.data = data;
	}
}