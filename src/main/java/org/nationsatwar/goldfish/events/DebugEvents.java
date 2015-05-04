package org.nationsatwar.goldfish.events;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.PacketPrototype;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.TeleporterFix;

public class DebugEvents {
	
	@SubscribeEvent
	public void breakBlockEvent(BlockEvent.BreakEvent event) {
		
		if (event.state.getBlock().getMaterial().equals(Material.grass)) {
			
			Prototype prototype = PrototypeManager.getPrototype("Test");
			
			if (prototype == null)
				return;
			
			if (prototype.getMapData() == null)
				return;
			
			if (prototype.getMapData().getData().getString("Test") == null)
				return;
			
			int dimensionID = prototype.getPrototypeID();
			
			WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dimensionID);
			
			MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) event.getPlayer(), 
					dimensionID, new TeleporterFix(worldServer));
			
			event.getPlayer().posY += 2;
		}
	}
	
	@SubscribeEvent
	public void worldLoadEvent(WorldEvent.Load event) {
		
		if (!event.world.isRemote)
			PrototypeManager.loadPrototypes();
	}
	
	@SubscribeEvent
	public void playerLoginEvent(PlayerLoggedInEvent event) {
		
		if (!event.player.worldObj.isRemote)
			for (Prototype prototype : PrototypeManager.prototypeList.values())
				Goldfish.channel.sendTo(new PacketPrototype(prototype.getPrototypeName(), prototype.getPrototypeID()), 
						(EntityPlayerMP) event.player);
	}
}