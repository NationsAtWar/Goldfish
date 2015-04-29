package org.nationsatwar.goldfish;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterFix extends Teleporter {
	
	public TeleporterFix(WorldServer worldIn) {
		
		// All this class does is assure that the player isn't placed in a portal when loaded into an instance
		// I know, right.
		
		super(worldIn);
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw) {
		
		return;
	}
	
	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
		
		
		return false;
	}
	
	@Override
	public boolean makePortal(Entity entityIn) {
		
		return false;
	}
}