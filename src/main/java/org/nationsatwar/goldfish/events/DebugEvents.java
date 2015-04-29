package org.nationsatwar.goldfish.events;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.nationsatwar.goldfish.TeleporterFix;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class DebugEvents {
	
	@SubscribeEvent
	public void breakBlockEvent(BlockEvent.BreakEvent event) {
		
		if (event.state.getBlock().getMaterial().equals(Material.grass)) {
			
			Prototype prototype = PrototypeManager.getPrototype("Test6");
			
			System.out.println(prototype == null);
			int dimensionID = prototype.getPrototypeID();
			
			WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dimensionID);
			
			MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) event.getPlayer(), 
					dimensionID, new TeleporterFix(worldServer));
		}
	}
}