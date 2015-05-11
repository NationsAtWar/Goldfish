package org.nationsatwar.goldfish.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.prototypes.create.PacketCreatePrototype;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.TeleporterFix;

public class DebugEvents {
	
	@SubscribeEvent
	public void breakBlockEvent(ServerTickEvent event) {
		
		if (event.side == Side.SERVER && PrototypeManager.prepPlayers.size() > 0) {
			
			for (int prototypeID : PrototypeManager.prepPlayers.keySet()) {

				EntityPlayer player = PrototypeManager.prepPlayers.get(prototypeID);
				
				WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(prototypeID);
				
				MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) player, 
						prototypeID, new TeleporterFix(worldServer));
				
				player.posY += 2;
				
				PrototypeManager.prepPlayers.remove(prototypeID);
				return;
			}
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
			for (Prototype prototype : PrototypeManager.getAllPrototypes().values())
				Goldfish.channel.sendTo(new PacketCreatePrototype(prototype.getPrototypeName(), prototype.getPrototypeID()), 
						(EntityPlayerMP) event.player);
	}
}