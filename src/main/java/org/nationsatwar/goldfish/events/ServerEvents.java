package org.nationsatwar.goldfish.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.instances.InstanceManager;
import org.nationsatwar.goldfish.packets.prototypes.create.PacketCreatePrototype;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.TeleporterFix;
import org.nationsatwar.palette.WorldLocation;

public class ServerEvents {
	
	@SubscribeEvent
	public void serverTickEvent(ServerTickEvent event) {
		
		if (event.side == Side.SERVER && PrototypeManager.prepPlayers.size() > 0) {
			
			for (int prototypeID : PrototypeManager.prepPlayers.keySet()) {

				EntityPlayer player = PrototypeManager.prepPlayers.get(prototypeID);
				
				WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(prototypeID);
				
				MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) player, 
						prototypeID, new TeleporterFix(worldServer));
				
				for (int i = 60; i < 120; i++) {
					
					if (worldServer.isAirBlock(new BlockPos(player.posX, i, player.posZ))) {
						
						player.posY = i;
						break;
					}
				}
				
				PrototypeManager.prepPlayers.remove(prototypeID);
				return;
			}
		}
		
		if (event.side == Side.SERVER && InstanceManager.prepPlayers.size() > 0) {
			
			for (EntityPlayer player : InstanceManager.prepPlayers.keySet()) {
				
				WorldLocation destination = InstanceManager.prepPlayers.get(player);
				int instanceID = destination.getWorldID();
				
				System.out.println(destination.getPosX());
				
				player.posX = destination.getPosX();
				player.posY = destination.getPosY();
				player.posZ = destination.getPosZ();
				
				WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(instanceID);
				
				MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) player, 
						instanceID, new TeleporterFix(worldServer));
				
				InstanceManager.prepPlayers.remove(player);
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